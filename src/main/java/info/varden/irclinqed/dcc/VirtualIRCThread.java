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
import info.varden.irclinqed.ServerSettings;
import info.varden.irclinqed.irc.IConnection;
import info.varden.irclinqed.irc.IRCPacket;
import info.varden.irclinqed.irc.IRCThread;

public class VirtualIRCThread extends IRCThread implements IConnection {
	
	private DCCThread thread;
	private IRCLinqed il;

	public VirtualIRCThread(IRCLinqed il, DCCThread thread) {
		super(il, new ServerSettings(thread.getHost(), thread.getPort(), thread.getNickname(), thread.getNickname(), new String[0]));
		this.il = il;
		this.thread = thread;
		this.util = new VirtualUtil(il, thread);
	}
	
	@Override
	public boolean sendRaw(IRCPacket packet, String s) {
		try {
			if (packet.sendsWithMessage()) {
				return this.thread.sendRaw(s.split(":")[1]);
			} else {
				return this.thread.sendRaw(s);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	@Override
	public void run() {
		
	}

	@Override
	public String getNetworkName() {
		return this.thread.getNetworkName();
	}

	@Override
	public String getHost() {
		return this.thread.getHost();
	}

	@Override
	public int getPort() {
		return this.thread.getPort();
	}

	@Override
	public boolean isDCC() {
		return true;
	}

	@Override
	public String getNickname() {
		return this.thread.getNickname();
	}

}
