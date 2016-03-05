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

import info.varden.irclinqed.ServerSettings;
import info.varden.irclinqed.dcc.DCCServerSettings;

class TimedConnectThread implements Runnable {
	
	private final int timeout;
	private final ConnectionManager cm;
	
	private ServerSettings info1;
	private DCCServerSettings info2;
	
	public TimedConnectThread(ConnectionManager cm, ServerSettings info, int timeout) {
		this.info1 = info;
		this.timeout = timeout;
		this.cm = cm;
	}
	
	public TimedConnectThread(ConnectionManager cm, DCCServerSettings info, int timeout) {
		this.info2 = info;
		this.timeout = timeout;
		this.cm = cm;
	}

	@Override
	public void run() {
		try {
			Thread.sleep(this.timeout);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if (this.info1 != null) {
			this.cm.connect(info1);
		} else if (this.info2 != null) {
			this.cm.connect(info2);
		}
	}
	
}
