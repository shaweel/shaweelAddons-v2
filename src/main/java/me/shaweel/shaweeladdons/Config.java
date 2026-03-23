package me.shaweel.shaweeladdons;

import me.shaweel.shaweeladdons.configmanager.ConfigGui;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

public class Config {
	public static Config instance;

	boolean openConfig = false;
	
	private Config() {
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (!openConfig || client.screen != null) return;
			openConfig = false;
			client.setScreen(new ConfigGui());
		});
	}

	public void show() {
		openConfig=true;
	}

	public static Config getInstance() {
		if (instance == null) instance = new Config();
		return instance;
	}
}
