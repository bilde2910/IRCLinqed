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
import info.varden.irclinqed.irc.UserHost;

public class Packet352WhoReply extends NumericResponsePacket {
	public Packet352WhoReply(IRCLinqed il, IRCThread thread, String command) {
		super(il, thread, command);
	}

	@Override
	public void handle() {
		String realname = getMessage().substring(getMessage().indexOf(" ") + 1);
		this.thread.addGuiListItem(new ListItem(getParam(5) + " (" + realname + ")", getParam(2) + "@" + getParam(3), getParam(4) + " (" + getMessage().split(" ")[0] + ")"));
		this.thread.userHosts.add(new UserHost(getParam(5), getParam(2), getParam(3)));
	}
}
