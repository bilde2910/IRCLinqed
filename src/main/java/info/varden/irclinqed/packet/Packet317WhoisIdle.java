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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Packet317WhoisIdle extends NumericResponsePacket {
	public Packet317WhoisIdle(IRCLinqed il, IRCThread thread, String command) {
		super(il, thread, command);
	}

	@Override
	public void handle() {
		int millis = Integer.parseInt(getParam(2)) * 1000;
	    TimeZone tz = TimeZone.getTimeZone("UTC");
	    SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
	    SimpleDateFormat df2 = new SimpleDateFormat("D");
	    df.setTimeZone(tz);
	    df2.setTimeZone(tz);
	    String time = (Integer.parseInt(df2.format(new Date(millis))) - 1) + " day(s), " + df.format(new Date(millis));
	    addMessageToChat(MessageType.SERVER_INFO, thread.getNetworkName(), getParam(1) + " has been idle for " + time);
	}
}
