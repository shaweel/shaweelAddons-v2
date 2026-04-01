package me.shaweel.shaweeladdons.config;

import java.util.List;

public abstract class ConfigWidget<P, T> {
	/**
	 * Handles a mouse click
	 * @param button The mouse button which was clicked
	 * @return <code>Boolean</code> Whether the click was consumed or not
	 */
	public abstract Boolean onClick(int button);
	public abstract Boolean isInHitbox(double x, double y);

	public abstract List<? extends ConfigWidget<?, ?>> getChildren();
	public abstract P getParent();

	public abstract T getValue();

	public abstract Boolean getExpanded();

	public abstract String getName();

	public abstract float getContentWidth();
}
