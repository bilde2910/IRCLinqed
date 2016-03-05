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
import info.varden.irclinqed.Util;
import info.varden.irclinqed.irc.IConnection;
import info.varden.irclinqed.irc.IRCThread;
import info.varden.irclinqed.irc.UserHost;
import info.varden.irclinqed.packet.PacketWho;

import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class IPSelector implements IUserIPResponseHandler {
	private final IConnection thread;
	private final IIPSelectionHandler handler;
	private final IRCLinqed il;
	private final String target;
	
	public IPSelector(IIPSelectionHandler handler, IRCLinqed il, IConnection thread, String target) {
		this.thread = thread;
		this.handler = handler;
		this.il = il;
		this.target = target;
	}
	
	public void findAsync() {
		if (this.thread instanceof IRCThread) {
			IRCThread thrd = (IRCThread) this.thread;
			thrd.setDCCIPRequesting(true);
			thrd.setUserIPResponseHandler(this);
			PacketWho packet = new PacketWho(this.il, thrd, this.target);
			packet.send();
		} else if (this.thread instanceof DCCThread) {
			DCCThread thrd = (DCCThread) this.thread;
			onUserHostResponse(new UserHost[] {
					new UserHost(thrd.getNickname(), "", thrd.getHost()),
					new UserHost(thrd.getNetworkName(), "", thrd.getHost())
			});
		}
	}

	@Override
	public void onUserHostResponse(UserHost[] hosts) {
		String hostFromIRC = "";
		String targetAddr = "";
		for (UserHost host : hosts) {
			if (host.nickname.equals(this.target)) {
				hostFromIRC = host.hostname;
				break;
			}
		}
		this.il.logInfo("Target user has host " + hostFromIRC);
		try {
			InetAddress host = InetAddress.getByName(hostFromIRC);
			targetAddr = host.getHostAddress();
			this.il.logInfo("Resolved to address " + targetAddr);
		} catch (UnknownHostException e1) {
			this.il.logWarning("Target user is using vhost! Assuming receiver is not on LAN network - DCC transfer may fail");
			e1.printStackTrace();
			this.handler.onIPSelected(this.il.ipAddress);
		}
		String ip = this.il.ipAddress;
		if (targetAddr.equals(this.il.ipAddress)) {
			try {
				ip = Util.getLanIPAddress();
			} catch (SocketException e) {
				e.printStackTrace();
				this.handler.onIPSelected("127.0.0.1");
			}
			this.il.logInfo("Target user is on LAN network (your IP matches target)");
		}
		this.handler.onIPSelected(ip);
	}
}
