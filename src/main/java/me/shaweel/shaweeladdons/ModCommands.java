package me.shaweel.shaweeladdons;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;

import me.shaweel.shaweeladdons.utils.Log;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

public class ModCommands {
	public static void initialize() {
		ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
			registerMainCommand(dispatcher, "shaweeladdons");
			registerMainCommand(dispatcher, "shaweel");
			registerMainCommand(dispatcher, "sha");
		});
	}

	/**
	 * Registers the main command to a command name of choice. Multiple can be made.
	 * @param dispatcher
	 * @param commandName
	 */
	private static void registerMainCommand(CommandDispatcher<FabricClientCommandSource> dispatcher, String commandName) {
		dispatcher.register(ClientCommandManager.literal(commandName)
		.then(ClientCommandManager.literal("debugMode").executes(context -> debugModeCommand(context)))
		.then(ClientCommandManager.literal("config").executes(context -> configCommand(context)))
		.executes(context -> configCommand(context)));
	}

	private static int configCommand(CommandContext<FabricClientCommandSource> context) {
		Log.debug("Opening config");
		Config config = Config.getInstance();
		config.show();
		return 1;
	}

	private static int debugModeCommand(CommandContext<FabricClientCommandSource> context) {
		Log.toggleDebugMode();
		return 1;
	}
}
