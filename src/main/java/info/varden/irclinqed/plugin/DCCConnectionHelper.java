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

import info.varden.irclinqed.dcc.DCCThread;

/**
 * A class for retrieving server connection data.
 * @author bilde2910
 */
public class DCCConnectionHelper {
	private final DCCThread thread;
	
	/**
	 * Initializes the plugin data.
	 * @param thread The thread instance.
	 */
	public DCCConnectionHelper(DCCThread thread) {
		this.thread = thread;
	}
	
	/**
	 * Returns the last chat command the user issued.
	 * @return The last command with arguments.
	 */
	public String[] getLastCommand() {
		return this.thread.getLastCommand();
	}
	
	/**
	 * Returns the name of the network used to perform the DCC handshake.
	 * @return The network name.
	 */
	public String getNetworkname() {
		return this.thread.getParentNetworkName();
	}
	
	/**
	 * Sends a message to the user.
	 * @param message The message to send.
	 * @return Whether or not the message was successfully sent.
	 */
	public boolean sendMessage(String user, String message) {
		return this.thread.sendMessage(message);
	}
	
	/**
	 * Returns the nickname of the other user of the DCC connection.
	 * @return The nickname of the other user.
	 */
	public String getTarget() {
		return this.thread.info.getTarget();
	}
	
	/**
	 * Returns the client's nickname.
	 * @return The nickname of the IRCLinqed user.
	 */
	public String getNickname() {
		return this.thread.info.getNickname();
	}
	
	/**
	 * Returns the IP address of the other user.
	 * @return The IP address of the other user as a dotted string.
	 */
	public String getHost() {
		return this.thread.getHost();
	}
	
	/**
	 * Returns the port the DCC connection is running on.
	 * @return The port of the DCC connection.
	 */
	public int getPort() {
		return this.thread.getPort();
	}
	
	/**
	 * Forces the IRC connection to abort.
	 */
	public void forceDisconnect() {
		this.thread.disconnect();
	}
}
