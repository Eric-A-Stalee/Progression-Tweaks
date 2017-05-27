package com.theprogrammingturkey.progressiontweaks.blocks.tileentities;

import java.util.List;

import com.theprogrammingturkey.gobblecore.util.Scheduler;
import com.theprogrammingturkey.gobblecore.util.Task;
import com.theprogrammingturkey.progressiontweaks.blocks.ProgressionBlocks;
import com.theprogrammingturkey.progressiontweaks.config.ProgressionSettings;
import com.theprogrammingturkey.progressiontweaks.network.PacketUdateFirePit;
import com.theprogrammingturkey.progressiontweaks.network.ProgressionPacketHandler;
import com.theprogrammingturkey.progressiontweaks.registries.FirePitRegistry;
import com.theprogrammingturkey.progressiontweaks.registries.FirePitRegistry.CookingResult;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;

public class TileFirePit extends TileEntity implements ITickable
{
	private ItemStack cooking;
	private int burnTimeLeft = -1;
	private int cookTimeLeft = -1;

	@Override
	public void update()
	{
		if(!this.worldObj.isRemote)
		{
			if(this.worldObj.isRaining())
			{
				if(this.burnTimeLeft > 0)
				{
					burnTimeLeft = -1;
					cookTimeLeft = -1;
					TileEntity tileentity = this.worldObj.getTileEntity(pos);
					this.worldObj.setBlockState(pos, ProgressionBlocks.FIRE_PIT_UNLIT.getDefaultState());
					this.worldObj.setBlockState(pos, ProgressionBlocks.FIRE_PIT_UNLIT.getDefaultState());
					tileentity.validate();
					this.worldObj.setTileEntity(pos, tileentity);
					worldObj.playSound((double) ((float) pos.getX() + 0.5F), (double) ((float) pos.getY() + 0.5F), (double) ((float) pos.getZ() + 0.5F), SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
					ProgressionPacketHandler.INSTANCE.sendToAll(new PacketUdateFirePit(getItemCooking(), getBurnTimeLeft(), getCookTimeLeft(), getPos().getX(), getPos().getY(), getPos().getZ()));
				}
				return;
			}

			if(burnTimeLeft > 0)
			{
				burnTimeLeft--;
				if(cookTimeLeft > 0)
					cookTimeLeft--;
			}

			if(burnTimeLeft == 0)
			{
				TileEntity tileentity = this.worldObj.getTileEntity(pos);
				this.worldObj.setBlockState(pos, ProgressionBlocks.FIRE_PIT_UNLIT.getDefaultState());
				this.worldObj.setBlockState(pos, ProgressionBlocks.FIRE_PIT_UNLIT.getDefaultState());
				tileentity.validate();
				this.worldObj.setTileEntity(pos, tileentity);
				burnTimeLeft = -1;
			}

			if(cookTimeLeft == 0)
			{
				CookingResult result = FirePitRegistry.INSTANCE.getResultFromInput(cooking);
				this.setCooking(result.getResult());
				this.worldObj.spawnEntityInWorld(new EntityXPOrb(worldObj, this.pos.getX(), this.pos.getY(), this.pos.getZ(), result.getXp()));
				cookTimeLeft = -1;
				ProgressionPacketHandler.INSTANCE.sendToAll(new PacketUdateFirePit(getItemCooking(), getBurnTimeLeft(), getCookTimeLeft(), getPos().getX(), getPos().getY(), getPos().getZ()));
			}

			Scheduler.scheduleTask(new Task("Fire_Pit_Mob_Attract", -1, 200)
			{
				@Override
				public void callback()
				{

				}

				@Override
				public void update()
				{
					if(worldObj == null)
					{
						Scheduler.removeTask(this);
						return;
					}

					TileEntity tileentity = worldObj.getTileEntity(pos);
					if(tileentity != null && tileentity.equals(TileFirePit.this))
					{
						EntityPlayer toAttack = null;
						for(EntityPlayer player : worldObj.playerEntities)
						{
							if(player.getPosition().distanceSq(getPos()) < 10)
							{
								toAttack = player;
								break;
							}
						}
						if(toAttack != null)
						{
							int radius = ProgressionSettings.firePitAttractionRadius;
							List<EntityMob> entities = worldObj.getEntitiesWithinAABB(EntityMob.class, new AxisAlignedBB(getPos().add(radius, radius, radius), getPos().add(-radius, -radius, -radius)));
							for(EntityMob ent : entities)
								if(ent.getAttackTarget() == null)
									ent.setAttackTarget(toAttack);
						}
					}
					else
					{
						Scheduler.removeTask(this);
					}
				}

			});
		}
	}

	public boolean isCooking()
	{
		return burnTimeLeft > 0;
	}

	public ItemStack getItemCooking()
	{
		return this.cooking == null ? null : this.cooking.copy();
	}

	public void startCooking(ItemStack stack, int duration)
	{
		this.cooking = stack.copy();
		this.cooking.func_190920_e(1);
		this.cookTimeLeft = duration;
	}

	public void setCooking(ItemStack stack)
	{
		if(stack != null)
		{
			this.cooking = stack.copy();
			this.cooking.func_190920_e(1);
		}
		else
		{
			this.cooking = null;
		}
	}

	public void startBurnTime(int burnTime)
	{
		this.burnTimeLeft = burnTime;
		TileEntity tileentity = this.worldObj.getTileEntity(pos);
		this.worldObj.setBlockState(pos, ProgressionBlocks.FIRE_PIT_LIT.getDefaultState());
		this.worldObj.setBlockState(pos, ProgressionBlocks.FIRE_PIT_LIT.getDefaultState());
		tileentity.validate();
		this.worldObj.setTileEntity(pos, tileentity);
	}

	public void setBurnTimeLeft(int burnTime)
	{
		this.burnTimeLeft = burnTime;
	}

	public int getBurnTimeLeft()
	{
		return this.burnTimeLeft;
	}

	public void setCookTimeLeft(int cookTimeLeft)
	{
		this.cookTimeLeft = cookTimeLeft;
	}

	public int getCookTimeLeft()
	{
		return this.cookTimeLeft;
	}

	public void dropCurrentItem(EntityPlayer player)
	{
		if(this.cooking != null)
		{
			EntityItem entityitem = new EntityItem(player.worldObj, player.posX, player.posY, player.posZ, this.cooking.copy());
			entityitem.setPickupDelay(5);
			player.worldObj.spawnEntityInWorld(entityitem);
			this.cooking = null;
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		nbt.setInteger("burnTime", this.burnTimeLeft);
		nbt.setInteger("cookTimeLeft", this.cookTimeLeft);
		NBTTagCompound cookingNBT = new NBTTagCompound();
		if(this.cooking != null)
			this.cooking.writeToNBT(cookingNBT);
		nbt.setTag("cookingItem", cookingNBT);
		return nbt;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		this.burnTimeLeft = nbt.getInteger("burnTime");
		this.cookTimeLeft = nbt.getInteger("cookTimeLeft");
		this.cooking = new ItemStack((NBTTagCompound) nbt.getTag("cookingItem"));
	}
}