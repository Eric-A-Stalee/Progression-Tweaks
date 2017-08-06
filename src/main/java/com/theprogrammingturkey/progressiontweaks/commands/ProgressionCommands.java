package com.theprogrammingturkey.progressiontweaks.commands;

import com.theprogrammingturkey.gobblecore.commands.BaseCommandHandler;
import com.theprogrammingturkey.gobblecore.commands.CommandManager;
import com.theprogrammingturkey.gobblecore.commands.SimpleSubCommand;
import com.theprogrammingturkey.gobblecore.config.QueuedMessageReporter;
import com.theprogrammingturkey.progressiontweaks.ProgressionCore;
import com.theprogrammingturkey.progressiontweaks.config.ProgressionConfigLoader;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

public class ProgressionCommands
{
	public static void loadCommands()
	{
		BaseCommandHandler commandHandler = new BaseCommandHandler(ProgressionCore.instance, "ProgressionTweaks");
		commandHandler.addCommandAliases("progressiontweaks", "PTweaks", "ptweaks", "ProgTweaks", "progtweaks");

		commandHandler.registerSubCommand("reload", new SimpleSubCommand("Refreshes the mod with any changes made in the mod's config", false)
		{
			@Override
			public boolean execute(MinecraftServer server, ICommandSender sender, String[] args)
			{
				ProgressionConfigLoader.loadFromConfig();
				QueuedMessageReporter.outputErrors((EntityPlayer) sender);
				sender.addChatMessage(new TextComponentString(TextFormatting.GREEN + "Reloaded"));
				return true;
			}
		});

		CommandManager.registerCommandHandler(commandHandler);
	}
}
