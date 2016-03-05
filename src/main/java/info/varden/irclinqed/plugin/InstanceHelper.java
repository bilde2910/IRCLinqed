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

import info.varden.irclinqed.IRCLinqed;
import info.varden.irclinqed.Util;

/**
 * A class for retrieving IRCLinqed data for plugins.
 * @author bilde2910
 */
public class InstanceHelper {
	private final IRCLinqed il;
	
	/**
	 * Initializes the InstanceData.
	 * @param il The IRCLinqed instance.
	 */
	public InstanceHelper(IRCLinqed il) {
		this.il = il;
	}
	
	/**
	 * Gets the event registry used for registering events.
	 * @return The event registry instance.
	 */
	public EventRegistry getEventRegistry() {
		return this.il.getEventRegistry();
	}
	
	/**
	 * Gets a setting from the IRCLinqed properties file.
	 * @param key The setting to look up.
	 * @return The value corresponding to the key, or a blank string if nothing was found.
	 */
	public String getSetting(String key) {
		return this.il.configLoader.getKeyWithFallback(key);
	}
	
	/**
	 * Gets an IRC server specific setting from the IRCLinqed properties file.
	 * @param server The server to search for the key, in host:port format, or just the host name, with port 6667.
	 * @param key The setting to look up.
	 * @return The value corresponding to the key for the specified server, or a blank string if nothing was found.
	 */
	public String getServerSetting(String server, String key) {
		return getServerSetting(Util.getHost(server), Util.getPort(server), key);
	}
	
	/**
	 * Gets an IRC server specific setting from the IRCLinqed properties file.
	 * @param host The server host name to look up from.
	 * @param port The server port to look up from.
	 * @param key The setting to look up.
	 * @return The value corresponding to the key for the specified server, or a blank string if nothing was found.
	 */
	public String getServerSetting(String host, int port, String key) {
		return this.il.configLoader.getServerSpecificKey(host, port, key);
	}
	
	/**
	 * Gets an IRCLinqed default setting.
	 * @param key The setting to look up.
	 * @return The default value corresponding to the specified key, or a blank string if nothing was found.
	 */
	public String getDefaultSetting(String key) {
		return this.il.configLoader.getFactorySetting(key);
	}
	
	/**
	 * Gets a setting IRCLinqed would default to for servers if no setting or the default setting is selected for a server specific setting.
	 * @param key The setting to look up.
	 * @return The default value corresponding to the specified server key, or a blank string if nothing was found.
	 */
	public String getDefaultServerSetting(String key) {
		return this.il.configLoader.getDefaultKeyFactoryFallback(key);
	}
	
	/**
	 * Gets all keys set for the specified server.
	 * @param server The server to search for keys, in host:port format, or just the host name, with port 6667.
	 * @return A list of keys set for the specified server.
	 */
	public String[] getAllServerKeys(String server) {
		return getAllServerKeys(Util.getHost(server), Util.getPort(server));
	}
	
	/**
	 * Gets all keys set for the specified server.
	 * @param host The server host name to look up from.
	 * @param port The server port to look up from.
	 * @return A list of keys set for the specified server.
	 */
	public String[] getAllServerKeys(String host, int port) {
		return this.il.configLoader.getAllServerKeys(host, port);
	}
	
	/**
	 * Gets the IRCLinqed version.
	 * @return A version string indicating the current IRCLinqed version.
	 */
	public String getVersion() {
		return this.il.VERSION;
	}
}
