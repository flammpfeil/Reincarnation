package mods.flammpfeil.reincarnation;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.MinecraftForgeClient;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class InitProxyClient extends InitProxy{
	@Override

	public void initializeEntitiesRenderer() {

	    RenderingRegistry.registerEntityRenderingHandler(EntitySpirit.class, new RenderSpirit());
	}

	@Override
	public void displayChatGui(String text) {
		Minecraft.getMinecraft().displayGuiScreen(new GuiChat(text));
	}
}
