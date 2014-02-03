package mods.flammpfeil.reincarnation;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EntityOwnable;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.potion.Potion;
import net.minecraft.world.World;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.Property;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import cpw.mods.fml.common.IFuelHandler;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

@Mod(name="Reincarnation",modid=Reincarnation.modid,useMetadata=false,version="@VERSION@")
@NetworkMod(clientSideRequired=true)
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
				Property propShiftItemId;
				propShiftItemId = mainConfiguration.getItem(Configuration.CATEGORY_ITEM, "Spirit", itemid);
				itemid= propShiftItemId.getInt();
			}
			{
				Property propShiftItemId;
				propShiftItemId = mainConfiguration.getItem(Configuration.CATEGORY_ITEM, "SpiritFragmentBottle", itemid2);
				itemid2= propShiftItemId.getInt();
			}
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
	}

	static public String LOCAL_ENTITY_SPIRIT_NAME = "spirit";
	static public String ENTITY_SPIRIT_NAME = String.format("%s.%s",modid,LOCAL_ENTITY_SPIRIT_NAME);

	@EventHandler
	public void init(FMLInitializationEvent evt){

		itemSpirit = (new ItemSpirit(itemid))
				.setUnlocalizedName("flammpfeil.reincarnation:spirit")
				.setTextureName("flammpfeil.reincarnation:spirit")
				.setCreativeTab(CreativeTabs.tabMisc);

		itemSpiritFragmentBottle = (new ItemFood(itemid2, 1, false))
				.setPotionEffect(Potion.regeneration.getId(), 2, 60, 1.0f)
				.setUnlocalizedName("flammpfeil.reincarnation:spiritfragmentbottle")
				.setTextureName("flammpfeil.reincarnation:bottled")
				.setCreativeTab(CreativeTabs.tabFood);


		int localEntityId = 0;
	    EntityRegistry.registerModEntity(EntitySpirit.class, LOCAL_ENTITY_SPIRIT_NAME, localEntityId, this, 100, 100, false);


		LanguageRegistry.instance().addName(itemSpiritFragmentBottle,"BottledSpiritFragment");
		LanguageRegistry.instance().addNameForObject(itemSpiritFragmentBottle,"ja_JP","瓶詰めの砕けた魂");

		LanguageRegistry.instance().addName(itemSpirit,"SpiritFragment");
		LanguageRegistry.instance().addNameForObject(itemSpirit,"ja_JP","砕けた魂");

		ItemStack bottled = new ItemStack(itemSpirit,1,1);
		LanguageRegistry.instance().addName(bottled,"BottledSpirit");
		LanguageRegistry.instance().addNameForObject(bottled,"ja_JP","瓶詰め魂");

		ItemStack egg = new ItemStack(itemSpirit,1,2);
		LanguageRegistry.instance().addName(egg,"BlessingSpirit");
		LanguageRegistry.instance().addNameForObject(egg,"ja_JP","祝福儀礼済み瓶詰め魂");

		LanguageRegistry.instance().addStringLocalization(ENTITY_SPIRIT_NAME, "Spirit");


		GameRegistry.addRecipe(new RecipeBlessingSpirit());
		GameRegistry.addShapelessRecipe(new ItemStack(itemSpirit, this.fragmentCountCraft,0), new ItemStack(itemSpirit,1,1));
		GameRegistry.addShapelessRecipe(new ItemStack(itemSpiritFragmentBottle), new ItemStack(itemSpirit), new ItemStack(Item.glassBottle));

	}


	//static public WorldLoadTickHandler tickHandler = new WorldLoadTickHandler();

	static public SpiritSpawnEventHandler handler = new SpiritSpawnEventHandler();

    @EventHandler
    public void postInit(FMLPostInitializationEvent evt)
    {
    	MinecraftForge.EVENT_BUS.register(handler);

    	InitProxy.proxy.initializeEntitiesRenderer();

    	//TickRegistry.registerTickHandler(tickHandler, Side.SERVER);
    }
}
