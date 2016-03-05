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
import info.varden.irclinqed.MessageType;
import info.varden.irclinqed.Util;
import info.varden.irclinqed.irc.IConnection;

public class VirtualUtil extends Util {
	
	private IRCLinqed il;
	private IConnection thread;

	public VirtualUtil(IRCLinqed il, IConnection thread) {
		super(il, thread);
		this.il = il;
		this.thread = thread;
	}
	
	@Override
	public void writeToChat(MessageType type, String message) {
		writeToChat(type, "", message);
	}
	
	@Override
	public void writeToChat(MessageType type, String target, String message) {
		switch (type) {
			case IRC_MESSAGE:
				super.writeToChat(MessageType.DCC_MESSAGE, target, message);
				break;
			case IRC_CTCP:
				super.writeToChat(MessageType.DCC_CTCP, target, message);
				break;
			case IRC_NOTICE:
				super.writeToChat(MessageType.DCC_MESSAGE, target, message);
				break;
			case IRC_CHANNOTICE:
				super.writeToChat(MessageType.DCC_MESSAGE, target, message);
				break;
			case IRC_PRIVATE:
				super.writeToChat(MessageType.DCC_MESSAGE, target, message);
				break;
			case IRC_JOIN:
				super.writeToChat(MessageType.DCC_JOIN, target, message);
				break;
			case IRC_LEAVE:
				super.writeToChat(MessageType.DCC_LEAVE, target, message);
				break;
			case IRC_USERACTION:
				super.writeToChat(MessageType.DCC_USERACTION, target, message);
				break;
			case IRC_ME:
				super.writeToChat(MessageType.DCC_ME, target, message);
				break;
			case IRC_PRIVATEME:
				super.writeToChat(MessageType.DCC_ME, target, message);
				break;
			case SERVER_INFO:
				super.writeToChat(MessageType.DCC_INFO, target, message);
				break;
			case SERVER_ERROR:
				super.writeToChat(MessageType.DCC_ERROR, target, message);
				break;
			case SERVER_CHANNOTICE:
				super.writeToChat(MessageType.DCC_CHANNOTICE, target, message);
				break;
			case SERVER_MOTD:
				super.writeToChat(MessageType.DCC_MOTD, target, message);
				break;
			default:
				super.writeToChat(type, target, message);
				break;
		}
	}

}
