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
import java.util.Map;
import java.util.Set;

import net.minecraft.stats.Achievement;
import net.minecraft.stats.StatBase;
import net.minecraftforge.common.MinecraftForge;

public class AchievementInterceptorMap implements Map<StatBase, Integer> {
	
	private final Map<StatBase, Integer> wrapped;
	
	public AchievementInterceptorMap(Map<StatBase, Integer> wrapped) {
		this.wrapped = wrapped;
	}

	@Override
	public void clear() {
		this.wrapped.clear();
	}

	@Override
	public boolean containsKey(Object arg0) {
		return this.wrapped.containsKey(arg0);
	}

	@Override
	public boolean containsValue(Object arg0) {
		return this.wrapped.containsValue(arg0);
	}

	@Override
	public Set<java.util.Map.Entry<StatBase, Integer>> entrySet() {
		return this.wrapped.entrySet();
	}

	@Override
	public Integer get(Object arg0) {
		return this.wrapped.get(arg0);
	}

	@Override
	public boolean isEmpty() {
		return this.wrapped.isEmpty();
	}

	@Override
	public Set<StatBase> keySet() {
		return this.wrapped.keySet();
	}

	@Override
	public Integer put(StatBase arg0, Integer arg1) {
		if (arg0 instanceof Achievement && !wrapped.containsKey(arg0)) {
			MinecraftForge.EVENT_BUS.post(new AchievementEvent((Achievement) arg0));
		}
		return this.wrapped.put(arg0, arg1);
	}

	@Override
	public void putAll(Map<? extends StatBase, ? extends Integer> arg0) {
		this.wrapped.putAll(arg0);
	}

	@Override
	public Integer remove(Object arg0) {
		return this.wrapped.remove(arg0);
	}

	@Override
	public int size() {
		return this.wrapped.size();
	}

	@Override
	public Collection<Integer> values() {
		return this.wrapped.values();
	}

}
