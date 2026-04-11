package me.shaweel.shaweeladdons.config.widgetTypes;

import me.shaweel.shaweeladdons.config.ConfigFile;
import me.shaweel.shaweeladdons.utils.Log;

public abstract class LastLayerWidget<T> implements ConfigWidget<ExpandableConfigWidgetWithLastLayerWidgets, T> {
	protected String name;
	protected int index;
	protected ExpandableConfigWidgetWithLastLayerWidgets parent;
	protected T value;

	@SuppressWarnings("unchecked")
	public LastLayerWidget(String name, ExpandableConfigWidgetWithLastLayerWidgets parent) {
		this.name = name;
		this.parent = parent;		
		this.value = (T) ConfigFile.readFromConfig(parent.getName() + "." + name + ".value", false);

		Boolean alreadyExists = false;

		for (ConfigWidget<?, ?> widget : this.parent.getChildren()) {
			if (widget.getName().equals(this.name)) alreadyExists = true;
		}

		if (alreadyExists) {
			Log.error(String.format("You've created a duplicate child of Feature %s, this is highly discouraged. EXPECT EVERYTHING TO BREAK!", this.parent.getName()));
		}

		this.parent.registerChild(this);
		this.index = this.parent.getChildren().indexOf(this);
	}
	
	public String getName() { return name; }

	public T getValue() { return value; }

	public ExpandableConfigWidgetWithLastLayerWidgets getParent() { return parent; }
}
