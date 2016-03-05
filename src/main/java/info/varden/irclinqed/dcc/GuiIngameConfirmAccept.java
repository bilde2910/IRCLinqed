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
import info.varden.irclinqed.forge.KeyAssociation;
import info.varden.irclinqed.gui.IKeyGuiReady;
import info.varden.irclinqed.irc.IRCThread;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiYesNo;
import net.minecraft.client.settings.KeyBinding;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiIngameConfirmAccept extends GuiYesNo implements IKeyGuiReady
{
    private String openConnWarning;
    private DCCRequestPacket packet;
    private boolean ready = false;
    private GuiDCCRequest overlay;
    private IRCLinqed il;
    private IRCThread thread;

    public GuiIngameConfirmAccept(DCCRequestPacket packet, GuiDCCRequest overlay) {
        super(null, "Are you sure you want to connect to this user?", packet.getNickname() + " (" + packet.getIPAddress() + ":" + packet.getPort() + ")", 0);
        this.buttonText1 = "Yes";
        this.buttonText2 = "No";
        this.openConnWarning = "Never open connections to anyone you don't trust!";
        this.packet = packet;
        this.overlay = overlay;
        this.il = packet.il;
        this.thread = packet.thread;
    }

    @Override
    public void initGui() {
    	this.buttonList.clear();
        this.buttonList.add(new GuiButton(0, this.width / 2 - 129, this.height / 6 + 96, 125, 20, this.buttonText1));
        this.buttonList.add(new GuiButton(1, this.width / 2 + 4, this.height / 6 + 96, 125, 20, this.buttonText2));
        this.overlay.unload();
    }

    @Override
    protected void actionPerformed(GuiButton button) {
    	setReady(false);
        if (button.id == 0) {
        	if (this.packet.getDCCType() == DCCType.CHAT) {
	        	DCCServerSettings info = new DCCServerSettings(this.thread, this.packet.getIPAddress(), this.packet.getPort(), this.thread.getNickname(), this.packet.getNickname());
	        	this.il.connectionManager.connect(info);
        	} else if (this.packet.getDCCType() == DCCType.SEND) {
        		Runnable r = new FileReceiveThread(this.packet);
        		Thread t = new Thread(r);
        		t.start();
        	}
        	this.mc.displayGuiScreen((GuiScreen) null);
        } else if (button.id == 1) {
        	this.mc.displayGuiScreen((GuiScreen) null);
        }
    }

    @Override
    public void drawScreen(int par1, int par2, float par3) {
        super.drawScreen(par1, par2, par3);
        this.drawCenteredString(this.fontRenderer, this.openConnWarning, this.width / 2, 110, 16764108);
    }
    
    public void setReady(boolean ready) {
    	this.ready = ready;
    }

	@Override
	public boolean isReady(KeyBinding binding) {
		if (binding.keyDescription.equals(KeyAssociation.DCC_REJECT.getName())) {
			this.il.guiQueue.remove(this.overlay);
	        this.overlay.unload();
	        setReady(false);
	        this.il.keyGuiQueue.remove(this);
		}
		return this.ready && binding.keyDescription.equals(KeyAssociation.DCC_ACCEPT.getName());
	}
}
