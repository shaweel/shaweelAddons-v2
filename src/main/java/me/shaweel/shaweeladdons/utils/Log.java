package me.shaweel.shaweeladdons.utils;


import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

public class Log {
	private final static Boolean isDevelopmentEnvironment = FabricLoader.getInstance().isDevelopmentEnvironment();
	private static Boolean debugMode = isDevelopmentEnvironment;
	/**
	 * Prints a message with the [shaweelAddons] prefix to chat.
	 * @param message the message to print
	 */
	public static void info(String message) {
		Minecraft.getInstance().player.displayClientMessage(Component.literal("§d[shaweelAddons] §7"+message), false);
	}

	/**
	 * Prints a message with the [shaweelAddons] [DEBUG] prefix to chat if debugMode is on.
	 * @param message the message to print
	 */
	public static void debug(String message) {
		if (!debugMode) return;
		Minecraft.getInstance().player.displayClientMessage(Component.literal("§d[shaweelAddons] §e[DEBUG] §7"+message), false);
	}

	/**
	 * Prints a message with the [shaweelAddons] [DEBUG] prefix to chat even if debugMode is off.
	 * @param message the message to print
	 */
	public static void pseudoDebug(String message) {
		Minecraft.getInstance().player.displayClientMessage(Component.literal("§d[shaweelAddons] §e[DEBUG] §7"+message), false);
	}

	/**
	 * Prints a message with the [shaweelAddons] [ERROR] prefix to chat.
	 * @param message the message to print
	 */
	public static void error(String message) {
		Minecraft.getInstance().player.displayClientMessage(Component.literal("§d[shaweelAddons] §4[ERROR] §7"+message), false);
	}

	/**
	 * Prints a message with the [shaweelAddons] [WARN] prefix to chat.
	 * @param message the message to print
	 */
	public static void warn(String message) {
		Minecraft.getInstance().player.displayClientMessage(Component.literal("§d[shaweelAddons] §6[WARNING] §7"+message), false);
	}

	/**
	 * Toggles debug mode and shows a warning in chat if not in a development environment.
	 */
	public static void toggleDebugMode() {
		if (!isDevelopmentEnvironment) {
			warn("You're currently not in a development environment. Debug Mode is meant to be used when debugging the mod and is not intended for normal use.");
		}
		if (debugMode) {
			debugMode = false;
			pseudoDebug("Debug mode has been §cdeactivated");
		} else if (!debugMode) {
			debugMode = true;
			pseudoDebug("Debug mode has been §aactivated");
		}
	}
}
