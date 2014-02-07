package mods.flammpfeil.reincarnation;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(name="Reincarnation",modid=Reincarnation.modid,useMetadata=false,version="r2")
public class Reincarnation{

	public static final String modid = "flammpfeil.reincarnation";

	public static Item itemSpirit;
	public static int itemid = 22812;
	public static Item itemSpiritFragmentBottle;
	public static int itemid2 = 22813;

	public static int fragmentCountDirectDrop = 2;
	public static int fragmentCountCraft = 3;
	public static int fragmentCountBossDrop = 5;

	public static Configuration mainConfiguration;

	@EventHandler
	public void preInit(FMLPreInitializationEvent evt){
		mainConfiguration = new Configuration(evt.getSuggestedConfigurationFile());

		try{
			mainConfiguration.load();

			{
				Property propFragmentCount;
				propFragmentCount = mainConfiguration.get(Configuration.CATEGORY_GENERAL, "fragmentCount_DirectDrop", fragmentCountDirectDrop);
				fragmentCountDirectDrop = propFragmentCount.getInt();
			}
			{
				Property propFragmentCount;
				propFragmentCount = mainConfiguration.get(Configuration.CATEGORY_GENERAL, "fragmentCount_Craft", fragmentCountCraft);
				fragmentCountCraft = propFragmentCount.getInt();
			}
			{
				Property propFragmentCount;
				propFragmentCount = mainConfiguration.get(Configuration.CATEGORY_GENERAL, "fragmentCount_BossDrop", fragmentCountBossDrop);
				fragmentCountBossDrop = propFragmentCount.getInt();
			}

		}
		finally
		{
			mainConfiguration.save();
		}



		itemSpirit = (new ItemSpirit())
				.setUnlocalizedName("flammpfeil.reincarnation.spirit")
				.setTextureName("flammpfeil.reincarnation:spirit")
				.setCreativeTab(CreativeTabs.tabMisc);

		GameRegistry.registerItem(itemSpirit, "spirit");

		itemSpiritFragmentBottle = (new ItemFood(1, 5, false))
				.setPotionEffect(Potion.regeneration.getId(), 2, 60, 1.0f)
				.setUnlocalizedName("flammpfeil.reincarnation.spiritfragmentbottle")
				.setTextureName("flammpfeil.reincarnation:bottled")
				.setCreativeTab(CreativeTabs.tabFood);

		GameRegistry.registerItem(itemSpiritFragmentBottle, "bottledSpritFragment");

		int localEntityId = 0;
	    EntityRegistry.registerModEntity(EntitySpirit.class, LOCAL_ENTITY_SPIRIT_NAME, localEntityId, this, 100, 100, false);

		GameRegistry.addRecipe(new RecipeBlessingSpirit());
		GameRegistry.addShapelessRecipe(new ItemStack(itemSpirit, this.fragmentCountCraft,0), new ItemStack(itemSpirit,1,1));
		GameRegistry.addShapelessRecipe(new ItemStack(itemSpiritFragmentBottle), new ItemStack(itemSpirit), new ItemStack(Items.glass_bottle));

	}

	static public String LOCAL_ENTITY_SPIRIT_NAME = "spirit";
	static public String ENTITY_SPIRIT_NAME = String.format("%s.%s",modid,LOCAL_ENTITY_SPIRIT_NAME);


	static public SpiritSpawnEventHandler handler = new SpiritSpawnEventHandler();

    @EventHandler
    public void postInit(FMLPostInitializationEvent evt)
    {
    	MinecraftForge.EVENT_BUS.register(handler);

    	SideProxy.proxy.initializeEntitiesRenderer();

    }
}
