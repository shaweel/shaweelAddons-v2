package me.shaweel.shaweeladdons.config;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import me.shaweel.shaweeladdons.config.widgets.Category;
import me.shaweel.shaweeladdons.utils.Log;
import net.fabricmc.loader.api.FabricLoader;

public class ConfigFile {
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	private static final Path CONFIG_FILE = FabricLoader.getInstance().getConfigDir().resolve("shaweelAddons/shaweelAddons.json");

	private static Map<String, Object> config = new HashMap<>();

	@SuppressWarnings("unchecked")
	private static Map<String, Object> parsePathToConfig(String path, int minusDepth) {
		String[] pathArray = path.split("\\.");
		pathArray = path.isEmpty() ? new String[0] : Arrays.copyOf(pathArray, pathArray.length - minusDepth);

		Map<String, Object> map = config;
		for (String entry : pathArray) {
			map = (Map<String, Object>) map.get(entry);
			if (map == null) {
				Log.debug(String.format("Invalid path: %s, this can either be fine or detrimental", path));
				return null;
			}
		}

		return map;
	}

	//TODO finish this method
	/*private static ConfigWidget<?, ?> parsePathToWidget() {
		
	}*/

	public static void updateConfig() {
		updateConfig(Category.getAllCategories(), "");
		save();
	}

	@SuppressWarnings("unchecked")
	private static void updateConfig(List<? extends ConfigWidget<?, ?>> widgets, String path) {
		if (path.isEmpty()) {
			config = new HashMap<>();
		}

		if (widgets == null) {
			return;
		}

		Map<String, Object> map = parsePathToConfig(path, 0);

		for (ConfigWidget<?, ?> widget : widgets) {
			String entry = widget.getName();
			Object value = widget.getValue();
			Boolean expanded = widget.getExpanded();

			map.put(entry, new HashMap<>());

			((Map<String, Object>) map.get(entry)).put("value", value);
			((Map<String, Object>) map.get(entry)).put("expanded", expanded);

			updateConfig(widget.getChildren(), path.isEmpty() ? entry : path + "." + entry);
		}
	}

	private static void save() {
		try {
			Files.createDirectories(CONFIG_FILE.getParent());
			Files.writeString(CONFIG_FILE, GSON.toJson(config).replace("  ", "\t"));
		} catch (Exception exception) {
			Log.error("Failed to save configuration. Exception: "+exception.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	public static void load() {
		try {
			if (Files.exists(CONFIG_FILE)) {
				config = GSON.fromJson(Files.readString(CONFIG_FILE), Map.class);
				return;
			}

			updateConfig();

		} catch (Exception exception) {
			Log.error("Failed to load configuration. Exception: "+exception.getMessage());
		}
	}

	//TODO finish this method
	/*private static Object getDefaultValue(String path) {
		
	}*/

	//TODO uncomment when getDefaultValue() is done
	public static Object readFromConfig(String path) {
		Map<String, Object> map = parsePathToConfig(path, 1);
		//if (map == null) return getDefaultValue(path);
		if (map == null) return null;
		
		String[] pathArray = path.split("\\.");
		Object result = map.get(pathArray[pathArray.length - 1]);
		//if (result == null) return getDefaultValue(path);
		return result; 
	}

	static {
		load();
	}
}