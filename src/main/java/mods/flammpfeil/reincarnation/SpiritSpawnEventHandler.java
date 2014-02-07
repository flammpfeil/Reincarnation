package mods.flammpfeil.reincarnation;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.boss.IBossDisplayData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class SpiritSpawnEventHandler {

	public static Map<Integer,DamageSource> damageMap = new HashMap<Integer,DamageSource>();

	@SubscribeEvent
	public void onLivingUpdate(LivingUpdateEvent event){

		if(event.entityLiving instanceof EntityPlayer){
			EntityPlayer el = (EntityPlayer)event.entityLiving;

			PotionEffect effect = el.getActivePotionEffect(Potion.regeneration);

			if(el.getHealth() <= 0 && el.worldObj.isRemote && !el.getEntityData().hasKey("dying")){
				SideProxy.proxy.displayChatGui("dying now");
			}

			if(el.deathTime >= 15 && !(effect != null && effect.getAmplifier() == 60)){
				if(el.isSneaking() && !el.getEntityData().hasKey("dead")){
					el.setHealth(0);
					el.getEntityData().setBoolean("dead", true);

					el.deathTime =19;
					if(!el.worldObj.isRemote){
						DamageSource ds = damageMap.get(el.getEntityId());
						damageMap.remove(el.getEntityId());
						if(ds == null)
							ds = DamageSource.generic;
						el.onDeath(ds);
					}
				}else if(!el.getEntityData().hasKey("dead")){
					el.getEntityData().setBoolean("dying",true);
					el.deathTime = 15;

					el.setHealth(0.01f);
					el.getFoodStats().setFoodLevel(6);
					el.stopUsingItem();

					if(el.isInWater()){
						el.motionX = 0;
						el.motionY = 0;
						el.motionZ = 0;
					}

					if(!el.worldObj.isRemote){

						el.addPotionEffect(new PotionEffect(Potion.jump.getId(),2,-60,true));
						el.addPotionEffect(new PotionEffect(Potion.digSlowdown.getId(),2,60,true));
						el.addPotionEffect(new PotionEffect(Potion.moveSlowdown.getId(),2,60,true));
						el.addPotionEffect(new PotionEffect(Potion.weakness.getId(),2,60,true));
						el.addPotionEffect(new PotionEffect(Potion.waterBreathing.getId(),2,60,true));
						el.addPotionEffect(new PotionEffect(Potion.fireResistance.getId(),2,60,true));
						el.addPotionEffect(new PotionEffect(Potion.resistance.getId(),2,60,true));
						el.hurtResistantTime = el.maxHurtResistantTime;
					}
				}
			}else if(el.getHealth() > el.getMaxHealth()/3){
				el.deathTime = 0;
				el.getEntityData().removeTag("dying");
			}

		}
	}

	@SubscribeEvent
	public void onLivingDeath(LivingDeathEvent event){
		if(event.entityLiving instanceof EntityPlayer){
			EntityPlayer el = (EntityPlayer)event.entityLiving;

			if(!el.worldObj.isRemote){
				if(!el.getEntityData().hasKey("dying")){

					if(damageMap.size() > 10){
						damageMap.clear();
					}
					damageMap.put(el.getEntityId(), event.source);
					event.setCanceled(true);

					NBTTagCompound entityTag = new NBTTagCompound();

		            EntityLiving entitySpirit = (EntityLiving)EntityList.createEntityByName(Reincarnation.ENTITY_SPIRIT_NAME, el.worldObj);

		            entitySpirit.setCustomNameTag(String.format("SpiritFragment of \"%s\"", el.getDisplayName()));

		            entitySpirit.getEntityData().setString("playerName", el.getCommandSenderName());

		            double y = Math.min(Math.max(el.posY + 0.5,2.0),256.0);
		            entitySpirit.setLocationAndAngles(
		            		el.posX,
		            		y,
		            		el.posZ,
		            		el.worldObj.rand.nextFloat() * 360.0F, 0.0F);

		            el.worldObj.spawnEntityInWorld(entitySpirit);

				}else if(!el.getEntityData().hasKey("dead")){
					event.setCanceled(true);
				}
			}
		}
	}

	@SubscribeEvent
	public void onLivingDrops(LivingDropsEvent event){
		if(!event.entityLiving.worldObj.isRemote &&  event.entityLiving instanceof EntityLiving && !(event.entityLiving instanceof EntitySpirit)){
			EntityLiving el = (EntityLiving)event.entityLiving;

			if(el instanceof IBossDisplayData){
	    		ItemStack fragment = new ItemStack(Reincarnation.itemSpirit,Reincarnation.fragmentCountBossDrop);
	    		el.entityDropItem(fragment, 0.0f);
			}

			if(el.hasCustomNameTag()){

		        String s = EntityList.getEntityString(el);

		        if (s != null){
					NBTTagCompound entityTag = new NBTTagCompound();

		        	entityTag.setString("id", s);

					el.writeToNBT(entityTag);

		            EntityLiving entitySpirit = (EntityLiving)EntityList.createEntityByName(Reincarnation.ENTITY_SPIRIT_NAME, el.worldObj);

		            entitySpirit.setCustomNameTag(String.format("Spirit of \"%s\"", el.getCustomNameTag()));

		            entitySpirit.getEntityData().setTag("entitydata", entityTag);

		            entitySpirit.setLocationAndAngles(
		            		el.posX,
		            		Math.min(Math.max(el.posY + 0.5,2.0),256.0),
		            		el.posZ,
		            		el.worldObj.rand.nextFloat() * 360.0F, 0.0F);

		            el.worldObj.spawnEntityInWorld(entitySpirit);
		        }
			}
		}
	}
}