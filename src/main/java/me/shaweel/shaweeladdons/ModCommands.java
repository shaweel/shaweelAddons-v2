package me.shaweel.shaweeladdons;

import com.mojang.brigadier.CommandDispatcher;

import me.shaweel.shaweeladdons.config.ConfigGui;
import me.shaweel.shaweeladdons.utils.Log;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

public class ModCommands {
	/**
	 * Initializes the main command for /shaweelAddons, /shaweel and /sha
	 */
	public static void initialize() {
		ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
			registerMainCommand(dispatcher, "shaweeladdons");
			registerMainCommand(dispatcher, "shaweel");
			registerMainCommand(dispatcher, "sha");
		});
	}

	/**
	 * Registers the main command to a command name of choice. There can be multiple of these.
	 * This is meant to be used with <code>ClientCommandRegistrationCallback.EVENT.register()</code> which passes you a
	 * <code>CommandDispatcher&lt;FabricClientCommandSource&gt;</code> to use as the dispatcher parameter.
	 * @param dispatcher
	 * @param commandName The command name to register the main command to
	 */
	private static void registerMainCommand(CommandDispatcher<FabricClientCommandSource> dispatcher, String commandName) {
		dispatcher.register(ClientCommandManager.literal(commandName)
		.then(ClientCommandManager.literal("debugmode").executes(context -> debugModeCommand()))
		.then(ClientCommandManager.literal("config").executes(context -> configCommand()))
		.executes(context -> configCommand()));
	}

	/**
	 * The behaviour for /shaweeladdons config subcommand
	 * @param context
	 */
	//Returns an int to properly work with Fabric's ClientCommandManager
	private static int configCommand() {
		Log.debug("Opening config");
		new ConfigGui().open();
		return 1;
	}

	/**
	 * The behaviour for /shaweeladdons debugmode subcommand
	 * @param context
	 */
	//Returns an int to properly work with Fabric's ClientCommandManager
	private static int debugModeCommand() {
		Log.toggleDebugMode();
		return 1;
	}
}
