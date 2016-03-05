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

package info.varden.irclinqed.dcc;

import info.varden.irclinqed.IRCLinqed;
import info.varden.irclinqed.MessageType;
import info.varden.irclinqed.TokenGenerator;
import info.varden.irclinqed.Util;
import info.varden.irclinqed.ctcp.ActionPacket;
import info.varden.irclinqed.event.DCCConnectingEvent;
import info.varden.irclinqed.event.DCCFailedConnectEvent;
import info.varden.irclinqed.event.DCCSuccessfulConnectEvent;
import info.varden.irclinqed.forge.IKeyListener;
import info.varden.irclinqed.forge.KeyAssociation;
import info.varden.irclinqed.gui.GuiSimpleOverlay;
import info.varden.irclinqed.gui.GuiTimer;
import info.varden.irclinqed.gui.ITimerGui;
import info.varden.irclinqed.irc.IConnection;
import info.varden.irclinqed.plugin.DCCConnectionHelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

import net.minecraft.client.settings.KeyBinding;

public class DCCThread implements Runnable, IConnection, IIPSelectionHandler, ITimerGui, IKeyListener {
	private final String token;
	
	private IRCLinqed il;
	private Socket s;
	private BufferedReader i;
	private PrintWriter o;
	public Util util;
	public DCCServerSettings info;
	private String[] lastCommand;
	private DCCConnectionHelper connData;
	private ServerSocket server = null;

	private GuiSimpleOverlay overlay;
	private String ka_Cancel = "G";
	private GuiTimer t;
	private GuiTimer t2;
	private GuiTimer abort;
	private boolean showCancelKey = false;

	private String message;
	private boolean alreadyDisconnected = false;

	private boolean cancel = false;
	
	public DCCThread(IRCLinqed il, DCCServerSettings info) {
		this.token = new TokenGenerator().nextToken();
		this.il = il;
		this.info = info;
		this.util = new Util(this.il, this);
		this.connData = new DCCConnectionHelper(this);
	}

	@Override
	public void run() {
		if (!this.info.isHosting()) {
			try {
				
				this.util.writeToChat(MessageType.LQS_INFO, "Connecting to DCC host "
						+ this.info.getHost() + " port " + this.info.getPort()
						+ "...");
				this.il.getEventRegistry().postEvent(new DCCConnectingEvent(this.getConnectionData()));
				
				this.s = new Socket(this.info.getHost(), this.info.getPort());
				this.i = new BufferedReader(new InputStreamReader(this.s.getInputStream()));
				this.o = new PrintWriter(new OutputStreamWriter(this.s.getOutputStream()));
				this.util.writeToChat(MessageType.LQS_INFO,
						"Successfully connected to DCC host " + this.info.getHost()
								+ " port " + this.info.getPort() + "!");
				this.il.getEventRegistry().postEvent(new DCCSuccessfulConnectEvent(this.getConnectionData()));
			} catch (IOException e) {
				this.util.writeToChat(MessageType.LQS_ERROR,
						"Failed to connect to DCC host " + this.info.getHost() + " port "
								+ this.info.getPort() + ": " + e.getMessage());
				this.il.getEventRegistry().postEvent(new DCCFailedConnectEvent(this.getConnectionData(), e));
				e.printStackTrace();
			}
			try {
				parseInput();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			disconnect();
		} else {
			this.ka_Cancel = KeyAssociation.DCC_CANCEL.getDisplayedName();
			this.il.keyListenerQueue.add(this);
			this.message = "Initializing...";
			this.overlay = new GuiSimpleOverlay(this.il, new float[] {0.0F, 0.0F, 1.0F, 1.0F}, "DCC chat", this.message);
			this.overlay.load();
			this.t = new GuiTimer(this, 1, 50);
			this.t2 = new GuiTimer(this, 0, 2000);
			IPSelector ips = new IPSelector(this, this.il, this.info.getParentThread(), this.info.getTarget());
			ips.findAsync();
		}
	}
	
	public boolean sendRaw(String s) {
		if (this.o.checkError() || this.s.isClosed() || !this.s.isConnected()
				|| this.s.isOutputShutdown()) {
			return false;
		}
		this.il.logInfo("Sent packet: " + s);
		this.o.print(s + "\r\n");
		this.o.flush();
		return true;
	}
	
	public boolean sendMessage(String s) {
		if (sendRaw(s)) {
			this.util.writeToChat(MessageType.DCC_MESSAGE, "<" + this.info.getNickname() + "> " + s);
			return true;
		}
		return false;
	}

	@Override
	public String getNetworkName() {
		return this.info.getTarget();
	}

	@Override
	public String getHost() {
		return (this.info.isHosting() ? (this.s == null ? "127.0.0.1" : this.s.getInetAddress().getHostAddress()) : this.info.getHost());
	}

	@Override
	public int getPort() {
		return (this.info.isHosting() ? this.server.getLocalPort() : this.info.getPort());
	}

	@Override
	public boolean isDCC() {
		return true;
	}

	@Override
	public String getNickname() {
		return this.info.getNickname();
	}
	
	@Override
	public boolean disconnect() {
		if (!this.alreadyDisconnected) {
			this.alreadyDisconnected = true;
			try {
				// Close the connection
				if (this.info.isHosting()) {
					this.util.writeToChat(MessageType.LQS_INFO, "Closed DCC session with " + this.info.getTarget() + " (" + this.getHost() + ":" + this.getPort() + ")");
				} else {
					this.util.writeToChat(MessageType.LQS_INFO, "Disconnected from DCC host " + this.info.getTarget() + " (" + this.getHost() + ":" + this.getPort() + ")");
				}
				this.il.connectionManager.disconnect(this.il.connectionManager.getConnectionPair(this), true);
				this.s.shutdownInput();
				this.s.shutdownOutput();
				this.s.close();
			} catch (IOException ex) {
				ex.printStackTrace();
				return false;
			}
			try {
				this.server.close();
			} catch (Exception ex) {
				ex.printStackTrace();
				return false;
			}
			return true;
		} else {
			return false;
		}
	}

	public String getParentNetworkName() {
		return this.info.getParentThread().getNetworkName();
	}
	
	public void setLastCommand(String[] command) {
		this.lastCommand = command;
	}

	public String[] getLastCommand() {
		return this.lastCommand;
	}
	
	public DCCConnectionHelper getConnectionData() {
		return this.connData;
	}

	@Override
	public String getToken() {
		return this.token;
	}

	@Override
	public boolean equals(IConnection arg0) {
		return this.getToken().equals(arg0.getToken());
	}

	@Override
	public void onIPSelected(String ipAddress) {
		try {
			if (this.cancel) {
				return;
			}
			this.server = new ServerSocket(0);
			this.server.setSoTimeout(60000);
			DCCRequestPacket packet = null;

			packet = new DCCRequestPacket(this.il, this.info.getParentThread(), this.info.getTarget(), DCCType.CHAT, "chat", ipAddress, server.getLocalPort());
			packet.send();
			this.message = "Waiting for connection...";
			this.s = this.server.accept();
			this.i = new BufferedReader(new InputStreamReader(this.s.getInputStream()));
			this.o = new PrintWriter(new OutputStreamWriter(this.s.getOutputStream()));
			this.overlay.unload();
			this.util.writeToChat(MessageType.LQS_INFO, this.info.getTarget() + " connected via DCC");
		} catch (SocketTimeoutException e) {
			this.overlay.unload();
			this.util.writeToChat(MessageType.DCC_ERROR, "Chat request timed out.");
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.t.stop();
		this.t2.stop();
		try {
			parseInput();
		} catch (Exception e) {
			//this.util.writeToChat(MessageType.LQS_ERROR, "Disconnected from DCC chat with " + this.info.getTarget());
			e.printStackTrace();
		}
		
		disconnect();
	}
	
	public void parseInput() throws IOException {
		String str;
		while (this.s.isConnected()) {
			str = this.i.readLine();
			if (str == null) {
				break;
			}
			System.out.println(str);
			if (str.startsWith("\u0001ACTION") && str.endsWith("\u0001")) {
				String virtualCommand = ":" + this.info.getTarget() +
						"!" + this.info.getTarget() + "@" + this.info.getHost() +
						" PRIVMSG " + getNickname() + " :" + str;
				System.out.println("Handling ACTION packet: " + virtualCommand);
				VirtualIRCThread fakeThread = new VirtualIRCThread(this.il, this);
				ActionPacket packet = new ActionPacket(this.il, fakeThread, virtualCommand);
				packet.handle();
			} else {
				this.util.writeToChat(MessageType.DCC_MESSAGE, "<" + this.info.getTarget() + "> " + str);
			}
		}
	}

	@Override
	public void ticked(int id, int tickCount) {
		if (id == 0) {
			this.showCancelKey = !this.showCancelKey;
		} else if (id == 1) {
			if (!this.showCancelKey) {
				this.overlay.setMessage(this.message);
			} else {
				this.overlay.setMessage("Press " + this.ka_Cancel + " to cancel");
			}
		} else if (id == 2) {
			this.abort.stop();
			try {
				this.server.close();
			} catch (Exception e) {
			}
			this.overlay.unload();
		}
	}

	@Override
	public void invoke(KeyBinding kb) {
		if (KeyAssociation.DCC_CANCEL.is(kb)) {
			this.t.stop();
			this.t2.stop();
			this.il.keyListenerQueue.remove(this);
			this.cancel = true;
			this.overlay.setMessage("§cAborting...");
			this.util.writeToChat(MessageType.DCC_ERROR, "Aborting chat request...");
			this.abort = new GuiTimer(this, 2, 3000);
		}
	}

	@Override
	public String getHostAndPort() {
		return getHost() + ":" + getPort();
	}

	@Override
	public String getRealname() {
		return this.info.getParentThread().info.getRealname();
	}

	@Override
	public String[] getChannels() {
		return new String[0];
	}

}
