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

public class GuiTimer {
	protected final ITimerGui gui;
	protected int tickCount = 0;
	private boolean run = true;
	private boolean stop = false;
	private int frequency;
	private final int id;
	
	public GuiTimer(ITimerGui gui, int id, int frequency) {
		this.gui = gui;
		this.frequency = frequency;
		this.id = id;
		Runnable r = new GuiTimerRunnable(this);
		Thread t = new Thread(r);
		t.start();
	}
	
	public void resume() {
		this.run = true;
	}
	
	public void pause() {
		this.run = false;
	}
	
	public void stop() {
		this.stop = true;
	}
	
	public boolean isPaused() {
		return this.run;
	}
	
	public boolean isStopped() {
		return this.stop;
	}
	
	public int getFrequency() {
		return this.frequency;
	}
	
	public int getTickCount() {
		return this.tickCount;
	}
	
	public int getId() {
		return this.id;
	}
}
