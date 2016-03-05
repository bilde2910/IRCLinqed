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
import info.varden.irclinqed.ServerSettings;
import info.varden.irclinqed.ThreadRunnablePair;
import info.varden.irclinqed.irc.ConnectedServerInfo;

import java.util.ArrayList;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

public class GuiScreenSwapConnections extends GuiScreenWithList {
	private GuiItemList itemList;
	private GuiButton pmButton;
	private GuiButton pmButton2;
	private GuiButton pmButton3;
	private IRCLinqed il;
	
	public GuiScreenSwapConnections(IRCLinqed il) {
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
		ConnectedServerInfo[] sinfo = this.il.connectionManager.getConnectedServersInfo();
		for (ConnectedServerInfo server : sinfo) {
			if (!server.isCurrent()) {
				continue;
			}
			String allChans = "§7Not on any channels";
			String[] chans = server.getChannels();
			if (chans.length > 0) {
				allChans = "Talking in " + chans.length + " channels";
			}
			if (server.isDCC()) {
				allChans = "§eDCC chat session (" + (server.isHosting() ? "host" : "client") + ")";
			}
			list.add(new ListItem(
					server,
					"§l" + server.getNetworkName() + " §r§l(" + server.getHostAndPort() + "§r§l)",
					server.getNickname() + " §r(" + server.getRealname() + "§r)",
					allChans
			));
		}
		for (ConnectedServerInfo server : sinfo) {
			if (server.isCurrent()) {
				continue;
			}
			String allChans = "§7Not on any channels";
			String[] chans = server.getChannels();
			if (chans.length > 0) {
				allChans = "Talking in " + chans.length + " channels";
			}
			list.add(new ListItem(
					server,
					server.getNetworkName() + " §r(" + server.getHostAndPort() + "§r)",
					server.getNickname() + " (" + server.getRealname() + ")",
					allChans
			));
		}
		this.pmButton = new GuiButton(5, this.width / 2 + 2, this.height - 48, 74, 20, "Switch to");
		this.pmButton.enabled = false;
		this.pmButton2 = new GuiButton(6, this.width / 2 - 76, this.height - 48, 74, 20, "Reconnect");
		this.pmButton2.enabled = false;
		this.pmButton3 = new GuiButton(9, this.width / 2 - 154, this.height - 48, 74, 20, "Disconnect");
		this.pmButton3.enabled = false;
        this.buttonList.add(this.pmButton);
        this.buttonList.add(this.pmButton2);
        this.buttonList.add(this.pmButton3);
        this.buttonList.add(new GuiButton(10, this.width / 2 + 80, this.height - 48, 74, 20, "Close"));
        this.itemList = new GuiItemList(this, list.toArray(new ListItem[0]));
        this.itemList.registerScrollButtons(7, 8);
    }
	
	@Override
	public void actionPerformed(GuiButton button) {
		if (button.enabled) {
			if (button.id == 5) {
				Minecraft.getMinecraft().displayGuiScreen((GuiScreen)null);
				this.il.connectionManager.setCurrentServer(((ConnectedServerInfo) this.itemList.getSelectedItem().metaData).getID());
			} else if (button.id == 6) {
				Minecraft.getMinecraft().displayGuiScreen((GuiScreen)null);
				ConnectedServerInfo sinfo = (ConnectedServerInfo) this.itemList.getSelectedItem().metaData;
				ThreadRunnablePair trp = this.il.connectionManager.getConnectionPair(sinfo.getID());
				ServerSettings info = new ServerSettings(sinfo.getHost(), sinfo.getPort(), sinfo.getNickname(), sinfo.getRealname(), sinfo.getChannels());
				trp.runnable.disconnect();
				this.il.connectionManager.connect(info);
			} else if (button.id == 9) {
				Minecraft.getMinecraft().displayGuiScreen((GuiScreen)null);
				ConnectedServerInfo sinfo = (ConnectedServerInfo) this.itemList.getSelectedItem().metaData;
				ThreadRunnablePair trp = this.il.connectionManager.getConnectionPair(sinfo.getID());
				this.il.connectionManager.disconnect(this.il.connectionManager.getConnectionPair(sinfo.getID()), false);
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
        this.drawCenteredString(this.fontRenderer, "Switch to another server", this.width / 2, 16, 16777215);
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
			this.pmButton2.enabled = !this.il.connectionManager.getConnectionPair(((ConnectedServerInfo) selectedItem.metaData).getID()).runnable.isDCC();
			this.pmButton3.enabled = true;
		}
		if (flag) {
			actionPerformed(this.pmButton);
		}
	}
}
