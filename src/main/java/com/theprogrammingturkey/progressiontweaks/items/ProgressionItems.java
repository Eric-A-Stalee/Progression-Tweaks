package com.theprogrammingturkey.progressiontweaks.items;

import com.theprogrammingturkey.gobblecore.items.BaseItem;
import com.theprogrammingturkey.gobblecore.items.BaseItemBlock;
import com.theprogrammingturkey.gobblecore.items.BaseItemFood;
import com.theprogrammingturkey.gobblecore.items.IItemHandler;
import com.theprogrammingturkey.gobblecore.items.ItemLoader;
import com.theprogrammingturkey.progressiontweaks.ProgressionCore;
import com.theprogrammingturkey.progressiontweaks.blocks.ProgressionBlocks;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.item.Item;

public class ProgressionItems implements IItemHandler
{
	public static BaseItemFood LIME;
	public static BaseItem SPEAR;
	public static BaseItem BROKEN_SPEAR_TIP;
	public static BaseItem BROKEN_SPEAR_SHAFT;
	public static BaseItem TOMAHAWK;
	public static Item STONE_HAMMER;
	public static BaseItemFood FLAT_BREAD;
	public static BaseItem UNFIRED_CLAY_BOWL;

	@Override
	public void registerItems(ItemLoader loader)
	{

		loader.setCreativeTab(ProgressionCore.modTab);

		loader.registerItem(LIME = new ItemLime());
		loader.registerItem(SPEAR = new ItemSpear());
		loader.registerItem(BROKEN_SPEAR_TIP = new ItemBrokenSpear("tip"));
		loader.registerItem(BROKEN_SPEAR_SHAFT = new ItemBrokenSpear("shaft"));
		loader.registerItem(TOMAHAWK = new ItemTomahawk());
		loader.registerItem(STONE_HAMMER = new ItemStoneHammer(), "stone_hammer");
		loader.registerItem(FLAT_BREAD = new BaseItemFood("flat_bread", 4, 0.2f, false));
		loader.registerItem(UNFIRED_CLAY_BOWL = new BaseItem("unfired_clay_bowl"));

		loader.registerBlockItem(ProgressionBlocks.FIRE_PIT_UNLIT);
		loader.registerBlockItem(ProgressionBlocks.BLANK_TELEPORTER);

		BaseItemBlock teleporter = (BaseItemBlock) Item.getItemFromBlock(ProgressionBlocks.BLANK_TELEPORTER);
		teleporter.addLore("Problem?");
		teleporter.setShiftToShowLore(false);
	}

	@Override
	public void registerModels(ItemLoader loader)
	{
		ItemModelMesher mesher = Minecraft.getMinecraft().getRenderItem().getItemModelMesher();
		loader.registerItemModel(mesher, LIME, 0);
		loader.registerItemModel(mesher, SPEAR, 0);
		loader.registerItemModel(mesher, BROKEN_SPEAR_TIP, 0);
		loader.registerItemModel(mesher, BROKEN_SPEAR_SHAFT, 0);
		loader.registerItemModel(mesher, TOMAHAWK, 0);
		loader.registerItemModel(mesher, STONE_HAMMER, 0, "stone_hammer");
		loader.registerItemModel(mesher, FLAT_BREAD, 0);
		loader.registerItemModel(mesher, UNFIRED_CLAY_BOWL, 0);
	}

}
