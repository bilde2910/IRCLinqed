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
import info.varden.irclinqed.gui.GuiOverlay;
import info.varden.irclinqed.gui.GuiTimer;
import info.varden.irclinqed.gui.ITimerGui;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;

import org.lwjgl.opengl.GL11;

public class GuiDCCRequest extends GuiOverlay implements ITimerGui {
	
	private final IRCLinqed il;
	private final Minecraft mc;
	private final GuiTimer timer;
	private final DCCRequestPacket packet;
	
	private String title;
	private int titleColor = 0xFF0000;
	private String message;
	private boolean mToggle = false;
	
	private String ka_Accept = "Y";
	private String ka_Reject = "U";
	
	public GuiDCCRequest(DCCRequestPacket packet) {
		this.il = packet.il;
		this.mc = Minecraft.getMinecraft();
		this.packet = packet;
		if (this.packet.getDCCType() == DCCType.CHAT) {
			this.title = "Incoming DCC chat";
		} else if (this.packet.getDCCType() == DCCType.SEND) {
			this.title = "Incoming DCC file transfer";
		} else {
			this.title = "Unknown DCC request";
		}
		this.ka_Accept = KeyAssociation.DCC_ACCEPT.getDisplayedName();
		this.ka_Reject = KeyAssociation.DCC_REJECT.getDisplayedName();
		this.message = "User: " + packet.getNickname();
		GuiIngameConfirmAccept gui = new GuiIngameConfirmAccept(packet, this);
		gui.setReady(true);
		this.il.keyGuiQueue.add(gui);
		this.timer = new GuiTimer(this, 0, 500);
	}

	@Override
	public void render(RenderGameOverlayEvent event) {
		if (event.isCancelable() || event.type != ElementType.ALL) {
			return;
		}
		
		int xPos = 0;
		int yPos = 0;
		int xTex = 96;
		int yTex = 202;
		int xSize = 255 - xTex + 1;
		int ySize = 233 - yTex + 1;
		
		GL11.glColor4f(1.0F, 0.0F, 0.0F, 1.0F);
		GL11.glDisable(GL11.GL_LIGHTING);
		this.mc.renderEngine.bindTexture(new ResourceLocation("textures/gui/achievement/achievement_background.png"));
		this.drawTexturedModalRect(xPos, yPos, xTex, yTex, xSize, ySize);
		
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.fontRenderer.drawString(this.title, 8, 7, this.titleColor);
		this.mc.fontRenderer.drawString(this.message, 8, 17, 0xFFFFFF);
	}

	@Override
	public void unload() {
		this.timer.stop();
		this.il.guiQueue.remove(this);
	}

	@Override
	public void load() {
		this.il.guiQueue.add(this);
	}

	@Override
	public void ticked(int id, int tickCount) {
		if (tickCount % 2 == 0) {
			this.titleColor = 0xFF0000;
		} else {
			this.titleColor = 0xFFFF00;
		}
		if (tickCount % 12 == 0) {
			this.message = "User: " + packet.getNickname();
			this.mToggle = !this.mToggle;
		} else if (tickCount % 8 == 0 && !this.mToggle) {
			this.message = "Press " + this.ka_Reject + " to reject";
		} else if ((tickCount % 4 == 0 && !this.mToggle) || (tickCount % 8 == 0 && this.mToggle)) {
			this.message = "Press " + this.ka_Accept + " to accept";
		} else if (tickCount % 4 == 0 && this.mToggle) {
			this.message = "Press " + this.ka_Reject + " to reject";
		}
	}

}
