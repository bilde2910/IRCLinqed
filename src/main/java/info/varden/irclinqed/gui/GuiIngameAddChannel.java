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
public class GuiIngameAddChannel extends GuiScreen
{
    private GuiScreen parentGuiScreen;
    private GuiTextField chan;
    private IRCLinqed il;
    private String server;

    public GuiIngameAddChannel(IRCLinqed il, GuiScreen parent, String server) {
        this.parentGuiScreen = parent;
        this.il = il;
        this.server = server;
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
    }

    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        this.buttonList.clear();
        this.buttonList.add(new GuiButton(0, this.width / 2 - 155, this.height - 28, 150, 20, "Add channel"));
        ((GuiButton)this.buttonList.get(0)).enabled = false;
        this.buttonList.add(new GuiButton(1, this.width / 2 + 5, this.height - 28, 150, 20, "Cancel"));
        this.chan = new GuiTextField(this.fontRenderer, this.width / 2 - 100, 60, 200, 20);
        this.chan.setFocused(true);
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
        			this.il.configLoader.setServerSpecificKey(Util.getHost(this.server), Util.getPort(this.server), "autojoin", this.il.configLoader.getServerSpecificKey(Util.getHost(this.server), Util.getPort(this.server), "autojoin", "") + this.chan.getText() + ",");
        			this.mc.displayGuiScreen(this.parentGuiScreen);
        		case 1:
        			this.mc.displayGuiScreen(this.parentGuiScreen);
        	}
        }
    }

    @Override
    protected void keyTyped(char par1, int par2) {
        if (this.chan.isFocused()) {
            this.chan.textboxKeyTyped(par1, par2);
        }

        if (par2 == 28 || par2 == 156) {
            this.actionPerformed((GuiButton)this.buttonList.get(0));
        }

        ((GuiButton)this.buttonList.get(0)).enabled = this.chan.getText().length() > 0;
    }

    @Override
    protected void mouseClicked(int par1, int par2, int par3) {
        super.mouseClicked(par1, par2, par3);
        this.chan.mouseClicked(par1, par2, par3);
    }

    @Override
    public void drawScreen(int par1, int par2, float par3) {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRenderer, "Add auto-join channel", this.width / 2, 20, 16777215);
        this.drawString(this.fontRenderer, "Channel:", this.width / 2 - 100, 47, 10526880);
        this.chan.drawTextBox();

        super.drawScreen(par1, par2, par3);
    }
}
