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

import info.varden.irclinqed.packet.PacketMode;
import info.varden.irclinqed.plugin.Event;
import info.varden.irclinqed.plugin.EventHandler;
import info.varden.irclinqed.plugin.IUserBannedHandler;

public class UserBannedEvent implements Event {
	private PacketMode packet;
	private String target;
	private boolean given;
	
	public UserBannedEvent(PacketMode packet, String target, boolean given) {
		this.packet = packet;
		this.target = target;
		this.given = given;
	}

	@Override
	public void handle(EventHandler handler) {
		if (handler instanceof IUserBannedHandler) {
			if (this.given) {
				((IUserBannedHandler) handler).onUserBanned(this.packet.thread.getConnectionData(), this.packet.getRawParts()[2], this.packet.getUserHost(), this.target);
			} else {
				((IUserBannedHandler) handler).onUserUnbanned(this.packet.thread.getConnectionData(), this.packet.getRawParts()[2], this.packet.getUserHost(), this.target);
			}
		}
	}

}
