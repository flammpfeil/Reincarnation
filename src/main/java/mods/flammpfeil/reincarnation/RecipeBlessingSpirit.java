package mods.flammpfeil.reincarnation;

import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.world.World;

public class RecipeBlessingSpirit extends ShapedRecipes
{
    public RecipeBlessingSpirit()
    {
        super(3, 3, new ItemStack[] {
        		null, new ItemStack(Reincarnation.itemSpirit, 0, 0), null,
        		null, new ItemStack(Reincarnation.itemSpirit, 0, 1), null,
        		new ItemStack(Items.golden_apple,1,0), null, new ItemStack(Items.experience_bottle,1,0)}
        , new ItemStack(Reincarnation.itemSpirit, 0, 2));
    }

    @Override
    public boolean matches(InventoryCrafting par1InventoryCrafting, World par2World)
    {
        if (!super.matches(par1InventoryCrafting, par2World))
        {
            return false;
        }
        else
        {
            ItemStack itemstack = null;

            for (int i = 0; i < par1InventoryCrafting.getSizeInventory() && itemstack == null; ++i)
            {
                ItemStack itemstack1 = par1InventoryCrafting.getStackInSlot(i);

                if (itemstack1 != null && itemstack1.getItem() == Reincarnation.itemSpirit && itemstack1.getItemDamage() == 1)
                {
                    itemstack = itemstack1;
                }
            }

            if (itemstack == null)
            {
                return false;
            }
            else
            {
                return itemstack.hasTagCompound() && itemstack.getTagCompound().hasKey(EntitySpirit.EntityDataStr);
            }
        }
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting par1InventoryCrafting)
    {
        ItemStack itemstack = null;

        for (int i = 0; i < par1InventoryCrafting.getSizeInventory() && itemstack == null; ++i)
        {
            ItemStack itemstack1 = par1InventoryCrafting.getStackInSlot(i);

            if (itemstack1 != null && itemstack1.getItem() == Reincarnation.itemSpirit && itemstack1.getItemDamage() == 1)
            {
                itemstack = itemstack1;
            }
        }

        itemstack = itemstack.copy();
        itemstack.stackSize = 1;

        itemstack.setStackDisplayName(itemstack.getDisplayName().replace("Bottled", ""));

        itemstack.setItemDamage(2);

        return itemstack;
    }
}

