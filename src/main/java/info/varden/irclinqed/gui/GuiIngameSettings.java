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
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiIngameSettings extends GuiScreen {
	
	private IRCLinqed il;
	
	public GuiIngameSettings(IRCLinqed il) {
		this.il = il;
	}
	
	@Override
	public void initGui() {
		this.buttonList.clear();
        this.buttonList.add(new GuiButton(4, this.width / 2 - 100, this.height / 4 + 120 - 16, "Done"));
        this.buttonList.add(new GuiButton(5, this.width / 2 - 100, this.height / 4 + 24 - 16, "Default settings"));
        this.buttonList.add(new GuiButton(6, this.width / 2 - 100, this.height / 4 + 48 - 16, "Server specific settings"));
	}
	
	@Override
	public void actionPerformed(GuiButton button) {
		if (button.enabled) {
			if (button.id == 4) {
				Minecraft.getMinecraft().displayGuiScreen((GuiScreen)null);
			} else if (button.id == 5) {
				Minecraft.getMinecraft().displayGuiScreen(new GuiIngameMainSettings(this.il, this, null));
			} else if (button.id == 6) {
				Minecraft.getMinecraft().displayGuiScreen(new GuiScreenSelectServer(this.il, this, false));
			}
		}
	}
	
	@Override
	protected void mouseMovedOrUp(int i, int j, int k) {
        super.mouseMovedOrUp(i, j, k);
    }
	
	@Override
	public void drawScreen(int par1, int par2, float par3) {
		this.drawDefaultBackground();
        this.drawCenteredString(this.fontRenderer, "IRCLinqed settings", this.width / 2, 40, 16777215);
        super.drawScreen(par1, par2, par3);
    }
	
	@Override
	public void updateScreen() {
        super.updateScreen();
    }
	
}
