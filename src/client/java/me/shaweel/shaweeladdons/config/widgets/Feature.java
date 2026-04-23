package me.shaweel.shaweeladdons.config.widgets;

import org.lwjgl.glfw.GLFW;

import me.shaweel.shaweeladdons.config.ConfigFile;
import me.shaweel.shaweeladdons.config.ConfigGui;
import me.shaweel.shaweeladdons.config.widgetTypes.ExpandableConfigWidgetWithLastLayerWidgets;
import me.shaweel.shaweeladdons.config.widgetTypes.LastLayerWidget;
import me.shaweel.shaweeladdons.utils.Animation;
import me.shaweel.shaweeladdons.utils.Easing;
import me.shaweel.shaweeladdons.utils.Log;
import me.shaweel.shaweeladdons.utils.NanoVG.NanoVGRenderer;

public class Feature extends ExpandableConfigWidgetWithLastLayerWidgets {
	private int id;

	private float minX;
	private float maxX;
	private float minY;
	private float maxY;

	private float textX;
	private float textY;

	private float toggledOpacity = 0;
	private Boolean toggled = false;
	private Animation togglingAnimation = new Animation(0, 0, 0, null, null);

	private float hoveredOpacity = 0;
	private boolean hovered = false;
	private Animation hoveringAnimation = new Animation(0, 0, 0, null, null);
	private Animation unhoveringAnimation = new Animation(0, 0, 0, null, null);

	public Feature(String name, Category parent) {
		this.name = name;
		this.parent = parent;
		this.toggled = (boolean) ConfigFile.readFromConfig(parent.getName() + "." + name + ".value", false);
		this.expanded = (boolean) ConfigFile.readFromConfig(parent.getName() + "." + name + ".expanded", false);

		Boolean alreadyExists = false;

		for (Feature feature : this.parent.getChildren()) {
			if (feature.name.equals(this.name)) alreadyExists = true;
		}

		if (alreadyExists) {
			Log.error("You've made a duplicate Feature, this is highly discouraged. EXPECT EVERYTHING TO BREAK!");
		}

		this.parent.registerChild(this);
	}

	@Override
	public void calculateCoordinates() {
		this.id = this.parent.getChildren().indexOf(this);

		this.minX = this.parent.getMinX();
		this.maxX = this.parent.getMaxX();

		this.minY = this.parent.getMaxY();

		for (int i = 0; i < id; i++) {
			this.minY += (this.parent.getChildren().get(i).getLowestPoint() - this.parent.getChildren().get(i).getMinY());
		}

		this.maxY = this.minY + ConfigGui.getFeatureYMargin()*2 + ConfigGui.getFeatureFontSize();

		this.textX = (this.maxX+this.minX)/2 - NanoVGRenderer.getStringWidth(this.name, ConfigGui.getFeatureFontSize(), ConfigGui.getFeatureFontWeight())/2;
		this.textY = this.minY + ConfigGui.getFeatureYMargin();

		if (this.toggled && !this.togglingAnimation.isRunning()) {
			this.toggledOpacity = 255;
		} else if (!this.toggled && !this.togglingAnimation.isRunning()) {
			this.toggledOpacity = 0;
		}

		if (this.hovered && !this.hoveringAnimation.isRunning()) {
			this.hoveredOpacity = ConfigGui.getMaxHoveredOpacity();
		} else if (!this.hovered && !this.unhoveringAnimation.isRunning()) {
			this.hoveredOpacity = 0;
		}

		this.calculateLowestPoint();
	}

	private void applyLowestPointScissor() {
		NanoVGRenderer.applyScissor(this.minX, this.parent.getMinY(), this.maxX, this.parent.getLowestPoint() + 1);
	}

	private void renderMainRectangle() {
		NanoVGRenderer.renderRectangle(this.minX, this.minY, this.maxX, this.maxY, ConfigGui.getBackgroundColor());
	}

	private void renderToggledRectangle() {
		int toggledColor = (ConfigGui.getPrimaryColor() & 0x00FFFFFF) | ((int) this.toggledOpacity << 24);
		NanoVGRenderer.renderRectangle(this.minX, this.minY, this.maxX, this.maxY, toggledColor);
	}

	private void renderHoveredRectangle() {
		int hoveredColor = (ConfigGui.getHoveredColor() & 0x00FFFFFF) | ((int) this.hoveredOpacity << 24);
		NanoVGRenderer.renderRectangle(this.minX, this.minY, this.maxX, this.maxY, hoveredColor);
	}

	private void renderFeatureName() {
		NanoVGRenderer.renderString(this.name, this.textX, this.textY, ConfigGui.getFeatureFontSize(), ConfigGui.getFeatureFontWeight(), ConfigGui.getTextColor());
	}

	private void renderAllChildren() {
		for (LastLayerWidget<?> child : this.children) {
			child.render();
		}
	}

	@Override
	public void render() {
		this.updateExpandingAnimation();
		this.hoveringAnimation.update();
		this.unhoveringAnimation.update();
		this.togglingAnimation.update();
		this.calculateCoordinates();

		this.applyLowestPointScissor();
		this.renderMainRectangle();
		this.renderToggledRectangle();
		this.renderHoveredRectangle();
		this.renderFeatureName();
		this.renderAllChildren();

		NanoVGRenderer.resetScissor();
	}

	private void onLeftClick() {
		this.toggled = !this.toggled;
		this.togglingAnimation = new Animation(this.toggledOpacity, this.toggled ? 255 : 0, ConfigGui.getToggleAnimationDuration(), value -> this.toggledOpacity = value, this.toggled ? Easing.EASE_OUT_QUAD : Easing.EASE_IN_QUAD);
		this.togglingAnimation.start();

		ConfigFile.updateConfig();
	}

	private void onRightClick() {
		this.expand();

		ConfigFile.updateConfig();
	}

	@Override
	public Boolean onClick(int button) {
		if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
			onLeftClick();
			return true;
		} else if (button == GLFW.GLFW_MOUSE_BUTTON_RIGHT) {
			onRightClick();
			return true;
		}
		
		return false;
	}

	@Override
	public void onHoverEnter() {
		this.hovered = true;
		this.hoveringAnimation = new Animation(this.hoveredOpacity, ConfigGui.getMaxHoveredOpacity(), ConfigGui.getHoverAnimationDuration(), value -> this.hoveredOpacity = value, Easing.EASE_OUT_QUAD);
		this.hoveringAnimation.start();
	}

	@Override
	public void onHoverExit() {
		this.hovered = false;
		this.unhoveringAnimation = new Animation(this.hoveredOpacity, 0, ConfigGui.getHoverAnimationDuration(), value -> this.hoveredOpacity = value, Easing.EASE_IN_QUAD);
		this.unhoveringAnimation.start();
	}

	@Override
	public Boolean isInHitbox(double x, double y) {
		return (x > this.minX && x < this.maxX &&
			y > this.minY && y < this.maxY && y < this.parent.getLowestPoint());
	}


	@Override
	public float getContentWidth() {
		return NanoVGRenderer.getStringWidth(this.name, ConfigGui.getFeatureFontSize(), ConfigGui.getFeatureFontWeight()) + ConfigGui.getFeatureXMargin() * 2;
	}

	@Override
	public Boolean getValue() { return this.toggled; }

	@Override
	public float getMinX() { return this.minX; }

	@Override
	public float getMaxX() { return this.maxX; }

	@Override
	public float getMinY() { return this.minY; }

	@Override
	public float getMaxY() { return this.maxY; }

	public float getTextX() { return this.textX; }
	public float getTextY() { return this.textY; }

	public float getToggledOpacity() { return toggledOpacity; }
	public float getHoveredOpacity() { return hoveredOpacity; }

	@Override
	public int getId() {
		return this.id;
	}

	@Override
	public void setId(int newId) {
		String caller = Thread.currentThread().getStackTrace()[2].getClassName();
		if (caller.equals(this.parent.getClass().getName())) {
			Log.error("The id of a ConfigWidget can only be set by itself or it's parent.");
			return;
		}

		this.id = newId;
	}
}
