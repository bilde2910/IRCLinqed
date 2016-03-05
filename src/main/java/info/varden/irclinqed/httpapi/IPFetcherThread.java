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

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IPFetcherThread implements Runnable {
	
	private static final Pattern IP_PATTERN = Pattern.compile("([1-2]?\\d{1,2}\\.){3}([1-2]?\\d{1,2})");
	private static final String[] LOOKUP_QUEUE = {
		"http://api.varden.info/inet/ip.php",
		"http://api.externalip.net/ip"
	};
	
	private final IIPFetcher handler;
	
	public IPFetcherThread(IIPFetcher handler) {
		this.handler = handler;
	}

	@Override
	public void run() {
		boolean ipFound = false;
		String ipAddress = null;
		for (int i = 0; i < LOOKUP_QUEUE.length; i++) {
			try {
				this.handler.onAttemptFetch(LOOKUP_QUEUE[i]);
				URL ipcon = new URL(LOOKUP_QUEUE[i]);
				URLConnection uc = ipcon.openConnection();
				BufferedReader br = new BufferedReader(new InputStreamReader(uc.getInputStream()));
				String inputLine = null;
				while ((inputLine = br.readLine()) != null) {
					ipAddress = inputLine;
				}
				if (ipAddress != null) {
					Matcher m = IP_PATTERN.matcher(ipAddress);
					if (m.find()) {
						ipFound = true;
					}
				}
			} catch (Throwable ex) {
				this.handler.onAttemptFailed(LOOKUP_QUEUE[i], ex);
				ipFound = false;
			}
			if (ipFound) {
				break;
			}
		}
		if (ipFound) {
			this.handler.onIPFound(ipAddress);
		} else {
			this.handler.onIPFound(null);
		}
	}

}
