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
import info.varden.irclinqed.packet.*;

public class PacketHandler {
	private IRCLinqed il;
	private IRCThread thread;

	public PacketHandler(IRCLinqed il, IRCThread thread) {
		this.il = il;
		this.thread = thread;
	}

	public IRCPacket getPacket(String command) {
		if ("ADMIN".equals(command.split(" ")[1])) {
			return new PacketAdmin(this.il, this.thread, command);
		} else if ("AWAY".equals(command.split(" ")[1])) {
			return new PacketAway(this.il, this.thread, command);
		} else if ("CONNECT".equals(command.split(" ")[1])) {
			return new PacketConnect(this.il, this.thread, command);
		} else if ("ERROR".equals(command.split(" ")[1])) {
			return new PacketError(this.il, this.thread, command);
		} else if ("INFO".equals(command.split(" ")[1])) {
			return new PacketInfo(this.il, this.thread, command);
		} else if ("INVITE".equals(command.split(" ")[1])) {
			return new PacketInvite(this.il, this.thread, command);
		} else if ("ISON".equals(command.split(" ")[1])) {
			return new PacketIson(this.il, this.thread, command);
		} else if ("JOIN".equals(command.split(" ")[1])) {
			return new PacketJoin(this.il, this.thread, command);
		} else if ("KICK".equals(command.split(" ")[1])) {
			return new PacketKick(this.il, this.thread, command);
		} else if ("KILL".equals(command.split(" ")[1])) {
			return new PacketKill(this.il, this.thread, command);
		} else if ("LINKS".equals(command.split(" ")[1])) {
			return new PacketLinks(this.il, this.thread, command);
		} else if ("LIST".equals(command.split(" ")[1])) {
			return new PacketList(this.il, this.thread, command);
		} else if ("MODE".equals(command.split(" ")[1])) {
			return new PacketMode(this.il, this.thread, command);
		} else if ("NAMES".equals(command.split(" ")[1])) {
			return new PacketNames(this.il, this.thread, command);
		} else if ("NICK".equals(command.split(" ")[1])) {
			return new PacketNick(this.il, this.thread, command);
		} else if ("NOTICE".equals(command.split(" ")[1])) {
			return new PacketNotice(this.il, this.thread, command);
		} else if ("OPER".equals(command.split(" ")[1])) {
			return new PacketOper(this.il, this.thread, command);
		} else if ("PART".equals(command.split(" ")[1])) {
			return new PacketPart(this.il, this.thread, command);
		} else if ("PASS".equals(command.split(" ")[1])) {
			return new PacketPass(this.il, this.thread, command);
		} else if ("PING".equals(command.split(" ")[1])) {
			return new PacketPing(this.il, this.thread, command);
		} else if ("PONG".equals(command.split(" ")[1])) {
			return new PacketPong(this.il, this.thread, command);
		} else if ("PRIVMSG".equals(command.split(" ")[1])) {
			return new PacketPrivmsg(this.il, this.thread, command);
		} else if ("QUIT".equals(command.split(" ")[1])) {
			return new PacketQuit(this.il, this.thread, command);
		} else if ("REHASH".equals(command.split(" ")[1])) {
			return new PacketRehash(this.il, this.thread, command);
		} else if ("RESTART".equals(command.split(" ")[1])) {
			return new PacketRestart(this.il, this.thread, command);
		} else if ("SERVER".equals(command.split(" ")[1])) {
			return new PacketServer(this.il, this.thread, command);
		} else if ("SQUIT".equals(command.split(" ")[1])) {
			return new PacketSQuit(this.il, this.thread, command);
		} else if ("STATS".equals(command.split(" ")[1])) {
			return new PacketStats(this.il, this.thread, command);
		} else if ("SUMMON".equals(command.split(" ")[1])) {
			return new PacketSummon(this.il, this.thread, command);
		} else if ("TIME".equals(command.split(" ")[1])) {
			return new PacketTime(this.il, this.thread, command);
		} else if ("TOPIC".equals(command.split(" ")[1])) {
			return new PacketTopic(this.il, this.thread, command);
		} else if ("TRACE".equals(command.split(" ")[1])) {
			return new PacketTrace(this.il, this.thread, command);
		} else if ("USERHOST".equals(command.split(" ")[1])) {
			return new PacketUserHost(this.il, this.thread, command);
		} else if ("USER".equals(command.split(" ")[1])) {
			return new PacketUser(this.il, this.thread, command);
		} else if ("USERS".equals(command.split(" ")[1])) {
			return new PacketUsers(this.il, this.thread, command);
		} else if ("VERSION".equals(command.split(" ")[1])) {
			return new PacketVersion(this.il, this.thread, command);
		} else if ("WALLOPS".equals(command.split(" ")[1])) {
			return new PacketWallOps(this.il, this.thread, command);
		} else if ("WHOIS".equals(command.split(" ")[1])) {
			return new PacketWhois(this.il, this.thread, command);
		} else if ("WHO".equals(command.split(" ")[1])) {
			return new PacketWho(this.il, this.thread, command);
		} else if ("WHOWAS".equals(command.split(" ")[1])) {
			return new PacketWhowas(this.il, this.thread, command);
		} else if ("001".equals(command.split(" ")[1])) {
			return new Packet001Welcome(this.il, this.thread, command);
		} else if ("002".equals(command.split(" ")[1])) {
			return new Packet002YourHost(this.il, this.thread, command);
		} else if ("003".equals(command.split(" ")[1])) {
			return new Packet003Created(this.il, this.thread, command);
		} else if ("004".equals(command.split(" ")[1])) {
			return new Packet004MyInfo(this.il, this.thread, command);
		} else if ("005".equals(command.split(" ")[1])) {
			return new Packet005Bounce(this.il, this.thread, command);
		} else if ("200".equals(command.split(" ")[1])) {
			return new Packet200TraceLink(this.il, this.thread, command);
		} else if ("201".equals(command.split(" ")[1])) {
			return new Packet201TraceConnecting(this.il, this.thread, command);
		} else if ("202".equals(command.split(" ")[1])) {
			return new Packet202TraceHandshake(this.il, this.thread, command);
		} else if ("203".equals(command.split(" ")[1])) {
			return new Packet203TraceUnknown(this.il, this.thread, command);
		} else if ("204".equals(command.split(" ")[1])) {
			return new Packet204TraceOperator(this.il, this.thread, command);
		} else if ("205".equals(command.split(" ")[1])) {
			return new Packet205TraceUser(this.il, this.thread, command);
		} else if ("206".equals(command.split(" ")[1])) {
			return new Packet206TraceServer(this.il, this.thread, command);
		} else if ("208".equals(command.split(" ")[1])) {
			return new Packet208TraceNewType(this.il, this.thread, command);
		} else if ("211".equals(command.split(" ")[1])) {
			return new Packet211StatsLinkInfo(this.il, this.thread, command);
		} else if ("212".equals(command.split(" ")[1])) {
			return new Packet212StatsCommands(this.il, this.thread, command);
		} else if ("213".equals(command.split(" ")[1])) {
			return new Packet213StatsCLine(this.il, this.thread, command);
		} else if ("214".equals(command.split(" ")[1])) {
			return new Packet214StatsNLine(this.il, this.thread, command);
		} else if ("215".equals(command.split(" ")[1])) {
			return new Packet215StatsILine(this.il, this.thread, command);
		} else if ("216".equals(command.split(" ")[1])) {
			return new Packet216StatsKLine(this.il, this.thread, command);
		} else if ("218".equals(command.split(" ")[1])) {
			return new Packet218StatsYLine(this.il, this.thread, command);
		} else if ("219".equals(command.split(" ")[1])) {
			return new Packet219EndOfStats(this.il, this.thread, command);
		} else if ("221".equals(command.split(" ")[1])) {
			return new Packet221UModeIs(this.il, this.thread, command);
		} else if ("241".equals(command.split(" ")[1])) {
			return new Packet241StatsLLine(this.il, this.thread, command);
		} else if ("242".equals(command.split(" ")[1])) {
			return new Packet242StatsUptime(this.il, this.thread, command);
		} else if ("243".equals(command.split(" ")[1])) {
			return new Packet243StatsOLine(this.il, this.thread, command);
		} else if ("244".equals(command.split(" ")[1])) {
			return new Packet244StatsHLine(this.il, this.thread, command);
		} else if ("251".equals(command.split(" ")[1])) {
			return new Packet251LUserClient(this.il, this.thread, command);
		} else if ("252".equals(command.split(" ")[1])) {
			return new Packet252LUserOp(this.il, this.thread, command);
		} else if ("253".equals(command.split(" ")[1])) {
			return new Packet253LUserUnknown(this.il, this.thread, command);
		} else if ("254".equals(command.split(" ")[1])) {
			return new Packet254LUserChannels(this.il, this.thread, command);
		} else if ("255".equals(command.split(" ")[1])) {
			return new Packet255LUserMe(this.il, this.thread, command);
		} else if ("256".equals(command.split(" ")[1])) {
			return new Packet256AdminMe(this.il, this.thread, command);
		} else if ("257".equals(command.split(" ")[1])) {
			return new Packet257AdminLoc1(this.il, this.thread, command);
		} else if ("258".equals(command.split(" ")[1])) {
			return new Packet258AdminLoc2(this.il, this.thread, command);
		} else if ("259".equals(command.split(" ")[1])) {
			return new Packet259AdminEmail(this.il, this.thread, command);
		} else if ("261".equals(command.split(" ")[1])) {
			return new Packet261TraceLog(this.il, this.thread, command);
		} else if ("300".equals(command.split(" ")[1])) {
			return new Packet300None(this.il, this.thread, command);
		} else if ("301".equals(command.split(" ")[1])) {
			return new Packet301Away(this.il, this.thread, command);
		} else if ("302".equals(command.split(" ")[1])) {
			return new Packet302UserHost(this.il, this.thread, command);
		} else if ("303".equals(command.split(" ")[1])) {
			return new Packet303Ison(this.il, this.thread, command);
		} else if ("305".equals(command.split(" ")[1])) {
			return new Packet305UnAway(this.il, this.thread, command);
		} else if ("306".equals(command.split(" ")[1])) {
			return new Packet306NowAway(this.il, this.thread, command);
		} else if ("311".equals(command.split(" ")[1])) {
			return new Packet311WhoisUser(this.il, this.thread, command);
		} else if ("312".equals(command.split(" ")[1])) {
			return new Packet312WhoisServer(this.il, this.thread, command);
		} else if ("313".equals(command.split(" ")[1])) {
			return new Packet313WhoisOperator(this.il, this.thread, command);
		} else if ("314".equals(command.split(" ")[1])) {
			return new Packet314WhowasUser(this.il, this.thread, command);
		} else if ("315".equals(command.split(" ")[1])) {
			return new Packet315EndOfWho(this.il, this.thread, command);
		} else if ("317".equals(command.split(" ")[1])) {
			return new Packet317WhoisIdle(this.il, this.thread, command);
		} else if ("318".equals(command.split(" ")[1])) {
			return new Packet318EndOfWhois(this.il, this.thread, command);
		} else if ("319".equals(command.split(" ")[1])) {
			return new Packet319WhoisChannels(this.il, this.thread, command);
		} else if ("321".equals(command.split(" ")[1])) {
			return new Packet321ListStart(this.il, this.thread, command);
		} else if ("322".equals(command.split(" ")[1])) {
			return new Packet322List(this.il, this.thread, command);
		} else if ("323".equals(command.split(" ")[1])) {
			return new Packet323ListEnd(this.il, this.thread, command);
		} else if ("324".equals(command.split(" ")[1])) {
			return new Packet324ChannelModeIs(this.il, this.thread, command);
		} else if ("331".equals(command.split(" ")[1])) {
			return new Packet331NoTopic(this.il, this.thread, command);
		} else if ("332".equals(command.split(" ")[1])) {
			return new Packet332Topic(this.il, this.thread, command);
		} else if ("341".equals(command.split(" ")[1])) {
			return new Packet341Inviting(this.il, this.thread, command);
		} else if ("342".equals(command.split(" ")[1])) {
			return new Packet342Summoning(this.il, this.thread, command);
		} else if ("351".equals(command.split(" ")[1])) {
			return new Packet351Version(this.il, this.thread, command);
		} else if ("352".equals(command.split(" ")[1])) {
			return new Packet352WhoReply(this.il, this.thread, command);
		} else if ("353".equals(command.split(" ")[1])) {
			return new Packet353NamReply(this.il, this.thread, command);
		} else if ("364".equals(command.split(" ")[1])) {
			return new Packet364Links(this.il, this.thread, command);
		} else if ("365".equals(command.split(" ")[1])) {
			return new Packet365EndOfLinks(this.il, this.thread, command);
		} else if ("366".equals(command.split(" ")[1])) {
			return new Packet366EndOfNames(this.il, this.thread, command);
		} else if ("367".equals(command.split(" ")[1])) {
			return new Packet367BanList(this.il, this.thread, command);
		} else if ("368".equals(command.split(" ")[1])) {
			return new Packet368EndOfBanList(this.il, this.thread, command);
		} else if ("369".equals(command.split(" ")[1])) {
			return new Packet369EndOfWhowas(this.il, this.thread, command);
		} else if ("371".equals(command.split(" ")[1])) {
			return new Packet371Info(this.il, this.thread, command);
		} else if ("372".equals(command.split(" ")[1])) {
			return new Packet372MOTD(this.il, this.thread, command);
		} else if ("374".equals(command.split(" ")[1])) {
			return new Packet374EndOfInfo(this.il, this.thread, command);
		} else if ("375".equals(command.split(" ")[1])) {
			return new Packet375MOTDStart(this.il, this.thread, command);
		} else if ("376".equals(command.split(" ")[1])) {
			return new Packet376EndOfMOTD(this.il, this.thread, command);
		} else if ("381".equals(command.split(" ")[1])) {
			return new Packet381YoureOper(this.il, this.thread, command);
		} else if ("382".equals(command.split(" ")[1])) {
			return new Packet382Rehashing(this.il, this.thread, command);
		} else if ("391".equals(command.split(" ")[1])) {
			return new Packet391Time(this.il, this.thread, command);
		} else if ("392".equals(command.split(" ")[1])) {
			return new Packet392UsersStart(this.il, this.thread, command);
		} else if ("393".equals(command.split(" ")[1])) {
			return new Packet393Users(this.il, this.thread, command);
		} else if ("394".equals(command.split(" ")[1])) {
			return new Packet394EndOfUsers(this.il, this.thread, command);
		} else if ("395".equals(command.split(" ")[1])) {
			return new Packet395NoUsers(this.il, this.thread, command);
		} else if ("401".equals(command.split(" ")[1])) {
			return new Packet401NoSuchNick(this.il, this.thread, command);
		} else if ("402".equals(command.split(" ")[1])) {
			return new Packet402NoSuchServer(this.il, this.thread, command);
		} else if ("403".equals(command.split(" ")[1])) {
			return new Packet403NoSuchChannel(this.il, this.thread, command);
		} else if ("404".equals(command.split(" ")[1])) {
			return new Packet404CannotSendToChan(this.il, this.thread, command);
		} else if ("405".equals(command.split(" ")[1])) {
			return new Packet405TooManyChannels(this.il, this.thread, command);
		} else if ("406".equals(command.split(" ")[1])) {
			return new Packet406WasNoSuchNick(this.il, this.thread, command);
		} else if ("407".equals(command.split(" ")[1])) {
			return new Packet407TooManyTargets(this.il, this.thread, command);
		} else if ("409".equals(command.split(" ")[1])) {
			return new Packet409NoOrigin(this.il, this.thread, command);
		} else if ("411".equals(command.split(" ")[1])) {
			return new Packet411NoRecipient(this.il, this.thread, command);
		} else if ("412".equals(command.split(" ")[1])) {
			return new Packet412NoTextToSend(this.il, this.thread, command);
		} else if ("413".equals(command.split(" ")[1])) {
			return new Packet413NoToplevel(this.il, this.thread, command);
		} else if ("414".equals(command.split(" ")[1])) {
			return new Packet414WildToplevel(this.il, this.thread, command);
		} else if ("421".equals(command.split(" ")[1])) {
			return new Packet421UnknownCommand(this.il, this.thread, command);
		} else if ("422".equals(command.split(" ")[1])) {
			return new Packet422NoMOTD(this.il, this.thread, command);
		} else if ("423".equals(command.split(" ")[1])) {
			return new Packet423NoAdminInfo(this.il, this.thread, command);
		} else if ("424".equals(command.split(" ")[1])) {
			return new Packet424FileError(this.il, this.thread, command);
		} else if ("431".equals(command.split(" ")[1])) {
			return new Packet431NoNicknameGiven(this.il, this.thread, command);
		} else if ("432".equals(command.split(" ")[1])) {
			return new Packet432ErroneousNickname(this.il, this.thread, command);
		} else if ("433".equals(command.split(" ")[1])) {
			return new Packet433NicknameInUse(this.il, this.thread, command);
		} else if ("436".equals(command.split(" ")[1])) {
			return new Packet436NickCollision(this.il, this.thread, command);
		} else if ("441".equals(command.split(" ")[1])) {
			return new Packet441UserNotInChannel(this.il, this.thread, command);
		} else if ("442".equals(command.split(" ")[1])) {
			return new Packet442NotOnChannel(this.il, this.thread, command);
		} else if ("443".equals(command.split(" ")[1])) {
			return new Packet443UserOnChannel(this.il, this.thread, command);
		} else if ("444".equals(command.split(" ")[1])) {
			return new Packet444NoLogin(this.il, this.thread, command);
		} else if ("445".equals(command.split(" ")[1])) {
			return new Packet445SummonDisabled(this.il, this.thread, command);
		} else if ("446".equals(command.split(" ")[1])) {
			return new Packet446UsersDisabled(this.il, this.thread, command);
		} else if ("451".equals(command.split(" ")[1])) {
			return new Packet451NotRegistered(this.il, this.thread, command);
		} else if ("461".equals(command.split(" ")[1])) {
			return new Packet461NeedMoreParams(this.il, this.thread, command);
		} else if ("462".equals(command.split(" ")[1])) {
			return new Packet462AlreadyRegistered(this.il, this.thread, command);
		} else if ("463".equals(command.split(" ")[1])) {
			return new Packet463NoPermForHost(this.il, this.thread, command);
		} else if ("464".equals(command.split(" ")[1])) {
			return new Packet464PasswdMismatch(this.il, this.thread, command);
		} else if ("465".equals(command.split(" ")[1])) {
			return new Packet465YoureBannedCreep(this.il, this.thread, command);
		} else if ("467".equals(command.split(" ")[1])) {
			return new Packet467KeySet(this.il, this.thread, command);
		} else if ("471".equals(command.split(" ")[1])) {
			return new Packet471ChannelIsFull(this.il, this.thread, command);
		} else if ("472".equals(command.split(" ")[1])) {
			return new Packet472UnknownMode(this.il, this.thread, command);
		} else if ("473".equals(command.split(" ")[1])) {
			return new Packet473InviteOnlyChan(this.il, this.thread, command);
		} else if ("474".equals(command.split(" ")[1])) {
			return new Packet474BannedFromChan(this.il, this.thread, command);
		} else if ("475".equals(command.split(" ")[1])) {
			return new Packet475BadChannelKey(this.il, this.thread, command);
		} else if ("481".equals(command.split(" ")[1])) {
			return new Packet481NoPrivileges(this.il, this.thread, command);
		} else if ("482".equals(command.split(" ")[1])) {
			return new Packet482ChanoprIvsNeeded(this.il, this.thread, command);
		} else if ("483".equals(command.split(" ")[1])) {
			return new Packet483CantKillServer(this.il, this.thread, command);
		} else if ("491".equals(command.split(" ")[1])) {
			return new Packet491NoOperHost(this.il, this.thread, command);
		} else if ("501".equals(command.split(" ")[1])) {
			return new Packet501UModeUnknownFlag(this.il, this.thread, command);
		} else if ("502".equals(command.split(" ")[1])) {
			return new Packet502UsersDontMatch(this.il, this.thread, command);
		}
		return new NullPacket(this.il, this.thread, command);
	}
}
