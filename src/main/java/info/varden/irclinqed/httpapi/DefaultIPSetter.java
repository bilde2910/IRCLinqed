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

package info.varden.irclinqed.httpapi;

import info.varden.irclinqed.IRCLinqed;

public class DefaultIPSetter implements IIPFetcher {
	
	private final IRCLinqed il;
	
	public DefaultIPSetter(IRCLinqed il) {
		this.il = il;
	}

	@Override
	public void onIPFound(String ip) {
		this.il.ipAddress = ip;
		if (ip != null) {
			this.il.logInfo("IP address found: " + ip);
		} else {
			this.il.logSevere("Could not obtain IP address! Internet DCC connections will not work!");
		}
	}

	@Override
	public void onAttemptFetch(String fromUrl) {
		this.il.logInfo("Trying to fetch IP address from " + fromUrl);
	}

	@Override
	public void onAttemptFailed(String url, Throwable ex) {
		this.il.logWarning("Failed to fetch IP address: " + ex.getMessage());
		ex.printStackTrace();
	}

}
