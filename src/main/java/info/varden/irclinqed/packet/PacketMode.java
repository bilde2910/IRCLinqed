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
import info.varden.irclinqed.event.ChannelModeChangedEvent;
import info.varden.irclinqed.event.ChannelModeratedEvent;
import info.varden.irclinqed.event.UserAdminEvent;
import info.varden.irclinqed.event.UserBannedEvent;
import info.varden.irclinqed.event.UserHalfopEvent;
import info.varden.irclinqed.event.UserModeChangedEvent;
import info.varden.irclinqed.event.UserOpEvent;
import info.varden.irclinqed.event.UserOwnerEvent;
import info.varden.irclinqed.event.UserQuietEvent;
import info.varden.irclinqed.event.UserVoiceEvent;
import info.varden.irclinqed.irc.IRCPacket;
import info.varden.irclinqed.irc.IRCThread;

public class PacketMode extends IRCPacket {
	private String channel;
	private String flags;
	private String user;
	
	public PacketMode(IRCLinqed il, IRCThread thread, String command) {
		super(il, thread, command);
	}
	
	public PacketMode(IRCLinqed il, IRCThread thread, String channel, String flags, String user) {
		super(il, thread, null);
		this.channel = channel;
		this.flags = flags;
		this.user = user;
	}

	@Override
	public void handle() {
		if (getBooleanKey("showmode")) {
			if (!getRawParts()[0].contains("!")) {
				return;
			}
			char[] modes = getParam(1).toCharArray();
			boolean give = true;
			int flag = 0;
			for (int i = 0; i < modes.length; i++) {
				if (modes[i] == '+') {
					give = true;
				} else if (modes[i] == '-') {
					give = false;
				} else {
					if (getParam(2) != null) {
						String user = getParam(flag + 2);
						this.il.getEventRegistry().postEvent(new UserModeChangedEvent(this, user, give, modes[i]));
						if (modes[i] == 'q' && this.thread.iSupport.get("PREFIX").contains("q")) {
							addMessageToChat(MessageType.IRC_USERACTION, getParam(0), getNickname() + " " + (give ? "gives" : "removes") + " owner " + (give ? "to" : "from") + " " + user);
							this.il.getEventRegistry().postEvent(new UserOwnerEvent(this, user, give));
						} else if (modes[i] == 'a') {
							addMessageToChat(MessageType.IRC_USERACTION, getParam(0), getNickname() + " " + (give ? "gives" : "removes") + " admin " + (give ? "to" : "from") + " " + user);
							this.il.getEventRegistry().postEvent(new UserAdminEvent(this, user, give));
						} else if (modes[i] == 'o') {
							addMessageToChat(MessageType.IRC_USERACTION, getParam(0), getNickname() + " " + (give ? "gives" : "removes") + " op " + (give ? "to" : "from") + " " + user);
							this.il.getEventRegistry().postEvent(new UserOpEvent(this, user, give));
						} else if (modes[i] == 'h'){
							addMessageToChat(MessageType.IRC_USERACTION, getParam(0), getNickname() + " " + (give ? "gives" : "removes") + " half-op " + (give ? "to" : "from") + " " + user);
							this.il.getEventRegistry().postEvent(new UserHalfopEvent(this, user, give));
						} else if (modes[i] == 'v'){
							addMessageToChat(MessageType.IRC_USERACTION, getParam(0), getNickname() + " " + (give ? "gives" : "removes") + " voice " + (give ? "to" : "from") + " " + user);
							this.il.getEventRegistry().postEvent(new UserVoiceEvent(this, user, give));
						} else if (modes[i] == 'q' && !this.thread.iSupport.get("PREFIX").contains("q")){
							addMessageToChat(MessageType.IRC_USERACTION, getParam(0), getNickname() + " " + (give ? "silences" : "unsilences") + " " + user + " on channel");
							this.il.getEventRegistry().postEvent(new UserQuietEvent(this, user, give));
						} else if (modes[i] == 'b'){
							addMessageToChat(MessageType.IRC_USERACTION, getParam(0), getNickname() + " " + (give ? "bans" : "unbans") + " " + user + " on channel");
							this.il.getEventRegistry().postEvent(new UserBannedEvent(this, user, give));
						} else {
							addMessageToChat(MessageType.IRC_USERACTION, getParam(0), getNickname() + " sets mode " + (give ? "+" : "-") + modes[i] + " on " + user);
						}
					} else {
						this.il.getEventRegistry().postEvent(new ChannelModeChangedEvent(this, give, modes[i]));
						if (modes[i] == 'm') {
							addMessageToChat(MessageType.IRC_USERACTION, getParam(0), getNickname() + " " + (give ? "sets" : "removes") + " channel moderated status");
							this.il.getEventRegistry().postEvent(new ChannelModeratedEvent(this, give));
						} else {
							addMessageToChat(MessageType.IRC_USERACTION, getParam(0), getNickname() + " sets mode " + (give ? "+" : "-") + modes[i] + " on channel");
						}
					}
					flag += 1;
				}
			}
		}
	}

	@Override
	public boolean send() {
		return thread.sendRaw(this, "MODE " + this.channel + " " + this.flags + (this.user == null ? "" : " " + this.user));
	}

	@Override
	public boolean sendsWithMessage() {
		return false;
	}
}
