package mods.flammpfeil.reincarnation;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class SideProxyClient extends SideProxy{
	@Override

	public void initializeEntitiesRenderer() {

	    RenderingRegistry.registerEntityRenderingHandler(EntitySpirit.class, new RenderSpirit());
	}


	@Override
	public void displayChatGui(String text) {
		Minecraft.getMinecraft().displayGuiScreen(new GuiChat(text));
	}
}
