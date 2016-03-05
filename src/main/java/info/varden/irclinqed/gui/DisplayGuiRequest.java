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
import net.minecraft.client.gui.GuiScreen;

public class DisplayGuiRequest implements Runnable {
	private GuiScreen gui;
	private IRCLinqed il;
	
	public DisplayGuiRequest(IRCLinqed il) {
		this.il = il;
	}
	
	public void create(GuiScreen gui) {
		Runnable threadDisplayGui = new DisplayGuiRequest(gui);
		this.il.th.addToQueue(threadDisplayGui);
	}
	
	private DisplayGuiRequest(GuiScreen gui) {
		this.gui = gui;
	}

	@Override
	public void run() {
		Minecraft.getMinecraft().displayGuiScreen(gui);
	}
}
