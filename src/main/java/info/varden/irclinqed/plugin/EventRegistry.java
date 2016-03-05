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

package info.varden.irclinqed.plugin;

import java.util.ArrayList;

public class EventRegistry {
	private ArrayList<EventHandler> handlers;
	
	public EventRegistry() {
		this.handlers = new ArrayList<EventHandler>();
	}
	
	/**
	 * Registers an event handler to the event registry.
	 * @param handler
	 */
	public void registerHandler(EventHandler handler) {
		handlers.add(handler);
	}
	
	/**
	 * Posts an event to the event registry for event handlers to handle.
	 * @param event The event to handle.
	 */
	public void postEvent(Event event) {
		for (EventHandler handler : this.handlers) {
			event.handle(handler);
		}
	}
}
