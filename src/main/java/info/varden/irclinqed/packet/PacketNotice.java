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
import info.varden.irclinqed.ctcp.CTCPResponsePacket;
import info.varden.irclinqed.event.ChannelNoticeReceivedEvent;
import info.varden.irclinqed.event.PrivateNoticeReceivedEvent;
import info.varden.irclinqed.irc.IRCPacket;
import info.varden.irclinqed.irc.IRCThread;

public class PacketNotice extends IRCPacket {
	protected String target;
	protected String message;
	
	public PacketNotice(IRCLinqed il, IRCThread thread, String command) {
		super(il, thread, command);
	}
	
	public PacketNotice(IRCLinqed il, IRCThread thread, String target, String message) {
		super(il, thread, null);
		this.target = target;
		this.message = message;
	}

	@Override
	public void handle() {
		if (getMessage().startsWith("\u0001") && getMessage().endsWith("\u0001")) {
			CTCPResponsePacket ctcp = new CTCPResponsePacket(this.il, this.thread, this.command);
			ctcp.handle();
			return;
		}
		if (!getNickname().contains(" ")) {
			String target = getRawParts()[2];
			if (target.startsWith("#")) {
				addMessageToChat(MessageType.IRC_CHANNOTICE, target, "<" + getNickname() + "> " + getMessage());
				this.il.getEventRegistry().postEvent(new ChannelNoticeReceivedEvent(this));
			} else {
				addMessageToChat(MessageType.IRC_NOTICE, getNickname(), "<" + getNickname() + "> §f" + getMessage());
				this.il.getEventRegistry().postEvent(new PrivateNoticeReceivedEvent(this));
			}
		}
	}
	
	public boolean send() {
		if (!this.message.startsWith("\u0001")) {
			if (this.target.startsWith("#")) {
				addMessageToChat(MessageType.IRC_CHANNOTICE, this.target, "<" + thread.info.getNickname() + "> " + this.message);
			} else {
				addMessageToChat(MessageType.IRC_NOTICE, this.target, "<" + thread.info.getNickname() + "> §f" + this.message);
			}
		}
		return thread.sendRaw(this, "NOTICE " + this.target + " :" + this.message);
	}

	@Override
	public boolean sendsWithMessage() {
		return true;
	}
}
