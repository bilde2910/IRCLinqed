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

package info.varden.irclinqed.dcc;

import info.varden.irclinqed.IRCLinqed;
import info.varden.irclinqed.Util;
import info.varden.irclinqed.gui.StringDrawer;
import info.varden.irclinqed.irc.IConnection;

import java.io.File;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiYesNo;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiIngameConfirmSend extends GuiYesNo
{
    private final IRCLinqed il;
    private final IConnection thread;
    private final String target;
    private final File file;
    private final StringDrawer ird;

    public GuiIngameConfirmSend(IRCLinqed il, IConnection thread, String target, File file) {
        super(null, "Are you sure you want to send this file?", "", 0);
        this.buttonText1 = "Yes";
        this.buttonText2 = "No";
        this.il = il;
        this.thread = thread;
        this.target = target;
        this.file = file;
        this.ird = new StringDrawer();
    }

    @Override
    public void initGui() {
    	this.buttonList.clear();
        this.buttonList.add(new GuiButton(0, this.width / 2 - 129, this.height / 6 + 96, 125, 20, this.buttonText1));
        this.buttonList.add(new GuiButton(1, this.width / 2 + 4, this.height / 6 + 96, 125, 20, this.buttonText2));
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.id == 0) {
        	Runnable r = new FileSendThread(this.il, this.thread, this.target, this.file);
        	Thread t = new Thread(r);
        	t.start();
        	this.mc.displayGuiScreen((GuiScreen) null);
        } else if (button.id == 1) {
        	this.mc.displayGuiScreen((GuiScreen) null);
        }
    }

    @Override
    public void drawScreen(int par1, int par2, float par3) {
        super.drawScreen(par1, par2, par3);
        this.ird.drawRightAlignedString(this.fontRenderer, "§7File name:", this.width / 2 - 6, 95, 16777215);
        this.ird.drawRightAlignedString(this.fontRenderer, "§7File size:", this.width / 2 - 6, 107, 16777215);
        this.ird.drawRightAlignedString(this.fontRenderer, "§7Target user:", this.width / 2 - 6, 119, 16777215);
        this.drawString(this.fontRenderer, Util.escapeFormatting(this.file.getName()), this.width / 2 + 6, 95, 16777215);
        this.drawString(this.fontRenderer, Util.getFileSizeWithUnit(this.file.length()), this.width / 2 + 6, 107, 16777215);
        this.drawString(this.fontRenderer, this.target, this.width / 2 + 6, 119, 16777215);
    }
}
