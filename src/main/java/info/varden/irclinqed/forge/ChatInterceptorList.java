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

package info.varden.irclinqed.forge;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import net.minecraft.client.gui.GuiNewChat;
import net.minecraftforge.common.MinecraftForge;

public class ChatInterceptorList implements List<Object> {
	
	private final List<Object> wrapped;
	
	public ChatInterceptorList(List<Object> wrapped) {
		this.wrapped = wrapped;
	}

	@Override
	public boolean add(Object e) {
		if (e instanceof String) {
			MinecraftForge.EVENT_BUS.post(new MessageSentEvent((String) e));
			if (this.wrapped.size() > 0) {
				if (((String) this.wrapped.get(this.wrapped.size() - 1)).equals((String) e)) {
					return true;
				}
			}
		}
		return this.wrapped.add(e);
	}

	@Override
	public void add(int index, Object element) {
		this.wrapped.add(index, element);
	}

	@Override
	public boolean addAll(Collection<? extends Object> c) {
		return this.wrapped.addAll(c);
	}

	@Override
	public boolean addAll(int index, Collection<? extends Object> c) {
		return this.wrapped.addAll(index, c);
	}

	@Override
	public void clear() {
		this.wrapped.clear();
	}

	@Override
	public boolean contains(Object o) {
		return this.wrapped.contains(o);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return this.wrapped.containsAll(c);
	}

	@Override
	public Object get(int index) {
		StackTraceElement[] trace = Thread.currentThread().getStackTrace();
		if (trace[2].getClassName().equalsIgnoreCase(GuiNewChat.class.getName())) {
			return "";
		}
		return this.wrapped.get(index);
	}

	@Override
	public int indexOf(Object o) {
		return this.wrapped.indexOf(o);
	}

	@Override
	public boolean isEmpty() {
		return this.wrapped.isEmpty();
	}

	@Override
	public Iterator<Object> iterator() {
		return this.wrapped.iterator();
	}

	@Override
	public int lastIndexOf(Object o) {
		return this.wrapped.lastIndexOf(o);
	}

	@Override
	public ListIterator<Object> listIterator() {
		return this.wrapped.listIterator();
	}

	@Override
	public ListIterator<Object> listIterator(int index) {
		return this.wrapped.listIterator(index);
	}

	@Override
	public boolean remove(Object o) {
		return this.wrapped.remove(o);
	}

	@Override
	public Object remove(int index) {
		return this.wrapped.remove(index);
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		return this.wrapped.removeAll(c);
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		return this.wrapped.retainAll(c);
	}

	@Override
	public Object set(int index, Object element) {
		return this.wrapped.set(index, element);
	}

	@Override
	public int size() {
		return this.wrapped.size();
	}

	@Override
	public List<Object> subList(int fromIndex, int toIndex) {
		return this.wrapped.subList(fromIndex, toIndex);
	}

	@Override
	public Object[] toArray() {
		return this.wrapped.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return this.wrapped.toArray(a);
	}

}
