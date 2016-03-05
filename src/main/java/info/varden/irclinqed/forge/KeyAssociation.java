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

import java.util.ArrayList;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;

import org.lwjgl.input.Keyboard;

public enum KeyAssociation {
	DCC_ACCEPT("Accept DCC", Keyboard.KEY_Y),
	DCC_REJECT("Reject DCC", Keyboard.KEY_U),
	DCC_CANCEL("Abort DCC Connection", Keyboard.KEY_G),
	CH_SERVERS("Select Active IRC Server", Keyboard.KEY_R),
	CH_CHANNEL("Select Active IRC Channel", Keyboard.KEY_F);
	
	private final String name;
	private final int defaultBinding;
	private final Minecraft mc = Minecraft.getMinecraft();
	
	public static KeyBinding[] getAllDefaultKeyBindings() {
		ArrayList<KeyBinding> kb = new ArrayList<KeyBinding>();
		KeyAssociation[] ka = KeyAssociation.class.getEnumConstants();
		for (KeyAssociation key : ka) {
			kb.add(new KeyBinding(key.getName(), key.getDefaultBinding()));
		}
		return kb.toArray(new KeyBinding[0]);
	}
	
	private KeyAssociation(String name, int defaultBinding) {
		this.name = name;
		this.defaultBinding = defaultBinding;
	}
	
	public boolean is(KeyBinding kb) {
		return kb.keyDescription.equals(this.name);
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getDisplayedName() {
		return this.mc.gameSettings.getKeyDisplayString(this.getCurrentBinding());
	}
	
	public int getDefaultBinding() {
		return this.defaultBinding;
	}
	
	public int getCurrentBinding() {
		for (int i = 0; true; i++) {
			try {
				if (this.mc.gameSettings.getKeyBindingDescription(i).equals(this.name)) {
					return this.mc.gameSettings.keyBindings[i].keyCode;
				}
			} catch (ArrayIndexOutOfBoundsException ex) {
				break;
			}
		}
		return this.defaultBinding;
	}
}
