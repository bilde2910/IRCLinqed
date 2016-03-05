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

import info.varden.irclinqed.packet.Packet323ListEnd;
import info.varden.irclinqed.packet.PacketJoin;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSmallButton;
import net.minecraft.client.gui.GuiTextField;

import org.lwjgl.input.Keyboard;

public class GuiScreenListReply extends GuiScreenWithList {
	private GuiItemList itemList;
	private ListItem[] items = null;
	private ListItem[] initItems = null;
	private Packet323ListEnd packet;
	public GuiSmallButton pmButton;
	private GuiTextField searchField;
	private String searchQuery = "";
	private int textColor = 16777215;
	
	public GuiScreenListReply(Packet323ListEnd packet) {
		this.packet = packet;
		this.items = this.packet.thread.getGuiListItems();
		this.initItems = this.items;
	}
	
	@Override
	public FontRenderer getFontRenderer() {
		return this.fontRenderer;
	}
	
	@Override
	public void initGui() {
		Keyboard.enableRepeatEvents(true);
		this.pmButton = new GuiSmallButton(5, this.width / 2 - 154, this.height - 48, "Join channel");
		this.pmButton.enabled = false;
        this.buttonList.add(this.pmButton);
        this.buttonList.add(new GuiSmallButton(6, this.width / 2 + 4, this.height - 48, "Close"));
        this.searchField = new GuiTextField(this.fontRenderer, this.width / 2 - 38, 40, 190, 20);
        this.itemList = new GuiItemList(this, this.items, 80);
        this.itemList.registerScrollButtons(7, 8);
        this.searchField.setText(this.searchQuery);
        this.searchField.setTextColor(this.textColor);
        this.searchField.setFocused(true);
    }
	
	@Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }
	
	@Override
	public void actionPerformed(GuiButton button) {
		if (button.enabled) {
			if (button.id == 5) {
				PacketJoin packet = new PacketJoin(this.packet.il, this.packet.thread, this.itemList.getSelectedItem().title);
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
    protected void keyTyped(char par1, int par2) {
		String srchTxt = this.searchField.getText();
        if (this.searchField.isFocused()) {
            this.searchField.textboxKeyTyped(par1, par2);
        }
        String rgxQuery = this.searchField.getText();
        if (!srchTxt.equals(rgxQuery)) {
        	try {
        		this.searchField.setTextColor(16777215);
	        	ArrayList<ListItem> tmp = new ArrayList<ListItem>();
	        	Pattern pattern = Pattern.compile(rgxQuery);
	        	for (ListItem item : this.initItems) {
	        		Matcher mt = pattern.matcher(item.title);
	        		Matcher m1 = pattern.matcher(item.line1);
	        		Matcher m2 = pattern.matcher(item.line2);
	        		if (mt.find() || m1.find() || m2.find()) {
	        			tmp.add(item);
	        		}
	        	}
	        	this.items = tmp.toArray(new ListItem[0]);
	        	this.itemList.changeList(this.items);
	        	this.searchQuery = rgxQuery;
        	} catch (PatternSyntaxException ex) {
        		this.searchField.setTextColor(16733525);
        	}
        }
    }
	
	@Override
    protected void mouseClicked(int par1, int par2, int par3) {
        super.mouseClicked(par1, par2, par3);
        this.searchField.mouseClicked(par1, par2, par3);
    }
	
	@Override
	protected void mouseMovedOrUp(int i, int j, int k) {
        super.mouseMovedOrUp(i, j, k);
    }
	
	@Override
	public void drawScreen(int par1, int par2, float par3) {
        this.itemList.drawScreen(par1, par2, par3);
        this.drawCenteredString(this.fontRenderer, this.itemList.getSize() + " results for /list", this.width / 2, 16, 16777215);
        this.drawString(this.fontRenderer, "Search channels:", this.width / 2 - 152, 48, 16777215);
        this.searchField.drawTextBox();
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
