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
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;

import org.lwjgl.opengl.GL11;

public class GuiSimpleOverlay extends GuiOverlay {
	
	private final Minecraft mc;
	private final IRCLinqed il;
	
	private String title;
	private int titleColor = 0x55FFFF;
	private String message;
	private int messageColor = 0xFFFFFF;
	private float[] rgbaHue;
	
	public GuiSimpleOverlay(IRCLinqed il, float[] argbHue, String title, String message) {
		this(il, argbHue, title, message, 0x55FFFF, 0xFFFFFF);
	}
	
	public GuiSimpleOverlay(IRCLinqed il, float[] rgbaHue, String title, String message, int titleColor, int messageColor) {
		this.il = il;
		this.mc = Minecraft.getMinecraft();
		this.title = title;
		this.message = message;
		this.titleColor = titleColor;
		this.messageColor = messageColor;
		this.rgbaHue = rgbaHue;
	}
	
	public String getTitle() {
		return this.title;
	}
	
	public String getMessage() {
		return this.message;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public void setMessage(String message) {
		this.message = message;
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
		
		if (this.rgbaHue.length >= 4) {
			GL11.glColor4f(this.rgbaHue[0], this.rgbaHue[1], this.rgbaHue[2], this.rgbaHue[3]);
		} else {
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		}
		GL11.glDisable(GL11.GL_LIGHTING);
		this.mc.renderEngine.bindTexture(new ResourceLocation("textures/gui/achievement/achievement_background.png"));
		this.drawTexturedModalRect(xPos, yPos, xTex, yTex, xSize, ySize);
		
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.fontRenderer.drawString(this.title, 8, 7, this.titleColor);
		this.mc.fontRenderer.drawString(this.message, 8, 17, this.messageColor);
	}

	@Override
	public void unload() {
		this.il.guiQueue.remove(this);
	}

	@Override
	public void load() {
		this.il.guiQueue.add(this);
	}

}
