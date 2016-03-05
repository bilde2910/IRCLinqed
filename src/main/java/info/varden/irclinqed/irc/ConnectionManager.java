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
import info.varden.irclinqed.MessageType;
import info.varden.irclinqed.ServerSettings;
import info.varden.irclinqed.ThreadRunnablePair;
import info.varden.irclinqed.dcc.DCCServerSettings;
import info.varden.irclinqed.dcc.DCCThread;
import info.varden.irclinqed.packet.PacketQuit;

import java.util.ArrayList;

public class ConnectionManager {
	private IRCLinqed il;
	private ArrayList<ThreadRunnablePair> threadlist;
	private int currentTalkingIndex = -1;

	public ConnectionManager(IRCLinqed il) {
		this.il = il;
		threadlist = new ArrayList<ThreadRunnablePair>();
	}
	
	public ConnectedServerInfo[] getConnectedServersInfo() {
		ArrayList<ConnectedServerInfo> csi = new ArrayList<ConnectedServerInfo>();
		for (int i = 0; i < getConnectionCount(); i++) {
			csi.add(new ConnectedServerInfo(this.il, this.threadlist.get(i).runnable, i));
		}
		return csi.toArray(new ConnectedServerInfo[0]);
	}

	public void connect(ServerSettings info) {
		IRCThread it = new IRCThread(this.il, info);
		Thread thrd = new Thread(it);
		ThreadRunnablePair trr = new ThreadRunnablePair(it, thrd);
		trr.thread.start();
		this.threadlist.add(trr);
		this.currentTalkingIndex = this.threadlist.size() - 1;
	}
	
	public void connect(ServerSettings info, int timeout) {
		Runnable r = new TimedConnectThread(this, info, timeout);
		Thread t = new Thread(r);
		t.start();
	}
	
	public void connect(DCCServerSettings info) {
		DCCThread dt = new DCCThread(this.il, info);
		Thread thrd = new Thread(dt);
		ThreadRunnablePair trr = new ThreadRunnablePair(dt, thrd);
		trr.thread.start();
		this.threadlist.add(trr);
		this.currentTalkingIndex = this.threadlist.size() - 1;
	}
	
	public void connect(DCCServerSettings info, int timeout) {
		Runnable r = new TimedConnectThread(this, info, timeout);
		Thread t = new Thread(r);
		t.start();
	}
	
	public void disconnect(ThreadRunnablePair pair, boolean sentPacket) {
		if (!sentPacket) {
			if (pair.runnable instanceof IRCThread) {
				PacketQuit packet = new PacketQuit(this.il, (IRCThread) pair.runnable, null);
				packet.send();
			} else if (pair.runnable instanceof DCCThread) {
				pair.runnable.disconnect();
			}
		}
		threadlist.remove(pair);
		this.currentTalkingIndex = this.threadlist.size() - 1;
	}

	public String translateAliases(String host) {
		String host2 = host;
		String aliasPrefix = this.il.configLoader.getKey("prefix_alias", "!");
		if (host.startsWith(aliasPrefix)) {
			host = this.il.configLoader.getKey(
					"alias_" + host.substring(aliasPrefix.length()), null);
		}
		if (host == null) {
			this.il.util.writeToChat(
					MessageType.LQS_ERROR,
					"No alias specified for \""
							+ host2.substring(aliasPrefix.length()) + "\"");
			return null;
		}
		return host;
	}

	public ServerSettings getSavedSettingsForConnection(String host, int port) {
		return new ServerSettings(host, port,
				this.il.configLoader.getServerSpecificKey(host, port,
						"nickname"), this.il.configLoader.getServerSpecificKey(
						host, port, "realname"), this.il.configLoader
						.getServerSpecificKey(host, port, "autojoin")
						.split(","));
	}

	public void setCurrentServer(int serverindex) {
		this.currentTalkingIndex = serverindex;
	}

	public int getCurrentServer() {
		return this.currentTalkingIndex;
	}

	public int getConnectionCount() {
		return this.threadlist.size();
	}

	public ThreadRunnablePair getConnectionPair(int index) {
		try {
			return threadlist.get(index);
		} catch (ArrayIndexOutOfBoundsException ex) {
			ex.printStackTrace();
			return null;
		}
	}
	
	public ThreadRunnablePair getConnectionPair(IConnection runnable) {
		for (ThreadRunnablePair pair : this.threadlist) {
			if (pair.runnable.equals(runnable)) {
				return pair;
			}
		}
		return null;
	}
}
