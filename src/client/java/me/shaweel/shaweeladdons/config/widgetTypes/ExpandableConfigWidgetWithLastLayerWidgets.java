package me.shaweel.shaweeladdons.config.widgetTypes;

import java.util.ArrayList;
import java.util.List;

import me.shaweel.shaweeladdons.config.widgets.Category;

public abstract class ExpandableConfigWidgetWithLastLayerWidgets implements ConfigWidget<Category, Boolean> {
	protected String name;
	protected Category parent;
	protected List<LastLayerWidget<?>> children = new ArrayList<>();

	public void registerChild(LastLayerWidget<?> child) {
		this.children.add(child);
	}

	public List<LastLayerWidget<?>> getChildren() {
		return children;
	}

	public String getName() {
		return this.name;
	}

	public Category getParent() {
		return this.parent;
	}
}
