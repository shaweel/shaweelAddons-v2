package me.shaweel.shaweeladdons.config.widgets;

import java.util.List;

import me.shaweel.shaweeladdons.config.ConfigGui;
import me.shaweel.shaweeladdons.config.widgetTypes.ConfigWidget;
import me.shaweel.shaweeladdons.config.widgetTypes.ExpandableConfigWidgetWithLastLayerWidgets;
import me.shaweel.shaweeladdons.config.widgetTypes.LastLayerWidget;
import me.shaweel.shaweeladdons.utils.Log;
import me.shaweel.shaweeladdons.utils.NanoVG.NanoVGRenderer;

public class Button extends LastLayerWidget<Void> {
	private float minX;
	private float minY;
	private float maxX;
	private float maxY;

	private float minRectangleX;
	private float minRectangleY;
	private float maxRectangleX;
	private float maxRectangleY;
	
	public Button(String name, ExpandableConfigWidgetWithLastLayerWidgets parent) {
		super(name, parent);
	}

	private void renderRectangle() {
		NanoVGRenderer.renderRectangle(this.minX, this.minY, this.maxX, this.maxY, ConfigGui.getBackgroundColor());
	}

	private void renderButtonOutline() {
		NanoVGRenderer.renderRectangle(this.minRectangleX, this.minRectangleY, this.maxRectangleX, this.maxRectangleY, ConfigGui.getPrimaryColor());
	}

	private void renderButton() {
		NanoVGRenderer.renderRectangle(this.minRectangleX+1, this.minRectangleY+1, this.maxRectangleX-1, this.maxRectangleY-1, ConfigGui.getSecondaryBackgroundColor());
	}

	@Override
	public void render() {
		this.calculateCoordinates();

		this.renderRectangle();

		this.renderButtonOutline();
		this.renderButton();
	}

	@Override
	public void calculateCoordinates() {
		this.minX = this.parent.getMinX();
		this.maxX = this.parent.getMaxX();

		if (this.id == 0) {
			this.minY = this.parent.getMaxY();
		} else {
			this.minY = this.parent.getChildById(this.id - 1).getMaxY();
		}

		Log.debug("id:"+this.id+"; minY:"+this.minY);

		this.maxY = this.minY + ConfigGui.getButtonYMargin() * 2 + ConfigGui.getOptionFontSize();

		this.minRectangleX = this.minX + ConfigGui.getButtonXMargin();
		this.maxRectangleX = this.maxX - ConfigGui.getButtonXMargin();
		this.minRectangleY = this.minY + ConfigGui.getButtonYMargin();
		this.maxRectangleY = this.maxY - ConfigGui.getButtonYMargin();
	}

	@Override
	public void onHoverEnter() {
		
	}

	@Override
	public void onHoverExit() {
		
	}

	@Override
	public Boolean isInHitbox(double x, double y) {
		return false;
	}

	@Override
	public Boolean onClick(int button) {
		return false;
	}

	@Override
	public List<? extends ConfigWidget<?, ?>> getChildren() { return null; }

	@Override
	public float getContentWidth() {
		return 1;
	}

	@Override
	public float getMinX() { return minX; }

	@Override
	public float getMinY() { return minY; }

	@Override
	public float getMaxX() { return maxX; }

	@Override
	public float getMaxY() { return maxY; }
	
}
