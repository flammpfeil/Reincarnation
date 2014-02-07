package mods.flammpfeil.reincarnation;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.INpc;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EntitySpirit extends EntityLiving implements INpc{

	public EntitySpirit(World par1World) {
		super(par1World);
        this.setSize(0.2F, 0.2F);
	}

    @Override
    protected boolean canDespawn()
    {
        return false;
    }


    @Override
    public boolean isEntityInvulnerable()
    {
        return true;
    }

    @Override
    public boolean canBePushed()
    {
        return false;
    }

    @Override
    protected boolean shouldSetPosAfterLoading()
    {
        return false;
    }

    @Override
    protected void collideWithEntity(Entity par1Entity) {}

    @Override
    protected void collideWithNearbyEntities() {}

    @Override
    protected boolean canTriggerWalking()
    {
        return false;
    }

    @Override
    protected void fall(float par1) {}

    @Override
    protected void updateFallState(double par1, boolean par3) {}

    @Override
    public boolean doesEntityNotTriggerPressurePlate()
    {
        return true;
    }

    @Override
    public boolean handleWaterMovement()
    {
        return false;
    }
    @Override
    public boolean handleLavaMovement()
    {
        return false;
    }

    @Override
    public float getBrightness(float par1)
    {
        return 230.0f;
    }
    @Override
    public int getBrightnessForRender(float par1)
    {
        return 230;
    }

    @Override
    public boolean attackEntityFrom(DamageSource par1DamageSource, float par2) {
    	Entity tag = par1DamageSource.getSourceOfDamage();
    	if(tag != null && tag instanceof EntityPlayer && ((EntityPlayer)tag).getHeldItem() == null){

    		this.setDead();
    		this.spawnExplosionParticle();

    		if(this.getEntityData().hasKey("entitydata")){

	    		if(!this.worldObj.isRemote){
		    		ItemStack fragment = new ItemStack(Reincarnation.itemSpirit, Reincarnation.fragmentCountDirectDrop);
		    		this.entityDropItem(fragment, 0.0f);
	    		}
    		}else if(!this.worldObj.isRemote && this.rand.nextDouble() < 0.1){
	    		ItemStack fragment = new ItemStack(Reincarnation.itemSpirit);
	    		this.entityDropItem(fragment, 0.0f);
    		}

    		return true;
    	}

    	return super.attackEntityFrom(par1DamageSource, par2);
    }

    int age = 0;

    @Override
    public void onLivingUpdate() {
    	if(!this.worldObj.isRemote){
    		if(!this.getEntityData().hasKey("entitydata")){
    			age++;

    			//5min despawn playerDethPoint
    			if(6000 < age){
    				this.setDead();
    			}
    		}
    	}
    	newPosRotationIncrements = 0;

    	this.motionY = 0.0f;
    	this.motionX = 0.0f;
    	this.motionZ = 0.0f;

    	super.onLivingUpdate();

    	if(this.ticksExisted % 5 == 0)
    		this.worldObj.spawnParticle("portal"
        		,this.posX + (this.rand.nextDouble() - 0.5D) * (double)this.width
        		,this.posY + 0.5
        		,this.posZ + (this.rand.nextDouble() - 0.5D) * (double)this.width
        		,(this.rand.nextDouble() - 0.5D) * (double)this.width
        		,-this.rand.nextDouble() * 2.0
        		,(this.rand.nextDouble() - 0.5D) * (double)this.width
        		);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readEntityFromNBT(par1NBTTagCompound);

        if(par1NBTTagCompound.hasKey("entitydata"))
        	this.getEntityData().setTag("entitydata", par1NBTTagCompound.getCompoundTag("entitydata"));
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.writeEntityToNBT(par1NBTTagCompound);
        if(this.getEntityData().hasKey("entitydata"))
        	par1NBTTagCompound.setTag("entitydata", this.getEntityData().getCompoundTag("entitydata"));
    }

    @Override
    protected boolean interact(EntityPlayer par1EntityPlayer) {

    	if(par1EntityPlayer.getHeldItem() != null){

    		if(par1EntityPlayer.getHeldItem().getItem() == Items.glass_bottle){

		    	if(this.getEntityData().hasKey("entitydata")){
					ItemStack bottle = par1EntityPlayer.getHeldItem();

		    		ItemStack bottled = new ItemStack(Reincarnation.itemSpirit,1,1);

		    		if(this.hasCustomNameTag())
		    			bottled.setStackDisplayName("Bottled" + this.getCustomNameTag());

		    		bottled.getTagCompound().setTag("entitydata", this.getEntityData().getCompoundTag("entitydata"));

		    		--bottle.stackSize;

		    		if(bottle.stackSize <= 0){
		    			par1EntityPlayer.setCurrentItemOrArmor(0, bottled);
		    		}else if (!par1EntityPlayer.inventory.addItemStackToInventory(bottled))
		            {
		        		if(!this.worldObj.isRemote){
		        			par1EntityPlayer.dropPlayerItemWithRandomChoice(bottled,false);
		        		}
		            }
	    		}else{

		    		if(!this.worldObj.isRemote && this.rand.nextDouble() < 0.3){
			    		ItemStack fragment = new ItemStack(Reincarnation.itemSpirit);
			    		this.entityDropItem(fragment, 0.0f);
		    		}
	    		}

	    		this.setDead();
	    		this.spawnExplosionParticle();

	    		return true;
    		}

    	}

    	return super.interact(par1EntityPlayer);
    }
}
