package me.shaweel.shaweeladdons;

import com.mojang.brigadier.CommandDispatcher;

import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.network.chat.Component;

public class ModCommands {
	public static void initialize() {
		ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
			registerMainCommand(dispatcher);
		});
	}

	private static void registerMainCommand(CommandDispatcher<FabricClientCommandSource> dispatcher) {
		dispatcher.register(ClientCommandManager.literal("shaweeladdons").executes(context -> {
			context.getSource().sendFeedback(Component.literal("Hello"));
			return 1;
		}));
	}
}
