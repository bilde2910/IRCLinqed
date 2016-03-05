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

package info.varden.irclinqed.irc;

import info.varden.irclinqed.IRCLinqed;
import info.varden.irclinqed.MessageType;

public abstract class IRCPacket {
	public IRCLinqed il;
	public IRCThread thread;
	public String command;
	public boolean outputEnabled = true;

	public abstract void handle();
	public abstract boolean sendsWithMessage();
	public abstract boolean send();

	public IRCPacket(IRCLinqed il, IRCThread thread, String command) {
		this.il = il;
		this.thread = thread;
		this.command = command;
	}

	public String[] getRawParts() {
		return command.split(" ");
	}

	public String getRawParamString() {
		return command.split(":")[1];
	}

	public String getMessage() {
		int idx = command.indexOf(":", 1);
		try {
			return command.substring(idx + 1);
		} catch (Exception ex) {
			return "";
		}
	}
	
	public void setOutputEnabled(boolean enabled) {
		this.outputEnabled = enabled;
	}

	public String getNickname() {
		return getUserHost().nickname;
	}

	public String getHostname() {
		return getUserHost().hostname;
	}

	public String getIRCTargetUsername() {
		return getRawParts()[2];
	}

	public String getParam(int i) {
		try {
			return getRawParts()[i + 2];
		} catch (Exception ex) {
			return null;
		}
	}

	public String getKey(String key) {
		return this.il.configLoader.getServerSpecificKey(
				this.thread.info.getHost(), this.thread.info.getPort(), key);
	}

	public boolean getBooleanKey(String key) {
		return this.il.configLoader.getServerSpecificBooleanKey(key,
				this.thread.info.getHost(), this.thread.info.getPort());
	}

	public void addMessageToChat(MessageType type, String target, String message) {
		if (this.outputEnabled) {
			this.thread.util.writeToChat(type, target, message);
		}
	}

	public void addMessageToChat(MessageType type, String message) {
		if (this.outputEnabled) {
			this.thread.util.writeToChat(type, message);
		}
	}

	public void writeRawReplyToChat(MessageType type, String target) {
		addMessageToChat(type, target, getMessage());
	}

	public void writeRawReplyToChat(MessageType type) {
		addMessageToChat(type, getMessage());
	}

	public void writeRawReplyToChatWithFirstParam(MessageType type,
			String target) {
		addMessageToChat(type, target, getMessage() + " (" + getParam(1));
	}

	public void writeRawReplyToChatWithFirstParam(MessageType type) {
		addMessageToChat(type, getMessage());
	}

	public UserHost getUserHost() {
		return new UserHost(getRawParts()[0].split(":")[1]);
	}
}
