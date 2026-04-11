package me.shaweel.shaweeladdons.config.widgets;


import java.util.List;

import me.shaweel.shaweeladdons.config.ConfigGui;
import me.shaweel.shaweeladdons.config.widgetTypes.ConfigWidget;
import me.shaweel.shaweeladdons.config.widgetTypes.ExpandableConfigWidgetWithLastLayerWidgets;
import me.shaweel.shaweeladdons.config.widgetTypes.LastLayerWidget;
import me.shaweel.shaweeladdons.utils.Log;
import me.shaweel.shaweeladdons.utils.NanoVG.NanoVGRenderer;

public class SwitchButton extends LastLayerWidget<Boolean> {
	private static final int FONT_SIZE = 7;
	private static final int FONT_WEIGHT = 400;
	private static final float BUTTON_WIDTH = 20;

	private float minX;
	private float maxX;
	private float minY;
	private float maxY;

	private float textX;
	private float textY;

	private float switchMinX;
	private float switchMaxX;
	private float switchMinY;
	private float switchMaxY;
	private float switchRectangleRadius;
	
	public SwitchButton(String name, ExpandableConfigWidgetWithLastLayerWidgets parent) {
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
		return NanoVGRenderer.getStringWidth(this.name, FONT_SIZE, FONT_WEIGHT);
	}

	@Override
	public void calculateCoordinates() {
		this.minX = this.parent.getMinX();
		this.maxX = this.parent.getMaxX();

		this.minY = this.parent.getMaxY() - 1;

		for (int i = 0; i < index; i++) {
			this.minY += ConfigGui.getYPadding() * 2 + FONT_SIZE - 1;
		}

		this.maxY = this.minY + ConfigGui.getYPadding() * 2 + FONT_SIZE;

		this.textX = this.minX + ConfigGui.getOptionPadding();
		this.textY = this.minY + ConfigGui.getYPadding();

		this.switchMaxX = this.maxX - ConfigGui.getOptionPadding();
		this.switchMinX = this.switchMaxX - BUTTON_WIDTH; 
		this.switchMinY = this.minY + ConfigGui.getYPadding();
		this.switchMaxY = this.maxY - ConfigGui.getYPadding();
		this.switchRectangleRadius = (this.switchMaxY - this.switchMinY) / 2;
	}

	private void renderRectangle() {
		NanoVGRenderer.drawRectangle(this.minX, this.minY, this.maxX, this.maxY, ConfigGui.getBackgroundColor());
	}

	private void renderName() {
		NanoVGRenderer.drawString(this.name, this.textX, this.textY, FONT_SIZE, FONT_WEIGHT, ConfigGui.getTextColor());
	}

	private void renderButton() {
		NanoVGRenderer.drawRectangle(this.switchMinX, this.switchMinY, this.switchMaxX, this.switchMaxY, 
			this.switchRectangleRadius, ConfigGui.getPrimaryColor());
	}

	@Override
	public void render() {
		this.calculateCoordinates();
		this.renderRectangle();
		this.renderName();
		this.renderButton();
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

	@Override
	public List<ConfigWidget<?, ?>> getChildren() {
		return null;
	}
}
