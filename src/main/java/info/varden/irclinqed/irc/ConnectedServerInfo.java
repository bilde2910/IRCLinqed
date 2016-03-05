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

package info.varden.irclinqed.irc;

import info.varden.irclinqed.IRCLinqed;
import info.varden.irclinqed.dcc.DCCThread;

public class ConnectedServerInfo {
	private final IRCLinqed il;
	private final IConnection thread;
	private final int id;
	
	public ConnectedServerInfo(IRCLinqed il, IConnection thread, int id) {
		this.il = il;
		this.thread = thread;
		this.id = id;
	}
	
	public String getNetworkName() {
		return this.thread.getNetworkName();
	}
	
	public String getHostAndPort() {
		return this.thread.getHostAndPort();
	}
	
	public String getHost() {
		return this.thread.getHost();
	}
	
	public int getPort() {
		return this.thread.getPort();
	}
	
	public String getNickname() {
		return this.thread.getNickname();
	}
	
	public String getRealname() {
		return this.thread.getRealname();
	}
	
	public String[] getChannels() {
		return this.thread.getChannels();
	}
	
	public boolean isCurrent() {
		int currentIdx = this.il.connectionManager.getCurrentServer();
		return this.il.connectionManager.getConnectionPair(currentIdx).runnable.equals(this.thread);
	}
	
	public int getID() {
		return this.id;
	}
	
	public boolean isDCC() {
		return this.thread.isDCC();
	}
	
	public boolean isHosting() {
		if (this.thread instanceof DCCThread) {
			return ((DCCThread) this.thread).info.isHosting();
		} else {
			return false;
		}
	}
}
