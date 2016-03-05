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
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiIngameAddServer extends GuiScreen
{
    private GuiScreen parentGuiScreen;
    private GuiTextField serverIP;
    private IRCLinqed il;

    public GuiIngameAddServer(IRCLinqed il, GuiScreen parent) {
        this.parentGuiScreen = parent;
        this.il = il;
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
    }

    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        this.buttonList.clear();
        this.buttonList.add(new GuiButton(0, this.width / 2 - 155, this.height - 28, 150, 20, "Add server"));
        ((GuiButton)this.buttonList.get(0)).enabled = false;
        this.buttonList.add(new GuiButton(1, this.width / 2 + 5, this.height - 28, 150, 20, "Cancel"));
        this.serverIP = new GuiTextField(this.fontRenderer, this.width / 2 - 100, 60, 200, 20);
        this.serverIP.setFocused(true);
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
        			this.il.configLoader.setKey("servers", this.il.configLoader.getKey("servers", "") + this.serverIP.getText() + ",");
        			this.mc.displayGuiScreen(this.parentGuiScreen);
        		case 1:
        			this.mc.displayGuiScreen(this.parentGuiScreen);
        	}
        }
    }

    @Override
    protected void keyTyped(char par1, int par2) {
        if (this.serverIP.isFocused()) {
            this.serverIP.textboxKeyTyped(par1, par2);
        }

        if (par2 == 28 || par2 == 156) {
            this.actionPerformed((GuiButton)this.buttonList.get(0));
        }

        ((GuiButton)this.buttonList.get(0)).enabled = this.serverIP.getText().length() > 0;
    }

    protected void mouseClicked(int par1, int par2, int par3)
    {
        super.mouseClicked(par1, par2, par3);
        this.serverIP.mouseClicked(par1, par2, par3);
    }

    @Override
    public void drawScreen(int par1, int par2, float par3)
    {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRenderer, "Add IRC server", this.width / 2, 20, 16777215);
        this.drawString(this.fontRenderer, "Enter server IP/port:", this.width / 2 - 100, 47, 10526880);
        this.serverIP.drawTextBox();

        super.drawScreen(par1, par2, par3);
    }
}
