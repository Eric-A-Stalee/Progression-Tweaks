package com.theprogrammingturkey.progressiontweaks;

import org.apache.logging.log4j.Logger;

import com.google.gson.JsonElement;
import com.theprogrammingturkey.gobblecore.IModCore;
import com.theprogrammingturkey.gobblecore.blocks.BlockManager;
import com.theprogrammingturkey.gobblecore.entity.EntityManager;
import com.theprogrammingturkey.gobblecore.items.ItemManager;
import com.theprogrammingturkey.gobblecore.managers.WebHookManager;
import com.theprogrammingturkey.gobblecore.managers.WebHookManager.ModWebHook;
import com.theprogrammingturkey.gobblecore.network.NetworkManager;
import com.theprogrammingturkey.gobblecore.proxy.IBaseProxy;
import com.theprogrammingturkey.gobblecore.proxy.ProxyManager;
import com.theprogrammingturkey.progressiontweaks.blocks.ProgressionBlocks;
import com.theprogrammingturkey.progressiontweaks.commands.ProgressionCommands;
import com.theprogrammingturkey.progressiontweaks.config.ProgressionConfigLoader;
import com.theprogrammingturkey.progressiontweaks.entity.ProgressionEntities;
import com.theprogrammingturkey.progressiontweaks.items.ProgressionItems;
import com.theprogrammingturkey.progressiontweaks.network.ProgressionPackets;
import com.theprogrammingturkey.progressiontweaks.util.ProgressionCrafting;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = ProgressionCore.MODID, version = ProgressionCore.VERSION, name = ProgressionCore.NAME, dependencies = "after:gobblecore")
public class ProgressionCore implements IModCore
{
	public static final String MODID = "progressiontweaks";
	public static final String NAME = "Progression Tweaks";
	public static final String VERSION = "@VERSION@";

	@Instance(value = MODID)
	public static ProgressionCore instance;

	@SidedProxy(clientSide = "com.theprogrammingturkey.progressiontweaks.proxy.ClientProxy", serverSide = "com.theprogrammingturkey.progressiontweaks.proxy.CommonProxy")
	public static IBaseProxy proxy;

	public static CreativeTabs modTab = new CreativeTabs(MODID)
	{
		public ItemStack getTabIconItem()
		{
			return new ItemStack(ProgressionBlocks.FIRE_PIT_UNLIT);
		}
	};

	public static Logger logger;

	public ProgressionCore()
	{
		BlockManager.registerBlockHandler(new ProgressionBlocks(), this);
		ItemManager.registerItemHandler(new ProgressionItems(), this);
		EntityManager.registerEntityHandler(new ProgressionEntities(), this);
		NetworkManager.registerNetworkHandler(new ProgressionPackets(), this);
	}

	@EventHandler
	public void load(FMLPreInitializationEvent event)
	{
		logger = event.getModLog();
		ProgressionConfigLoader.loadConfigSettings(event.getSuggestedConfigurationFile());

		ProxyManager.registerModProxy(proxy);

		ProgressionCommands.loadCommands();
	}

	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		ProgressionCrafting.initCrafting();

		WebHookManager.registerHook(new ModWebHook(this)
		{
			@Override
			public void onResponse(JsonElement json)
			{

			}
		});
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
		ProgressionConfigLoader.loadFromConfig();
		//ProgressionAchievements.loadAchievements();
	}

	@Override
	public String getModID()
	{
		return MODID;
	}

	@Override
	public String getName()
	{
		return NAME;
	}

	@Override
	public String getVersion()
	{
		return VERSION;
	}
}
