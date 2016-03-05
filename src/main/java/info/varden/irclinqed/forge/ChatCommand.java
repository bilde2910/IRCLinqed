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

package info.varden.irclinqed.forge;

import info.varden.irclinqed.IRCLinqed;
import info.varden.irclinqed.MessageType;
import info.varden.irclinqed.ThreadRunnablePair;
import info.varden.irclinqed.ctcp.ActionPacket;
import info.varden.irclinqed.ctcp.CTCPRequestPacket;
import info.varden.irclinqed.ctcp.CTCPType;
import info.varden.irclinqed.dcc.DCCServerSettings;
import info.varden.irclinqed.dcc.DCCThread;
import info.varden.irclinqed.dcc.GuiScreenOpenFile;
import info.varden.irclinqed.dcc.VirtualIRCThread;
import info.varden.irclinqed.gui.DisplayGuiRequest;
import info.varden.irclinqed.irc.IRCThread;
import info.varden.irclinqed.packet.PacketAway;
import info.varden.irclinqed.packet.PacketJoin;
import info.varden.irclinqed.packet.PacketLinks;
import info.varden.irclinqed.packet.PacketList;
import info.varden.irclinqed.packet.PacketMode;
import info.varden.irclinqed.packet.PacketNames;
import info.varden.irclinqed.packet.PacketNick;
import info.varden.irclinqed.packet.PacketNotice;
import info.varden.irclinqed.packet.PacketPart;
import info.varden.irclinqed.packet.PacketPrivmsg;
import info.varden.irclinqed.packet.PacketWho;
import info.varden.irclinqed.packet.PacketWhois;
import info.varden.irclinqed.packet.PacketWhowas;

import java.util.List;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;

public class ChatCommand implements ICommand {
	
	private IRCLinqed il;
	
	public ChatCommand(IRCLinqed il) {
		this.il = il;
	}

	@Override
	public int compareTo(Object o) {
		return 0;
	}

	@Override
	public String getCommandName() {
		return this.il.configLoader.getKey("chatchar", "#");
	}

	@Override
	public String getCommandUsage(ICommandSender icommandsender) {
		return null;
	}

	@Override
	public List getCommandAliases() {
		return null;
	}

	@Override
	public void processCommand(ICommandSender icommandsender, String[] astring) {
		IRCLinqed inst = this.il;
		int curserv = inst.connectionManager.getCurrentServer();
		if (curserv < 0) {
			this.il.util.writeToChat(MessageType.LQS_ERROR, "You're not on a server! Connect to a server using //irc connect or //irc connect host(:port).");
			return;
		}
		ThreadRunnablePair trp = inst.connectionManager.getConnectionPair(curserv);
		if (trp.runnable instanceof IRCThread) {
			IRCThread thrd = (IRCThread) trp.runnable;
			thrd.setLastCommand(astring);
			if (astring.length >= 1) {
				if (astring[0].startsWith("/")) {
					if ("/away".equals(astring[0].toLowerCase())) {
						String message = "";
						for (int i = 1; i < astring.length; i++) {
							message += " " + astring[i];
						}
						if (message.length() >= 1) {
							message = message.substring(1);
						}
						PacketAway packet = new PacketAway(this.il, thrd, message);
						packet.send();
					} else if ("/names".equals(astring[0].toLowerCase())) {
						String chan = thrd.getChannel();
						if (astring.length >= 2) {
							if (!"".equals(astring[1]) && astring[1] != null) {
								chan = astring[1];
							}
						}
						if ("".equals(chan) || chan == null) {
							this.il.util.writeToChat(MessageType.LQS_ERROR, "You're not on a channel, and no channel was specified for /names.");
						} else {
							chan = chan.toString();
							PacketNames packet = new PacketNames(inst, thrd, chan);
							packet.send();
						}
					} else if ("/banlist".equals(astring[0].toLowerCase())) {
						String chan = thrd.getChannel();
						if (astring.length >= 2) {
							if (!"".equals(astring[1]) && astring[1] != null) {
								chan = astring[1];
							}
						}
						if ("".equals(chan) || chan == null) {
							this.il.util.writeToChat(MessageType.LQS_ERROR, "You're not on a channel, and no channel was specified for /banlist.");
						} else {
							chan = chan.toString();
							PacketMode packet = new PacketMode(inst, thrd, chan, "+b", null);
							packet.send();
						}
					} else if ("/links".equals(astring[0].toLowerCase())) {
						String mask = "";
						if (astring.length >= 2) {
							if (!"".equals(astring[1]) && astring[1] != null) {
								mask = astring[1];
							}
						}
						PacketLinks packet = new PacketLinks(inst, thrd, mask);
						packet.send();
					} else if ("/list".equals(astring[0].toLowerCase())) {
						PacketList packet = new PacketList(inst, thrd, null);
						packet.send();
					} else if ("/quit".equals(astring[0].toLowerCase())) {
						this.il.connectionManager.disconnect(trp, false);
					} else if (astring.length >= 2) {
						if ("/join".equals(astring[0].toLowerCase())) {
							for (int i = 1; i < astring.length; i++) {
								PacketJoin packet = new PacketJoin(inst, thrd, astring[i]);
								packet.send();
							}
						} else if ("/part".equals(astring[0].toLowerCase())) {
							for (int i = 1; i < astring.length; i++) {
								PacketPart packet = new PacketPart(inst, thrd, astring[i]);
								packet.send();
							}
						} else if ("/nick".equals(astring[0].toLowerCase())) {
							if (thrd.info.setNickname(astring[1])) {
								PacketNick packet = new PacketNick(inst, thrd, astring[1]);
								packet.send();
							}
						} else if ("/whois".equals(astring[0].toLowerCase())) {
							PacketWhois packet = new PacketWhois(inst, thrd, astring[1]);
							packet.send();
						} else if ("/whowas".equals(astring[0].toLowerCase())) {
							PacketWhowas packet = new PacketWhowas(inst, thrd, astring[1]);
							packet.send();
						} else if ("/who".equals(astring[0].toLowerCase())) {
							PacketWho packet = new PacketWho(inst, thrd, astring[1]);
							packet.send();
						} else if ("/me".equals(astring[0].toLowerCase())) {
							String chan = thrd.getChannel();
							if (chan != null) {
								String message = "";
								for (int i = 1; i < astring.length; i++) {
									message += " " + astring[i];
								}
								message = message.substring(1);
								ActionPacket packet = new ActionPacket(inst, thrd, chan, message);
								packet.send();
							} else {
								this.il.util.writeToChat(MessageType.LQS_ERROR, "You're not talking on a channel - select a channel with /join #channel.");
							}
						} else if ("/voice".equals(astring[0].toLowerCase())) {
							String chan = thrd.getChannel();
							if (chan != null) {
								String users = "";
								String mode = "+";
								for (int i = 1; i < astring.length; i++) {
									users += " " + astring[i];
									mode += "v";
								}
								users = users.substring(1);
								PacketMode packet = new PacketMode(inst, thrd, chan, mode, users);
								packet.send();
							} else {
								this.il.util.writeToChat(MessageType.LQS_ERROR, "You're not talking on a channel - select a channel with /join #channel.");
							}
						} else if ("/op".equals(astring[0].toLowerCase())) {
							String chan = thrd.getChannel();
							if (chan != null) {
								String users = "";
								String mode = "+";
								for (int i = 1; i < astring.length; i++) {
									users += " " + astring[i];
									mode += "o";
								}
								users = users.substring(1);
								PacketMode packet = new PacketMode(inst, thrd, chan, mode, users);
								packet.send();
							} else {
								this.il.util.writeToChat(MessageType.LQS_ERROR, "You're not talking on a channel - select a channel with /join #channel.");
							}
						} else if ("/devoice".equals(astring[0].toLowerCase())) {
							String chan = thrd.getChannel();
							if (chan != null) {
								String users = "";
								String mode = "-";
								for (int i = 1; i < astring.length; i++) {
									users += " " + astring[i];
									mode += "v";
								}
								users = users.substring(1);
								PacketMode packet = new PacketMode(inst, thrd, chan, mode, users);
								packet.send();
							} else {
								this.il.util.writeToChat(MessageType.LQS_ERROR, "You're not talking on a channel - select a channel with /join #channel.");
							}
						} else if ("/deop".equals(astring[0].toLowerCase())) {
							String chan = thrd.getChannel();
							if (chan != null) {
								String users = "";
								String mode = "-";
								for (int i = 1; i < astring.length; i++) {
									users += " " + astring[i];
									mode += "o";
								}
								users = users.substring(1);
								PacketMode packet = new PacketMode(inst, thrd, chan, mode, users);
								packet.send();
							} else {
								this.il.util.writeToChat(MessageType.LQS_ERROR, "You're not talking on a channel - select a channel with /join #channel.");
							}
						} else if (astring.length >= 3) {
							if ("/msg".equals(astring[0].toLowerCase())) {
								String target = astring[1];
								String message = "";
								for (int i = 2; i < astring.length; i++) {
									message += " " + astring[i];
								}
								message = message.substring(1);
								PacketPrivmsg packet = new PacketPrivmsg(inst, thrd, target, message);
								packet.send();
							} else if ("/notice".equals(astring[0].toLowerCase())) {
								String target = astring[1];
								String message = "";
								for (int i = 2; i < astring.length; i++) {
									message += " " + astring[i];
								}
								message = message.substring(1);
								PacketNotice packet = new PacketNotice(inst, thrd, target, message);
								packet.send();
							} else if ("/ctcp".equals(astring[0].toLowerCase())) {
								String target = astring[1];
								String message = astring[2];
								CTCPType type = CTCPType.UNKNOWN;
								if (message.equalsIgnoreCase("ping")) {
									type = CTCPType.PING;
								} else if (message.equalsIgnoreCase("source")) {
									type = CTCPType.SOURCE;
								} else if (message.equalsIgnoreCase("time")) {
									type = CTCPType.TIME;
								} else if (message.equalsIgnoreCase("version")) {
									type = CTCPType.VERSION;
								}
								CTCPRequestPacket packet = new CTCPRequestPacket(inst, thrd, type, target);
								packet.send();
							} else if ("/mode".equals(astring[0].toLowerCase())) {
								String target = astring[1];
								String mode = astring[2];
								String users = "";
								if (astring.length >= 4) {
									for (int i = 3; i < astring.length; i++) {
										users += " " + astring[i];
									}
								}
								if ("".equals(users)) {
									users = null;
								}
								PacketMode packet = new PacketMode(inst, thrd, target, mode, users);
								packet.send();
							} else if ("/dcc".equals(astring[0].toLowerCase())) {
								if (this.il.ipAddress != null) {
									if ("send".equals(astring[1].toLowerCase())) {
										GuiScreenOpenFile giof = new GuiScreenOpenFile(this.il, thrd, null, astring[2]);
										DisplayGuiRequest dgr = new DisplayGuiRequest(this.il);
										dgr.create(giof);
									} else if ("chat".equals(astring[1].toLowerCase())) {
										DCCServerSettings info = new DCCServerSettings(thrd, thrd.getNickname(), astring[2]);
							        	this.il.connectionManager.connect(info);
									}
								} else {
									this.il.util.writeToChat(MessageType.LQS_ERROR, "Failed to fetch your IP address. Your IP address is required to initiate DCC actions. You can try to re-obtain your IP address by running //irc refreship.");
								}
							}
						}
					}
				} else {
					if (thrd.getChannel() != null) {
						String message = "";
						for (String part : astring) {
							message += " " + part;
						}
						message = message.substring(1);
						PacketPrivmsg packet = new PacketPrivmsg(inst, thrd, thrd.getChannel(), message);
						packet.send();
					} else {
						this.il.util.writeToChat(MessageType.LQS_ERROR, "You're not talking on a channel - select a channel with /join #channel.");
					}
				}
			}
		} else if (trp.runnable instanceof DCCThread) {
			DCCThread thrd = (DCCThread) trp.runnable;
			thrd.setLastCommand(astring);
			if (astring.length >= 1) {
				if (astring[0].startsWith("/")) {
					if ("/quit".equals(astring[0].toLowerCase())) {
						this.il.connectionManager.disconnect(trp, false);
					} else if (astring.length >= 2) {
						if ("/me".equals(astring[0].toLowerCase())) {
							String message = "";
							for (int i = 1; i < astring.length; i++) {
								message += " " + astring[i];
							}
							message = message.substring(1);
							ActionPacket packet = new ActionPacket(inst, new VirtualIRCThread(this.il, thrd), thrd.getNickname(), message);
							packet.send();
						}
					}
				} else {
					String message = "";
					for (String part : astring) {
						message += " " + part;
					}
					message = message.substring(1);
					thrd.sendMessage(message);
				}
			}
		}
	}

	@Override
	public boolean canCommandSenderUseCommand(ICommandSender icommandsender) {
		return true;
	}

	@Override
	public List addTabCompletionOptions(ICommandSender icommandsender,
			String[] astring) {
		return null;
	}

	@Override
	public boolean isUsernameIndex(String[] astring, int i) {
		return false;
	}

}
