package me.shaweel.shaweeladdons.config;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import me.shaweel.shaweeladdons.utils.Log;
import net.fabricmc.loader.api.FabricLoader;

public class ConfigFile {
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	private static final Path CONFIG_FILE = FabricLoader.getInstance().getConfigDir().resolve("shaweelAddons/shaweelAddons.json");

	private static void fetchConfig(List<? extends ConfigWidget<?>> widgets) {
		
	}
	
	private static void save(ConfigFile configFile) {
		try {
			Files.createDirectories(CONFIG_FILE);
			Files.writeString(CONFIG_FILE, GSON.toJson(configFile));
		} catch (Exception exception) {
			Log.error("Failed to save configuration. Exception: "+exception.getMessage());
		}
	}

	public static ConfigFile load() {
		try {
			if (Files.exists(CONFIG_FILE)) {
				return GSON.fromJson(Files.readString(CONFIG_FILE), ConfigFile.class);
			}

			ConfigFile defaultConfig = new ConfigFile();
			save(defaultConfig);
			return defaultConfig;

		} catch (Exception exception) {
			Log.error("Failed to load configuration. Exception: "+exception.getMessage());
			return new ConfigFile();
		}
	}

	static {
		fetchConfig(null);
	}
}
