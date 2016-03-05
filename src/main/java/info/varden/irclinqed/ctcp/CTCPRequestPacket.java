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
import info.varden.irclinqed.dcc.DCCRequestPacket;
import info.varden.irclinqed.irc.IRCThread;
import info.varden.irclinqed.packet.PacketPrivmsg;

import java.util.Date;

public class CTCPRequestPacket extends PacketPrivmsg {
	protected CTCPType type;
	private String ctcpparam;
	
	public CTCPRequestPacket(IRCLinqed il, IRCThread thread, String command) {
		super(il, thread, command);
		String ctcpField = this.command.split("\u0001")[1];
		String ctcpType = ctcpField.split(" ")[0].toUpperCase();
		if ("PING".equals(ctcpType)) {
			this.type = CTCPType.PING;
			this.ctcpparam = ctcpField.substring(4).trim();
		} else if ("SOURCE".equals(ctcpType)) {
			this.type = CTCPType.SOURCE;
			this.ctcpparam = ctcpField.substring(6).trim();
		} else if ("TIME".equals(ctcpType)) {
			this.type = CTCPType.TIME;
			this.ctcpparam = ctcpField.substring(4).trim();
		} else if ("VERSION".equals(ctcpType)) {
			this.type = CTCPType.VERSION;
			this.ctcpparam = ctcpField.substring(7).trim();
		} else if ("DCC".equals(ctcpType)) {
			this.type = CTCPType.DCC;
			this.ctcpparam = ctcpField;
		} else {
			this.type = CTCPType.UNKNOWN;
			this.ctcpparam = ctcpField;
		}
	}
	
	public CTCPRequestPacket(IRCLinqed il, IRCThread thread, CTCPType type, String user) {
		super(il, thread, user, null);
		this.type = type;
		if (type == CTCPType.PING) {
			this.message = "\u0001PING\u0001";
		} else if (type == CTCPType.SOURCE) {
			this.message = "\u0001SOURCE\u0001";
		} else if (type == CTCPType.TIME) {
			this.message = "\u0001TIME\u0001";
		} else if (type == CTCPType.VERSION) {
			this.message = "\u0001VERSION\u0001";
		}
	}
	
	public CTCPType getCTCPType() {
		return this.type;
	}
	
	@Override
	public void handle() {
		if (this.type == CTCPType.PING) {
			CTCPResponsePacket response = new CTCPResponsePacket(this.il, this.thread, CTCPType.PING, getNickname(), this.ctcpparam);
			response.send();
			addMessageToChat(MessageType.IRC_CTCP, "§cCTCP Ping request received from " + getNickname());
		} else if (type == CTCPType.SOURCE) {
			CTCPResponsePacket response = new CTCPResponsePacket(this.il, this.thread, CTCPType.ERRMSG, getNickname(), "SOURCE :Not supported");
			response.send();
			addMessageToChat(MessageType.IRC_CTCP, "§cCTCP Source request received from " + getNickname());
		} else if (type == CTCPType.TIME) {
			CTCPResponsePacket response = new CTCPResponsePacket(this.il, this.thread, CTCPType.TIME, getNickname(), String.format("%tc", new Date()));
			response.send();
			addMessageToChat(MessageType.IRC_CTCP, "§cCTCP Time request received from " + getNickname());
		} else if (type == CTCPType.VERSION) {
			CTCPResponsePacket response = new CTCPResponsePacket(this.il, this.thread, CTCPType.VERSION, getNickname(), "IRCLinqed " + IRCLinqed.VERSION + " in-game Minecraft IRC client (by bilde2910)");
			response.send();
			addMessageToChat(MessageType.IRC_CTCP, "§cCTCP Version request received from " + getNickname());
		} else if (type == CTCPType.DCC) {
			if (this.il.configLoader.getServerSpecificBooleanKey("allowdcc", this.thread.getHost(), this.thread.getPort())) {
				DCCRequestPacket packet = new DCCRequestPacket(this.il, this.thread, this.command);
				packet.handle();
			}
		} else {
			CTCPResponsePacket response = new CTCPResponsePacket(this.il, this.thread, CTCPType.ERRMSG, getNickname(), this.ctcpparam.split(" ")[0] + " :Not supported");
			response.send();
			addMessageToChat(MessageType.IRC_CTCP, "§cUnknown CTCP request (" + this.ctcpparam.split(" ")[0] + ") received from " + getNickname());
		}
	}
	
	@Override
	public boolean send() {
		String pingts = "";
		if (this.type == CTCPType.PING) {
			pingts = " " + System.currentTimeMillis();
		}
		if (this.type != CTCPType.UNKNOWN) {
			this.message = "\u0001" + this.type.toString() + pingts + "\u0001";
		} else {
			addMessageToChat(MessageType.LQS_ERROR, "Could not send CTCP packet: unknown type!");
		}
		return super.send();
	}
}
