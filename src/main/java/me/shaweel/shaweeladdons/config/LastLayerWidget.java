package me.shaweel.shaweeladdons.config;

import me.shaweel.shaweeladdons.utils.Log;

public abstract class LastLayerWidget<T> implements LastLayerChild, ConfigWidget<ExpandableWidgetWithLastLayerChildren, T> {
	private String name;
	private int index;
	private ExpandableWidgetWithLastLayerChildren parent;
	private Object value;

	protected abstract void calculateCoordinates();

	public LastLayerWidget(String name, ExpandableWidgetWithLastLayerChildren parent) {
		this.name = name;
		this.parent = parent;		
		this.value = ConfigFile.readFromConfig(parent.getName() + "." + name + ".value", false);

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
