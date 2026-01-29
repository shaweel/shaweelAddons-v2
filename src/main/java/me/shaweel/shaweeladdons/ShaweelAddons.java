package me.shaweel.shaweeladdons;

import net.fabricmc.api.ClientModInitializer;

public class ShaweelAddons implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		ModCommands.initialize();
	}
}