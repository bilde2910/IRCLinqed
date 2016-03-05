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
import info.varden.irclinqed.packet.PacketNotice;

public class CTCPResponsePacket extends PacketNotice {
	private CTCPType type;
	private String ctcpparam;

	public CTCPResponsePacket(IRCLinqed il, IRCThread thread, String command) {
		super(il, thread, command);
		String ctcpField = this.command.split("\u0001")[1];
		String ctcpType = ctcpField.split(" ")[0].toUpperCase();
		if ("ERRMSG".equalsIgnoreCase(ctcpType)) {
			this.type = CTCPType.ERRMSG;
			this.ctcpparam = ctcpField.substring(6).trim();
		} else if ("PING".equalsIgnoreCase(ctcpType)) {
			this.type = CTCPType.PING;
			this.ctcpparam = ctcpField.substring(5).trim();
		} else if ("SOURCE".equalsIgnoreCase(ctcpType)) {
			this.type = CTCPType.SOURCE;
			this.ctcpparam = ctcpField.substring(6).trim();
		} else if ("TIME".equalsIgnoreCase(ctcpType)) {
			this.type = CTCPType.TIME;
			this.ctcpparam = ctcpField.substring(5).trim();
		} else if ("VERSION".equalsIgnoreCase(ctcpType)) {
			this.type = CTCPType.VERSION;
			this.ctcpparam = ctcpField.substring(7).trim();
		} else {
			this.type = CTCPType.UNKNOWN;
			this.ctcpparam = ctcpField;
		}
	}
	
	public CTCPResponsePacket(IRCLinqed il, IRCThread thread, CTCPType type, String user, String command) {
		super(il, thread, user, null);
		this.type = type;
		if (!"".equals(command) && command != null) {
			command = " " + command;
		}
		if (type == CTCPType.PING) {
			this.message = "\u0001PING" + command + "\u0001";
		} else if (type == CTCPType.SOURCE) {
			this.message = "\u0001SOURCE" + command + "\u0001";
		} else if (type == CTCPType.TIME) {
			this.message = "\u0001TIME" + command + "\u0001";
		} else if (type == CTCPType.VERSION) {
			this.message = "\u0001VERSION" + command + "\u0001";
		} else if (type == CTCPType.ERRMSG) {
			this.message = "\u0001ERRMSG" + command + "\u0001";
		}
	}

	public CTCPType getCTCPType() {
		return this.type;
	}
	
	private String getDeCTCPdMessage() {
		String message = getMessage();
		return message.substring(1, message.length() - 1);
	}
	
	@Override
	public void handle() {
		if (this.type == CTCPType.ERRMSG) {
			addMessageToChat(MessageType.IRC_CTCP, "§4CTCP query failed: " + this.ctcpparam);
		} else if (this.type == CTCPType.PING) {
			addMessageToChat(MessageType.IRC_CTCP, "§cCTCP Ping reply from " + getNickname());
		} else if (this.type == CTCPType.SOURCE) {
			addMessageToChat(MessageType.IRC_CTCP, "§cCTCP Source reply from " + getNickname());
		} else if (this.type == CTCPType.TIME) {
			addMessageToChat(MessageType.IRC_CTCP, "§cCTCP Time reply from " + getNickname() + ": " + getDeCTCPdMessage());
		} else if (this.type == CTCPType.VERSION) {
			addMessageToChat(MessageType.IRC_CTCP, "§cCTCP Version reply from " + getNickname() + ": " + getDeCTCPdMessage());
		}
	}
	
	@Override
	public boolean send() {
		return super.send();
	}
}
