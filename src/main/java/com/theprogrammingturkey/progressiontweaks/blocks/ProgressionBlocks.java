package com.theprogrammingturkey.progressiontweaks.blocks;

import com.theprogrammingturkey.gobblecore.blocks.BaseBlock;
import com.theprogrammingturkey.gobblecore.blocks.BlockLoader;
import com.theprogrammingturkey.gobblecore.blocks.IBlockHandler;
import com.theprogrammingturkey.progressiontweaks.ProgressionCore;
import com.theprogrammingturkey.progressiontweaks.blocks.tileentities.TileFirePit;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemModelMesher;

public class ProgressionBlocks implements IBlockHandler
{
	public static BaseBlock FIRE_PIT_LIT;
	public static BaseBlock FIRE_PIT_UNLIT;
	public static BaseBlock BLANK_TELEPORTER;
	public static BaseBlock MACHINE_FRAME;
	public static BaseBlock NANOMACHINE_FRAME;

	@Override
	public void registerBlocks(BlockLoader loader)
	{
		loader.registerBlock(FIRE_PIT_LIT = new BlockFirePit(true));

		loader.setCreativeTab(ProgressionCore.modTab);

		loader.registerBlock(FIRE_PIT_UNLIT = new BlockFirePit(false), TileFirePit.class);
		loader.registerBlock(BLANK_TELEPORTER = new BaseBlock("blank_teleporter"));
		loader.registerBlock(MACHINE_FRAME = new BaseBlock("machine_frame"));
		loader.registerBlock(NANOMACHINE_FRAME = new BaseBlock("nanomachine_frame")
		{
			public boolean isOpaqueCube(IBlockState state)
			{
				return false;
			}

			public boolean isFullCube(IBlockState state)
			{
				return false;
			}
		});
	}

	@Override
	public void registerModels(BlockLoader loader)
	{
		ItemModelMesher mesher = Minecraft.getMinecraft().getRenderItem().getItemModelMesher();

		loader.registerBlockModel(mesher, FIRE_PIT_UNLIT, 0);
		loader.registerBlockModel(mesher, BLANK_TELEPORTER, 0);
		loader.registerBlockModel(mesher, MACHINE_FRAME, 0);
		loader.registerBlockModel(mesher, NANOMACHINE_FRAME, 0);
	}

}
