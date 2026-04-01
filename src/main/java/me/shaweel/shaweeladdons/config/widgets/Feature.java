package me.shaweel.shaweeladdons.config.widgets;

import java.util.List;

import org.lwjgl.glfw.GLFW;

import me.shaweel.shaweeladdons.config.ConfigFile;
import me.shaweel.shaweeladdons.config.ConfigGui;
import me.shaweel.shaweeladdons.config.ConfigWidget;
import me.shaweel.shaweeladdons.utils.Log;
import me.shaweel.shaweeladdons.utils.NanoVG.NanoVGRenderer;

public class Feature extends ConfigWidget<Category, Boolean> {
	private String name;
	private int index;
	private Category parent;

	private static final int FONT_SIZE = 9;
	private static final int FONT_WEIGHT = 500;
	private static final float MAX_HOVERED_OPACITY = 20;
	private static final float ANIMATION_DURATION = 50;

	private float y;

	private float squareMinX;
	private float squareMaxX;
	private float squareMinY;
	private float squareMaxY;

	private float textX;
	private float textY;

	private float toggledOpacity = 0;
	private float lastToggledOpacity = 0;
	private long lastToggleTime;
	private Boolean toggled = false;
	private Boolean enabling = false;
	private Boolean disabling = false;
	private float toggleGoal;

	private float hoveredOpacity = 0;
	private float lastHoveredOpacity = 0;
	private long lastHoverTime;
	private Boolean hovered = false;
	private Boolean enteringHover = false;
	private Boolean exitingHover = false;
	private float hoverGoal;

	public Feature(String name, Category parent) {
		this.name = name;
		this.parent = parent;		
		this.toggled = (boolean) ConfigFile.readFromConfig(parent.getName() + "." + name + ".value", false);

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

	private void calculateCoordinates() {
		this.y = this.parent.getSquareMaxY() - 1;

		this.index = this.parent.getChildren().indexOf(this);

		for (Feature feature : this.parent.getChildren()) {
			if (this.parent.getChildren().indexOf(feature) >= this.index) break;
			this.y += ConfigGui.getYPadding()*2 + FONT_SIZE - 1;
		}

		this.squareMinX = this.parent.getSquareMinX();
		this.squareMaxX = this.parent.getSquareMaxX();
		this.squareMinY = this.y;
		this.squareMaxY = this.y + ConfigGui.getYPadding()*2 + FONT_SIZE;

		this.textX = (this.squareMaxX+this.squareMinX)/2 - NanoVGRenderer.getStringWidth(this.name, FONT_SIZE, FONT_WEIGHT)/2;
		this.textY = this.y + ConfigGui.getYPadding();
	}

	private void applyLowestPointScissor() {
		NanoVGRenderer.applyScissor(this.squareMinX, this.parent.getSquareMinY(), this.squareMaxX, this.parent.getLowestPoint());
	}

	private void drawMainRectangle() {
		NanoVGRenderer.drawRect(this.squareMinX, this.squareMinY, this.squareMaxX, this.squareMaxY, ConfigGui.getBackgroundColor());
	}

	private void drawToggledRectangle() {
		int toggledColor = (ConfigGui.getPrimaryColor() & 0x00FFFFFF) | ((int) this.toggledOpacity << 24);
		NanoVGRenderer.drawRect(this.squareMinX, this.squareMinY, this.squareMaxX, this.squareMaxY, toggledColor);
	}

	private void drawHoveredRectangle() {
		int hoveredColor = (ConfigGui.getHoveredColor() & 0x00FFFFFF) | ((int) this.hoveredOpacity << 24);
		NanoVGRenderer.drawRect(this.squareMinX, this.squareMinY, this.squareMaxX, this.squareMaxY, hoveredColor);
	}

	private void drawFeatureName() {
		NanoVGRenderer.drawString(this.name, this.textX, this.textY, FONT_SIZE, FONT_WEIGHT, ConfigGui.getTextColor());
	}

	public void render() {
		this.calculateCoordinates();

		if (enabling || disabling) { 
			enableOrDisable();
		} else if (!toggled) {
			this.toggledOpacity = 0;
		} else if (toggled) {
			this.toggledOpacity = 255;
		}

		if (enteringHover || exitingHover) { 
			hoverOrUnhover();
		} else if (!hovered) {
			this.hoveredOpacity = 0;
		} else if (hovered) {
			this.hoveredOpacity = MAX_HOVERED_OPACITY;
		}

		applyLowestPointScissor();
		drawMainRectangle();
		drawToggledRectangle();
		drawHoveredRectangle();
		drawFeatureName();
		NanoVGRenderer.resetScissor();
	}

	private void enableOrDisable() {
		final long elapsed = System.currentTimeMillis() - lastToggleTime;
		final float progress = Math.min(elapsed / ANIMATION_DURATION, 1f);

		if (progress >= 1) {
			this.toggled = this.enabling;
			this.enabling = false;
			this.disabling = false;
			this.toggledOpacity = this.toggleGoal;
		}

		this.toggledOpacity = this.lastToggledOpacity + (this.toggleGoal - this.toggledOpacity) * progress;
	}

	private void hoverOrUnhover() {
		final long elapsed = System.currentTimeMillis() - lastHoverTime;
		final float progress = Math.min(elapsed / ANIMATION_DURATION, 1f);

		if (progress >= 1) {
			this.hovered = this.enteringHover;
			this.enteringHover = false;
			this.exitingHover = false;
			this.hoveredOpacity = this.hoverGoal;
		}

		this.hoveredOpacity = this.lastHoveredOpacity + (this.hoverGoal - this.hoveredOpacity) * progress;
	}

	@Override
	public Boolean onClick(int button) {
		if (button != GLFW.GLFW_MOUSE_BUTTON_LEFT) {
			return false;
		}

		this.lastToggleTime = System.currentTimeMillis();
		this.lastToggledOpacity = toggledOpacity;

		if (toggled && !disabling || enabling) {
			this.toggleGoal = 0;
			this.enabling = false;
			this.disabling = true;
		} else {
			this.toggleGoal = 255;
			this.enabling = true;
			this.disabling = false;
		}

		ConfigFile.updateConfig();
		return true;
	}

	@Override
	public void onHoverEnter() {
		this.lastHoverTime = System.currentTimeMillis();
		this.lastHoveredOpacity = hoveredOpacity;

		this.hoverGoal = MAX_HOVERED_OPACITY;
		this.enteringHover = true;
		this.exitingHover = false;
	}

	@Override
	public void onHoverExit() {
		this.lastHoverTime = System.currentTimeMillis();
		this.lastHoveredOpacity = hoveredOpacity;

		this.hoverGoal = 0;
		this.enteringHover = false;
		this.exitingHover = true;
	}

	@Override
	public List<ConfigWidget<?, ?>> getChildren() {
		return null;
	}

	@Override
	public Boolean isInHitbox(double x, double y) {
		return (x > this.squareMinX && x < this.squareMaxX &&
			y > this.squareMinY && y < this.squareMaxY && y < this.parent.getLowestPoint());
	}

	@Override
	public Boolean getValue() {
		return this.toggled && !this.disabling || this.enabling;
	}

	@Override
	public Boolean getExpanded() {
		return null;
	}

	@Override
	public float getContentWidth() {
		return NanoVGRenderer.getStringWidth(this.name, FONT_SIZE, FONT_WEIGHT);
	}

	public float getSquareMinX() {
		return this.squareMinX;
	}

	public float getSquareMaxX() {
		return this.squareMaxX;
	}

	public float getSquareMinY() {
		return this.squareMinY;
	}

	public float getSquareMaxY() {
		return this.squareMaxY;
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

	@Override
	public String getName() {
		return this.name;
	}

	public Category getParent() {
		return this.parent;
	}
}
