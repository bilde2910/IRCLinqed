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

package info.varden.irclinqed.gui;

import info.varden.irclinqed.irc.IRCThread;

public class ThreadClearGuiListItems implements Runnable {
	
	private IRCThread thread;
	
	private ThreadClearGuiListItems(IRCThread thread) {
		this.thread = thread;
	}
	
	public static void clear(IRCThread thread) {
		Runnable r = new ThreadClearGuiListItems(thread);
		Thread t = new Thread(r);
		t.start();
	}

	@Override
	public void run() {
		try {
			Thread.sleep(100);
			this.thread.clearGuiListItems();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
