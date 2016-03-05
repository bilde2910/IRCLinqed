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

public class GuiTimerRunnable implements Runnable {
	
	private final GuiTimer timer;
	
	protected GuiTimerRunnable(GuiTimer timer) {
		this.timer = timer;
	}

	@Override
	public void run() {
		try {
			Thread.sleep(this.timer.getFrequency());
			while (!this.timer.isStopped()) {
				if (!this.timer.isPaused()) {
					Thread.sleep(1);
				} else {
					this.timer.tickCount++;
					this.timer.gui.ticked(this.timer.getId(), this.timer.getTickCount());
					Thread.sleep(this.timer.getFrequency());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}