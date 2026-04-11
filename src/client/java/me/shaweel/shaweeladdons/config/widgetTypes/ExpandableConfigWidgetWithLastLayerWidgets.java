package me.shaweel.shaweeladdons.config.widgetTypes;

import java.util.ArrayList;
import java.util.List;

import me.shaweel.shaweeladdons.config.ConfigGui;
import me.shaweel.shaweeladdons.config.widgets.Category;
import me.shaweel.shaweeladdons.utils.Animation;

public abstract class ExpandableConfigWidgetWithLastLayerWidgets implements ConfigWidget<Category, Boolean>, ExpandableConfigWidget {
	protected String name;
	protected Category parent;

	protected float lowestPoint;
	private Animation expandingAnimation = new Animation(0, 0, 0, null);
	protected boolean expanded;
	protected List<LastLayerWidget<?>> children = new ArrayList<>();

	protected void expand() {
		this.expanded = !this.expanded;
		this.expandingAnimation = new Animation(this.lowestPoint, this.expanded ? this.getLowestExpandedPoint() : this.getLowestUnexpandedPoint(), 
		ConfigGui.getExpandingAnimationDuration(), value -> this.lowestPoint = value);
		this.expandingAnimation.start();
	}

	protected float getLowestExpandedPoint() {
		if (this.children.size() == 0) {
			return this.getMaxY();
		}
		float lowestExpandedPoint = this.getMaxY();
		for (LastLayerWidget<?> child : this.children) {
			float lowestChildPoint = child.getMaxY();
			if (lowestChildPoint > lowestExpandedPoint) lowestExpandedPoint = lowestChildPoint;
		}

		return lowestExpandedPoint;
	}

	protected float getLowestUnexpandedPoint() { return this.getMaxY(); }

	protected void updateExpandingAnimation() {
		this.expandingAnimation.update();
	}

	protected void calculateLowestPoint() {
		if (this.expanded && !this.expandingAnimation.isRunning()) {
			this.lowestPoint = this.getLowestExpandedPoint();
		} else if (!this.expanded && !this.expandingAnimation.isRunning()) {
			this.lowestPoint = this.getLowestUnexpandedPoint();
		}
	}

	public float getLowestPoint() { return this.lowestPoint; }

	public Boolean getExpanded() { return this.expanded; }

	public void registerChild(LastLayerWidget<?> child) {
		this.children.add(child);
	}

	public List<LastLayerWidget<?>> getChildren() { return children; }

	public String getName() { return this.name; }

	public Category getParent() { return this.parent; }
}
