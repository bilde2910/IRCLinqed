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
public class GuiIngameQuitSettings extends GuiScreen {
	
    private final GuiScreen parentScreen;
    private final String server;
    private final IRCLinqed il;
    
    private GuiTextField quit;
    private GuiTextField part;

    public GuiIngameQuitSettings(IRCLinqed il, GuiScreen parent, String server) {
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
        this.quit = new GuiTextField(this.fontRenderer, this.width / 2 - 152, this.height / 6 + 34, 304, 20);
        this.part = new GuiTextField(this.fontRenderer, this.width / 2 - 152, this.height / 6 + 82, 304, 20);
        if (this.server == null) {
        	this.quit.setText(this.il.configLoader.getDefaultKeyFactoryFallback("quitreason"));
        	this.part.setText(this.il.configLoader.getDefaultKeyFactoryFallback("partreason"));
        } else {
        	String host = Util.getHost(this.server);
			int port = Util.getPort(this.server);
        	this.quit.setText(this.il.configLoader.getServerSpecificKey(host, port, "quitreason", "", false));
        	this.part.setText(this.il.configLoader.getServerSpecificKey(host, port, "partreason", "", false));
        }
        this.quit.setFocused(true);
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
            			if (!"".equals(this.quit.getText())) {
            				this.il.configLoader.setDefaultKey("quitreason", this.quit.getText());
            			} else {
            				this.il.configLoader.removeDefault("quitreason");
            			}
            			if (!"".equals(this.part.getText())) {
            				this.il.configLoader.setDefaultKey("partreason", this.part.getText());
            			} else {
            				this.il.configLoader.removeDefault("partreason");
            			}
            		} else {
            			String host = Util.getHost(this.server);
            			int port = Util.getPort(this.server);
            			if (!"".equals(this.quit.getText())) {
            				this.il.configLoader.setServerSpecificKey(host, port, "quitreason", this.quit.getText());
            			} else {
            				this.il.configLoader.removeServerSpecificKey(host, port, "quitreason");
            			}
            			if (!"".equals(this.part.getText())) {
            				this.il.configLoader.setServerSpecificKey(host, port, "partreason", this.part.getText());
            			} else {
            				this.il.configLoader.removeServerSpecificKey(host, port, "partreason");
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
        if (this.quit.isFocused()) {
            this.quit.textboxKeyTyped(par1, par2);
        } else if (this.part.isFocused()) {
            this.part.textboxKeyTyped(par1, par2);
        }

        if (par2 == 28 || par2 == 156) {
            this.actionPerformed((GuiButton)this.buttonList.get(0));
        }
    }
    
    protected void mouseClicked(int par1, int par2, int par3) {
        super.mouseClicked(par1, par2, par3);
        this.quit.mouseClicked(par1, par2, par3);
        this.part.mouseClicked(par1, par2, par3);
    }

    @Override
    public void drawScreen(int par1, int par2, float par3) {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRenderer, (this.server == null ? "Default quit reasons" : "Quit reasons for " + this.server), this.width / 2, this.height / 6 - 20, 16777215);
        this.drawString(this.fontRenderer, "Quit reason:", this.width / 2 - 150, this.height / 6 + 16, 16777215);
        this.drawString(this.fontRenderer, "Part reason:", this.width / 2 - 150, this.height / 6 + 64, 16777215);
        this.quit.drawTextBox();
        this.part.drawTextBox();
        super.drawScreen(par1, par2, par3);
    }
}
