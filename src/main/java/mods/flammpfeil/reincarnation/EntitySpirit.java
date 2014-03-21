package mods.flammpfeil.reincarnation;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.INpc;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EntitySpirit extends EntityLiving implements INpc,IInventory{

	public static final String PlayerInventoryStr = "playerInventory";
    public static final String EntityDataStr = "entitydata";
    public static final String PlayerExpStr = "playerExp";

	public EntitySpirit(World par1World) {
		super(par1World);
        this.setSize(0.2F, 0.2F);
	}

	int talkFlag = 0;
	@Override
	public int getTalkInterval() {
		talkFlag++;
		talkFlag %= 5;
		return talkFlag == 0  ? -1000 : 100;
	}

	@Override
	protected String getLivingSound() {
		return Reincarnation.enabledSound ? "random.orb" : null;
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

    		if(this.getEntityData().hasKey(PlayerInventoryStr) && 0 < this.getEntityData().getTagList(PlayerInventoryStr,10).tagCount()){
	    		NBTTagList list = this.getEntityData().getTagList(PlayerInventoryStr,10);
	    		for(int i = 0;i < list.tagCount(); i++){
	    			ItemStack item = ItemStack.loadItemStackFromNBT(list.getCompoundTagAt(i));

		    		this.entityDropItem(item, 0.0f);
	    		}
	    	}

    		if(this.getEntityData().hasKey(PlayerExpStr)){
    			int exp = this.getEntityData().getInteger(PlayerExpStr);
    			if(0 < exp){
                    while (exp > 0)
                    {
                        int j = EntityXPOrb.getXPSplit(exp);
                        exp -= j;
                        this.worldObj.spawnEntityInWorld(new EntityXPOrb(this.worldObj, this.posX, this.posY, this.posZ, j));
                    }
    			}
	    	}

    		this.setDead();
    		this.spawnExplosionParticle();

    		if(this.getEntityData().hasKey(EntityDataStr)){

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
    		if(!(this.getEntityData().hasKey(EntityDataStr)
    			||(this.getEntityData().hasKey(PlayerInventoryStr) && 0 < this.getEntityData().getTagList(PlayerInventoryStr,10).tagCount())
    				)){

    			age++;

    			//5min despawn playerDethPoint
    			if(6000 < age){
    				this.setDead();
    	    		this.spawnExplosionParticle();
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

        if(par1NBTTagCompound.hasKey(EntityDataStr))
        	this.getEntityData().setTag(EntityDataStr, par1NBTTagCompound.getCompoundTag(EntityDataStr));
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.writeEntityToNBT(par1NBTTagCompound);
        if(this.getEntityData().hasKey(EntityDataStr))
        	par1NBTTagCompound.setTag(EntityDataStr, this.getEntityData().getCompoundTag(EntityDataStr));
    }

    @Override
    protected boolean interact(EntityPlayer par1EntityPlayer) {

    	if(par1EntityPlayer.getHeldItem() != null){

    		if(par1EntityPlayer.getHeldItem().getItem() == Items.glass_bottle){

		    	if(this.getEntityData().hasKey(EntityDataStr)){
					ItemStack bottle = par1EntityPlayer.getHeldItem();

		    		ItemStack bottled = new ItemStack(Reincarnation.itemSpirit,1,1);

		    		if(this.hasCustomNameTag())
		    			bottled.setStackDisplayName("Bottled" + this.getCustomNameTag());

		    		bottled.getTagCompound().setTag(EntityDataStr, this.getEntityData().getCompoundTag(EntityDataStr));

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

			    	if(this.getEntityData().hasKey(PlayerInventoryStr) && 0 < this.getEntityData().getTagList(PlayerInventoryStr,10).tagCount()){
			    		NBTTagList list = this.getEntityData().getTagList(PlayerInventoryStr,10);
			    		for(int i = 0;i < list.tagCount(); i++){
			    			ItemStack item = ItemStack.loadItemStackFromNBT(list.getCompoundTagAt(i));

				    		this.entityDropItem(item, 0.0f);
			    		}
			    	}

		    		if(!this.worldObj.isRemote && this.rand.nextDouble() < 0.3){
			    		ItemStack fragment = new ItemStack(Reincarnation.itemSpirit);
			    		this.entityDropItem(fragment, 0.0f);
		    		}
	    		}

	    		this.setDead();
	    		this.spawnExplosionParticle();

	    		return true;
    		}

    	}else if(this.getEntityData().hasKey(PlayerInventoryStr) && 0 < this.getEntityData().getTagList(PlayerInventoryStr,10).tagCount()){
    		par1EntityPlayer.displayGUIChest(this);
    		return true;
    	}

    	return super.interact(par1EntityPlayer);
    }

	@Override
	public int getSizeInventory() {
		return 45;
	}

	@Override
	public ItemStack getStackInSlot(int i) {
		NBTTagList list = this.getEntityData().getTagList(PlayerInventoryStr,10);
		if(i < list.tagCount())
			return ItemStack.loadItemStackFromNBT(list.getCompoundTagAt(i));
		else
			return null;
	}

	@Override
	public ItemStack decrStackSize(int i, int j) {
		NBTTagList list = this.getEntityData().getTagList(PlayerInventoryStr,10);
		if(i < list.tagCount()){

			NBTTagCompound tag = list.getCompoundTagAt(i);
			ItemStack item = ItemStack.loadItemStackFromNBT(tag);

            if(item.stackSize <= j)
            {
                ItemStack itemstack = item;
                list.removeTag(i);
                markDirty();
                return itemstack;
            }
            ItemStack itemstack1 = item.splitStack(j);
            if(item.stackSize == 0)
            {
                list.removeTag(i);
            }else{
            	item.writeToNBT(tag);
            }
            markDirty();
            return itemstack1;
		}
		return null;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int i) {
		ItemStack item = getStackInSlot(i);
		if(item != null){
			NBTTagList list = this.getEntityData().getTagList(PlayerInventoryStr,10);
			list.removeTag(i);
		}
        markDirty();
		return item;
	}

	@Override
	public void setInventorySlotContents(int i, ItemStack itemstack) {
		NBTTagList list = this.getEntityData().getTagList(PlayerInventoryStr,10);
		if(itemstack !=null){
			NBTTagCompound tag = new NBTTagCompound();

			itemstack.writeToNBT(tag);

			list.appendTag(tag);
		}else{
			if(i < list.tagCount())
				list.removeTag(i);
		}
        markDirty();
	}

    @Override
    public String getInventoryName() {
        return this.getCustomNameTag();
    }

    @Override
    public boolean hasCustomInventoryName() {
        return true;
    }

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer entityplayer) {
		NBTTagList list = this.getEntityData().getTagList(PlayerInventoryStr,10);
		return 0 < list.tagCount();
	}

    @Override
    public void markDirty() {

    }

    @Override
	public void openInventory() {
	}

	@Override
	public void closeInventory() {
	}

	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack) {
		return false;
	}
}
