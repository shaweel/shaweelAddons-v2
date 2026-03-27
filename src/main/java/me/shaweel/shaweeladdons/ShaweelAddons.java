package me.shaweel.shaweeladdons;

import me.shaweel.shaweeladdons.config.NanoVG.NanoVGPiPRenderer;
import net.fabricmc.api.ClientModInitializer;

public class ShaweelAddons implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		ModCommands.initialize();
		NanoVGPiPRenderer.initialize();
	}
}