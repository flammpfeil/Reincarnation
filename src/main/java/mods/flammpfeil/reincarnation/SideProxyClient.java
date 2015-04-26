package mods.flammpfeil.reincarnation;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import cpw.mods.fml.client.registry.RenderingRegistry;
import net.minecraft.entity.player.EntityPlayer;

import java.util.UUID;

public class SideProxyClient extends SideProxy{
	@Override

	public void initializeEntitiesRenderer() {

	    RenderingRegistry.registerEntityRenderingHandler(EntitySpirit.class, new RenderSpirit());
	}

	@Override
	public void displayChatGui(String text) {
		Minecraft.getMinecraft().displayGuiScreen(new GuiChat(text));
	}
    @Override
    public boolean canShowGui(UUID player){return Minecraft.getMinecraft().thePlayer.getUniqueID().equals(player);}
}
