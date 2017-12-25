package com.theprogrammingturkey.progressiontweaks.items;

import javax.annotation.Nullable;

import com.google.common.collect.Multimap;
import com.theprogrammingturkey.gobblecore.items.BaseItem;
import com.theprogrammingturkey.progressiontweaks.ProgressionCore;
import com.theprogrammingturkey.progressiontweaks.entity.EntitySpear;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumAction;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemSpear extends BaseItem
{

	public ItemSpear()
	{
		super("spear");
		super.setMaxStackSize(1);
		super.addLore("Did you know you can throw the spear?");
		super.addLore("Simply use it like you would a bow!");
		super.addLore("Deals +10 Damage to mobs smaller than a block");
		this.addPropertyOverride(new ResourceLocation(ProgressionCore.MODID, "pull"), new IItemPropertyGetter()
		{
			@SideOnly(Side.CLIENT)
			public float apply(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn)
			{
				return entityIn == null ? 0.0F : (entityIn.getActiveItemStack().getItem() != ProgressionItems.SPEAR ? 0.0F : (float) (stack.getMaxItemUseDuration() - entityIn.getItemInUseCount()) / 20.0F);
			}
		});
	}

	public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft)
	{
		if(entityLiving instanceof EntityPlayer)
		{
			EntityPlayer entityplayer = (EntityPlayer) entityLiving;
			boolean flag = entityplayer.capabilities.isCreativeMode || EnchantmentHelper.getEnchantmentLevel(Enchantments.INFINITY, stack) > 0;

			int i = this.getMaxItemUseDuration(stack) - timeLeft;
			i = net.minecraftforge.event.ForgeEventFactory.onArrowLoose(stack, worldIn, entityplayer, i, !stack.func_190926_b() || flag);
			if(i < 0)
				return;

			if(!stack.func_190926_b() || flag)
			{
				float f = ItemBow.getArrowVelocity(i) / 2;

				if((double) f >= 0.1D)
				{
					if(!worldIn.isRemote)
					{
						EntitySpear entitySpear = new EntitySpear(worldIn, entityplayer);
						entitySpear.setAim(entityplayer, entityplayer.rotationPitch, entityplayer.rotationYaw, 1.0F, f * 3.0F, 1.0F);

						stack.damageItem(1, entityplayer);

						worldIn.spawnEntityInWorld(entitySpear);
					}

					worldIn.playSound((EntityPlayer) null, entityplayer.posX, entityplayer.posY, entityplayer.posZ, SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.PLAYERS, 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 1.2F) + f * 0.5F);

					if(!entityplayer.capabilities.isCreativeMode)
					{
						stack.func_190918_g(1);

						if(stack.func_190926_b())
						{
							entityplayer.inventory.deleteStack(stack);
						}
					}

					entityplayer.addStat(StatList.getObjectUseStats(this));
				}
			}
		}
	}

	/**
	 * How long it takes to use or consume an item
	 */
	public int getMaxItemUseDuration(ItemStack stack)
	{
		return 72000;
	}

	/**
	 * returns the action that specifies what animation to play when the items is being used
	 */
	public EnumAction getItemUseAction(ItemStack stack)
	{
		return EnumAction.BOW;
	}

	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand)
	{
		player.setActiveHand(hand);
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
	}
	
    @SuppressWarnings("deprecation")
	public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot equipmentSlot)
    {
        Multimap<String, AttributeModifier> multimap = super.getItemAttributeModifiers(equipmentSlot);

		if(equipmentSlot == EntityEquipmentSlot.MAINHAND)
        {
            multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getAttributeUnlocalizedName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", 3, 0));
            multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getAttributeUnlocalizedName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", -1.5D, 0));
        }

        return multimap;
    }

}
