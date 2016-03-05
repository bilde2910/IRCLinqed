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
import info.varden.irclinqed.event.UserJoinedEvent;
import info.varden.irclinqed.irc.IRCPacket;
import info.varden.irclinqed.irc.IRCThread;

public class PacketJoin extends IRCPacket {
	public PacketJoin(IRCLinqed il, IRCThread thread, String command) {
		super(il, thread, command);
	}

	@Override
	public void handle() {
		addMessageToChat(MessageType.IRC_JOIN, getRawParts()[2], getNickname() + " joined channel");
		this.il.getEventRegistry().postEvent(new UserJoinedEvent(this));
	}

	@Override
	public boolean send() {
		addMessageToChat(MessageType.LQS_CHANANNOUNCE, "Now talking in " + this.command);
		thread.setChannel(this.command);
		if (thread.addChannel(this.command)) {
			return thread.sendRaw(this, "JOIN " + this.command);
		}
		return true;
	}

	@Override
	public boolean sendsWithMessage() {
		return false;
	}
}
