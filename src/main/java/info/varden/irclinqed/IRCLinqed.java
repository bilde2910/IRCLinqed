/*
   Copyright 2014 Marius Lindvall

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/

package info.varden.irclinqed;

import info.varden.irclinqed.forge.ChatCommand;
import info.varden.irclinqed.forge.CommonProxy;
import info.varden.irclinqed.forge.ForgeEventHandler;
import info.varden.irclinqed.forge.ForgeKeyBind;
import info.varden.irclinqed.forge.IKeyListener;
import info.varden.irclinqed.forge.IRCCommand;
import info.varden.irclinqed.forge.KeyAssociation;
import info.varden.irclinqed.gui.DisplayGuiRequest;
import info.varden.irclinqed.gui.GuiOverlay;
import info.varden.irclinqed.gui.GuiScreenSwapChannels;
import info.varden.irclinqed.gui.GuiScreenSwapConnections;
import info.varden.irclinqed.gui.IKeyGuiReady;
import info.varden.irclinqed.gui.TickHandlerDisplayGui;
import info.varden.irclinqed.httpapi.DefaultIPSetter;
import info.varden.irclinqed.httpapi.IPFetcherThread;
import info.varden.irclinqed.irc.ConnectionManager;
import info.varden.irclinqed.irc.IRCThread;
import info.varden.irclinqed.plugin.EventRegistry;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.client.registry.KeyBindingRegistry;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

@Mod(modid=IRCLinqed.MODID, name=IRCLinqed.NAME, version=IRCLinqed.VERSION)
@NetworkMod(clientSideRequired=false, serverSideRequired=false)
public class IRCLinqed implements IKeyListener {
	public static final String MODID = "info.varden.irclinqed";
	public static final String NAME = "IRCLinqed";
	public static final String VERSION = "0.0.7a";
	
	public static boolean clientSide = true;
	public static boolean hasInitializedChatInterceptor = false;
	private Minecraft mc;
	public ConfigLoader configLoader;
	public ConnectionManager connectionManager;
	public TickHandlerDisplayGui th;
	public DisplayGuiRequest displayRequests;
	public Util util;
	private EventRegistry eventRegistry;
	public List<GuiOverlay> guiQueue;
	public List<IKeyGuiReady> keyGuiQueue;
	public List<IKeyListener> keyListenerQueue;
	public String ipAddress = null;
	public String lanAddress = null;
	
	public ChatCommand cc;
	public IRCCommand ic;
	
	@SidedProxy(clientSide="info.varden.irclinqed.forge.ClientProxy", serverSide="info.varden.irclinqed.forge.ServerProxy")
	public static CommonProxy proxy;
	
	@Instance(value = "info.varden.irclinqed")
	public static IRCLinqed instance;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		this.mc = Minecraft.getMinecraft();
		logInfo("Initializing IRCLinqed...");
		logInfo("Getting public IP address...");
		DefaultIPSetter ips = new DefaultIPSetter(this);
		Runnable ift = new IPFetcherThread(ips);
		Thread t = new Thread(ift);
		t.start();
		this.displayRequests = new DisplayGuiRequest(this);
		this.configLoader = new ConfigLoader(this);
		this.configLoader.loadConfig();
		this.connectionManager = new ConnectionManager(this);
		this.util = new Util(this);
		this.eventRegistry = new EventRegistry();
		this.guiQueue = new ArrayList<GuiOverlay>();
		this.keyGuiQueue = new ArrayList<IKeyGuiReady>();
		this.keyListenerQueue = new ArrayList<IKeyListener>();
		this.keyListenerQueue.add(this);
		logInfo("Initialized.");
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event) {
		logInfo("Initializing interceptors...");
		MinecraftForge.EVENT_BUS.register(new ForgeEventHandler(this));
		proxy.init(event);
		logInfo("Interceptors initialized.");
		logInfo("Adding key handlers...");
		KeyBinding[] key = KeyAssociation.getAllDefaultKeyBindings();
		boolean[] repeat = {false, false, false, false, false};
		KeyBindingRegistry.registerKeyBinding(new ForgeKeyBind(this, key, repeat));
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		logInfo("Registering command handlers to Forge event bus...");
		this.ic = new IRCCommand(this);
		ClientCommandHandler.instance.registerCommand(this.ic);
		this.cc = new ChatCommand(this);
		ClientCommandHandler.instance.registerCommand(this.cc);
		logInfo("Command handlers registered.");
		logInfo("Registering tick handler...");
		this.th = new TickHandlerDisplayGui(this);
		TickRegistry.registerTickHandler(this.th, Side.CLIENT);
		logInfo("Tick handler registered.");
	}
	
	public void logInfo(String message) {
		this.mc.getLogAgent().logInfo("[MOD] IRCLinqed - " + message);
	}
	
	public void logWarning(String message) {
		this.mc.getLogAgent().logWarning("[MOD] IRCLinqed - " + message);
	}
	
	public void logSevere(String message) {
		this.mc.getLogAgent().logSevere("[MOD] IRCLinqed - " + message);
	}

	public EventRegistry getEventRegistry() {
		return this.eventRegistry;
	}

	@Override
	public void invoke(KeyBinding kb) {
		if (KeyAssociation.CH_SERVERS.is(kb)) {
			this.mc.displayGuiScreen(new GuiScreenSwapConnections(this));
		} else if (KeyAssociation.CH_CHANNEL.is(kb)) {
			int index = this.connectionManager.getCurrentServer();
			if (index >= 0) {
				if (this.connectionManager.getConnectionPair(index).runnable instanceof IRCThread) {
					this.mc.displayGuiScreen(new GuiScreenSwapChannels(this));
				} else {
					this.util.writeToChat(MessageType.LQS_ERROR, "DCC connections do not support channels!");
				}
			} else {
				this.util.writeToChat(MessageType.LQS_ERROR, "You're not on a server! Connect to a server using //irc connect or //irc connect host(:port).");
			}
		}
	}
}
