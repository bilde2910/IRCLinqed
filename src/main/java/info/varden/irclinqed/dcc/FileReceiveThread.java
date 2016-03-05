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
import info.varden.irclinqed.forge.IKeyListener;
import info.varden.irclinqed.forge.KeyAssociation;
import info.varden.irclinqed.irc.IRCThread;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.nio.ByteBuffer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;

import org.apache.commons.io.output.CountingOutputStream;

public class FileReceiveThread implements Runnable, IFileTransferConnection, IKeyListener {
	private final DCCRequestPacket packet;
	private final String host;
	private final int port;
	private final File file;
	private final long filesize;
	private final IRCThread thread;
	private final IRCLinqed il;
	private CountingOutputStream cos;
	private GuiFileProgress gfp;
	private boolean cancel = false;
	
	public FileReceiveThread(DCCRequestPacket packet) {
		this.packet = packet;
		this.host = packet.getIPAddress();
		this.port = packet.getPort();
		String dccParts[] = packet.getMessage().replace("\u0001", "").split(" ");
		File f = new File(packet.getArg());
		if (f.isAbsolute()) {
			this.file = f;
		} else {
			this.file = new File(new File(Minecraft.getMinecraft().mcDataDir, "dccfiles"), packet.getArg());
		}
		this.filesize = Long.parseLong(dccParts[5 + packet.getArg().split(" ").length - 1]);
		this.thread = packet.thread;
		this.il = packet.il;
	}

	@Override
	public void run() {
		try {
			this.il.keyListenerQueue.add(this);
			this.gfp = new GuiFileProgress(this.packet.il, this);
			this.packet.il.guiQueue.add(this.gfp);
			if (!this.file.getParentFile().exists()) {
				this.file.getParentFile().mkdirs();
			}
			if (!this.file.exists()) {
				this.file.createNewFile();
			}
			Socket s = new Socket(this.host, this.port);
			InputStream i = s.getInputStream();
			this.cos = new CountingOutputStream(new FileOutputStream(this.file));
			byte[] buff = new byte[1024];
			int k = -1;
			while ((k = i.read(buff)) > -1 && !this.cancel) {
				this.cos.write(buff, 0, k);
				s.getOutputStream().write(ByteBuffer.allocate(4).putInt((int) getByteCount()).array());
			}
			s.shutdownInput();
			s.shutdownOutput();
			s.close();
			this.cos.close();
			this.gfp.unload();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean isSending() {
		return false;
	}

	@Override
	public File getFile() {
		return this.file;
	}

	@Override
	public long getTotalSize() {
		return this.filesize;
	}

	@Override
	public String getSender() {
		return this.packet.getNickname();
	}

	@Override
	public String getReceiver() {
		return this.thread.getNickname();
	}

	@Override
	public long getByteCount() {
		if (this.cos == null) {
			return 0L;
		}
		return this.cos.getByteCount();
	}
	
	@Override
	public void invoke(KeyBinding kb) {
		if (KeyAssociation.DCC_CANCEL.is(kb)) {
			this.il.keyListenerQueue.remove(this);
			this.cancel = true;
			this.gfp.cancel();
			this.il.util.writeToChat(MessageType.DCC_ERROR, "Aborting file transfer...");
		}
	}
	
}
