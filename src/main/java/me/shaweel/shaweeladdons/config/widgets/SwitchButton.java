package me.shaweel.shaweeladdons.config.widgets;

import java.util.List;

import me.shaweel.shaweeladdons.config.ConfigFile;
import me.shaweel.shaweeladdons.config.ConfigWidget;
import me.shaweel.shaweeladdons.config.FeatureChild;
import me.shaweel.shaweeladdons.config.LastLayerWidget;
import me.shaweel.shaweeladdons.utils.Log;

public class SwitchButton extends LastLayerWidget<Boolean> {
	private String name;
	private int index;
	private Feature parent;

	private float toggledOpacity = 0;
	private float lastToggledOpacity = 0;
	private long lastToggleTime;
	private Boolean toggled = false;
	private Boolean enabling = false;
	private Boolean disabling = false;
	private float toggleGoal;

	public SwitchButton(String name, Feature parent) {
		this.name = name;
		this.parent = parent;		
		this.toggled = (boolean) ConfigFile.readFromConfig(parent.getName() + "." + name + ".value", false);

		Boolean alreadyExists = false;

		for (ConfigWidget<?, ?> widget : this.parent.getChildren()) {
			if (widget.getName().equals(this.name)) alreadyExists = true;
		}

		if (alreadyExists) {
			Log.error(String.format("You've created a duplicate child of Feature %s, this is highly discouraged. EXPECT EVERYTHING TO BREAK!", this.parent.getName()));
		}

		this.parent.registerChild(this);
		this.index = this.parent.getChildren().indexOf(this);

		this.calculateCoordinates();
	}
}
