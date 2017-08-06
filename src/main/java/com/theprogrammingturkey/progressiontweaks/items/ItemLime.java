package com.theprogrammingturkey.progressiontweaks.items;

import com.theprogrammingturkey.gobblecore.items.BaseItemFood;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemLime extends BaseItemFood
{
	public ItemLime()
	{
		super("lime", 1, 0.2f, false, 16);
		super.addLore("Green tacos live on!");
	}

	public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving)
	{
		if(entityLiving instanceof EntityPlayer)
		{
			//((EntityPlayer) entityLiving).addStat(ProgressionAchievements.toTheCore);
		}
		return super.onItemUseFinish(stack, worldIn, entityLiving);
	}
}
