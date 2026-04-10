package me.shaweel.shaweeladdons.config.widgetTypes;

import java.util.ArrayList;
import java.util.List;

import me.shaweel.shaweeladdons.config.widgets.Category;

public abstract class ExpandableConfigWidgetWithLastLayerChildren implements ConfigWidget<Category, Boolean> {
	protected String name;
	protected Category parent;
	private List<LastLayerChild> children = new ArrayList<>();

	public void registerChild(LastLayerChild child) {
		this.children.add(child);
	}

	public List<ConfigWidget<?, ?>> getChildren() {
		return null;
	}

	public String getName() {
		return this.name;
	}

	public Category getParent() {
		return this.parent;
	}
}
