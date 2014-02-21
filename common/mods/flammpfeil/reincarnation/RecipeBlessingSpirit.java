package mods.flammpfeil.reincarnation;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapData;

public class RecipeBlessingSpirit extends ShapedRecipes
{
    public RecipeBlessingSpirit()
    {
        super(3, 3, new ItemStack[] {
        		null, new ItemStack(Reincarnation.itemSpirit, 0, 0), null,
        		null, new ItemStack(Reincarnation.itemSpirit, 0, 1), null,
        		new ItemStack(Item.appleGold,1,0), null, new ItemStack(Item.expBottle,1,0)}
        , new ItemStack(Reincarnation.itemSpirit, 0, 2));
    }

    /**
     * Used to check if a recipe matches current crafting inventory
     */
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

                if (itemstack1 != null && itemstack1.itemID == Reincarnation.itemSpirit.itemID && itemstack1.getItemDamage() == 1)
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
                return itemstack.hasTagCompound() && itemstack.getTagCompound().hasKey("entitydata");
            }
        }
    }

    /**
     * Returns an Item that is the result of this recipe
     */
    public ItemStack getCraftingResult(InventoryCrafting par1InventoryCrafting)
    {
        ItemStack itemstack = null;

        for (int i = 0; i < par1InventoryCrafting.getSizeInventory() && itemstack == null; ++i)
        {
            ItemStack itemstack1 = par1InventoryCrafting.getStackInSlot(i);

            if (itemstack1 != null && itemstack1.itemID == Reincarnation.itemSpirit.itemID && itemstack1.getItemDamage() == 1)
            {
                itemstack = itemstack1;
            }
        }

        itemstack = itemstack.copy();
        itemstack.stackSize = 1;

        itemstack.setItemName(itemstack.getDisplayName().replace("Bottled", ""));

        itemstack.setItemDamage(2);

        return itemstack;
    }
}

