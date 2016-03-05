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
import info.varden.irclinqed.MessageType;
import info.varden.irclinqed.irc.IRCThread;
import info.varden.irclinqed.irc.NumericResponsePacket;

public class Packet319WhoisChannels extends NumericResponsePacket {
	public Packet319WhoisChannels(IRCLinqed il, IRCThread thread, String command) {
		super(il, thread, command);
	}

	@Override
	public void handle() {
		String channels[] = getMessage().split(" ");
		String chan = "";
		for (String channel : channels) {
			if (!"".equals(channel) && channel != null) {
				if (channel.length() >= 1) {
					if ("~".equals(channel.substring(0, 1))) {
						channel = "§5~" + channel.substring(1) + "§r";
					} else if ("&".equals(channel.substring(0, 1))) {
						channel = "§4@" + channel.substring(1) + "§r";
					} else if ("@".equals(channel.substring(0, 1))) {
						channel = "§a@" + channel.substring(1) + "§r";
					} else if ("%".equals(channel.substring(0, 1))) {
						channel = "§b%" + channel.substring(1) + "§r";
					} else if ("+".equals(channel.substring(0, 1))) {
						channel = "§e+" + channel.substring(1) + "§r";
					}
				}
				chan += " " + channel;
			}
		}
		addMessageToChat(MessageType.SERVER_INFO, thread.getNetworkName(), getParam(1) + " is on channels " + chan.substring(1));
	}
}
