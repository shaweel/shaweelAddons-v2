package me.shaweel.shaweeladdons.config;

import java.util.List;

public abstract class ConfigWidget<P> {
	/**
	 * Handles a mouse click
	 * @param button The mouse button which was clicked
	 * @return <code>boolean</code> Whether the click was consumed or not
	 */
	public abstract boolean onClick(int button);
	public abstract boolean isInHitbox(double x, double y);
	public abstract List<? extends ConfigWidget<?>> getChildren();
	public abstract P getParent();
}
