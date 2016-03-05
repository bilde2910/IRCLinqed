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

import info.varden.irclinqed.IRCLinqed;

import java.util.EnumSet;
import java.util.LinkedList;
import java.util.Queue;

import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;

public class TickHandlerDisplayGui implements ITickHandler {
	
	private Queue<Runnable> runQueue;
	private IRCLinqed il;
	
	public TickHandlerDisplayGui(IRCLinqed il) {
		this.il = il;
		runQueue = new LinkedList<Runnable>();
	}
	
	public void addToQueue(Runnable r) {
		this.runQueue.add(r);
	}

	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData) {}

	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData) {}

	@Override
	public EnumSet<TickType> ticks() {
		for (int i = 0; i < this.runQueue.size(); i++) {
			this.runQueue.poll().run();
		}
		return null;
	}

	@Override
	public String getLabel() {
		return null;
	}

}
