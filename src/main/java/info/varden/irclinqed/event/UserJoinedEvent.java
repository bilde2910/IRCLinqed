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

package info.varden.irclinqed.event;

import info.varden.irclinqed.packet.PacketJoin;
import info.varden.irclinqed.plugin.Event;
import info.varden.irclinqed.plugin.EventHandler;
import info.varden.irclinqed.plugin.IUserListChangeHandler;

public class UserJoinedEvent implements Event {
	private PacketJoin packet;
	
	public UserJoinedEvent(PacketJoin packet) {
		this.packet = packet;
	}

	@Override
	public void handle(EventHandler handler) {
		if (handler instanceof IUserListChangeHandler) {
			((IUserListChangeHandler) handler).onUserJoined(this.packet.thread.getConnectionData(), this.packet.getRawParts()[2], this.packet.getUserHost());
		}
	}

}
