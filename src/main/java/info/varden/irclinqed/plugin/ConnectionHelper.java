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

import info.varden.irclinqed.irc.IRCPacket;
import info.varden.irclinqed.irc.IRCThread;

/**
 * A class for retrieving server connection data.
 * @author bilde2910
 */
public class ConnectionHelper {
	private final IRCThread thread;
	
	/**
	 * Initializes the plugin data.
	 * @param thread The thread instance.
	 */
	public ConnectionHelper(IRCThread thread) {
		this.thread = thread;
	}
	
	/**
	 * Checks whether or not the connection has registered on the server.
	 * @return True if the connection has registered on the server, otherwise false.
	 */
	public boolean hasRegistered() {
		return !this.thread.isConnecting();
	}
	
	/**
	 * Gets the channel the user is currently chatting on.
	 * @return The current channel.
	 */
	public String getCurrentChannel() {
		return this.thread.getChannel();
	}
	
	/**
	 * Sets the channel the user is currently chatting on.
	 * @param channel The channel to talk on.
	 */
	public void setCurrentChannel(String channel) {
		this.thread.setChannel(channel);
	}
	
	/**
	 * Returns a list of the channels the user is currently in.
	 * @return A list of channels.
	 */
	public String[] getChannels() {
		return this.thread.getChannels();
	}
	
	/**
	 * Returns the last chat command the user issued.
	 * @return The last command with arguments.
	 */
	public String[] getLastCommand() {
		return this.thread.getLastCommand();
	}
	
	/**
	 * Returns whether or not the user is on the specified channel.
	 * @param channel The channel name.
	 * @return True if the user is chatting on the channel, otherwise false.
	 */
	public boolean isOnChannel(String channel) {
		return this.thread.isOnChannel(channel);
	}
	
	/**
	 * Returns the name of the currently connected IRC network.
	 * @return The network name.
	 */
	public String getNetworkname() {
		return this.thread.getNetworkName();
	}
	
	/**
	 * Sends a packet to the IRC server.
	 * @param packet The packet to send.
	 * @return Whether or not the packet was successfully sent.
	 */
	public boolean sendPacket(IRCPacket packet) {
		return this.thread.sendPacket(packet);
	}
	
	/**
	 * Sends a message to the channel the user is currently chatting in.
	 * @param message The message to send.
	 * @return Whether or not the message was successfully sent.
	 */
	public boolean sendChannelMessage(String message) {
		return this.thread.sendMessage(getCurrentChannel(), message);
	}
	
	/**
	 * Sends a private message to a user.
	 * @param user The user to send a message to.
	 * @param message The message to send.
	 * @return Whether or not the message was successfully sent.
	 */
	public boolean sendPrivateMessage(String user, String message) {
		return this.thread.sendMessage(user, message);
	}
	
	/**
	 * Sends a notice message to the channel the user is currently chatting in.
	 * @param notice The notice message to send.
	 * @return Whether or not the notice message was successfully sent.
	 */
	public boolean sendChannelNotice(String notice) {
		return this.thread.sendNotice(getCurrentChannel(), notice);
	}
	
	/**
	 * Sends a notice message to a user.
	 * @param user The user to send a notice message to.
	 * @param notice The notice message to send.
	 * @return Whether or not the notice message was successfully sent.
	 */
	public boolean sendPrivateNotice(String user, String notice) {
		return this.thread.sendNotice(getCurrentChannel(), notice);
	}
	
	/**
	 * Gets whether or not the specified ISupport parameter was sent by the server when connecting.
	 * @param key The parameter to search for.
	 * @return True if the parameter was sent, otherwise false.
	 */
	public boolean isISupportSpecified(String key) {
		return this.thread.iSupport.containsKey(key);
	}
	
	/**
	 * Gets an ISupport parameter sent by the server upon connecting.
	 * @param key The parameter to search for.
	 * @return The value of the requested ISupport parameter, or null if the parameter was not found.
	 */
	public String getISupportValue(String key) {
		return this.thread.iSupport.get(key);
	}
	
	/**
	 * Forces the IRC connection to abort.
	 */
	public void forceDisconnect() {
		this.thread.disconnect();
	}
}
