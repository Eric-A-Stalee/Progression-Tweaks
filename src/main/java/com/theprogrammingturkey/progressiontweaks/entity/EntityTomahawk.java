package com.theprogrammingturkey.progressiontweaks.entity;

import com.theprogrammingturkey.progressiontweaks.items.ProgressionItems;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class EntityTomahawk extends EntityCustomThrowable
{
	public float rot = 0;

	public EntityTomahawk(World worldIn)
	{
		super(worldIn);
		this.setDamage(5);
	}

	public EntityTomahawk(World worldIn, EntityLivingBase throwerIn)
	{
		super(worldIn, throwerIn);
		this.setDamage(5);
	}

	public EntityTomahawk(World worldIn, double x, double y, double z)
	{
		super(worldIn, x, y, z);
		this.setDamage(5);
	}

	@Override
	protected ItemStack getEntityStack()
	{
		return new ItemStack(ProgressionItems.TOMAHAWK);
	}
}