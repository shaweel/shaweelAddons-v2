package me.shaweel.shaweeladdons.config.widgets;

import java.util.List;

import org.lwjgl.glfw.GLFW;

import me.shaweel.shaweeladdons.config.ConfigGui;
import me.shaweel.shaweeladdons.config.widgetTypes.ConfigWidget;
import me.shaweel.shaweeladdons.config.widgetTypes.ExpandableConfigWidgetWithLastLayerWidgets;
import me.shaweel.shaweeladdons.config.widgetTypes.LastLayerWidget;
import me.shaweel.shaweeladdons.utils.Animation;
import me.shaweel.shaweeladdons.utils.Easing;
import me.shaweel.shaweeladdons.utils.NanoVG.NanoVGRenderer;

public class Button extends LastLayerWidget<Void> {
	private Runnable action;

	private float minX;
	private float minY;
	private float maxX;
	private float maxY;

	private float minRectangleX;
	private float minRectangleY;
	private float maxRectangleX;
	private float maxRectangleY;

	private float textX;
	private float textY;

	private float hoveredOpacity = 0;
	private boolean hovered = false;
	private Animation hoveringAnimation = new Animation(0, 0, 0, null, null);
	private Animation unhoveringAnimation = new Animation(0, 0, 0, null, null);

	private float pulseOpacity = 0;
	private Animation pulseAnimation = new Animation(0, 0, 0, null, null);
	private Animation unpulseAnimation = new Animation(0, 0, 0, null, null);
	
	public Button(String name, ExpandableConfigWidgetWithLastLayerWidgets parent, Runnable action) {
		super(name, parent);
		this.action = action;
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

	private void renderHoveredButton() {
		int hoveredColor = (ConfigGui.getHoveredColor() & 0x00FFFFFF) | ((int) this.hoveredOpacity << 24);
		NanoVGRenderer.renderRectangle(this.minRectangleX+1, this.minRectangleY+1, this.maxRectangleX-1, this.maxRectangleY-1, hoveredColor);
	}

	private void renderPulseButton() {
		int pulseColor = (ConfigGui.getPrimaryColor() & 0x00FFFFFF) | ((int) this.pulseOpacity << 24);
		NanoVGRenderer.renderRectangle(this.minRectangleX+1, this.minRectangleY+1, this.maxRectangleX-1, this.maxRectangleY-1, pulseColor);
	}

	private void renderText() {
		NanoVGRenderer.renderCenteredString(this.name, this.textX, this.textY, ConfigGui.getOptionFontSize(), ConfigGui.getOptionFontWeight(), ConfigGui.getTextColor());
	}

	@Override
	public void render() {
		this.hoveringAnimation.update();
		this.unhoveringAnimation.update();
		this.pulseAnimation.update();
		this.unpulseAnimation.update();
		this.calculateCoordinates();

		this.renderRectangle();

		this.renderButtonOutline();
		this.renderButton();
		this.renderHoveredButton();
		this.renderPulseButton();
		this.renderText();
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

		this.maxY = this.minY + ConfigGui.getButtonYMargin() * 2 + ConfigGui.getOptionFontSize() + ConfigGui.getButtonTextYPadding() * 2;

		this.minRectangleX = this.minX + ConfigGui.getButtonXMargin();
		this.maxRectangleX = this.maxX - ConfigGui.getButtonXMargin();
		this.minRectangleY = this.minY + ConfigGui.getButtonYMargin();
		this.maxRectangleY = this.maxY - ConfigGui.getButtonYMargin();

		this.textX = (this.maxX + this.minX) / 2;
		this.textY = (this.maxY + this.minY) / 2;

		if (this.hovered && !this.hoveringAnimation.isRunning()) {
			this.hoveredOpacity = ConfigGui.getMaxHoveredOpacity();
		} else if (!this.hovered && !this.unhoveringAnimation.isRunning()) {
			this.hoveredOpacity = 0;
		}
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
		return (x > this.minRectangleX && x < this.maxRectangleX &&
			y > this.minRectangleY && y < this.maxRectangleY && y < this.parent.getLowestPoint() && y < this.parent.getParent().getLowestPoint());
	}

	@Override
	public Boolean onClick(int button) {
		if (button != GLFW.GLFW_MOUSE_BUTTON_LEFT) {
			return false;
		}


		new Thread(() -> {
			try {
				this.pulseAnimation = new Animation(this.pulseOpacity, 255, ConfigGui.getClickAnimationDuration() / 2, value -> this.pulseOpacity = value, Easing.EASE_OUT_QUAD);
				this.pulseAnimation.start();
				Thread.sleep((long) ConfigGui.getClickAnimationDuration() / 2);
				this.unpulseAnimation = new Animation(this.pulseOpacity, 0, ConfigGui.getClickAnimationDuration() / 2, value -> this.pulseOpacity = value, Easing.EASE_IN_QUAD);
				this.unpulseAnimation.start();
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}).start();

		this.action.run();

		return true;
	}

	@Override
	public List<? extends ConfigWidget<?, ?>> getChildren() { return null; }

	@Override
	public float getContentWidth() {
		return NanoVGRenderer.getStringWidth(this.name, ConfigGui.getOptionFontSize(), ConfigGui.getOptionFontWeight())
		 + ConfigGui.getButtonXMargin() * 2
		 + ConfigGui.getButtonTextXPadding() * 2;
	}

	@Override
	public float getMinX() { return minX; }

	@Override
	public float getMinY() { return minY; }

	@Override
	public float getMaxX() { return maxX; }

	@Override
	public float getMaxY() { return maxY; }

	@Override
	public ConfigWidget<?, ?> getChildById(int id) { return null; }
}
