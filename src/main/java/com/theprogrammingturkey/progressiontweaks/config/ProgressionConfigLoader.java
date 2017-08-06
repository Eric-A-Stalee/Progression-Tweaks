package com.theprogrammingturkey.progressiontweaks.config;

import java.io.File;

import com.theprogrammingturkey.gobblecore.config.QueuedMessageReporter;
import com.theprogrammingturkey.gobblecore.util.GameUtil;
import com.theprogrammingturkey.progressiontweaks.ProgressionCore;
import com.theprogrammingturkey.progressiontweaks.registries.FirePitRegistry;

import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.config.Configuration;

public class ProgressionConfigLoader
{
	public static Configuration config;

	public static final String firePitCat = "Firepit Settings";

	public static final String itemCat = "Item Settings";

	public static void loadConfigSettings(File file)
	{
		config = new Configuration(file);
	}

	public static void loadFromConfig()
	{
		config.load();
		config.setCategoryComment(firePitCat, "Fire Pit Settings");

		FirePitRegistry.INSTANCE.clearRegistry();

		String[] recipes = config.getStringList("Recipes", firePitCat, new String[0], "Recipes for the fire pit to be able to cook. Format, things in () are optional -> \"ModID:ItemName(:meta)-ModID:ItemName(:meta)-duration-xp\" With the first item being the input and the second being the cooked output.");
		for(String s : recipes)
		{
			String[] items = s.split("-");
			if(items.length == 4)
			{
				ItemStack stack1 = parseItemStack(items[0], s);
				ItemStack stack2 = parseItemStack(items[1], s);
				int duration = parseInt(items[2], s);
				int xp = parseInt(items[3], s);

				if(stack1 != null && stack2 != null)
					FirePitRegistry.INSTANCE.registerCookingRecipe(stack1, stack2, duration, xp);
			}
			else
			{
				error("Failed to parse fire pit recipe with the input of \"" + s + "\"");
			}
		}

		String[] fuels = config.getStringList("Fuels", firePitCat, new String[0], "Fuel items for the fire pit to be able to accept. Format, things in () are optional -> \"ModID:ItemName(:meta)-duration\" With duration in ticks.");
		for(String s : fuels)
		{
			String[] items = s.split("-");
			if(items.length == 2)
			{
				ItemStack stack = parseItemStack(items[0], s);
				int duration = parseInt(items[1], s);
				if(stack != null)
					FirePitRegistry.INSTANCE.registerFuel(stack, duration);
			}
			else
			{
				error("Failed to parse fire pit fuel with the input of \"" + s + "\"");
			}
		}

		ProgressionSettings.firePitAttractionRadius = config.getInt("Attraction Radius", firePitCat, 16, 1, 100, "Attraction radius for the fire pit to make the mobs attact to. Square radius from corner to center.");

		ProgressionSettings.SpearBreakChance = config.getInt("Spear Break Chance", itemCat, 15, 0, Integer.MAX_VALUE, "Chance of a thrown spear to break. 1 in x chance. Set to 0 for never.");

		config.save();
	}

	private static ItemStack parseItemStack(String stackString, String recipe)
	{
		ItemStack toReturn = null;
		String[] item = stackString.split(":");
		if(item.length >= 2)
		{
			int meta = 0;
			if(item.length == 3)
				meta = parseInt(item[2], recipe);
			toReturn = GameUtil.getItemStack(item[0], item[1], 1, meta);
		}

		if(toReturn == null)
		{
			error("Failed to parse itemstack of \"" + stackString + "\" for the recipe \"" + recipe + "\".");
		}

		return toReturn;
	}

	private static int parseInt(String intString, String recipe)
	{
		try
		{
			return Integer.parseInt(intString);
		} catch(NumberFormatException e)
		{
			error("Failed to parse integer of \"" + intString + "\" for the recipe \"" + recipe + "\". Defaulting to 1.");
			return 1;
		}
	}

	private static void error(String message)
	{
		QueuedMessageReporter.queueErrorMessage(TextFormatting.RED, ProgressionCore.NAME, message);
	}
}
