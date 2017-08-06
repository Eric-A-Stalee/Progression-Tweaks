package com.theprogrammingturkey.progressiontweaks.util;

import com.theprogrammingturkey.progressiontweaks.ProgressionCore;
import com.theprogrammingturkey.progressiontweaks.blocks.ProgressionBlocks;
import com.theprogrammingturkey.progressiontweaks.items.ProgressionItems;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ProgressionCrafting
{

	public static void initCrafting()
	{
		ResourceLocation group = new ResourceLocation(ProgressionCore.MODID);
		// Spear
		GameRegistry.addShapedRecipe(new ResourceLocation(ProgressionCore.MODID, "Spear"), group, new ItemStack(ProgressionItems.SPEAR), "S  ", " S ", "  F", 'S', new ItemStack(Items.STICK), 'F', new ItemStack(Items.FLINT));
		GameRegistry.addShapelessRecipe(new ResourceLocation(ProgressionCore.MODID, "Spear_Shaft_Repair"), group, new ItemStack(ProgressionItems.SPEAR), Ingredient.func_193367_a(ProgressionItems.BROKEN_SPEAR_SHAFT), Ingredient.func_193367_a(Items.STICK), Ingredient.func_193367_a(Items.STICK));
		GameRegistry.addShapelessRecipe(new ResourceLocation(ProgressionCore.MODID, "Spear_Tip_Repair"), group, new ItemStack(ProgressionItems.SPEAR), Ingredient.func_193367_a(ProgressionItems.BROKEN_SPEAR_TIP), Ingredient.func_193367_a(Items.FLINT));

		// Fire Pit
		GameRegistry.addShapedRecipe(new ResourceLocation(ProgressionCore.MODID, "Fire_Pit"), group, new ItemStack(ProgressionBlocks.FIRE_PIT_UNLIT), "CCC", "S S", " S ", 'S', new ItemStack(Items.STICK), 'C', new ItemStack(Blocks.STONE_SLAB, 1, 3));
	}
}
