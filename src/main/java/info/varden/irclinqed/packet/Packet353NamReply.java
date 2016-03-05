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

package info.varden.irclinqed.packet;

import info.varden.irclinqed.IRCLinqed;
import info.varden.irclinqed.gui.ListItem;
import info.varden.irclinqed.irc.IRCThread;
import info.varden.irclinqed.irc.NumericResponsePacket;

public class Packet353NamReply extends NumericResponsePacket {
	public Packet353NamReply(IRCLinqed il, IRCThread thread, String command) {
		super(il, thread, command);
	}

	@Override
	public void handle() {
		String[] users = getMessage().trim().split(" ");
		for (String user : users) {
			if (!"".equals(user) && user != null) {
				String rank = "Regular user";
				if (user.length() >= 1) {
					if ("~".equals(user.substring(0, 1))) {
						rank = "§5Owner";
						user = user.substring(1);
					} else if ("&".equals(user.substring(0, 1))) {
						rank = "§4Admin";
						user = user.substring(1);
					} else if ("@".equals(user.substring(0, 1))) {
						rank = "§aOperator";
						user = user.substring(1);
					} else if ("%".equals(user.substring(0, 1))) {
						rank = "§bHalf-operator";
						user = user.substring(1);
					} else if ("+".equals(user.substring(0, 1))) {
						rank = "§eVoiced";
						user = user.substring(1);
					}
				}
				this.thread.addGuiListItem(new ListItem(user, getRawParts()[getRawParts().length - 1], rank));
			}
		}
	}
}
