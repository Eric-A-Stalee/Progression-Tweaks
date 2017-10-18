package com.theprogrammingturkey.progressiontweaks.blocks;

import java.util.Random;

import com.theprogrammingturkey.gobblecore.blocks.BaseBlock;
import com.theprogrammingturkey.gobblecore.network.NetworkManager;
import com.theprogrammingturkey.progressiontweaks.ProgressionCore;
import com.theprogrammingturkey.progressiontweaks.blocks.tileentities.TileFirePit;
import com.theprogrammingturkey.progressiontweaks.network.PacketUdateFirePit;
import com.theprogrammingturkey.progressiontweaks.registries.FirePitRegistry;
import com.theprogrammingturkey.progressiontweaks.registries.FirePitRegistry.CookingResult;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockFirePit extends BaseBlock implements ITileEntityProvider
{
	private boolean lit;

	public BlockFirePit(boolean lit)
	{
		super("fire_pit_" + (lit ? "lit" : "unlit"));
		this.lit = lit;
		this.setHardness(2f);
		this.setLightOpacity(0);
		if(lit)
			this.setLightLevel(0.9375F);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta)
	{
		return new TileFirePit();
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float side, float hitX, float hitY)
	{
		if(!(world.getTileEntity(pos) instanceof TileFirePit))
			return false;

		TileFirePit te = (TileFirePit) world.getTileEntity(pos);

		if(world.isRemote || player == null || player instanceof FakePlayer)
			return true;

		if(te != null)
		{
			ItemStack heldItem = player.getHeldItem(hand);
			boolean flag = false;
			CookingResult result = FirePitRegistry.INSTANCE.getResultFromInput(heldItem);
			if(result != null && te.getItemCooking() == null)
			{
				ItemStack output = result.getResult();
				int burntime = result.getDuration();

				if(output != null && te.getItemCooking() == null)
				{
					te.startCooking(heldItem, burntime);
					player.inventory.decrStackSize(player.inventory.currentItem, 1);
					flag = true;
				}
			}
			else
			{
				int burntime = FirePitRegistry.INSTANCE.getBurnTimeFromFuel(heldItem);
				if(burntime != -1)
				{
					te.addBurnTime(burntime);
					player.inventory.decrStackSize(player.inventory.currentItem, 1);
					world.playSound((EntityPlayer) null, pos, SoundEvents.ITEM_FIRECHARGE_USE, SoundCategory.BLOCKS, 0.5F, world.rand.nextFloat() * 0.1F + 0.9F);
					flag = true;
				}
				else
				{
					te.dropCurrentItem(player);
					flag = true;
				}
			}

			if(flag)
			{
				NetworkManager.getSimpleNetwork(ProgressionCore.instance).sendToAll(new PacketUdateFirePit(te.getItemCooking(), te.getBurnTimeLeft(), te.getCookTimeLeft(), te.getPos().getX(), te.getPos().getY(), te.getPos().getZ()));
				return true;
			}
		}

		return false;
	}

	public void onEntityWalk(World worldIn, BlockPos pos, Entity entityIn)
	{
		if(lit)
			entityIn.setFire(5);
		super.onEntityWalk(worldIn, pos, entityIn);
	}

	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand)
	{
		if(this.lit)
		{
			if(rand.nextInt(24) == 0)
				worldIn.playSound((double) ((float) pos.getX() + 0.5F), (double) ((float) pos.getY() + 0.5F), (double) ((float) pos.getZ() + 0.5F), SoundEvents.BLOCK_FIRE_AMBIENT, SoundCategory.BLOCKS, 1.0F + rand.nextFloat(), rand.nextFloat() * 0.7F + 0.3F, false);

			double x = 0.5 + pos.getX();
			double y = pos.getY();
			double z = 0.5 + pos.getZ();
			worldIn.spawnParticle(EnumParticleTypes.FLAME, x, y, z, (rand.nextDouble() / 10) - 0.05, 0.05f, (rand.nextDouble() / 10) - 0.05);
			worldIn.spawnParticle(EnumParticleTypes.SMOKE_LARGE, x, y + 0.75, z, (rand.nextDouble() / 10) - 0.05, 0.01f, (rand.nextDouble() / 10) - 0.05);
		}
	}

	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}

	public boolean isFullCube(IBlockState state)
	{
		return false;
	}

	public Item getItemDropped(IBlockState state, Random rand, int fortune)
	{
		return Item.getItemFromBlock(ProgressionBlocks.FIRE_PIT_UNLIT);
	}

	public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state)
	{
		return new ItemStack(ProgressionBlocks.FIRE_PIT_UNLIT);
	}
}