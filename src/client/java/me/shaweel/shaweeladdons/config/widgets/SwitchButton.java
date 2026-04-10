package me.shaweel.shaweeladdons.config.widgets;


import me.shaweel.shaweeladdons.config.ConfigGui;
import me.shaweel.shaweeladdons.config.widgetTypes.ConfigWidget;
import me.shaweel.shaweeladdons.config.widgetTypes.ExpandableConfigWidgetWithLastLayerChildren;
import me.shaweel.shaweeladdons.config.widgetTypes.LastLayerWidget;
import me.shaweel.shaweeladdons.utils.Log;
import me.shaweel.shaweeladdons.utils.NanoVG.NanoVGRenderer;

public class SwitchButton extends LastLayerWidget<Boolean> {
	private static final int FONT_SIZE = 8;
	private static final int FONT_WEIGHT = 400;

	private float minX;
	private float maxX;
	private float minY;
	private float maxY;
	
	public SwitchButton(String name, ExpandableConfigWidgetWithLastLayerChildren parent) {
		super(name, parent);
	}

	@Override
	public Boolean onClick(int button) {
		Log.error("Unimplemented method 'onClick'");
		return false;
	}

	@Override
	public void onHoverEnter() {
		Log.error("Unimplemented method 'onHoverEnter'");
	}

	@Override
	public void onHoverExit() {
		Log.error("Unimplemented method 'onHoverExit'");
	}

	@Override
	public Boolean isInHitbox(double x, double y) {
		Log.error("Unimplemented method 'isInHitbox'");
		return false;
	}

	@Override
	public float getContentWidth() {
		Log.error("Unimplemented method 'getContentWidth'");
		return 0f;
	}

	@Override
	public void calculateCoordinates() {
		this.minX = this.parent.getMinX();
		this.maxX = this.parent.getMaxX();

		this.minY = this.parent.getMaxY() - 1;

		for (ConfigWidget<?, ?> child : this.parent.getChildren()) {
			if (this.parent.getChildren().indexOf(child) >= index) break;
			this.minY += ConfigGui.getXPadding() * 2 + FONT_SIZE - 1;
		}

		this.maxY = this.minY + ConfigGui.getXPadding() * 2 + FONT_SIZE;
	}

	private void renderRectangle() {
		NanoVGRenderer.drawRectangle(minX, minY, maxX, maxY, ConfigGui.getBackgroundColor());
	}

	@Override
	public void render() {
		this.calculateCoordinates();
		this.renderRectangle();
	}


	@Override
	public float getMinX() {
		return this.minX;
	}

	@Override
	public float getMinY() {
		return this.minY;
	}

	@Override
	public float getMaxX() {
		return this.maxX;
	}

	@Override
	public float getMaxY() {
		return this.maxY;
	}
}
