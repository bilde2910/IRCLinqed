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
import info.varden.irclinqed.forge.KeyAssociation;
import info.varden.irclinqed.gui.GuiOverlay;
import info.varden.irclinqed.gui.GuiTimer;
import info.varden.irclinqed.gui.ITimerGui;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;

import org.lwjgl.opengl.GL11;

public class GuiFileProgress extends GuiOverlay implements ITimerGui {
	
	private final Minecraft mc;
	private final GuiTimer timer;
	private final GuiTimer byteTimer;
	private final IFileTransferConnection s;
	private final IRCLinqed il;
	
	private String title;
	private int titleColor = 0x55FFFF;
	private String message;
	private boolean mToggle = false;
	private boolean showBytes = false;
	
	private String ka_Cancel = "G";
	
	public GuiFileProgress(IRCLinqed il, IFileTransferConnection s) {
		this.il = il;
		this.s = s;
		this.mc = Minecraft.getMinecraft();
		this.title = "DCC file transfer";
		this.ka_Cancel = KeyAssociation.DCC_CANCEL.getDisplayedName();
		this.ticked(0, 0);
		this.ticked(1, 0);
		this.timer = new GuiTimer(this, 1, 500);
		this.byteTimer = new GuiTimer(this, 0, 50);
	}
	
	public void cancel() {
		this.byteTimer.stop();
		this.message = "§cAborting...";
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
		
		GL11.glColor4f(0.0F, 0.0F, 1.0F, 1.0F);
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
		this.byteTimer.stop();
		this.il.guiQueue.remove(this);
	}

	@Override
	public void load() {
		this.il.guiQueue.add(this);
	}

	@Override
	public void ticked(int id, int tickCount) {
		if (id == 0) {
			long tSize = this.s.getTotalSize();
			long cSize = this.s.getByteCount();
			this.message = Util.getFileSizeWithUnit(cSize) + " of " + Util.getFileSizeWithUnit(tSize);
		} else if (id == 1) {
			if (tickCount % 12 == 0) {
				this.title = "DCC file transfer";
				this.mToggle = !this.mToggle;
			} else if (tickCount % 8 == 0 && !this.mToggle) {
				this.title = "User: " + (s.isSending() ? s.getReceiver() : s.getSender());
				this.showBytes = false;
			} else if ((tickCount % 4 == 0 && !this.mToggle) || (tickCount % 8 == 0 && this.mToggle)) {
				this.title = "Press " + this.ka_Cancel + " to cancel";
				this.showBytes = false;
			} else if (tickCount % 4 == 0 && this.mToggle) {
				this.title = "User: " + (s.isSending() ? s.getReceiver() : s.getSender());
				this.showBytes = false;
			}
		}
	}

}
