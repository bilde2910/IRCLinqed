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
import info.varden.irclinqed.event.UserLeftEvent;
import info.varden.irclinqed.irc.IRCPacket;
import info.varden.irclinqed.irc.IRCThread;

public class PacketPart extends IRCPacket {
	public PacketPart(IRCLinqed il, IRCThread thread, String command) {
		super(il, thread, command);
	}

	@Override
	public void handle() {
		addMessageToChat(MessageType.IRC_LEAVE, getRawParts()[2], getNickname() + " left channel (" + getMessage() + ")");
		this.il.getEventRegistry().postEvent(new UserLeftEvent(this));
	}

	@Override
	public boolean send() {
		if (thread.removeChannel(this.command)) {
			thread.setChannel(null);
			return thread.sendRaw(this, "PART " + this.command + " :" + getKey("partreason"));
		}
		return true;
	}

	@Override
	public boolean sendsWithMessage() {
		return true;
	}
}
