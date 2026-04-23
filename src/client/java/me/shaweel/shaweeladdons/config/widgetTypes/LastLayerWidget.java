package me.shaweel.shaweeladdons.config.widgetTypes;

import me.shaweel.shaweeladdons.config.ConfigFile;
import me.shaweel.shaweeladdons.utils.Log;

public abstract class LastLayerWidget<T> implements ConfigWidget<ExpandableConfigWidgetWithLastLayerWidgets, T> {
	protected String name;
	protected int id;
	protected ExpandableConfigWidgetWithLastLayerWidgets parent;
	protected T value;

	@SuppressWarnings("unchecked")
	public LastLayerWidget(String name, ExpandableConfigWidgetWithLastLayerWidgets parent) {
		this.name = name;
		this.parent = parent;
		this.value = (T) ConfigFile.readFromConfig(this.parent.getParent().getName() + "." + parent.getName() + "." + name + ".value", false);

		this.parent.registerChild(this);
	}

	public void setId(int newId) {
		String caller = Thread.currentThread().getStackTrace()[2].getClassName();
		if (caller.equals(this.parent.getClass().getName())) {
			Log.error("The id of a ConfigWidget can only be set by itself or it's parent.");
			return;
		}

		this.id = newId;
	}

	public int getId() { return this.id; }

	public String getName() { return this.name; }

	public T getValue() { return this.value; }

	public ExpandableConfigWidgetWithLastLayerWidgets getParent() { return parent; }
}
