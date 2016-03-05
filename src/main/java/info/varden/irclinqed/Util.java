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

import info.varden.irclinqed.dcc.DCCThread;
import info.varden.irclinqed.irc.IConnection;
import info.varden.irclinqed.irc.IRCThread;

import java.io.File;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;

public class Util {
	private final IRCLinqed il;
	private IConnection thread;
	
	public Util(IRCLinqed il) {
		this.il = il;
	}
	
	public Util(IRCLinqed il, IConnection thread) {
		this.il = il;
		this.thread = thread;
	}
	
	public static Util extractUtil(IConnection thread) {
		if (thread instanceof IRCThread) {
			return ((IRCThread) thread).util;
		} else if (thread instanceof DCCThread) {
			return ((DCCThread) thread).util;
		}
		return null;
	}

	public static File getConfigFile() {
		return new File(Minecraft.getMinecraft().mcDataDir, "config/IRCLinqed.properties");
	}
	
	public static String getHost(String dns) {
		if (dns.contains(":")) {
			return dns.split(":")[0];
		} else {
			return dns;
		}
	}
	
	public static int getPort(String dns) {
		if (dns.contains(":")) {
			return Integer.parseInt(dns.split(":")[1]);
		} else {
			return 6667;
		}
	}
	
	public static String getFileSizeWithUnit(long size) {
		String[] units = {"bytes", "KiB", "MiB", "GiB", "TiB"};
		String unit = "byte";
		size = size * 100L;
		long base = 0x10000000000L;
		for (int i = units.length - 1; i >= 0; i--) {
			if ((size / 100L) >= base) {
				unit = units[i];
				size /= base;
				break;
			}
			base >>= 10;
		}
		double nsize = (size / 100D);
		return nsize + " " + unit;
	}
	
	public static String escapeFormatting(String str) {
		return escapeFormatting(str, "f");
	}
	
	public static String escapeFormatting(String str, String color) {
		return str.replace("§", "§" + color);
	}
	
	public static String getLanIPAddress() throws SocketException {
		for (final Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces(); interfaces.hasMoreElements();) {
			final NetworkInterface cur = interfaces.nextElement();
			if (cur.isLoopback()) {
				continue;
			}
			for (final InterfaceAddress addr : cur.getInterfaceAddresses()) {
				final InetAddress inet_addr = addr.getAddress();
				if (!(inet_addr instanceof Inet4Address)) {
					continue;
				}
				return inet_addr.getHostAddress();
			}
		}
		return null;
	}
	
	public static void addMessageToChat(String message) {
		Minecraft.getMinecraft().thePlayer.addChatMessage(message);
	}
	
	public void writeToChat(MessageType type, String message) {
		writeToChat(type, "", message);
	}
	
	public void writeToChat(MessageType type, String target, String message) {
		message = parseIRCFormatting(message);
		if (this.thread != null) {
			if (!this.il.configLoader.getServerSpecificBooleanKey("showformatting", this.thread.getHost(), this.thread.getPort())) {
				message = EnumChatFormatting.func_110646_a(message);
			}
		}
		switch (type) {
			case DCC_MESSAGE:
			case DCC_INFO:
			case DCC_ERROR:
				addMessageToChat("§f[§e§o" + this.thread.getNetworkName() + "§r§f] " + message);
				break;
			case DCC_CHANNOTICE:
				addMessageToChat("§6[§e§o" + this.thread.getNetworkName() + "§r§6] " + message);
				break;
			case DCC_MOTD:
				addMessageToChat("§8[§e§o" + this.thread.getNetworkName() + "§r§8] [§5MotD§8] §f" + message);
				break;
			case DCC_USERACTION:
			case DCC_ME:
			case DCC_JOIN:
			case DCC_LEAVE:
				addMessageToChat("§f[§e§o" + this.thread.getNetworkName() + "§r§f] §b* " + message);
				break;
			case DCC_CTCP:
				addMessageToChat("§f[§e§o" + this.thread.getNetworkName() + "§r§f] " + message);
				break;
			case LQS_ERROR:
				addMessageToChat("§c[§4IRCLinqed§c] " + message);
				break;
			case LQS_INFO:
				addMessageToChat("§8[§4IRCLinqed§8] " + message);
				break;
			case LQS_CHANANNOUNCE:
				addMessageToChat("§f[§4IRCLinqed§f] §c* " + message);
				break;
			case LQS_USERACTION:
				addMessageToChat("§f[§4IRCLinqed§f] §b* " + message);
				break;
			case LQS_BUG:
				addMessageToChat("§8[§4IRCLinqed§8] §4IRCLinqed bug detected: " + message + " Please report to developer.");
				break;
			case IRC_MESSAGE:
				addMessageToChat("§f[§6" + this.thread.getNetworkName() + "§r§f] §f[§5" + target + "§f] " + message);
				break;
			case IRC_CTCP:
				addMessageToChat("§f[§6" + this.thread.getNetworkName() + "§r§f] " + message);
				break;
			case IRC_NOTICE:
				addMessageToChat("§f[§6" + this.thread.getNetworkName() + "§r§f] §e[§d§o" + target + "§o§e] " + message);
				break;
			case IRC_CHANNOTICE:
				addMessageToChat("§f[§6" + this.thread.getNetworkName() + "§r§f] §6[§5" + target + "§6] " + message);
				break;
			case IRC_PRIVATE:
				addMessageToChat("§f[§6" + this.thread.getNetworkName() + "§r§f] §7[§d§o" + target + "§o§7] " + message);
				break;
			case IRC_JOIN:
			case IRC_LEAVE:
			case IRC_USERACTION:
			case IRC_ME:
				addMessageToChat("§f[§6" + this.thread.getNetworkName() + "§r§f] §f[§5" + target + "§f] §b* " + message);
				break;
			case IRC_PRIVATEME:
				addMessageToChat("§f[§6" + this.thread.getNetworkName() + "§r§f] §7[§d§o" + target + "§o§7] §b* " + message);
				break;
			case SERVER_INFO:
			case SERVER_ERROR:
				addMessageToChat("§f[§6" + this.thread.getNetworkName() + "§r§f] " + message);
				break;
			case SERVER_CHANNOTICE:
				addMessageToChat("§6[§6" + this.thread.getNetworkName() + "§r§6] " + message);
				break;
			case SERVER_MOTD:
				addMessageToChat("§8[§6" + this.thread.getNetworkName() + "§r§8] [§5MotD§8] §f" + message);
				break;
			default:
				addMessageToChat("§f[§5" + target + "§f] " + message);
				break;
		}
	}
	
	public static String parseIRCFormatting(String message) {
		boolean bold = false, italic = false, underline = false;
		String msg = "";
		char[] chrs = message.toCharArray();
		for (int i = 0; i < chrs.length; i++) {
			if (chrs[i] == '\u0002') {
				bold = !bold;
				msg += "§l";
			} else if (chrs[i] == '\u001D' || chrs[i] == '\u0016') {
				italic = !italic;
				msg += "§o";
			} else if (chrs[i] == '\u001F') {
				underline = !underline;
				msg += "§n";
			} else if (chrs[i] == '\u000F') {
				bold = false;
				italic = false;
				underline = false;
				msg += "§r";
			} else if (chrs[i] == '\u0003') {
				boolean ok = false;
				if (chrs.length > i + 5) {
					String s = new String(new char[] {chrs[i + 1], chrs[i + 2], chrs[i + 3], chrs[i + 4], chrs[i + 5]});
					Pattern p = Pattern.compile("((0\\d)|(1[1-5])),((0\\d)|(1[1-5]))");
					Matcher m = p.matcher(s);
					if (m.find()) {
						int color = Integer.parseInt(s.substring(0, 2));
						msg += ircColorToMinecraftColor(color);
						i += 5;
						ok = true;
					}
				}
				if (chrs.length > i + 4 && !ok) {
					String s = new String(new char[] {chrs[i + 1], chrs[i + 2], chrs[i + 3], chrs[i + 4]});
					Pattern p1 = Pattern.compile("\\d,((0\\d)|(1[1-5]))");
					Matcher m1 = p1.matcher(s);
					if (m1.find()) {
						int color = Integer.parseInt(s.substring(0, 1));
						msg += ircColorToMinecraftColor(color);
						i += 4;
						ok = true;
					}
					Pattern p2 = Pattern.compile("((0\\d)|(1[1-5])),\\d");
					Matcher m2 = p2.matcher(s);
					if (m2.find()) {
						int color = Integer.parseInt(s.substring(0, 1));
						msg += ircColorToMinecraftColor(color);
						i += 4;
						ok = true;
					}
				}
				if (chrs.length > i + 3 && !ok) {
					String s = new String(new char[] {chrs[i + 1], chrs[i + 2], chrs[i + 3]});
					Pattern p = Pattern.compile("\\d,\\d");
					Matcher m = p.matcher(s);
					if (m.find()) {
						int color = Integer.parseInt(s.substring(0, 1));
						msg += ircColorToMinecraftColor(color);
						i += 3;
						ok = true;
					}
				}
				if (chrs.length > i + 2 && !ok) {
					String s = new String(new char[] {chrs[i + 1], chrs[i + 2]});
					Pattern p = Pattern.compile("(0\\d)|(1[1-5])");
					Matcher m = p.matcher(s);
					if (m.find()) {
						int color = Integer.parseInt(s);
						msg += ircColorToMinecraftColor(color);
						i += 2;
						ok = true;
					}
				}
				if (chrs.length > i + 1 && !ok) {
					String s = new String(new char[] {chrs[i + 1]});
					Pattern p = Pattern.compile("\\d");
					Matcher m = p.matcher(s);
					if (m.find()) {
						int color = Integer.parseInt(s);
						msg += ircColorToMinecraftColor(color);
						i += 1;
						ok = true;
					}
				}
			} else {
				msg += chrs[i];
			}
		}
		return msg;
	}
	
	public static String ircColorToMinecraftColor(int num) {
		switch (num) {
			case 0:
				return "§f";
			case 1:
			case 2:
			case 3:
			case 5:
			case 6:
			case 7:
				return "§" + (num - 1);
			case 4:
				return "§" + num;
			case 8:
				return "§e";
			case 9:
				return "§a";
			case 10:
				return "§3";
			case 11:
				return "§b";
			case 12:
				return "§9";
			case 13:
				return "§d";
			case 14:
				return "§8";
			case 15:
				return "§7";
			default:
				return "§f";
		}
	}
}
