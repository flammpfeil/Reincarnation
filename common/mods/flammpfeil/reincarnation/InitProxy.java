package mods.flammpfeil.reincarnation;

import cpw.mods.fml.common.SidedProxy;

public class InitProxy {
	@SidedProxy(clientSide = "mods.flammpfeil.reincarnation.InitProxyClient", serverSide = "mods.flammpfeil.reincarnation.InitProxy")
	public static InitProxy proxy;


	public void initializeEntitiesRenderer() {}

}
