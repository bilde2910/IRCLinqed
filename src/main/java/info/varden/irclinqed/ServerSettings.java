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

package info.varden.irclinqed;

import java.util.ArrayList;

public class ServerSettings {
	private String host;
	private int port;
	private String nickname;
	private String realname;
	private String[] autoJoin;
	
	private ArrayList<String> channels;
	
	public ServerSettings(String host, int port, String nickname, String realname, String[] autoJoin) {
		this.host = host;
		this.port = port;
		this.nickname = nickname;
		this.realname = realname;
		this.autoJoin = autoJoin;
		this.channels = new ArrayList<String>();
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
	
	public String getRealname() {
		return this.realname;
	}
	
	public String[] getAutoJoin() {
		return this.autoJoin;
	}
	
	public String[] getChannels() {
		return channels.toArray(new String[0]);
	}
	
	public boolean setNickname(String nick) {
		if (this.nickname == nick) {
			return false;
		} else {
			this.nickname = nick;
			return true;
		}
	}
	
	public boolean addChannel(String channel) {
		if (this.channels.contains(channel)) {
			return false;
		} else {
			channels.add(channel);
			return true;
		}
	}
	
	public boolean removeChannel(String channel) {
		if (this.channels.contains(channel)) {
			channels.remove(channel);
			return true;
		} else {
			return false;
		}
	}
}
