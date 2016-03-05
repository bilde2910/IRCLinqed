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
import info.varden.irclinqed.Setting;
import info.varden.irclinqed.Util;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiIngameIRCMinecraftBridgeSettings extends GuiScreen {
	
    private final GuiScreen parentScreen;
    private final String server;
    private final IRCLinqed il;

    public GuiIngameIRCMinecraftBridgeSettings(IRCLinqed il, GuiScreen parent, String server) {
        this.parentScreen = parent;
        this.server = server;
        this.il = il;
    }

    @Override
    public void initGui() {
        this.buttonList.clear();
        this.buttonList.add(new GuiButton(101, this.width / 2 - 100, this.height / 6 + 10, "Show MotD: " + this.il.configLoader.getOnOffDefault(Setting.SHOW_MOTD, this.server)));
        this.buttonList.add(new GuiButton(100, this.width / 2 - 100, this.height / 6 + 34, "Show CTCP requests: " + this.il.configLoader.getOnOffDefault(Setting.SHOW_CTCP, this.server)));
        this.buttonList.add(new GuiButton(102, this.width / 2 - 100, this.height / 6 + 58, "Show formatting: " + this.il.configLoader.getOnOffDefault(Setting.SHOW_FORMATTING, this.server)));
        this.buttonList.add(new GuiButton(103, this.width / 2 - 100, this.height / 6 + 82, "Show mode changes: " + this.il.configLoader.getOnOffDefault(Setting.SHOW_MODE, this.server)));
        this.buttonList.add(new GuiButton(105, this.width / 2 - 100, this.height / 6 + 106, "Show nick prefixes: " + this.il.configLoader.getOnOffDefault(Setting.SHOW_NICKNAME_PREFIX, this.server)));
        this.buttonList.add(new GuiButton(104, this.width / 2 - 100, this.height / 6 + 130, "Allow DCC: " + this.il.configLoader.getOnOffDefault(Setting.ALLOW_DCC, this.server)));
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
            			this.il.configLoader.setDefaultKey(Setting.SHOW_MOTD, value);
            		} else {
            			this.il.configLoader.setServerSpecificKey(host, port, Setting.SHOW_MOTD, value);
            		}
            		button.displayString = "Show MotD: " + this.il.configLoader.getOnOffDefault(Setting.SHOW_MOTD, this.server);
            		break;
            	case 100:
            		if (server == null) {
            			this.il.configLoader.setDefaultKey(Setting.SHOW_CTCP, value);
            		} else {
            			this.il.configLoader.setServerSpecificKey(host, port, Setting.SHOW_CTCP, value);
            		}
            		button.displayString = "Show CTCP requests: " + this.il.configLoader.getOnOffDefault(Setting.SHOW_CTCP, this.server);
            		break;
            	case 102:
            		if (server == null) {
            			this.il.configLoader.setDefaultKey(Setting.SHOW_FORMATTING, value);
            		} else {
            			this.il.configLoader.setServerSpecificKey(host, port, Setting.SHOW_FORMATTING, value);
            		}
            		button.displayString = "Show formatting: " + this.il.configLoader.getOnOffDefault(Setting.SHOW_FORMATTING, this.server);
            		break;
            	case 103:
            		if (server == null) {
            			this.il.configLoader.setDefaultKey(Setting.SHOW_MODE, value);
            		} else {
            			this.il.configLoader.setServerSpecificKey(host, port, Setting.SHOW_MODE, value);
            		}
            		button.displayString = "Show mode changes: " + this.il.configLoader.getOnOffDefault(Setting.SHOW_MODE, this.server);
            		break;
            	case 105:
            		if (server == null) {
            			this.il.configLoader.setDefaultKey(Setting.SHOW_NICKNAME_PREFIX, value);
            		} else {
            			this.il.configLoader.setServerSpecificKey(host, port, Setting.SHOW_NICKNAME_PREFIX, value);
            		}
            		button.displayString = "Show nick prefixes: " + this.il.configLoader.getOnOffDefault(Setting.SHOW_NICKNAME_PREFIX, this.server);
            		break;
            	case 104:
            		if (server == null) {
            			this.il.configLoader.setDefaultKey(Setting.ALLOW_DCC, value);
            		} else {
            			this.il.configLoader.setServerSpecificKey(host, port, Setting.ALLOW_DCC, value);
            		}
            		button.displayString = "Allow DCC: " + this.il.configLoader.getOnOffDefault(Setting.ALLOW_DCC, this.server);
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
        this.drawCenteredString(this.fontRenderer, (this.server == null ? "Default IRC to Minecraft bridging settings" : "IRC to Minecraft bridging settings for " + this.server), this.width / 2, this.height / 6 - 20, 16777215);
        super.drawScreen(par1, par2, par3);
    }
}
