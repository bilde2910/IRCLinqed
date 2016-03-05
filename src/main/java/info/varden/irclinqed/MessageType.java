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

public enum MessageType {
	DCC_MESSAGE,
	DCC_INFO,
	DCC_ERROR,
	DCC_CHANNOTICE,
	DCC_RAW,
	DCC_MOTD,
	DCC_USERACTION,
	DCC_ME,
	DCC_JOIN,
	DCC_LEAVE,
	DCC_CTCP,
	LQS_ERROR,
	LQS_INFO,
	LQS_CHANANNOUNCE,
	LQS_USERACTION,
	LQS_BUG,
	IRC_MESSAGE,
	IRC_NOTICE,
	IRC_PRIVATE,
	IRC_CTCP,
	IRC_CHANNOTICE,
	IRC_JOIN,
	IRC_LEAVE,
	IRC_USERACTION,
	IRC_ME,
	IRC_PRIVATEME,
	SERVER_INFO,
	SERVER_ERROR,
	SERVER_CHANNOTICE,
	SERVER_MOTD;
}
