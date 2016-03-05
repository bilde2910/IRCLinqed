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

package info.varden.irclinqed.ctcp;

import info.varden.irclinqed.IRCLinqed;
import info.varden.irclinqed.MessageType;
import info.varden.irclinqed.irc.IRCThread;
import info.varden.irclinqed.packet.PacketPrivmsg;

public class ActionPacket extends PacketPrivmsg {

	public ActionPacket(IRCLinqed il, IRCThread thread, String command) {
		super(il, thread, command);
	}
	
	public ActionPacket(IRCLinqed il, IRCThread thread, String target, String message) {
		super(il, thread, target, message);
	}
	
	private String getAction() {
		String message = getMessage();
		message = message.substring(7, message.length() - 1).trim();
		return message;
	}
	
	@Override
	public void handle() {
		String target = getRawParts()[2];
		if (target.startsWith("#")) {
			addMessageToChat(MessageType.IRC_ME, target, getNickname() + " " + getAction());
		} else {
			addMessageToChat(MessageType.IRC_PRIVATEME, getNickname(), getNickname() + " " + getAction());
		}
	}
	
	@Override
	public boolean send() {
		if (this.target.startsWith("#")) {
			addMessageToChat(MessageType.IRC_ME, this.target, this.thread.info.getNickname() + " " + this.message);
		} else {
			addMessageToChat(MessageType.IRC_PRIVATEME, this.target, this.thread.info.getNickname() + " " + this.message);
		}
		return thread.sendRaw(this, "PRIVMSG " + this.target + " :\u0001ACTION " + this.message + "\u0001");
	}

}
