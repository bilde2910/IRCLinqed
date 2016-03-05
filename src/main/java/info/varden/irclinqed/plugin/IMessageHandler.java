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

package info.varden.irclinqed.plugin;

import info.varden.irclinqed.irc.UserHost;

public interface IMessageHandler {
	public void onChannelMessageReceived(ConnectionHelper network, String channel, UserHost sender, String message);
	public void onPrivateMessageReceived(ConnectionHelper network, UserHost sender, String message);
	public void onChannelNoticeReceived(ConnectionHelper network, String channel, UserHost sender, String message);
	public void onPrivateNoticeReceived(ConnectionHelper network, UserHost sender, String message);
}