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
import info.varden.irclinqed.irc.AutojoinThread;
import info.varden.irclinqed.irc.IRCThread;
import info.varden.irclinqed.packet.PacketPart;

import java.util.ArrayList;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

public class GuiScreenSwapChannels extends GuiScreenWithList {
	private GuiItemList itemList;
	private GuiButton pmButton;
	private GuiButton pmButton2;
	private GuiButton pmButton3;
	private IRCLinqed il;
	
	public GuiScreenSwapChannels(IRCLinqed il) {
		this.il = il;
	}
	
	@Override
	public FontRenderer getFontRenderer() {
		return this.fontRenderer;
	}
	
	@Override
	public void initGui() {
		this.buttonList.clear();
		ArrayList<ListItem> list = new ArrayList<ListItem>();
		IRCThread thrd = (IRCThread) this.il.connectionManager.getConnectionPair(this.il.connectionManager.getCurrentServer()).runnable;
		String[] chans = thrd.getChannels();
		for (String chan : chans) {
			if (!thrd.getChannel().equals(chan)) {
				continue;
			}
			list.add(new ListItem("§l" + chan, "", ""));
		}
		for (String chan : chans) {
			if (thrd.getChannel().equals(chan)) {
				continue;
			}
			list.add(new ListItem(chan, "", ""));
		}
		this.pmButton = new GuiButton(5, this.width / 2 + 2, this.height - 48, 74, 20, "Switch to");
		this.pmButton.enabled = false;
		this.pmButton2 = new GuiButton(6, this.width / 2 - 76, this.height - 48, 74, 20, "Rejoin");
		this.pmButton2.enabled = false;
		this.pmButton3 = new GuiButton(9, this.width / 2 - 154, this.height - 48, 74, 20, "Part");
		this.pmButton3.enabled = false;
        this.buttonList.add(this.pmButton);
        this.buttonList.add(this.pmButton2);
        this.buttonList.add(this.pmButton3);
        this.buttonList.add(new GuiButton(10, this.width / 2 + 80, this.height - 48, 74, 20, "Close"));
        this.itemList = new GuiItemList(this, list.toArray(new ListItem[0]), 32, 16);
        this.itemList.registerScrollButtons(7, 8);
    }
	
	@Override
	public void actionPerformed(GuiButton button) {
		if (button.enabled) {
			if (button.id == 5) {
				Minecraft.getMinecraft().displayGuiScreen((GuiScreen)null);
				IRCThread thrd = (IRCThread) this.il.connectionManager.getConnectionPair(this.il.connectionManager.getCurrentServer()).runnable;
				thrd.setChannel(this.itemList.getSelectedItem().title);
			} else if (button.id == 6) {
				Minecraft.getMinecraft().displayGuiScreen((GuiScreen)null);
				IRCThread thrd = (IRCThread) this.il.connectionManager.getConnectionPair(this.il.connectionManager.getCurrentServer()).runnable;
				String chan = this.itemList.getSelectedItem().title;
				PacketPart packet = new PacketPart(this.il, thrd, chan);
				packet.send();
				Runnable ajt = new AutojoinThread(this.il, thrd, new String[] {chan});
				Thread ajtt = new Thread(ajt);
				ajtt.start();
			} else if (button.id == 9) {
				Minecraft.getMinecraft().displayGuiScreen((GuiScreen)null);
				IRCThread thrd = (IRCThread) this.il.connectionManager.getConnectionPair(this.il.connectionManager.getCurrentServer()).runnable;
				String chan = this.itemList.getSelectedItem().title;
				PacketPart packet = new PacketPart(this.il, thrd, chan);
				packet.send();
			} else if (button.id == 10) {
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
        this.drawCenteredString(this.fontRenderer, "Switch to another channel", this.width / 2, 16, 16777215);
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
			this.pmButton2.enabled = false;
			this.pmButton3.enabled = false;
		} else {
			this.pmButton.enabled = true;
			this.pmButton2.enabled = true;
			this.pmButton3.enabled = true;
			
		}
		if (flag) {
			actionPerformed(this.pmButton);
		}
	}
}
