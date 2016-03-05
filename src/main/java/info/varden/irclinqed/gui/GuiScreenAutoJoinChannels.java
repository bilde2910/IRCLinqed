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

import java.util.ArrayList;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiYesNo;
import net.minecraft.client.resources.I18n;

public class GuiScreenAutoJoinChannels extends GuiScreenWithList {
	private GuiItemList itemList;
	private GuiButton pmButton2;
	private IRCLinqed il;
	private final String server;
	private final GuiScreen parentScreen;
	private boolean deleteClicked;
	
	public GuiScreenAutoJoinChannels(IRCLinqed il, GuiScreen parent, String server) {
		this.il = il;
		this.server = server;
		this.parentScreen = parent;
	}
	
	@Override
	public FontRenderer getFontRenderer() {
		return this.fontRenderer;
	}
	
	@Override
	public void initGui() {
		this.buttonList.clear();
		ArrayList<ListItem> list = new ArrayList<ListItem>();
		String[] chanList = this.il.configLoader.getServerSpecificKey(Util.getHost(this.server), Util.getPort(this.server), "autojoin").split(",");
		float seconds = 1.0F;
		for (String chan : chanList) {
			if (!"".equals(server)) {
				list.add(new ListItem(chan, "Join after " + seconds + " seconds", "Server: " + this.server));
				seconds++;
			}
		}
		this.pmButton2 = new GuiButton(6, this.width / 2 - 76, this.height - 48, 74, 20, "Remove");
		this.pmButton2.enabled = false;
        this.buttonList.add(this.pmButton2);
        this.buttonList.add(new GuiButton(9, this.width / 2 - 154, this.height - 48, 74, 20, "Add"));
        this.buttonList.add(new GuiButton(10, this.width / 2 + 2, this.height - 48, 150, 20, "Done"));
        this.itemList = new GuiItemList(this, list.toArray(new ListItem[0]), 32);
        this.itemList.registerScrollButtons(7, 8);
    }
	
	@Override
	public void actionPerformed(GuiButton button) {
		if (button.enabled) {
			if (button.id == 6) {
				this.deleteClicked = true;
                String s1 = I18n.getString("selectServer.deleteQuestion");
                String s2 = "\'" + this.itemList.getSelectedItem().title + "\' " + I18n.getString("selectServer.deleteWarning");
                String s3 = I18n.getString("selectServer.deleteButton");
                String s4 = I18n.getString("gui.cancel");
                GuiYesNo guiyesno = new GuiYesNo(this, s1, s2, s3, s4, this.itemList.getSelectedIndex());
                this.mc.displayGuiScreen(guiyesno);
			} else if (button.id == 9) {
				Minecraft.getMinecraft().displayGuiScreen(new GuiIngameAddChannel(this.il, this, this.server));
			} else if (button.id == 10) {
				Minecraft.getMinecraft().displayGuiScreen(this.parentScreen);
			} else {
				itemList.actionPerformed(button);
			}
		}
	}
	
	@Override
	public void confirmClicked(boolean yes, int index) {
		this.deleteClicked = false;
		if (yes) {
			String chan = this.itemList.getAtIndex(index).title;
			String chanList = this.il.configLoader.getServerSpecificKey(Util.getHost(this.server), Util.getPort(this.server), "autojoin");
			chanList = chanList.replace(chan + ",", "");
			this.il.configLoader.setServerSpecificKey(Util.getHost(this.server), Util.getPort(this.server), "autojoin", chanList);
			this.mc.displayGuiScreen(this);
		}
	}
	
	@Override
	protected void mouseMovedOrUp(int i, int j, int k) {
        super.mouseMovedOrUp(i, j, k);
    }
	
	@Override
	public void drawScreen(int par1, int par2, float par3) {
        this.itemList.drawScreen(par1, par2, par3);
        this.drawCenteredString(this.fontRenderer, "Auto-join channels for " + this.server, this.width / 2, 16, 16777215);
        super.drawScreen(par1, par2, par3);
    }
	
	@Override
	public void updateScreen() {
        super.updateScreen();
    }

	@Override
	public void elementClicked(int index, ListItem selectedItem, boolean flag) {
		if (index < 0) {
			this.pmButton2.enabled = false;
		} else {
			this.pmButton2.enabled = true;
		}
	}
}
