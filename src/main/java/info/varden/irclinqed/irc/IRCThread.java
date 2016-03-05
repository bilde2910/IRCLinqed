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
import info.varden.irclinqed.ServerSettings;
import info.varden.irclinqed.TokenGenerator;
import info.varden.irclinqed.Util;
import info.varden.irclinqed.dcc.IUserIPResponseHandler;
import info.varden.irclinqed.event.ConnectingEvent;
import info.varden.irclinqed.event.FailedConnectEvent;
import info.varden.irclinqed.event.PacketHandledEvent;
import info.varden.irclinqed.event.PacketReceivedEvent;
import info.varden.irclinqed.event.SuccessfulConnectEvent;
import info.varden.irclinqed.gui.ListItem;
import info.varden.irclinqed.packet.PacketNick;
import info.varden.irclinqed.packet.PacketNotice;
import info.varden.irclinqed.packet.PacketPrivmsg;
import info.varden.irclinqed.packet.PacketUser;
import info.varden.irclinqed.plugin.ConnectionHelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class IRCThread implements Runnable, IConnection {
	private final String token;
	
	private IRCLinqed il;
	public ServerSettings info;
	private PacketHandler ph;
	private String currentChannel;
	private ArrayList<String> channels;
	private boolean connecting = true;
	private String[] lastCommand;
	public Map<String, String> iSupport;
	private ConnectionHelper connData;

	private ArrayList<ListItem> guiListItems;
	private boolean dccWhoIssued = false;
	private IUserIPResponseHandler dccIPH = null;
	public ArrayList<UserHost> userHosts = new ArrayList<UserHost>();

	private Socket s;
	private BufferedReader i;
	private PrintWriter o;
	public Util util;

	public IRCThread(IRCLinqed il, ServerSettings info) {
		this.token = new TokenGenerator().nextToken();
		this.il = il;
		this.info = info;
		this.ph = new PacketHandler(this.il, this);
		this.channels = new ArrayList<String>();
		this.iSupport = new HashMap<String, String>();
		this.guiListItems = new ArrayList<ListItem>();
		this.util = new Util(this.il, this);
		this.connData = new ConnectionHelper(this);
	}

	public boolean sendRaw(IRCPacket packet, String s) {
		if (this.o.checkError() || this.s.isClosed() || !this.s.isConnected()
				|| this.s.isOutputShutdown()) {
			return false;
		}
		this.il.logInfo("Sent packet: " + s);
		this.o.print(s + "\r\n");
		this.o.flush();
		return true;
	}

	public static boolean sendPacket(IRCPacket packet) {
		return packet.send();
	}

	public boolean sendMessage(String target, String message) {
		PacketPrivmsg packetPrivmsg = new PacketPrivmsg(this.il, this, target,
				message);
		return packetPrivmsg.send();
	}

	public boolean sendNotice(String target, String message) {
		PacketNotice packetNotice = new PacketNotice(this.il, this, target,
				message);
		return packetNotice.send();
	}

	@Override
	public void run() {
		try {
			this.il.util.writeToChat(MessageType.LQS_INFO, "Connecting to "
					+ this.info.getHost() + " port " + this.info.getPort()
					+ "...");
			this.il.getEventRegistry().postEvent(
					new ConnectingEvent(this.getConnectionData()));

			this.s = new Socket(this.info.getHost(), this.info.getPort());

			this.i = new BufferedReader(new InputStreamReader(
					this.s.getInputStream()));
			this.o = new PrintWriter(new OutputStreamWriter(
					this.s.getOutputStream()));

			PacketUser packetUser = new PacketUser(this.il, this, "IRCLinqed",
					"0", "*", info.getRealname());
			PacketNick packetNick = new PacketNick(this.il, this,
					info.getNickname());
			packetUser.send();
			packetNick.send();
		} catch (IOException ex) {
			this.il.util.writeToChat(MessageType.LQS_ERROR,
					"Failed to connect to " + this.info.getHost() + " port "
							+ this.info.getPort() + ": " + ex.getMessage());
			this.il.getEventRegistry().postEvent(
					new FailedConnectEvent(this.getConnectionData(), ex));
			ex.printStackTrace();
		}
		try {
			String str;
			while (this.s.isConnected()) {
				str = i.readLine();
				System.out.println(str);

				if (str.startsWith("PING ")) {
					this.o.print("PONG " + str.substring(5) + "\r\n");
					this.o.flush();
				}

				if (str.charAt(0) != ':')
					continue;

				if (str.split(" ")[1].equals("001")) {
					// Welcome message received
					this.connecting = false;
					this.il.util.writeToChat(MessageType.LQS_INFO,
							"Successfully connected to " + this.info.getHost()
									+ " port " + this.info.getPort() + "!");
					String nickserv = this.il.configLoader.getServerSpecificKey(this.info.getHost(), this.info.getPort(), "nickserv");
					if (!"".equals(nickserv)) {
						PacketPrivmsg nsauth = new PacketPrivmsg(this.il, this, "NickServ", "IDENTIFY " + nickserv);
						nsauth.setOutputEnabled(false);
						nsauth.send();
					}
					Runnable ajt = new AutojoinThread(this.il, this,
							info.getAutoJoin());
					Thread ajtt = new Thread(ajt);
					ajtt.start();
					this.il.getEventRegistry()
							.postEvent(
									new SuccessfulConnectEvent(this
											.getConnectionData()));
				}
				// Process the command
				IRCPacket packet = this.ph.getPacket(str);
				this.il.getEventRegistry().postEvent(
						new PacketReceivedEvent(packet));
				packet.handle();
				this.il.getEventRegistry().postEvent(
						new PacketHandledEvent(packet));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		disconnect();
	}

	public void setChannel(String chan) {
		this.currentChannel = chan;
	}

	public String getChannel() {
		return this.currentChannel;
	}

	public boolean addChannel(String channel) {
		if (isOnChannel(channel)) {
			return false;
		} else {
			this.channels.add(channel);
			return true;
		}
	}

	public boolean removeChannel(String channel) {
		if (isOnChannel(channel)) {
			this.channels.remove(channel);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public String[] getChannels() {
		return this.channels.toArray(new String[0]);
	}

	public boolean isOnChannel(String channel) {
		return this.channels.contains(channel);
	}

	public boolean isConnecting() {
		return this.connecting;
	}

	@Override
	public String getNetworkName() {
		return (this.iSupport.containsKey("NETWORK") ? this.iSupport
				.get("NETWORK") : this.info.getHost());
	}

	public String[] getLastCommand() {
		return this.lastCommand;
	}

	public void setLastCommand(String[] command) {
		this.lastCommand = command;
	}

	public void addGuiListItem(ListItem item) {
		this.guiListItems.add(item);
	}

	public void removeGuiListItem(ListItem item) {
		this.guiListItems.remove(item);
	}

	public void sortGuiListItems() {
		Collections.sort(this.guiListItems);
	}

	public void clearGuiListItems() {
		this.guiListItems.clear();
	}

	public ListItem[] getGuiListItems() {
		return this.guiListItems.toArray(new ListItem[0]);
	}
	
	public void setDCCIPRequesting(boolean requesting) {
		this.dccWhoIssued = requesting;
	}
	
	public boolean isDCCIPRequesting() {
		return this.dccWhoIssued;
	}
	
	public void setUserIPResponseHandler(IUserIPResponseHandler handler) {
		this.dccIPH = handler;
	}
	
	public IUserIPResponseHandler getUserIPResponseHandler() {
		return this.dccIPH;
	}

	public ConnectionHelper getConnectionData() {
		return this.connData;
	}

	@Override
	public String getHost() {
		return this.info.getHost();
	}

	@Override
	public int getPort() {
		return this.info.getPort();
	}

	@Override
	public boolean isDCC() {
		return false;
	}
	
	@Override
	public String getNickname() {
		return this.info.getNickname();
	}

	@Override
	public boolean disconnect() {
		try {
			// Close the connection
			this.util.writeToChat(MessageType.LQS_INFO, "Disconnected from " + getNetworkName() + " (" + this.getHost() + ":" + this.getPort() + ")");
			this.il.connectionManager.disconnect(this.il.connectionManager.getConnectionPair(this), true);
			this.s.shutdownInput();
			this.s.shutdownOutput();
			this.s.close();
			return true;
		} catch (IOException ex) {
			ex.printStackTrace();
			return false;
		}
	}
	
	@Override
	public boolean equals(IConnection arg0) {
		return this.getToken().equals(arg0.getToken());
	}

	@Override
	public String getToken() {
		return this.token;
	}

	@Override
	public String getHostAndPort() {
		return getHost() + ":" + getPort();
	}

	@Override
	public String getRealname() {
		return this.info.getRealname();
	}

}
