package com.theprogrammingturkey.progressiontweaks.registries;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.item.ItemStack;

public class FirePitRegistry
{
	public static final FirePitRegistry INSTANCE = new FirePitRegistry();
	private Map<ItemStack, CookingResult> cookingRegistry = new HashMap<ItemStack, CookingResult>();
	private Map<ItemStack, Integer> cookingFuelRegistry = new HashMap<ItemStack, Integer>();

	public FirePitRegistry()
	{
		
	}

	public void clearRegistry()
	{
		cookingRegistry.clear();
		cookingFuelRegistry.clear();
	}

	public void registerCookingRecipe(ItemStack input, ItemStack result, int duration, int xp)
	{
		cookingRegistry.put(input, new CookingResult(result, duration, xp));
	}

	public void registerFuel(ItemStack fuel, int burnTime)
	{
		cookingFuelRegistry.put(fuel, burnTime);
	}

	public CookingResult getResultFromInput(ItemStack input)
	{
		if(input == null)
			return null;

		CookingResult result = null;
		for(ItemStack stack : cookingRegistry.keySet())
			if(stack.getItem().equals(input.getItem()) && stack.getItemDamage() == input.getItemDamage())
				result = cookingRegistry.get(stack);
		return result;
	}

	public int getBurnTimeFromFuel(ItemStack input)
	{
		if(input == null)
			return -1;

		int result = -1;
		for(ItemStack stack : cookingFuelRegistry.keySet())
			if(stack.getItem().equals(input.getItem()) && stack.getItemDamage() == input.getItemDamage())
				result = cookingFuelRegistry.get(stack);
		return result;
	}

	public static class CookingResult
	{
		private ItemStack result;
		private int duration;
		private int xp;

		public CookingResult(ItemStack result, int duration, int xp)
		{
			this.result = result;
			this.duration = duration;
			this.xp = xp;
		}

		public ItemStack getResult()
		{
			return result;
		}

		public int getDuration()
		{
			return duration;
		}

		public int getXp()
		{
			return xp;
		}
	}
}
