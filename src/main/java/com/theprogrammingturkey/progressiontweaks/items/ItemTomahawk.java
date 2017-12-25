package com.theprogrammingturkey.progressiontweaks.items;

import com.google.common.collect.Multimap;
import com.theprogrammingturkey.gobblecore.items.BaseItem;
import com.theprogrammingturkey.progressiontweaks.entity.EntityTomahawk;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

public class ItemTomahawk extends BaseItem
{

	public ItemTomahawk()
	{
		super("tomahawk");
		super.setMaxStackSize(1);
	}

	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand hand)
	{
		ItemStack itemstack = playerIn.getHeldItem(hand);

		if(!playerIn.capabilities.isCreativeMode)
		{
			itemstack.func_190918_g(1);
		}

		worldIn.playSound((EntityPlayer) null, playerIn.posX, playerIn.posY, playerIn.posZ, SoundEvents.ENTITY_SNOWBALL_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

		if(!worldIn.isRemote)
		{
			EntityTomahawk tomahawk = new EntityTomahawk(worldIn, playerIn);
			tomahawk.setAim(playerIn, playerIn.rotationPitch, playerIn.rotationYaw, 0.0F, 1f, 1.0F);
			worldIn.spawnEntityInWorld(tomahawk);
		}

		playerIn.addStat(StatList.getObjectUseStats(this));
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
	}

	@SuppressWarnings("deprecation")
	public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot equipmentSlot)
	{
		Multimap<String, AttributeModifier> multimap = super.getItemAttributeModifiers(equipmentSlot);

		if(equipmentSlot == EntityEquipmentSlot.MAINHAND)
		{
			multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getAttributeUnlocalizedName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", 2, 0));
			multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getAttributeUnlocalizedName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", -1.5D, 0));
		}

		return multimap;
	}
}
