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

package info.varden.irclinqed.forge;

import info.varden.irclinqed.IRCLinqed;
import info.varden.irclinqed.ThreadRunnablePair;
import info.varden.irclinqed.ctcp.ActionPacket;
import info.varden.irclinqed.dcc.DCCThread;
import info.varden.irclinqed.dcc.VirtualIRCThread;
import info.varden.irclinqed.gui.GuiOverlay;
import info.varden.irclinqed.irc.IRCThread;
import info.varden.irclinqed.packet.PacketPrivmsg;

import java.lang.reflect.Field;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.ForgeSubscribe;

public class ForgeEventHandler {
	private final IRCLinqed il;
	private boolean lastGuiWasDeath = false;
	
	public ForgeEventHandler(IRCLinqed il) {
		this.il = il;
	}

	@ForgeSubscribe
	public void onAchievement(AchievementEvent event) {
		int connCount = this.il.connectionManager.getConnectionCount();
		for (int i = 0; i < connCount; i++) {
			ThreadRunnablePair pair = this.il.connectionManager.getConnectionPair(i);
			try {
				if (pair.runnable instanceof IRCThread) {
					IRCThread thrd = (IRCThread) pair.runnable;
					if (this.il.configLoader.getServerSpecificBooleanKey("sendachievements", pair.runnable.getHost(), pair.runnable.getPort())) {
						String chan = thrd.getChannel();
						if (chan != null) {
							ActionPacket packet = new ActionPacket(this.il, thrd, chan, "earned the achievement \u000309" + I18n.getString(event.achievement.getName()) + "\u000F!");
							packet.send();
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	@ForgeSubscribe
	public void onMessageSent(MessageSentEvent event) {
		if (!event.message.startsWith("//irc") && !event.message.startsWith("# ")) {
			if (event.message.startsWith("/")) {
				this.il.cc.processCommand(null, event.message.split(" "));
			} else {
				int index = this.il.connectionManager.getCurrentServer();
					if (index > -1) {
					ThreadRunnablePair pair = this.il.connectionManager.getConnectionPair(index);
					try {
						if (pair.runnable instanceof IRCThread) {
							IRCThread thrd = (IRCThread) pair.runnable;
							if (this.il.configLoader.getServerSpecificBooleanKey("sendall", pair.runnable.getHost(), pair.runnable.getPort())) {
								String chan = thrd.getChannel();
								if (chan != null) {
									PacketPrivmsg packet = new PacketPrivmsg(this.il, thrd, chan, event.message);
									packet.send();
								}
							}
						} else if (pair.runnable instanceof DCCThread) {
							DCCThread thrd = (DCCThread) pair.runnable;
							thrd.sendMessage(event.message);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	@ForgeSubscribe
	public void onRenderGameOverlay(RenderGameOverlayEvent event) {
		for (GuiOverlay g : this.il.guiQueue) {
			if (g != null) {
				g.render(event);
			}
		}
	}
	
	@ForgeSubscribe
	public void onGuiOpen(GuiOpenEvent event) {
		if (!this.il.hasInitializedChatInterceptor) {
			this.il.hasInitializedChatInterceptor = true;
			if (this.il.clientSide) {
				try {
					System.out.println("Adding chat interceptor");
					Field field = GuiNewChat.class.getDeclaredFields()[1];
					field.setAccessible(true);
					GuiNewChat gnc = Minecraft.getMinecraft().ingameGUI.getChatGUI();
					List oldList = (List) field.get(gnc);
					field.set(gnc, new ChatInterceptorList(oldList));
					System.out.println("Chat interceptor added");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		if (event.gui instanceof GuiGameOver) {
			if (!this.lastGuiWasDeath) {
				this.lastGuiWasDeath = true;
				int connCount = this.il.connectionManager.getConnectionCount();
				for (int i = 0; i < connCount; i++) {
					ThreadRunnablePair pair = this.il.connectionManager.getConnectionPair(i);
					try {
						if (pair.runnable instanceof IRCThread) {
							IRCThread thrd = (IRCThread) pair.runnable;
							if (this.il.configLoader.getServerSpecificBooleanKey("senddeath", pair.runnable.getHost(), pair.runnable.getPort())) {
								String chan = thrd.getChannel();
								if (chan != null) {
									ActionPacket packet = new ActionPacket(this.il, thrd, chan, "died");
									packet.send();
								}
							}
						} else if (pair.runnable instanceof DCCThread && this.il.configLoader.getBooleanKey("senddeath")) {
							DCCThread thrd = (DCCThread) pair.runnable;
							VirtualIRCThread fake = new VirtualIRCThread(this.il, thrd);
							ActionPacket packet = new ActionPacket(this.il, fake, "", "died");
							packet.send();
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		} else {
			this.lastGuiWasDeath = false;
		}
		if (!(event.gui instanceof GuiChat)) {
			
		}
	}
}
