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
import info.varden.irclinqed.gui.GuiItemList;
import info.varden.irclinqed.gui.GuiScreenWithList;
import info.varden.irclinqed.gui.ListItem;
import info.varden.irclinqed.irc.IConnection;

import java.io.File;
import java.util.ArrayList;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

public class GuiScreenOpenFile extends GuiScreenWithList {
	private GuiItemList itemList;
	private GuiButton pmButton;
	private IRCLinqed il;
	private IConnection thread;
	private final GuiScreen parentScreen;
	private final String target;
	protected File location = new File(System.getProperty("user.home"));
	private ListItem[] listing;
	private ArrayList<File> names = new ArrayList<File>();
	private ArrayList<Boolean> dirs = new ArrayList<Boolean>();
	
	public GuiScreenOpenFile(IRCLinqed il, IConnection thread, GuiScreen parent, String target) {
		this.il = il;
		this.thread = thread;
		this.parentScreen = parent;
		this.target = target;
	}
	
	private ListItem[] getFileListing() {
		this.names.clear();
		this.dirs.clear();
		ArrayList<ListItem> list = new ArrayList<ListItem>();
		for (File file : this.location.listFiles()) {
			if (file.isDirectory()) {
				list.add(new ListItem(Util.escapeFormatting(file.getName()), "Directory", ""));
				this.names.add(file);
				this.dirs.add(true);
			}
		}
		for (File file : this.location.listFiles()) {
			if (file.isFile()) {
				list.add(new ListItem("§e" + Util.escapeFormatting(file.getName(), "e"), Util.getFileSizeWithUnit(file.length()), ""));
				this.names.add(file);
				this.dirs.add(false);
			}
		}
		return list.toArray(new ListItem[0]);
	}
	
	@Override
	public FontRenderer getFontRenderer() {
		return this.fontRenderer;
	}
	
	@Override
	public void initGui() {
		this.buttonList.clear();
		this.listing = getFileListing();
		this.pmButton = new GuiButton(10, this.width / 2 + 80, this.height - 48, 74, 20, "Open");
		this.pmButton.enabled = false;
        this.buttonList.add(this.pmButton);
        this.buttonList.add(new GuiButton(6, this.width / 2 - 76, this.height - 48, 74, 20, "Up"));
        this.buttonList.add(new GuiButton(9, this.width / 2 - 154, this.height - 48, 74, 20, "Home"));
        this.buttonList.add(new GuiButton(5, this.width / 2 + 2, this.height - 48, 74, 20, "Cancel"));
        this.itemList = new GuiItemList(this, this.listing, 80, 26);
        this.itemList.registerScrollButtons(7, 8);
    }
	
	@Override
	public void actionPerformed(GuiButton button) {
		if (button.enabled) {
			if (button.id == 5) {
				Minecraft.getMinecraft().displayGuiScreen((GuiScreen)null);
			} else if (button.id == 6) {
				this.location = this.location.getParentFile();
				this.listing = getFileListing();
				this.itemList.changeList(this.listing);
			} else if (button.id == 9) {
				this.location = new File(System.getProperty("user.home"));
				this.listing = getFileListing();
				this.itemList.changeList(this.listing);
			} else if (button.id == 10) {
				System.out.println("Title: " + this.itemList.getSelectedItem().title);
				if (this.dirs.get(this.itemList.getSelectedIndex())) {
					this.location = this.names.get(this.itemList.getSelectedIndex());
					System.out.println("Location is now: " + this.location.getAbsolutePath());
					this.listing = getFileListing();
					this.itemList.changeList(this.listing);
				} else {
					GuiIngameConfirmSend gics = new GuiIngameConfirmSend(this.il, this.thread, this.target, this.names.get(this.itemList.getSelectedIndex()));
					this.il.displayRequests.create(gics);
				}
			} else {
				this.itemList.actionPerformed(button);
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
        this.drawCenteredString(this.fontRenderer, "Send file to " + this.target, this.width / 2, 16, 16777215);
        this.drawString(this.fontRenderer, "§7" + Util.escapeFormatting(this.location.getAbsolutePath(), "7"), this.width / 2 - 152, 48, 16777215);
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
