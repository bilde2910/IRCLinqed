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

import info.varden.irclinqed.irc.IRCThread;

public class DCCServerSettings {
	private String host;
	private int port;
	private String nickname;
	private String target;
	private IRCThread parentThread;
	private final boolean hosting;
	
	public DCCServerSettings(IRCThread parentThread, String host, int port, String nickname, String target) {
		this.host = host;
		this.port = port;
		this.nickname = nickname;
		this.target = target;
		this.parentThread = parentThread;
		this.hosting = false;
	}
	
	public DCCServerSettings(IRCThread parentThread, String nickname, String target) {
		this.nickname = nickname;
		this.target = target;
		this.parentThread = parentThread;
		this.hosting = true;
	}
	
	public String getHost() {
		return this.host;
	}
	
	public int getPort() {
		return this.port;
	}
	
	public String getNickname() {
		return this.nickname;
	}
	
	public String getTarget() {
		return this.target;
	}
	
	public IRCThread getParentThread() {
		return this.parentThread;
	}
	
	public boolean setNickname(String nick) {
		if (this.nickname == nick) {
			return false;
		} else {
			this.nickname = nick;
			return true;
		}
	}
	
	public boolean isHosting() {
		return this.hosting;
	}
}
