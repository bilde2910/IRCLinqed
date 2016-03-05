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
import info.varden.irclinqed.packet.PacketJoin;

public class AutojoinThread implements Runnable {
	private IRCLinqed il;
	private IRCThread thread;
	private String[] channels;

	public AutojoinThread(IRCLinqed il, IRCThread thread, String[] channels) {
		this.thread = thread;
		this.channels = channels;
	}

	@Override
	public void run() {
		for (String aChan : this.channels) {
			if (aChan.startsWith("#") && aChan.length() >= 2) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				PacketJoin packetJoin = new PacketJoin(this.il, this.thread, aChan);
				packetJoin.send();
			}
		}
	}

}
