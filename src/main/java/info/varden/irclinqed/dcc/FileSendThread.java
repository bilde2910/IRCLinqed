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
import info.varden.irclinqed.Util;
import info.varden.irclinqed.forge.IKeyListener;
import info.varden.irclinqed.forge.KeyAssociation;
import info.varden.irclinqed.gui.GuiSimpleOverlay;
import info.varden.irclinqed.gui.GuiTimer;
import info.varden.irclinqed.gui.ITimerGui;
import info.varden.irclinqed.irc.IConnection;
import info.varden.irclinqed.irc.IRCThread;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

import net.minecraft.client.settings.KeyBinding;

import org.apache.commons.io.output.CountingOutputStream;

public class FileSendThread implements Runnable, IFileTransferConnection, IIPSelectionHandler, IKeyListener, ITimerGui {
	
	private final IRCLinqed il;
	private final File file;
	private final String target;
	
	private final long totalSize;
	private final IConnection thread;
	private CountingOutputStream cos;
	private boolean cancel = false;
	
	private GuiSimpleOverlay overlay;
	private GuiFileProgress gfp;
	private String message;
	private GuiTimer t;
	private GuiTimer t2;
	private GuiTimer abort;
	private String ka_Cancel = "G";
	private boolean showCancelKey = false;
	private ServerSocket server;
	
	public FileSendThread(IRCLinqed il, IConnection thread, String target, File file) {
		this.il = il;
		this.file = file;
		this.target = target;
		this.totalSize = this.file.length();
		this.thread = thread;
	}

	@Override
	public void run() {
		this.ka_Cancel = KeyAssociation.DCC_CANCEL.getDisplayedName();
		this.il.keyListenerQueue.add(this);
		this.message = "Initializing...";
		this.overlay = new GuiSimpleOverlay(this.il, new float[] {0.0F, 0.0F, 1.0F, 1.0F}, "DCC file transfer", this.message);
		this.overlay.load();
		this.t = new GuiTimer(this, 1, 50);
		this.t2 = new GuiTimer(this, 0, 2000);
		IPSelector ips = new IPSelector(this, this.il, this.thread, this.target);
		ips.findAsync();
	}

	@Override
	public boolean isSending() {
		return true;
	}

	@Override
	public File getFile() {
		return this.file;
	}

	@Override
	public long getTotalSize() {
		return this.totalSize;
	}

	@Override
	public String getSender() {
		return this.thread.getNickname();
	}

	@Override
	public String getReceiver() {
		return this.target;
	}

	@Override
	public long getByteCount() {
		if (this.cos == null) {
			return 0L;
		}
		return this.cos.getByteCount();
	}

	@Override
	public void onIPSelected(String ipAddress) {
		try {
			if (!this.file.exists()) {
				return;
			}
			this.server = new ServerSocket(0);
			this.server.setSoTimeout(60000);
			DCCRequestPacket packet = null;
			String filename = this.file.getName();
			if (filename.split(" ").length > 1) {
				filename = "\"" + filename + "\"";
			}
			if (this.thread instanceof IRCThread) {
				packet = new DCCRequestPacket(this.il, (IRCThread) this.thread, this.target, DCCType.SEND, filename, ipAddress, server.getLocalPort(), this.totalSize);
			} else if (this.thread instanceof DCCThread) {
				VirtualIRCThread thrd = new VirtualIRCThread(this.il, (DCCThread) this.thread);
				packet = new DCCRequestPacket(this.il, thrd, this.target, DCCType.SEND, filename, ipAddress, server.getLocalPort(), this.totalSize);
			} else {
				this.server.close();
				return;
			}
			packet.send();
			if (this.cancel) {
				this.server.close();
				return;
			}
			this.message = "Waiting for connection...";
			Socket s = this.server.accept();
			this.overlay.unload();
			this.gfp = new GuiFileProgress(this.il, this);
			this.il.guiQueue.add(this.gfp);
			InputStream i = new FileInputStream(this.file);
			this.cos = new CountingOutputStream(s.getOutputStream());
			byte[] buff = new byte[1024];
			int k = -1;
			while ((k = i.read(buff)) > -1 && !this.cancel) {
				this.cos.write(buff, 0, k);
				s.getInputStream().read(new byte[4]);
			}
			s.shutdownInput();
			s.shutdownOutput();
			s.close();
			this.server.close();
			i.close();
			this.gfp.unload();
		} catch (SocketTimeoutException e) {
			this.overlay.unload();
			Util.extractUtil(this.thread).writeToChat(MessageType.DCC_ERROR, "File send timed out.");
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.overlay.unload();
	}
	
	@Override
	public void invoke(KeyBinding kb) {
		if (KeyAssociation.DCC_CANCEL.is(kb)) {
			this.t.stop();
			this.t2.stop();
			this.il.keyListenerQueue.remove(this);
			this.cancel = true;
			if (this.gfp != null) {
				this.gfp.cancel();
			}
			this.overlay.setMessage("§cAborting...");
			new Util(this.il, this.thread).writeToChat(MessageType.DCC_ERROR, "Aborting file transfer...");
			this.abort = new GuiTimer(this, 2, 3000);
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

}
