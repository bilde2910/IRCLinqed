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

package info.varden.irclinqed.packet;

import info.varden.irclinqed.IRCLinqed;
import info.varden.irclinqed.MessageType;
import info.varden.irclinqed.irc.IRCThread;
import info.varden.irclinqed.irc.NumericResponsePacket;

import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiScreen;

public class Packet433NicknameInUse extends NumericResponsePacket {
	public Packet433NicknameInUse(IRCLinqed il, IRCThread thread, String command) {
		super(il, thread, command);
	}

	@Override
	public void handle() {
		if (thread.isConnecting()) {
			String failedNick = getRawParts()[2];
			if (!failedNick.matches("IRCLinqed\\d+")) {
				String nick = "IRCLinqed" + (new Random().nextInt(9999-1000)+1000);
				addMessageToChat(MessageType.SERVER_ERROR, thread.getNetworkName(), "Nick " + failedNick + " already taken, trying again with " + nick);
				thread.info.setNickname(nick);
				PacketNick packetNick = new PacketNick(this.il, this.thread, nick);
				packetNick.send();
			} else {
				addMessageToChat(MessageType.SERVER_ERROR, thread.getNetworkName(), "Nick " + failedNick + " already taken, please enter a new nick manually");
				Minecraft.getMinecraft().displayGuiScreen((GuiScreen)null);
				Minecraft.getMinecraft().displayGuiScreen(new GuiChat(this.il.configLoader.getKey("chatchar", "#") + " /nick "));
			}
		}
		writeRawReplyToChatWithFirstParam(MessageType.SERVER_ERROR, thread.getNetworkName());
	}
}
