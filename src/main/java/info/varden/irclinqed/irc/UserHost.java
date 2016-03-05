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

public class UserHost {
	public String nickname;
	public String username;
	public String hostname;

	public UserHost(String hostmask) {
		String[] npts = hostmask.split("!");
		this.nickname = npts[0];
		if (npts.length > 1) {
			String[] hpts = npts[1].split("@");
			this.username = hpts[0];
			this.hostname = hpts[1];
		} else {
			this.username = "";
			this.hostname = "";
		}
	}
	
	public UserHost(String nickname, String username, String hostname) {
		this.nickname = nickname;
		this.username = username;
		this.hostname = hostname;
	}
}
