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

import info.varden.irclinqed.IRCLinqed;

import java.lang.reflect.Field;
import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatFileWriter;
import cpw.mods.fml.common.event.FMLInitializationEvent;

public class ClientProxy implements CommonProxy {

	@Override
	public void init(FMLInitializationEvent event) {
		IRCLinqed.clientSide = true;
		try {
			System.out.println("Adding achievement interceptor");
			Field field = StatFileWriter.class.getDeclaredFields()[1];
			field.setAccessible(true);
			StatFileWriter sfw = Minecraft.getMinecraft().statFileWriter;
			Map<StatBase, Integer> oldMap = (Map<StatBase, Integer>) field.get(sfw);
			field.set(sfw, new AchievementInterceptorMap(oldMap));
			System.out.println("Achievement interceptor added");
		} catch (Exception e) {
			throw new RuntimeException("Failed to intercept achievements", e);
		}
	}

}
