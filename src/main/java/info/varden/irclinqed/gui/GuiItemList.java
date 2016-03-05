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

import info.varden.irclinqed.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiSlot;
import net.minecraft.client.renderer.Tessellator;

public class GuiItemList extends GuiSlot {
	
	private GuiScreenWithList mainGuiScreen;
	private volatile ListItem[] items;
	private ListItem selectedItem;
	private int currentlySelected = -1;
	
	protected final int slotHeight;

	public GuiItemList(GuiScreenWithList guiScreen, ListItem[] items) {
		this(guiScreen, items, 32);
	}
	
	public GuiItemList(GuiScreenWithList guiScreen, ListItem[] items, int topSpacing) {
		this(guiScreen, items, topSpacing, 36);
	}
	
	public GuiItemList(GuiScreenWithList guiScreen, ListItem[] items, int topSpacing, int slotHeight) {
		super(Minecraft.getMinecraft(), guiScreen.width, guiScreen.height, topSpacing, guiScreen.height - 55 + 4, slotHeight);
		this.slotHeight = slotHeight;
		this.mainGuiScreen = guiScreen;
		this.items = items;
	}
	
	public ListItem getAtIndex(int index) {
		return this.items[index];
	}
	
	public ListItem getSelectedItem() {
		return this.selectedItem;
	}
	
	public int getSelectedIndex() {
		return this.currentlySelected;
	}
	
	public void changeList(ListItem[] list) {
		this.items = list;
	}

	@Override
	protected int getSize() {
		return this.items.length;
	}

	@Override
	protected void elementClicked(int i, boolean flag) {
		this.selectedItem = this.items[i];
		this.currentlySelected = i;
		this.mainGuiScreen.elementClicked(i, this.selectedItem, flag);
	}

	@Override
	protected boolean isSelected(int i) {
		return this.currentlySelected == i;
	}

	@Override
	protected void drawBackground() {
		this.mainGuiScreen.drawDefaultBackground();
	}
	
	@Override
	protected int getContentHeight()
    {
        return this.getSize() * this.slotHeight;
    }

	@Override
	protected void drawSlot(int i, int j, int k, int l, Tessellator tessellator) {
		if (i < this.items.length) {
	        String s = this.items[i].title;
	
	        if (s.length() > 40)
	        {
	            s = Util.parseIRCFormatting(s.substring(0, 40).trim() + "...");
	        }
	        
	        String t = this.items[i].line1;
	
	        if (t.length() > 40)
	        {
	            t = Util.parseIRCFormatting(t.substring(0, 40).trim() + "...");
	        }
	        
	        String u = this.items[i].line2;
	
	        if (u.length() > 40)
	        {
	            u = Util.parseIRCFormatting(u.substring(0, 40).trim() + "...");
	        }
	        this.mainGuiScreen.drawString(mainGuiScreen.getFontRenderer(), s, j + 2, k + 1, this.items[i].titleColor);
	        this.mainGuiScreen.drawString(mainGuiScreen.getFontRenderer(), t, j + 2, k + 12 + 10 * 0, this.items[i].line1Color);
	        this.mainGuiScreen.drawString(mainGuiScreen.getFontRenderer(), u, j + 2, k + 12 + 10 * 1, this.items[i].line2Color);
		}
	}

}
