# IRCLinqed

## What is it?

IRCLinqed is a mod for Minecraft that lets you chat over IRC in-game. No longer do you need any extra 3rd party chatting software, you can chat from right within Minecraft's chat window.

## Features

* Most extensive in-game IRC client in Minecraft's history
* Toggle a variety of settings, ranging from whether or not to display the MotD to showing mode changes and formatting
* Set a ton of server specific settings, including nickname, realname, part and quit reasons
* Chat on multiple servers and easily switch between them
* Nice looking interfaces for /who, /names, /banlist, /links, /list and more
* Search channels by regex
* Chat directly over DCC
* Send and receive files over DCC
* Wide range of IRC commands supported

## Screenshots



## Controls

* **R key**  
Lists connected servers, and allows for quickly switching between them.
* **F key**  
Lists joined channels on the currently active server, and allows for quickly switching between them.
* **Y key**  
Accepts an incoming DCC connection.
* **U key**  
Rejects an incoming DCC connection.
* **G key**  
Aborts a currently pending DCC request or active DCC file transfer.

## Supported commands

* **//irc connect \(\[server\]\(:\[port\]\)\)**  
Connects to an IRC server at port 6667 or the specified port, if one is given. If no server is given, opens the server list.
* **//irc config**  
Opens the configuration interface.
* **//irc refreship**  
Queries Varden Development's API servers to re-retrieve your network's public IP address. Primarily used to fix issues initiating DCC connections.
* **# /admin**  
Gets information about the currently connected IRC server's admins.
* **# /away \(\[reason\]\)**  
Sets yourself as away if a reason is given, or removes your away status if no reason was given.
* **# /banlist**  
Shows a list of banned users on the channel you're on or the specified channel, if one is given.
* **# /connect \[server1\] \[port\] \(\[server2\]\)**  
Requests server2 \(or current server, if not specified\) to connect to server1 at the specified port. Requires being an IRC operator.
* **# /ctcp \[user\] \[ping|source|time|version\]**  
Sends a CTCP message to the specified user.
* **# /dcc send \[user\]**  
Sends a file to a user.
* **# /dcc chat \[user\]**  
Opens a direct chat connection between you and another IRC user.
* **# /deop \[user\]**  
Deops \(sets mode -o\) the specified user.
* **# /devoice \[user\]**  
Devoices \(sets mode -v\) the specified user.
* **# /info \(\[server\]\)**  
Gets information from the specified IRC server, or the currently connected server if no server is given.
* **# /invite \[nickname\]**  
Invites the specified user to the current channel.
* **# /ison \[nicknames..\]**  
Gets whether or not the specified users are online.
* **# /join \[channel\] \(\[password\]\)**  
Joins the specified channel, optionally with a channel password.
* **# /kick \[nickname\] \(\[reason\]\)**  
Kicks the specified user off the current channel.
* **# /kill \[nickname\] \(\[comment\]\)**  
Kicks the specified user off the entire IRC network. Requires being an IRC operator.
* **# /knock \[channel\] \(\[message\]\)**  
Requests to join the specified channel.
* **# /links**  
Shows a list of links to other IRC servers that the one you're currently connected to knows about.
* **# /list**  
Shows a list of channels on the network.
* **# /lusers \(\[mask\] \(\[server\]\)\)**  
Lists information about clients connected to the IRC server. Defaults to the current IRC server.
* **# /me \[message\]**  
Sends an action message to IRC.
* **# /mode \[channel\] {\[+|-\]\[mode\]} \({users}\)**  
Sets the specified modes for the given users on the specified channel.
* **# /motd**  
Asks the server to re-send the server MotD. \(Requires MotD display to be turned on in settings.\)
* **# /msg \[user\] \[message\]**  
Sends a private message to the specified user.
* **# /cmsg \[channel\] \[nickname\] \[message\]**  
Sends a message to the specified user on the specified channel that bypasses spam protection limits, if the server supports it.
* **# /cnotice \[channel\] \[nickname\] \[message\]**  
Sends a notice message to the specified user on the specified channel that bypasses spam protection limits, if the server supports it.
* **# /names \(\[channel\]\)**  
Lists everyone on the channel you're on or the specified channel, if one is given.
* **# /nick \[nickname\]**  
Changes your nickname.
* **# /notice \[user|channel\] \[message\]**  
Sends a notice message to the specified user or channel.
* **# /op \[user\]**  
Ops \(sets mode +o\) the specified user.
* **# /oper \[username\] \[password\]**  
Elevates yourself to IRC operator status, if the correct username and password is specified.
* **# /part \[channel\]**  
Leaves the specified channel without disconnecting from the IRC network.
* **# /quit**  
Disconnects you from the IRC server.
* **# /rehash**  
Reloads the IRC server's configuration. Requires being an IRC operator.
* **# /restart**  
Restarts the IRC server. Requires being an IRC operator.
* **# /rules**  
Lists the server rules, if the server supports it.
* **# /servlist \(\[mask\] \(\[type\]\)\)**  
Lists network services connected to the IRC server.
* **# /silence \[hostname\]**  
Silences the specified hostname, if the server supports it.
* **# /silencelist**  
Gets a list of the users you've silenced with /silence. \(Partially unimplemented.\)
* **# /squit \[server\] \[reason\]**  
Requests the specified IRC server to disconnect from the network.
* **# /time \(\[server\]\)**  
Gets the specified IRC server's current time, defaults to the current IRC server if no server is given.
* **# /topic \(\[channel\] \(\[topic\]\)\)**  
Gets or sets the topic for the specified channel. If no channel is given, gets the topic for the current channel.
* **# /unsilence \[hostname\]**  
Unsilences a hostname previously silenced with the /silence command.
* **# /unwatch \[nickname\]**  
Stops watching a nickname previously watched with the /watch command.
* **# /userhost \[nicknames..\]**  
Gets information about the specified nicknames.
* **# /userip \[nickname\]**  
Gets the IP of the specified nickname, if the server supports it.
* **# /version \(\[version\]\)**  
Gets version information from the specified server, or the current server if no server is given.
* **# /voice \[user\]**  
Voices \(sets mode +v\) the specified user.
* **# /watch \[nickname\]**  
Watches the specified nickname, if the server supports it.
* **# /watchlist**  
Gets a list of the users you've watched with /watch. \(Partially unimplemented.\)
* **# /who \[hostmask\]**  
Shows a list of users on the channels you are on who matches the hostmask specified.
* **# /whois \[nickname\]**  
Shows information about a user currently online.
* **# /whowas \[nickname\]**  
Shows information about a user who disconnected recently.

## Installation

1. Install Minecraft Forge.
2. Open the Forge mods folder.
3. Drop the [IRCLinqed jar file](https://varden.info/download.php?sid=12) in there.
4. Play Minecraft!

## Frequently Asked Questions

**Q: Does this mod require ModLoader or Forge?**  
A: Yes - Forge is required.

**Q: Can I include this mod in my mod pack?**  
A: If you contact me prior to adding it, and you get my written consent, then yes. I usually accept my things being put in mod packs, but NOT if it has been added without my consent or without understanding of the requirements I send you.

**Q: How do I install it?**  
A: Refer to the Installation section just above.

**Q: Do I have to pay anything to download this mod?**  
A: Of course not!

**Q: When I enter my search query in the channel list window, the search field turns red. Why?**  
A: The search field uses Java regex to search for channels. If the field turns red, that means your regex query is incorrect. Have a look at this article for a Java regex guide.

**Q: How do I switch between channels I'm on?**  
A: Use the /join command, as if you were joining the channel.

**Q: How do I switch between servers I'm on?**  
A: Multi-server support is currently just in Alpha, and switching between servers hasn't been implemented yet. You can, however, disconnect from a server with the /quit command, you'll then automatically talk in the previous server you connected to. This may be buggy.

**Q: How do I send messages over IRC?**  
A: In your standard chatbox (press T in-game), enter a number sign (#), followed by a space, then your message.

**Q: How do I send commands to IRC?**  
A: You send them just like normal IRC messages; just enter your command instead of the message.

**Q: I found a bug! How do I report it?**  
A: Send us a message over at the Contact Us page (see navbar at top), or post a reply on our Minecraft Forums topic. Remember to include a description of what you did prior to and when the bug occurred, and, if possible, include a log.

**Q: What does it mean that the mod is in alpha?**  
A: Alpha means that the mod is not fully developed, and it's known to contain bugs and miss features. It's not a finished product and requires major work to complete.

## Known bugs

* Minecraft to IRC settings are currently not functional.
* Doesn't completely implement RFC 1459 and RFC 2812
* There are more bugs. Please report them if you see them.
