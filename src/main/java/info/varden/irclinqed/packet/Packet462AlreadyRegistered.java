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

public class Packet462AlreadyRegistered extends NumericResponsePacket {
	public Packet462AlreadyRegistered(IRCLinqed il, IRCThread thread,
			String command) {
		super(il, thread, command);
	}

	@Override
	public void handle() {
		addMessageToChat(MessageType.LQS_BUG, "ERR_ALREADYREGISTERED (462) received.");
	}
}
