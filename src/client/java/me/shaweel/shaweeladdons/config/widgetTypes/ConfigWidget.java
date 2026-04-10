package me.shaweel.shaweeladdons.config.widgetTypes;

import java.util.List;

public interface ConfigWidget<P, T> {
	void render();
	void calculateCoordinates();

	void onHoverEnter();
	void onHoverExit();
	
	Boolean isInHitbox(double x, double y);
	Boolean onClick(int button);

	List<? extends ConfigWidget<?, ?>> getChildren();
	P getParent();
	T getValue();
	String getName();

	float getContentWidth();

	float getMinX();
	float getMinY();
	float getMaxX();
	float getMaxY();
}
