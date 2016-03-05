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
import info.varden.irclinqed.ctcp.ActionPacket;
import info.varden.irclinqed.ctcp.CTCPRequestPacket;
import info.varden.irclinqed.event.ChannelMessageReceivedEvent;
import info.varden.irclinqed.event.PrivateMessageReceivedEvent;
import info.varden.irclinqed.irc.IRCPacket;
import info.varden.irclinqed.irc.IRCThread;

public class PacketPrivmsg extends IRCPacket {
	protected String target;
	protected String message;
	
	public PacketPrivmsg(IRCLinqed il, IRCThread thread, String command) {
		super(il, thread, command);
	}
	
	public PacketPrivmsg(IRCLinqed il, IRCThread thread, String target, String message) {
		super(il, thread, null);
		this.target = target;
		this.message = message;
	}

	@Override
	public void handle() {
		if (getMessage().startsWith("\u0001ACTION") && getMessage().endsWith("\u0001")) {
			ActionPacket ctcp = new ActionPacket(this.il, this.thread, this.command);
			ctcp.handle();
			return;
		} else if (getMessage().startsWith("\u0001") && getMessage().endsWith("\u0001")) {
			CTCPRequestPacket ctcp = new CTCPRequestPacket(this.il, this.thread, this.command);
			ctcp.handle();
			return;
		}
		String target = getRawParts()[2];
		if (target.startsWith("#")) {
			addMessageToChat(MessageType.IRC_MESSAGE, target, "<" + getNickname() + "> " + getMessage());
			this.il.getEventRegistry().postEvent(new ChannelMessageReceivedEvent(this));
		} else {
			addMessageToChat(MessageType.IRC_PRIVATE, getNickname(), "<" + getNickname() + "> §f" + getMessage());
			this.il.getEventRegistry().postEvent(new PrivateMessageReceivedEvent(this));
		}
	}
	
	@Override
	public boolean send() {
		if (!this.message.startsWith("\u0001")) {
			if (this.target.startsWith("#")) {
				addMessageToChat(MessageType.IRC_MESSAGE, this.target, "<" + thread.info.getNickname() + "> " + this.message);
			} else {
				addMessageToChat(MessageType.IRC_PRIVATE, this.target, "<" + thread.info.getNickname() + "> §f" + this.message);
			}
		}
		return thread.sendRaw(this, "PRIVMSG " + this.target + " :" + this.message);
	}

	@Override
	public boolean sendsWithMessage() {
		return true;
	}

}
