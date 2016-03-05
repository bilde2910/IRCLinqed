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

package info.varden.irclinqed;

public final class Setting {
	private Setting() {
		throw new AssertionError("You can not instantiate Setting!");
	}
	public static final String NICKNAME = "username";
	public static final String REALNAME = "realname";
	public static final String QUIT_REASON = "quitreason";
	public static final String PART_REASON = "partreason";
	public static final String SHOW_MOTD = "showmotd";
	public static final String SHOW_CTCP = "showctcp";
	public static final String SHOW_FORMATTING = "showformatting";
	public static final String SHOW_MODE = "showmode";
	public static final String SHOW_NICKNAME_PREFIX = "shownickprefix";
	public static final String ALLOW_DCC = "allowdcc";
	public static final String SEND_DEATH_MESSAGES = "senddeath";
	public static final String SEND_ACHIEVEMENT_MESSAGES = "sendachievements";
	public static final String SEND_ALL_MESSAGES = "sendall";
}
