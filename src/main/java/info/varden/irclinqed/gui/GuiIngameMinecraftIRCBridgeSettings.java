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

package info.varden.irclinqed.gui;

import info.varden.irclinqed.IRCLinqed;
import info.varden.irclinqed.Util;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiIngameMinecraftIRCBridgeSettings extends GuiScreen {
	
    private final GuiScreen parentScreen;
    private final String server;
    private final IRCLinqed il;

    public GuiIngameMinecraftIRCBridgeSettings(IRCLinqed il, GuiScreen parent, String server) {
        this.parentScreen = parent;
        this.server = server;
        this.il = il;
    }

    @Override
    public void initGui() {
        this.buttonList.clear();
        this.buttonList.add(new GuiButton(101, this.width / 2 - 100, this.height / 6 + 10, "Send death messages: " + this.il.configLoader.getOnOffDefault("senddeath", this.server)));
        this.buttonList.add(new GuiButton(100, this.width / 2 - 100, this.height / 6 + 34, "Send achievements: " + this.il.configLoader.getOnOffDefault("sendachievements", this.server)));
        this.buttonList.add(new GuiButton(102, this.width / 2 - 100, this.height / 6 + 58, "Send all chat to IRC: " + this.il.configLoader.getOnOffDefault("sendall", this.server)));
//        this.buttonList.add(new GuiButton(103, this.width / 2 - 100, this.height / 6 + 82, ": " + this.il.configLoader.getOnOffDefault("", this.server)));
//        this.buttonList.add(new GuiButton(105, this.width / 2 - 100, this.height / 6 + 106, ": " + this.il.configLoader.getOnOffDefault("", this.server)));
//        this.buttonList.add(new GuiButton(104, this.width / 2 - 100, this.height / 6 + 130, ": " + this.il.configLoader.getOnOffDefault("", this.server)));
        this.buttonList.add(new GuiButton(200, this.width / 2 - 100, this.height / 6 + 168, "Done"));
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.enabled) {
        	String value = null;
        	String host = null;
        	int port = 6667;
        	if (button.id >= 100 && button.id <= 105) {
        		if (server != null) {
        			host = Util.getHost(this.server);
        			port = Util.getPort(this.server);
        		}
        		value = button.displayString;
    			if (value.contains("§7")) {
    				value = "true";
    			} else if (value.contains("§a")) {
    				value = "false";
    			} else if (value.contains("§c")) {
    				value = "default";
    			}
        	}
            switch (button.id) {
            	case 101:
            		if (server == null) {
            			this.il.configLoader.setDefaultKey("senddeath", value);
            		} else {
            			this.il.configLoader.setServerSpecificKey(host, port, "senddeath", value);
            		}
            		button.displayString = "Send death messages: " + this.il.configLoader.getOnOffDefault("senddeath", this.server);
            		break;
            	case 100:
            		if (server == null) {
            			this.il.configLoader.setDefaultKey("sendachievements", value);
            		} else {
            			this.il.configLoader.setServerSpecificKey(host, port, "sendachievements", value);
            		}
            		button.displayString = "Send achievements: " + this.il.configLoader.getOnOffDefault("sendachievements", this.server);
            		break;
            	case 102:
            		if (server == null) {
            			this.il.configLoader.setDefaultKey("sendall", value);
            		} else {
            			this.il.configLoader.setServerSpecificKey(host, port, "sendall", value);
            		}
            		button.displayString = "Send all chat to IRC: " + this.il.configLoader.getOnOffDefault("sendall", this.server);
            		break;
            	case 103:
            		if (server == null) {
            			this.il.configLoader.setDefaultKey("", value);
            		} else {
            			this.il.configLoader.setServerSpecificKey(host, port, "", value);
            		}
            		button.displayString = ": " + this.il.configLoader.getOnOffDefault("", this.server);
            		break;
            	case 105:
            		if (server == null) {
            			this.il.configLoader.setDefaultKey("", value);
            		} else {
            			this.il.configLoader.setServerSpecificKey(host, port, "", value);
            		}
            		button.displayString = ": " + this.il.configLoader.getOnOffDefault("", this.server);
            		break;
            	case 104:
            		if (server == null) {
            			this.il.configLoader.setDefaultKey("", value);
            		} else {
            			this.il.configLoader.setServerSpecificKey(host, port, "", value);
            		}
            		button.displayString = ": " + this.il.configLoader.getOnOffDefault("", this.server);
            		break;
            	case 200:
            		this.mc.displayGuiScreen(this.parentScreen);
            		break;
            }
        }
    }

    @Override
    public void drawScreen(int par1, int par2, float par3) {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRenderer, (this.server == null ? "Default Minecraft to IRC bridging settings" : "Minecraft to IRC bridging settings for " + this.server), this.width / 2, this.height / 6 - 20, 16777215);
        super.drawScreen(par1, par2, par3);
    }
}
