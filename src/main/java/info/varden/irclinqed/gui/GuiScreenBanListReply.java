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

import info.varden.irclinqed.packet.Packet368EndOfBanList;
import info.varden.irclinqed.packet.PacketMode;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSmallButton;

public class GuiScreenBanListReply extends GuiScreenWithList {
	private GuiItemList itemList;
	public GuiSmallButton pmButton;
	private Packet368EndOfBanList packet;
	
	public GuiScreenBanListReply(Packet368EndOfBanList packet) {
		this.packet = packet;
		
	}
	
	@Override
	public FontRenderer getFontRenderer() {
		return this.fontRenderer;
	}
	
	@Override
	public void initGui() {
		this.pmButton = new GuiSmallButton(5, this.width / 2 - 154, this.height - 48, "Unban");
		this.pmButton.enabled = false;
        this.buttonList.add(this.pmButton);
        this.buttonList.add(new GuiSmallButton(6, this.width / 2 + 4, this.height - 48, "Close"));
        this.itemList = new GuiItemList(this, this.packet.thread.getGuiListItems());
        this.itemList.registerScrollButtons(7, 8);
    }
	
	@Override
	public void actionPerformed(GuiButton button) {
		if (button.enabled) {
			if (button.id == 5) {
				PacketMode packet = new PacketMode(this.packet.il, this.packet.thread, this.packet.getParam(1), "-b", this.itemList.getSelectedItem().title);
				packet.send();
				Minecraft.getMinecraft().displayGuiScreen((GuiScreen)null);
			} else if (button.id == 6) {
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
        this.drawCenteredString(this.fontRenderer, this.itemList.getSize() + " active bans on " + this.packet.getParam(1), this.width / 2, 16, 16777215);
        super.drawScreen(par1, par2, par3);
    }
	
	@Override
	public void updateScreen() {
        super.updateScreen();
    }

	@Override
	public void elementClicked(int index, ListItem selectedItem, boolean flag) {
		if (index < 0) {
			this.pmButton.enabled = false;
		} else {
			this.pmButton.enabled = true;
		}
		if (flag) {
			actionPerformed(this.pmButton);
		}
	}
}
