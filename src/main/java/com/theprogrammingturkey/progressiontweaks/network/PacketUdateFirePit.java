package com.theprogrammingturkey.progressiontweaks.network;

import com.theprogrammingturkey.progressiontweaks.ProgressionCore;
import com.theprogrammingturkey.progressiontweaks.blocks.tileentities.TileFirePit;

import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketUdateFirePit implements IMessage
{

	public ItemStack cooking;
	public int burnTime;
	public int cookTimeLeft;
	public int x;
	public int y;
	public int z;

	public PacketUdateFirePit()
	{

	}

	public PacketUdateFirePit(ItemStack cooking, int burnTime, int cookTimeLeft, int x, int y, int z)
	{
		this.cooking = cooking;
		this.burnTime = burnTime;
		this.cookTimeLeft = cookTimeLeft;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		NBTTagCompound cookingNBT = new NBTTagCompound();
		if(this.cooking != null)
			this.cooking.writeToNBT(cookingNBT);
		ByteBufUtils.writeTag(buf, cookingNBT);
		buf.writeInt(burnTime);
		buf.writeInt(cookTimeLeft);
		buf.writeInt(x);
		buf.writeInt(y);
		buf.writeInt(z);

	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		this.cooking = new ItemStack((NBTTagCompound) ByteBufUtils.readTag(buf));
		this.burnTime = buf.readInt();
		this.cookTimeLeft = buf.readInt();
		this.x = buf.readInt();
		this.y = buf.readInt();
		this.z = buf.readInt();
	}

	public static final class Handler implements IMessageHandler<PacketUdateFirePit, IMessage>
	{
		@Override
		public IMessage onMessage(PacketUdateFirePit message, MessageContext ctx)
		{
			TileEntity firepit;

			if((firepit = ProgressionCore.proxy.getClientPlayer().worldObj.getTileEntity(new BlockPos(message.x, message.y, message.z))) != null)
			{
				if(firepit instanceof TileFirePit)
				{
					((TileFirePit) firepit).setBurnTimeLeft(message.burnTime);
					((TileFirePit) firepit).setCookTimeLeft(message.burnTime);
					((TileFirePit) firepit).setCooking(message.cooking);
				}
			}
			return null;

		}
	}

}
