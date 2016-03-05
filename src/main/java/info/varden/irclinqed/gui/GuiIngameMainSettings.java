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
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiIngameMainSettings extends GuiScreen {
	
    private final GuiScreen parentScreen;
    private final String server;
    private final IRCLinqed il;

    public GuiIngameMainSettings(IRCLinqed il, GuiScreen parent, String server) {
        this.parentScreen = parent;
        this.server = server;
        this.il = il;
    }

    @Override
    public void initGui() {
        this.buttonList.clear();
        this.buttonList.add(new GuiButton(101, this.width / 2 - 152, this.height / 6 + 26, 150, 20, "Identity"));
        this.buttonList.add(new GuiButton(100, this.width / 2 + 2, this.height / 6 + 26, 150, 20, "Quit reasons"));
        this.buttonList.add(new GuiButton(102, this.width / 2 - 152, this.height / 6 + 76, 150, 20, "IRC to Minecraft"));
        this.buttonList.add(new GuiButton(103, this.width / 2 + 2, this.height / 6 + 76, 150, 20, "Minecraft to IRC"));
        this.buttonList.add(new GuiButton(105, this.width / 2 - 152, this.height / 6 + 126, 150, 20, "Connection settings"));
        this.buttonList.add(new GuiButton(104, this.width / 2 + 2, this.height / 6 + 126, 150, 20, "Auto-join channels"));
        this.buttonList.add(new GuiButton(200, this.width / 2 - 100, this.height / 6 + 168, "Done"));
        ((GuiButton) this.buttonList.get(4)).enabled = false;
        if (this.server == null) {
        	((GuiButton) this.buttonList.get(5)).enabled = false;
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.enabled) {
            switch (button.id) {
            	case 101:
            		this.mc.displayGuiScreen(new GuiIngameIdentitySettings(this.il, this, this.server));
            		break;
            	case 100:
            		this.mc.displayGuiScreen(new GuiIngameQuitSettings(this.il, this, this.server));
            		break;
            	case 102:
            		this.mc.displayGuiScreen(new GuiIngameIRCMinecraftBridgeSettings(this.il, this, this.server));
            		break;
            	case 103:
            		this.mc.displayGuiScreen(new GuiIngameMinecraftIRCBridgeSettings(this.il, this, this.server));
            		break;
            	case 105:
            		break;
            	case 104:
            		this.mc.displayGuiScreen(new GuiScreenAutoJoinChannels(this.il, this, this.server));
            		break;
            	case 200:
            		this.mc.displayGuiScreen(this.parentScreen);
            		break;
            }
        }
    }

    @Override
    public void drawScreen(int par1, int par2, float par3) {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRenderer, (this.server == null ? "Default server settings" : "Settings for " + this.server), this.width / 2, this.height / 6 - 20, 16777215);
        this.drawCenteredString(this.fontRenderer, "General settings", this.width / 2, this.height / 6 + 10, 16777215);
        this.drawCenteredString(this.fontRenderer, "Chat bridging settings", this.width / 2, this.height / 6 + 60, 16777215);
        this.drawCenteredString(this.fontRenderer, "Connection settings", this.width / 2, this.height / 6 + 110, 16777215);
        super.drawScreen(par1, par2, par3);
    }
}
