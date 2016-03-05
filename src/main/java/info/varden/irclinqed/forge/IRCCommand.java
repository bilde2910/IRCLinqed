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
import info.varden.irclinqed.gui.GuiIngameSettings;
import info.varden.irclinqed.gui.GuiScreenSelectServer;
import info.varden.irclinqed.httpapi.IPFetcherThread;
import info.varden.irclinqed.httpapi.IngameIPSetter;

import java.util.List;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;

public class IRCCommand implements ICommand {

	private IRCLinqed il;
	
	public IRCCommand(IRCLinqed il) {
		this.il = il;
	}
	
	@Override
	public int compareTo(Object o) {
		return 0;
	}

	@Override
	public String getCommandName() {
		return "/irc";
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
		if (astring.length >= 1) {
			if (astring[0].equalsIgnoreCase("config")) {
				this.il.displayRequests.create(new GuiIngameSettings(this.il));
			} else if (astring[0].equalsIgnoreCase("refreship")) {
				this.il.logInfo("Getting public IP address...");
				this.il.util.writeToChat(MessageType.LQS_INFO, "Trying to obtain IP address...");
				IngameIPSetter ips = new IngameIPSetter(this.il);
				Runnable ift = new IPFetcherThread(ips);
				Thread t = new Thread(ift);
				t.start();
			} else if ("connect".equals(astring[0].toLowerCase())) {
				if (astring.length >= 2) {
					String host = astring[1];
					int port = 6667;
					if (host.contains(":")) {
						String[] hsplit = host.split(":");
						host = hsplit[0];
						port = Integer.parseInt(hsplit[1]);
					}
					
					host = this.il.connectionManager.translateAliases(host);
					if (host == null) {
						return;
					}
					
					this.il.connectionManager.connect(this.il.connectionManager.getSavedSettingsForConnection(host, port));
				} else {
					this.il.displayRequests.create(new GuiScreenSelectServer(this.il, null, true));
				}
			} else if ("disconnect".equals(astring[0].toLowerCase())) {
				
			}
		}
	}

	@Override
	public boolean canCommandSenderUseCommand(ICommandSender icommandsender) {
		return true;
	}

	@Override
	public List addTabCompletionOptions(ICommandSender icommandsender, String[] astring) {
		return null;
	}

	@Override
	public boolean isUsernameIndex(String[] astring, int i) {
		return false;
	}

}
