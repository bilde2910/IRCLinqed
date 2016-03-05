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
import net.minecraft.client.gui.GuiTextField;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiIngameIdentitySettings extends GuiScreen {
	
    private final GuiScreen parentScreen;
    private final String server;
    private final IRCLinqed il;
    
    private GuiTextField nickname;
    private GuiTextField altnick;
    private GuiTextField afk;
    private GuiTextField realname;
    private GuiTextField nickserv;

    public GuiIngameIdentitySettings(IRCLinqed il, GuiScreen parent, String server) {
        this.parentScreen = parent;
        this.server = server;
        this.il = il;
    }

    @Override
    public void initGui() {
    	Keyboard.enableRepeatEvents(true);
        this.buttonList.clear();
        this.buttonList.add(new GuiButton(0, this.width / 2 - 152, this.height / 6 + 168, 150, 20, "Done"));
        this.buttonList.add(new GuiButton(200, this.width / 2 + 2, this.height / 6 + 168, 150, 20, "Cancel"));
        this.nickname = new GuiTextField(this.fontRenderer, this.width / 2 + 2, this.height / 6 + 10, 150, 20);
        this.altnick = new GuiTextField(this.fontRenderer, this.width / 2 + 2, this.height / 6 + 34, 150, 20);
        this.afk = new GuiTextField(this.fontRenderer, this.width / 2 + 2, this.height / 6 + 58, 150, 20);
        this.realname = new GuiTextField(this.fontRenderer, this.width / 2 + 2, this.height / 6 + 82, 150, 20);
        this.nickserv = new GuiTextField(this.fontRenderer, this.width / 2 + 2, this.height / 6 + 106, 150, 20);
        this.nickserv.setMaxStringLength(64);
        if (this.server == null) {
        	this.nickname.setText(this.il.configLoader.getDefaultKeyFactoryFallback("nickname"));
        	this.altnick.setText(this.il.configLoader.getDefaultKeyFactoryFallback("altnick"));
        	this.afk.setText(this.il.configLoader.getDefaultKeyFactoryFallback("afknick"));
        	this.realname.setText(this.il.configLoader.getDefaultKeyFactoryFallback("realname"));
        	this.nickserv.setText(this.il.configLoader.getDefaultKeyFactoryFallback("nickserv"));
        } else {
        	String host = Util.getHost(this.server);
			int port = Util.getPort(this.server);
        	this.nickname.setText(this.il.configLoader.getServerSpecificKey(host, port, "nickname", "", false));
        	this.altnick.setText(this.il.configLoader.getServerSpecificKey(host, port, "altnick", "", false));
        	this.afk.setText(this.il.configLoader.getServerSpecificKey(host, port, "afknick", "", false));
        	this.realname.setText(this.il.configLoader.getServerSpecificKey(host, port, "realname", "", false));
        	this.nickserv.setText(this.il.configLoader.getServerSpecificKey(host, port, "nickserv", "", false));
        }
        this.nickname.setFocused(true);
    }
    
    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.enabled) {
            switch (button.id) {
            	case 0:
            		if (this.server == null) {
            			if (!"".equals(this.nickname.getText())) {
            				this.il.configLoader.setDefaultKey("nickname", this.nickname.getText());
            			} else {
            				this.il.configLoader.removeDefault("nickname");
            			}
            			if (!"".equals(this.altnick.getText())) {
            				this.il.configLoader.setDefaultKey("altnick", this.altnick.getText());
            			} else {
            				this.il.configLoader.removeDefault("altnick");
            			}
            			if (!"".equals(this.afk.getText())) {
            				this.il.configLoader.setDefaultKey("afknick", this.afk.getText());
            			} else {
            				this.il.configLoader.removeDefault("afknick");
            			}
            			if (!"".equals(this.realname.getText())) {
            				this.il.configLoader.setDefaultKey("realname", this.realname.getText());
            			} else {
            				this.il.configLoader.removeDefault("realname");
            			}
            			if (!"".equals(this.nickserv.getText())) {
            				this.il.configLoader.setDefaultKey("nickserv", this.nickserv.getText());
            			} else {
            				this.il.configLoader.removeDefault("nickserv");
            			}
            		} else {
            			String host = Util.getHost(this.server);
            			int port = Util.getPort(this.server);
            			if (!"".equals(this.nickname.getText())) {
            				this.il.configLoader.setServerSpecificKey(host, port, "nickname", this.nickname.getText());
            			} else {
            				this.il.configLoader.removeServerSpecificKey(host, port, "nickname");
            			}
            			if (!"".equals(this.altnick.getText())) {
            				this.il.configLoader.setServerSpecificKey(host, port, "altnick", this.altnick.getText());
            			} else {
            				this.il.configLoader.removeServerSpecificKey(host, port, "altnick");
            			}
            			if (!"".equals(this.afk.getText())) {
            				this.il.configLoader.setServerSpecificKey(host, port, "afknick", this.afk.getText());
            			} else {
            				this.il.configLoader.removeServerSpecificKey(host, port, "afknick");
            			}
            			if (!"".equals(this.realname.getText())) {
            				this.il.configLoader.setServerSpecificKey(host, port, "realname", this.realname.getText());
            			} else {
            				this.il.configLoader.removeServerSpecificKey(host, port, "realname");
            			}
            			if (!"".equals(this.nickserv.getText())) {
            				this.il.configLoader.setServerSpecificKey(host, port, "nickserv", this.nickserv.getText());
            			} else {
            				this.il.configLoader.removeServerSpecificKey(host, port, "nickserv");
            			}
            		}
            	case 200:
            		this.mc.displayGuiScreen(this.parentScreen);
            		break;
            }
        }
    }
    
    @Override
    protected void keyTyped(char par1, int par2) {
        if (this.nickname.isFocused()) {
            this.nickname.textboxKeyTyped(par1, par2);
        } else if (this.altnick.isFocused()) {
            this.altnick.textboxKeyTyped(par1, par2);
        } else if (this.afk.isFocused()) {
            this.afk.textboxKeyTyped(par1, par2);
        } else if (this.realname.isFocused()) {
            this.realname.textboxKeyTyped(par1, par2);
        } else if (this.nickserv.isFocused()) {
            this.nickserv.textboxKeyTyped(par1, par2);
        }

        if (par2 == 28 || par2 == 156) {
            this.actionPerformed((GuiButton)this.buttonList.get(0));
        }
    }
    
    @Override
    protected void mouseClicked(int par1, int par2, int par3) {
        super.mouseClicked(par1, par2, par3);
        this.nickname.mouseClicked(par1, par2, par3);
        this.altnick.mouseClicked(par1, par2, par3);
        this.afk.mouseClicked(par1, par2, par3);
        this.realname.mouseClicked(par1, par2, par3);
        this.nickserv.mouseClicked(par1, par2, par3);
    }

    @Override
    public void drawScreen(int par1, int par2, float par3) {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRenderer, (this.server == null ? "Default server identity" : "Identity for " + this.server), this.width / 2, this.height / 6 - 20, 16777215);
        this.drawString(this.fontRenderer, "Nickname:", this.width / 2 - 150, this.height / 6 + 16, 16777215);
        this.drawString(this.fontRenderer, "Alternative nickname:", this.width / 2 - 150, this.height / 6 + 40, 16777215);
        this.drawString(this.fontRenderer, "Away nickname:", this.width / 2 - 150, this.height / 6 + 64, 16777215);
        this.drawString(this.fontRenderer, "Realname:", this.width / 2 - 150, this.height / 6 + 88, 16777215);
        this.drawString(this.fontRenderer, "Nickserv password:", this.width / 2 - 150, this.height / 6 + 112, 16777215);
        this.nickname.drawTextBox();
        this.altnick.drawTextBox();
        this.afk.drawTextBox();
        this.realname.drawTextBox();
        this.nickserv.drawTextBox();
        super.drawScreen(par1, par2, par3);
    }
}
