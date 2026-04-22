package me.shaweel.shaweeladdons.config.widgets;

import java.util.List;

import org.lwjgl.glfw.GLFW;

import me.shaweel.shaweeladdons.config.ConfigFile;
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

	private float squareMaxMinX;
	private float squareMinMinX;

	private float squareMinX;
	private float squareMinY;
	private float squareMaxX;
	private float squareMaxY;

	private float hoveredOpacity = 0;
	private boolean hovered = false;
	private Animation hoveringAnimation = new Animation(0, 0, 0, null);
	private Animation unhoveringAnimation = new Animation(0, 0, 0, null);

	private float toggledOpacity = 0;
	private Animation togglingSquareAnimation = new Animation(0, 0, 0, null);
	private Animation togglingOpacityAnimation = new Animation(0, 0, 0, null);
	
	public SwitchButton(String name, ExpandableConfigWidgetWithLastLayerWidgets parent) {
		super(name, parent);
	}

	@Override
	public Boolean onClick(int button) {
		if (button != GLFW.GLFW_MOUSE_BUTTON_LEFT) {
			return false;
		}

		this.value = !this.value;
		this.togglingSquareAnimation = new Animation(this.squareMinX, this.value ? this.squareMaxMinX : this.squareMinMinX, ConfigGui.getToggleAnimationDuration(), value -> this.squareMinX = value);
		this.togglingSquareAnimation.start();

		this.togglingOpacityAnimation = new Animation(this.toggledOpacity, this.value ? 255 : 0, ConfigGui.getToggleAnimationDuration(), value -> this.toggledOpacity = value);
		this.togglingOpacityAnimation.start();

		ConfigFile.updateConfig();

		return true;
	}

	@Override
	public void onHoverEnter() {
		this.hovered = true;
		this.hoveringAnimation = new Animation(this.hoveredOpacity, ConfigGui.getMaxHoveredOpacity(), ConfigGui.getHoverAnimationDuration(), value -> this.hoveredOpacity = value);
		this.hoveringAnimation.start();
	}

	@Override
	public void onHoverExit() {
		this.hovered = false;
		this.unhoveringAnimation = new Animation(this.hoveredOpacity, 0, ConfigGui.getHoverAnimationDuration(), value -> this.hoveredOpacity = value);
		this.unhoveringAnimation.start();
	}

	@Override
	public Boolean isInHitbox(double x, double y) {
		return (x > this.switchMinX && x < this.switchMaxX &&
			y > this.switchMinY && y < this.switchMaxY && y < this.parent.getLowestPoint() && y < this.parent.getParent().getLowestPoint());
	}

	@Override
	public float getContentWidth() {
		return NanoVGRenderer.getStringWidth(this.name, ConfigGui.getOptionFontSize(), ConfigGui.getOptionFontWeight())
		 + ConfigGui.getLastLayerWidgetXMargin() * 2
		 + ConfigGui.getSwitchWidth() 
		 + ConfigGui.getSwitchTextPadding();
	}

	@Override
	public void calculateCoordinates() {
		this.minX = this.parent.getMinX();
		this.maxX = this.parent.getMaxX();

		this.minY = this.parent.getMaxY();

		for (int i = 0; i < index; i++) {
			this.minY += ConfigGui.getSwitchYMargin() * 2 + ConfigGui.getOptionFontSize();
		}

		this.maxY = this.minY + ConfigGui.getSwitchYMargin() * 2 + ConfigGui.getOptionFontSize();

		this.textX = this.minX + ConfigGui.getLastLayerWidgetXMargin();
		this.textY = this.minY + ConfigGui.getLastLayerWidgetXMargin();

		this.switchMaxX = this.maxX - ConfigGui.getLastLayerWidgetXMargin();
		this.switchMinX = this.switchMaxX - ConfigGui.getSwitchWidth();
		this.switchMinY = this.minY + ConfigGui.getSwitchYMargin();
		this.switchMaxY = this.maxY - ConfigGui.getSwitchYMargin();

		this.squareMinY = this.switchMinY + ConfigGui.getSwitchSquarePadding();
		this.squareMaxY = this.switchMaxY - ConfigGui.getSwitchSquarePadding();

		float squareSideLength = (this.squareMaxY - this.squareMinY);

		this.squareMaxMinX = this.switchMaxX - ConfigGui.getSwitchSquarePadding() - squareSideLength;
		this.squareMinMinX = this.switchMinX + ConfigGui.getSwitchSquarePadding();

		if (this.value && !this.togglingSquareAnimation.isRunning() && !this.togglingOpacityAnimation.isRunning()) {
			this.squareMinX = squareMaxMinX;
			this.toggledOpacity = 255;
		} else if (!this.value && !this.togglingSquareAnimation.isRunning() && !this.togglingOpacityAnimation.isRunning()) {
			this.squareMinX = squareMinMinX;
			this.toggledOpacity = 0;
		}
		
		if (this.hovered && !this.hoveringAnimation.isRunning()) {
			this.hoveredOpacity = ConfigGui.getMaxHoveredOpacity();
		} else if (!this.hovered && !this.unhoveringAnimation.isRunning()) {
			this.hoveredOpacity = 0;
		}

		this.squareMaxX = this.squareMinX + squareSideLength;

		Log.debug("maxX="+this.maxX);
		Log.debug("minX="+this.minX);
		Log.debug("w="+(this.maxX-this.minX));
		Log.debug("switchWidth="+ConfigGui.getSwitchWidth());
		Log.debug("text width="+NanoVGRenderer.getStringWidth(this.name, ConfigGui.getOptionFontSize(), ConfigGui.getOptionFontWeight()));
		Log.debug((this.switchMaxX - this.switchMinX) == ConfigGui.getSwitchWidth());
	}

	private void renderRectangle() {
		NanoVGRenderer.drawRectangle(this.minX, this.minY, this.maxX, this.maxY, ConfigGui.getBackgroundColor());
	}

	private void renderName() {
		NanoVGRenderer.drawString(this.name, this.textX, this.textY, ConfigGui.getOptionFontSize(), ConfigGui.getOptionFontWeight(), ConfigGui.getTextColor());
	}

	private void renderHoveredSwitchRectangle() {
		int hoveredColor = (ConfigGui.getHoveredColor() & 0x00FFFFFF) | ((int) this.hoveredOpacity << 24);
		NanoVGRenderer.drawRectangle(this.switchMinX, this.switchMinY, this.switchMaxX, this.switchMaxY, hoveredColor);
	}

	private void renderToggledSwitchRectangle() {
		int toggledColor = (ConfigGui.getPrimaryColor() & 0x00FFFFFF) | ((int) this.toggledOpacity << 24);
		NanoVGRenderer.drawRectangle(this.switchMinX, this.switchMinY, this.switchMaxX, this.switchMaxY, toggledColor);
	}

	private void renderSwitchRectangle() {
		NanoVGRenderer.drawRectangle(this.switchMinX, this.switchMinY, this.switchMaxX, this.switchMaxY, ConfigGui.getSecondaryBackgroundColor());
	}

	private void renderSwitchSquare() {
		NanoVGRenderer.drawRectangle(this.squareMinX, this.squareMinY, this.squareMaxX, this.squareMaxY, ConfigGui.getTextColor());
	}

	@Override
	public void render() {
		this.hoveringAnimation.update();
		this.unhoveringAnimation.update();
		this.togglingSquareAnimation.update();
		this.togglingOpacityAnimation.update();

		this.calculateCoordinates();
		this.renderRectangle();
		this.renderName();
		this.renderSwitchRectangle();
		this.renderToggledSwitchRectangle();
		this.renderHoveredSwitchRectangle();
		this.renderSwitchSquare();
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
