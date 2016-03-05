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

import info.varden.irclinqed.packet.Packet365EndOfLinks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSmallButton;

public class GuiScreenLinksReply extends GuiScreenWithList {
	private GuiItemList itemList;
	private Packet365EndOfLinks packet;
	
	public GuiScreenLinksReply(Packet365EndOfLinks packet) {
		this.packet = packet;
	}
	
	@Override
	public FontRenderer getFontRenderer() {
		return this.fontRenderer;
	}
	
	@Override
	public void initGui() {
        this.buttonList.add(new GuiSmallButton(6, this.width / 2 + 4, this.height - 48, "Close"));
        this.itemList = new GuiItemList(this, this.packet.thread.getGuiListItems());
        this.itemList.registerScrollButtons(7, 8);
    }
	
	@Override
	public void actionPerformed(GuiButton button) {
		if (button.enabled) {
			if (button.id == 6) {
				Minecraft.getMinecraft().displayGuiScreen((GuiScreen)null);
			} else {
				itemList.actionPerformed(button);
			}
		}
	}
	
	@Override
	protected void mouseMovedOrUp(int i, int j, int k) {
        super.mouseMovedOrUp(i, j, k);
    }
	
	@Override
	public void drawScreen(int par1, int par2, float par3) {
        this.itemList.drawScreen(par1, par2, par3);
        this.drawCenteredString(this.fontRenderer, this.itemList.getSize() + " results for /links " + this.packet.getParam(1), this.width / 2, 16, 16777215);
        super.drawScreen(par1, par2, par3);
    }
	
	@Override
	public void updateScreen() {
        super.updateScreen();
    }

	@Override
	public void elementClicked(int index, ListItem selectedItem, boolean flag) {
		
	}
}
