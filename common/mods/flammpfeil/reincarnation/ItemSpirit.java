package mods.flammpfeil.reincarnation;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EntityLivingData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumMovingObjectType;
import net.minecraft.util.Facing;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class ItemSpirit extends Item {

	public ItemSpirit(int par1) {
		super(par1);
		this.setMaxStackSize(16);
	}

	@Override
	public String getUnlocalizedName(ItemStack par1ItemStack) {

		switch(par1ItemStack.getItemDamage()){
		case 1:
			return super.getUnlocalizedName() + ".bottled";
		case 2:
			return super.getUnlocalizedName() + ".egg";
		default:
			return super.getUnlocalizedName();
		}
	}

	@Override
	public boolean hasEffect(ItemStack par1ItemStack, int pass) {
		return true;
	}

	Icon fragment;
	Icon bottled;

	@Override
	public void registerIcons(IconRegister par1IconRegister) {

		bottled = par1IconRegister.registerIcon("flammpfeil.reincarnation:bottled");
		fragment = par1IconRegister.registerIcon("flammpfeil.reincarnation:fragment");

		super.registerIcons(par1IconRegister);
	}

	@Override
	public Icon getIconFromDamage(int par1) {
		return par1 == 0 ? fragment : par1 == 1 ? bottled : this.itemIcon;
	}

	@Override
	public boolean itemInteractionForEntity(ItemStack par1ItemStack,
			EntityPlayer par2EntityPlayer, EntityLivingBase par3EntityLivingBase) {

		if(par3EntityLivingBase instanceof EntityPlayer && par1ItemStack.getItemDamage() == 0){

			if(par3EntityLivingBase.deathTime > 0){

				par3EntityLivingBase.setHealth(par3EntityLivingBase.getMaxHealth()/2);
				par3EntityLivingBase.deathTime = 0;
				par3EntityLivingBase.getEntityData().removeTag("dying");
				par3EntityLivingBase.addPotionEffect(new PotionEffect(Potion.regeneration.getId(),2,60,true));
				par3EntityLivingBase.worldObj.updateEntityWithOptionalForce(par3EntityLivingBase, false);
				par3EntityLivingBase.worldObj.updateEntity(par3EntityLivingBase);

			}
		}

		return false;
	}

	@Override
    public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10)
    {
        if (par1ItemStack.getItemDamage() != 2 ||par3World.isRemote)
        {
            return true;
        }
        else
        {
            int i1 = par3World.getBlockId(par4, par5, par6);
            par4 += Facing.offsetsXForSide[par7];
            par5 += Facing.offsetsYForSide[par7];
            par6 += Facing.offsetsZForSide[par7];
            double d0 = 0.0D;

            if (par7 == 1 && Block.blocksList[i1] != null && Block.blocksList[i1].getRenderType() == 11)
            {
                d0 = 0.5D;
            }

            Entity entity = spawnCreature(par3World, par1ItemStack, (double)par4 + 0.5D, (double)par5 + d0, (double)par6 + 0.5D);

            if (entity != null)
            {
                if (!par2EntityPlayer.capabilities.isCreativeMode)
                {
                    --par1ItemStack.stackSize;
                }
            }
            return true;
        }
    }

    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
	@Override
    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        if (par1ItemStack.getItemDamage() != 2 || par2World.isRemote)
        {
            return par1ItemStack;
        }
        else
        {
            MovingObjectPosition movingobjectposition = this.getMovingObjectPositionFromPlayer(par2World, par3EntityPlayer, true);

            if (movingobjectposition == null)
            {
                return par1ItemStack;
            }
            else
            {
                if (movingobjectposition.typeOfHit == EnumMovingObjectType.TILE)
                {
                    int i = movingobjectposition.blockX;
                    int j = movingobjectposition.blockY;
                    int k = movingobjectposition.blockZ;

                    if (!par2World.canMineBlock(par3EntityPlayer, i, j, k))
                    {
                        return par1ItemStack;
                    }

                    if (!par3EntityPlayer.canPlayerEdit(i, j, k, movingobjectposition.sideHit, par1ItemStack))
                    {
                        return par1ItemStack;
                    }

                    if (par2World.getBlockMaterial(i, j, k) == Material.water)
                    {
                        Entity entity = spawnCreature(par2World, par1ItemStack, (double)i, (double)j, (double)k);

                        if (entity != null)
                        {
                            if (!par3EntityPlayer.capabilities.isCreativeMode)
                            {
                                --par1ItemStack.stackSize;
                            }
                        }
                    }
                }

                return par1ItemStack;
            }
        }
    }

    /**
     * Spawns the creature specified by the egg's type in the location specified by the last three parameters.
     * Parameters: world, entityID, x, y, z.
     */
    public static Entity spawnCreature(World par0World, ItemStack par1, double par2, double par4, double par6)
    {
    	if(par1.hasTagCompound() && par1.getTagCompound().hasKey("entitydata"))
    	{
    		NBTTagCompound tag =par1.getTagCompound().getCompoundTag("entitydata");


            Entity entity = EntityList.createEntityFromNBT(tag, par0World);

            if (entity != null && entity instanceof EntityLivingBase)
            {
                EntityLiving entityliving = (EntityLiving)entity;
                entity.setLocationAndAngles(par2, par4, par6, MathHelper.wrapAngleTo180_float(par0World.rand.nextFloat() * 360.0F), 0.0F);
                entityliving.rotationYawHead = entityliving.rotationYaw;
                entityliving.renderYawOffset = entityliving.rotationYaw;
                //entityliving.onSpawnWithEgg((EntityLivingData)null);
                entityliving.setHealth(entityliving.getMaxHealth());
                entityliving.deathTime = 0;
                par0World.spawnEntityInWorld(entity);
                entityliving.playLivingSound();
                entityliving.spawnExplosionParticle();
            }

            return entity;
        }

    	return null;
    }
}
