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
import info.varden.irclinqed.irc.IRCPacket;
import info.varden.irclinqed.irc.IRCThread;

public class PacketUser extends IRCPacket {
	private String username;
	private String hostname;
	private String servername;
	private String realname;
	
	public PacketUser(IRCLinqed il, IRCThread thread, String command) {
		super(il, thread, command);
	}
	
	public PacketUser(IRCLinqed il, IRCThread thread, String username, String hostname, String servername, String realname) {
		super(il, thread, null);
		this.username = username;
		this.hostname = hostname;
		this.servername = servername;
		this.realname = realname;
	}

	@Override
	public void handle() {
		
	}

	@Override
	public boolean send() {
		return thread.sendRaw(this, "USER " + this.username + " " + this.hostname + " " + this.servername + " :" + this.realname);
	}

	@Override
	public boolean sendsWithMessage() {
		return true;
	}
}
