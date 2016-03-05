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

package info.varden.irclinqed.dcc;

import info.varden.irclinqed.IRCLinqed;
import info.varden.irclinqed.irc.IRCThread;
import info.varden.irclinqed.packet.PacketPrivmsg;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

public class DCCRequestPacket extends PacketPrivmsg {
	
	private String[] dccParts;
	private DCCType type;
	private String ip;
	private int port;
	private String param;
	private long size;
	
	public DCCRequestPacket(IRCLinqed il, IRCThread thread, String command) {
		super(il, thread, command);
		String message = this.getMessage().replace("\u0001", "");
		this.dccParts = message.split(" ");
		if ("CHAT".equals(this.dccParts[1])) {
			this.type = DCCType.CHAT;
		} else if ("SEND".equals(this.dccParts[1])) {
			this.type = DCCType.SEND;
		} else {
			this.type = DCCType.UNKNOWN;
		}
		this.param = this.dccParts[2];
		int shift = 0;
		if (this.param.startsWith("\"")) {
			while (!this.param.endsWith("\"")) {
				shift++;
				this.param += " " + this.dccParts[2 + shift];
			}
			this.param = this.param.substring(1, this.param.length() - 2);
		}
		this.ip = this.dccParts[3 + shift];
		this.port = Integer.parseInt(this.dccParts[4 + shift]);
	}
	
	public DCCRequestPacket(IRCLinqed il, IRCThread thread, String target, DCCType type, String param, String ip, int port) {
		this(il, thread, target, type, param, ip, port, 0L);
	}

	public DCCRequestPacket(IRCLinqed il, IRCThread thread, String target, DCCType type, String param, String ip, int port, long size) {
		super(il, thread, target, null);
		this.type = type;
		this.param = param;
		this.port = port;
		this.size = size;
		this.ip = ip;
	}
	
	public DCCType getDCCType() {
		return this.type;
	}
	
	public String getIPAddress() {
		try {
			return InetAddress.getByName(this.ip).getHostAddress();
		} catch (UnknownHostException e) {
			this.il.logSevere("Unknown DCC IP address: " + this.ip);
			e.printStackTrace();
			return null;
		}
	}
	
	public int getPort() {
		return this.port;
	}
	
	public String getArg() {
		return this.param;
	}
	
	@Override
	public void handle() {
		this.il.guiQueue.add(new GuiDCCRequest(this));
	}
	
	@Override
	public boolean send() {
		try {
			InetAddress ip = InetAddress.getByName(this.ip);
			int ipint = ByteBuffer.wrap(ip.getAddress()).getInt();
			this.message = "\u0001DCC " + this.type.name() + " " + this.param + " " + ipint + " " + this.port + (this.type == DCCType.SEND ? " " + this.size : "") + "\u0001";
			return super.send();
		} catch (UnknownHostException e) {
			e.printStackTrace();
			return false;
		}
	}

}
