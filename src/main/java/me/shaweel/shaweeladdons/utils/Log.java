package me.shaweel.shaweeladdons.utils;


import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

public class Log {
	static boolean debugMode = false;
	/**
	 * Prints a message with the [shaweelAddons] prefix to chat
	 * @param message the message to print
	 */
	public static void info(String message) {
		Minecraft.getInstance().player.displayClientMessage(Component.literal("§d[shaweelAddons] §7"+message), false);
	}

	/**
	 * Prints a message with the [shaweelAddons] [DEBUG] prefix to chat if debugMode is on
	 * @param message the message to print
	 */
	public static void debug(String message) {
		if (!debugMode) return;
		Minecraft.getInstance().player.displayClientMessage(Component.literal("§d[shaweelAddons] §e[DEBUG] §7"+message), false);
	}

	/**
	 * Prints a message with the [shaweelAddons] [DEBUG] prefix to chat even if debugMode is off
	 * @param message the message to print
	 */
	public static void pseudoDebug(String message) {
		Minecraft.getInstance().player.displayClientMessage(Component.literal("§d[shaweelAddons] §e[DEBUG] §7"+message), false);
	}

	/**
	 * Prints a message with the [shaweelAddons] [ERROR] prefix to chat
	 * @param message the message to print
	 */
	public static void error(String message) {
		Minecraft.getInstance().player.displayClientMessage(Component.literal("§d[shaweelAddons] §4[ERROR] §7"+message), false);
	}

	public static void toggleDebugMode() {
		if (debugMode) {
			debugMode = false;
			pseudoDebug("Debug mode has been §cdeactivated");
		} else if (!debugMode) {
			debugMode = true;
			pseudoDebug("Debug mode has been §aactivated");
		}
	}
}
