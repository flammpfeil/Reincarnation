package mods.flammpfeil.reincarnation;

import cpw.mods.fml.common.SidedProxy;
import net.minecraft.entity.player.EntityPlayer;

import java.util.UUID;

public class SideProxy {
	@SidedProxy(clientSide = "mods.flammpfeil.reincarnation.SideProxyClient", serverSide = "mods.flammpfeil.reincarnation.SideProxy")
	public static SideProxy proxy;


	public void initializeEntitiesRenderer() {}

	public void displayChatGui(String text){}

    public boolean canShowGui(UUID id){return false;}
}
