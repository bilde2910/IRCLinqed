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

package info.varden.irclinqed.forge;

import info.varden.irclinqed.IRCLinqed;
import info.varden.irclinqed.gui.IKeyGuiReady;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.settings.KeyBinding;
import cpw.mods.fml.client.registry.KeyBindingRegistry.KeyHandler;
import cpw.mods.fml.common.TickType;

public class ForgeKeyBind extends KeyHandler {
	
	private EnumSet tickTypes = EnumSet.of(TickType.CLIENT);
	private final IRCLinqed il;
	private Map<KeyBinding, Boolean> down;

	public ForgeKeyBind(IRCLinqed il, KeyBinding[] keyBindings, boolean[] repeatings) {
		super(keyBindings, repeatings);
		this.il = il;
		this.down = new HashMap<KeyBinding, Boolean>();
	}

	@Override
	public String getLabel() {
		return "IRCLinqed-ForgeKeyBind";
	}

	@Override
	public void keyDown(EnumSet<TickType> types, KeyBinding kb, boolean tickEnd, boolean isRepeat) {
		if (!this.down.containsKey(kb)) {
			this.down.put(kb, false);
		}
		if (Minecraft.getMinecraft().inGameHasFocus && !this.down.get(kb)) {
			this.down.put(kb, true);
			boolean cont = true;
			IKeyGuiReady gui = null;
			for (int i = 0; i < this.il.keyGuiQueue.size() && cont; i++) {
				gui = this.il.keyGuiQueue.get(i);
				if (gui.isReady(kb) && gui instanceof GuiScreen) {
					cont = false;
					this.il.keyGuiQueue.remove(i);
				} else {
					gui = null;
				}
			}
			for (int i = 0; i < this.il.keyListenerQueue.size() && cont; i++) {
				IKeyListener item = this.il.keyListenerQueue.get(i);
				item.invoke(kb);
			}
			if (gui != null) {
				Minecraft.getMinecraft().displayGuiScreen((GuiScreen) gui);
			}
		}
	}

	@Override
	public void keyUp(EnumSet<TickType> types, KeyBinding kb, boolean tickEnd) {
		this.down.put(kb, false);
	}

	@Override
	public EnumSet<TickType> ticks() {
		return this.tickTypes;
	}

}
