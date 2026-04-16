package me.shaweel.shaweeladdons.config.widgets;


import java.util.List;

import me.shaweel.shaweeladdons.config.ConfigGui;
import me.shaweel.shaweeladdons.config.widgetTypes.ConfigWidget;
import me.shaweel.shaweeladdons.config.widgetTypes.ExpandableConfigWidgetWithLastLayerWidgets;
import me.shaweel.shaweeladdons.config.widgetTypes.LastLayerWidget;
import me.shaweel.shaweeladdons.utils.Animation;
import me.shaweel.shaweeladdons.utils.Log;
import me.shaweel.shaweeladdons.utils.NanoVG.NanoVGRenderer;

public class SwitchButton extends LastLayerWidget<Boolean> {
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

	private float circleX;
	private float circleY;

	private float hoveredOpacity = 0;
	private boolean hovered = false;
	private Animation hoveringAnimation = new Animation(0, 0, 0, null);
	private Animation unhoveringAnimation = new Animation(0, 0, 0, null);
	
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
		this.hovered = true;
		this.hoveringAnimation = new Animation(this.hoveredOpacity, ConfigGui.getFeatureMaxHoveredOpacity(), ConfigGui.getFeatureHoverAnimationDuration(), value -> this.hoveredOpacity = value);
		this.hoveringAnimation.start();
	}

	@Override
	public void onHoverExit() {
		this.hovered = false;
		this.unhoveringAnimation = new Animation(this.hoveredOpacity, 0, ConfigGui.getFeatureHoverAnimationDuration(), value -> this.hoveredOpacity = value);
		this.unhoveringAnimation.start();
	}

	@Override
	public Boolean isInHitbox(double x, double y) {
		return (x > this.switchMinX && x < this.switchMaxX &&
			y > this.switchMinY && y < this.switchMaxY && y < this.parent.getLowestPoint() && y < this.parent.getParent().getLowestPoint());
	}

	@Override
	public float getContentWidth() {
		return NanoVGRenderer.getStringWidth(this.name, ConfigGui.getOptionFontSize(), ConfigGui.getOptionFontWeight());
	}

	@Override
	public void calculateCoordinates() {
		this.minX = this.parent.getMinX();
		this.maxX = this.parent.getMaxX();

		this.minY = this.parent.getMaxY();

		for (int i = 0; i < index; i++) {
			this.minY += ConfigGui.getOptionTextVerticalMargin() * 2 + ConfigGui.getOptionFontSize();
		}

		this.maxY = this.minY + ConfigGui.getOptionTextVerticalMargin() * 2 + ConfigGui.getOptionFontSize();

		this.textX = this.minX + ConfigGui.getOptionHorizontalMargin();
		this.textY = this.minY + ConfigGui.getOptionTextVerticalMargin();

		this.switchMaxX = this.maxX - ConfigGui.getOptionHorizontalMargin();
		this.switchMinX = this.switchMaxX - ConfigGui.getSwitchWidth();
		this.switchMinY = this.minY + ConfigGui.getSwitchVerticalMargin();
		this.switchMaxY = this.maxY - ConfigGui.getSwitchVerticalMargin();
		this.switchRectangleRadius = (this.switchMaxY - this.switchMinY) / 2;
		this.circleX = this.switchMaxX - this.switchRectangleRadius*2;
		this.circleY = this.switchMinY + 1;

		if (this.hovered && !this.hoveringAnimation.isRunning()) {
			this.hoveredOpacity = ConfigGui.getFeatureMaxHoveredOpacity();
		} else if (!this.hovered && !this.unhoveringAnimation.isRunning()) {
			this.hoveredOpacity = 0;
		}
	}

	private void renderRectangle() {
		NanoVGRenderer.drawRectangle(this.minX, this.minY, this.maxX, this.maxY, ConfigGui.getBackgroundColor());
	}

	private void renderName() {
		NanoVGRenderer.drawString(this.name, this.textX, this.textY, ConfigGui.getOptionFontSize(), ConfigGui.getOptionFontWeight(), ConfigGui.getTextColor());
	}

	private void renderHoveredSwitch() {
		int hoveredColor = (ConfigGui.getHoveredColor() & 0x00FFFFFF) | ((int) this.hoveredOpacity << 24);
		NanoVGRenderer.drawRectangle(this.switchMinX, this.switchMinY, this.switchMaxX, this.switchMaxY, 
			this.switchRectangleRadius, hoveredColor);
	}

	private void renderSwitch() {
		NanoVGRenderer.drawRectangle(this.switchMinX, this.switchMinY, this.switchMaxX, this.switchMaxY, 
			this.switchRectangleRadius, ConfigGui.getPrimaryColor());
		NanoVGRenderer.drawCircle(this.circleX, this.circleY, this.switchRectangleRadius - 1, ConfigGui.getTextColor());
	}

	@Override
	public void render() {
		this.hoveringAnimation.update();
		this.unhoveringAnimation.update();
		this.calculateCoordinates();
		this.renderRectangle();
		this.renderName();
		this.renderSwitch();
		this.renderHoveredSwitch();
	}


	@Override
	public float getMinX() { return this.minX; }

	@Override
	public float getMinY() { return this.minY; }

	@Override
	public float getMaxX() { return this.maxX; }

	@Override
	public float getMaxY() { return this.maxY; }

	@Override
	public List<ConfigWidget<?, ?>> getChildren() { return null; }
}
