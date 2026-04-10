package me.shaweel.shaweeladdons.config.widgets;

import org.lwjgl.glfw.GLFW;

import me.shaweel.shaweeladdons.config.ConfigFile;
import me.shaweel.shaweeladdons.config.ConfigGui;
import me.shaweel.shaweeladdons.config.widgetTypes.ExpandableConfigWidgetWithLastLayerChildren;
import me.shaweel.shaweeladdons.utils.Animation;
import me.shaweel.shaweeladdons.utils.Log;
import me.shaweel.shaweeladdons.utils.NanoVG.NanoVGRenderer;

public class Feature extends ExpandableConfigWidgetWithLastLayerChildren {
	private int index;

	private static final int FONT_SIZE = 9;
	private static final int FONT_WEIGHT = 500;
	private static final float MAX_HOVERED_OPACITY = 20;
	private static final float ANIMATION_DURATION = 50;

	private float minX;
	private float maxX;
	private float minY;
	private float maxY;

	private float textX;
	private float textY;

	private float toggledOpacity = 0;
	private Boolean toggled = false;
	private Animation togglingAnimation = new Animation(0, 0, 0, null);

	private float hoveredOpacity = 0;
	private Boolean hovered = false;
	private Animation hoveringAnimation = new Animation(0, 0, 0, null);
	private Animation unhoveringAnimation = new Animation(0, 0, 0, null);

	private Boolean expanded = false;

	public Feature(String name, Category parent) {
		this.name = name;
		this.parent = parent;
		this.toggled = (boolean) ConfigFile.readFromConfig(parent.getName() + "." + name + ".value", false);
		this.expanded = (boolean) ConfigFile.readFromConfig(name + ".expanded", false);

		Boolean alreadyExists = false;

		for (Feature feature : this.parent.getChildren()) {
			if (feature.name.equals(this.name)) alreadyExists = true;
		}

		if (alreadyExists) {
			Log.error("You've made a duplicate Feature, this is highly discouraged. EXPECT EVERYTHING TO BREAK!");
		}

		this.parent.registerChild(this);
		this.index = this.parent.getChildren().indexOf(this);

		this.calculateCoordinates();
	}

	@Override
	public void calculateCoordinates() {
		this.index = this.parent.getChildren().indexOf(this);

		this.minX = this.parent.getMinX();
		this.maxX = this.parent.getMaxX();

		this.minY = this.parent.getMaxY() - 1;

		for (Feature feature : this.parent.getChildren()) {
			if (this.parent.getChildren().indexOf(feature) >= this.index) break;
			this.minY += ConfigGui.getYPadding()*2 + FONT_SIZE - 1;
		}

		this.maxY = this.minY + ConfigGui.getYPadding()*2 + FONT_SIZE;

		this.textX = (this.maxX+this.minX)/2 - NanoVGRenderer.getStringWidth(this.name, FONT_SIZE, FONT_WEIGHT)/2;
		this.textY = this.minY + ConfigGui.getYPadding();

		if (this.toggled && !this.togglingAnimation.isRunning()) {
			this.toggledOpacity = 255;
		} else if (!this.toggled && !this.togglingAnimation.isRunning()) {
			this.toggledOpacity = 0;
		}

		if (this.hovered && !this.hoveringAnimation.isRunning()) {
			this.hoveredOpacity = MAX_HOVERED_OPACITY;
		} else if (!this.toggled && !this.unhoveringAnimation.isRunning()) {
			this.hoveredOpacity = 0;
		}
	}

	private void applyLowestPointScissor() {
		NanoVGRenderer.applyScissor(this.minX, this.parent.getMinY(), this.maxX, this.parent.getLowestPoint());
	}

	private void drawMainRectangle() {
		NanoVGRenderer.drawRectangle(this.minX, this.minY, this.maxX, this.maxY, ConfigGui.getBackgroundColor());
	}

	private void drawToggledRectangle() {
		int toggledColor = (ConfigGui.getPrimaryColor() & 0x00FFFFFF) | ((int) this.toggledOpacity << 24);
		NanoVGRenderer.drawRectangle(this.minX, this.minY, this.maxX, this.maxY, toggledColor);
	}

	private void drawHoveredRectangle() {
		int hoveredColor = (ConfigGui.getHoveredColor() & 0x00FFFFFF) | ((int) this.hoveredOpacity << 24);
		NanoVGRenderer.drawRectangle(this.minX, this.minY, this.maxX, this.maxY, hoveredColor);
	}

	private void drawFeatureName() {
		NanoVGRenderer.drawString(this.name, this.textX, this.textY, FONT_SIZE, FONT_WEIGHT, ConfigGui.getTextColor());
	}

	@Override
	public void render() {
		this.hoveringAnimation.update();
		this.unhoveringAnimation.update();
		this.togglingAnimation.update();
		this.calculateCoordinates();

		applyLowestPointScissor();
		drawMainRectangle();
		drawToggledRectangle();
		drawHoveredRectangle();
		drawFeatureName();
		NanoVGRenderer.resetScissor();
	}

	@Override
	public Boolean onClick(int button) {
		if (button != GLFW.GLFW_MOUSE_BUTTON_LEFT) {
			return false;
		}

		this.toggled = !this.toggled;
		this.togglingAnimation = new Animation(this.toggledOpacity, this.toggled ? 255 : 0, ANIMATION_DURATION, value -> this.toggledOpacity = value);
		this.togglingAnimation.start();

		ConfigFile.updateConfig();
		return true;
	}

	@Override
	public void onHoverEnter() {
		this.hovered = true;
		this.hoveringAnimation = new Animation(this.hoveredOpacity, MAX_HOVERED_OPACITY, ANIMATION_DURATION, value -> this.hoveredOpacity = value);
		this.hoveringAnimation.start();
	}

	@Override
	public void onHoverExit() {
		this.hovered = false;
		this.unhoveringAnimation = new Animation(this.hoveredOpacity, 0, ANIMATION_DURATION, value -> this.hoveredOpacity = value);
		this.unhoveringAnimation.start();
	}

	@Override
	public Boolean isInHitbox(double x, double y) {
		return (x > this.minX && x < this.maxX &&
			y > this.minY && y < this.maxY && y < this.parent.getLowestPoint());
	}

	@Override
	public Boolean getValue() {
		return this.toggled;
	}

	@Override
	public float getContentWidth() {
		return NanoVGRenderer.getStringWidth(this.name, FONT_SIZE, FONT_WEIGHT);
	}

	@Override
	public float getMinX() {
		return this.minX;
	}

	@Override
	public float getMaxX() {
		return this.maxX;
	}

	@Override
	public float getMinY() {
		return this.minY;
	}

	@Override
	public float getMaxY() {
		return this.maxY;
	}

	public float getTextX() {
		return this.textX;
	}

	public float getTextY() {
		return this.textY;
	}

	public float getToggledOpacity() {
		return toggledOpacity;
	}

	public float getHoveredOpacity() {
		return hoveredOpacity;
	}
}
