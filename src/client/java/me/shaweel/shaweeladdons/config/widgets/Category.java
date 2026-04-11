package me.shaweel.shaweeladdons.config.widgets;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.lwjgl.glfw.GLFW;

import me.shaweel.shaweeladdons.config.ConfigFile;
import me.shaweel.shaweeladdons.config.ConfigGui;
import me.shaweel.shaweeladdons.config.widgetTypes.ConfigWidget;
import me.shaweel.shaweeladdons.config.widgetTypes.ExpandableConfigWidget;
import me.shaweel.shaweeladdons.utils.Log;
import me.shaweel.shaweeladdons.utils.Animation;
import me.shaweel.shaweeladdons.utils.NanoVG.NanoVGPiPRenderer;
import me.shaweel.shaweeladdons.utils.NanoVG.NanoVGRenderer;
import net.minecraft.client.gui.GuiGraphics;

public class Category implements ConfigWidget<ConfigGui, Void>, ExpandableConfigWidget {
	private static List<Category> categories = new ArrayList<>();
	public List<Feature> children = new ArrayList<>();

	private final ConfigGui parent;
	private final String name;
	private float index;

	private static final int FONT_SIZE = 12; 
	private static final int FONT_WEIGHT = 400;

	private static final float INDICATOR_LINE_Y = 2;

	private float minX;
	private float maxX;
	private float minY;
	private float maxY;

	private float textX;
	private float textY;

	private float lowestPoint = Float.POSITIVE_INFINITY;

	private Animation expandingAnimation = new Animation(0, 0, 0, null);

	private Boolean expanded = false;

	@Override
	public void calculateCoordinates() {
		this.index = categories.indexOf(this);

		this.minY = ConfigGui.getCornerOffset();
		this.maxY = this.minY + ConfigGui.getCategoryYPadding()*2 + FONT_SIZE;

		this.minX = ConfigGui.getCornerOffset() * (this.index + 1);

		for (int i = 0; i < this.index; i++) {
			this.minX += this.parent.getWidestContentWidth() + ConfigGui.getCategoryXPadding();
		}

		this.maxX = this.minX + ConfigGui.getCategoryXPadding() + this.parent.getWidestContentWidth();

		this.textX = (this.maxX+this.minX)/2 - NanoVGRenderer.getStringWidth(this.name, FONT_SIZE, FONT_WEIGHT)/2;
		this.textY = this.minY + ConfigGui.getCategoryYPadding();

		if (this.expanded && !this.expandingAnimation.isRunning()) {
			this.lowestPoint = this.getLowestExpandedPoint();
		} else if (!this.expanded && !this.expandingAnimation.isRunning()) {
			this.lowestPoint = this.getLowestUnexpandedPoint();
		}
	}
	
	public Category(String name, ConfigGui parent) {
		this.name = name;
		this.parent = parent;
		this.expanded = (boolean) ConfigFile.readFromConfig(name + ".expanded", false);

		Boolean alreadyExists = false;

		for (Category category : categories) {
			if (category.name.equals(this.name)) alreadyExists = true;
		}

		if (alreadyExists) {
			Log.error("You've made a duplicate Category, this is highly discouraged. EXPECT EVERYTHING TO BREAK!");
		}

		categories.add(this);
		this.index = categories.indexOf(this);
	}

	private void drawMainRectangle() {
		NanoVGRenderer.drawRectangle(this.minX, this.minY, this.maxX, this.maxY, ConfigGui.getBackgroundColor());
	}

	private void drawCategoryName() {
		NanoVGRenderer.drawString(this.name, this.textX, this.textY, FONT_SIZE, FONT_WEIGHT, ConfigGui.getTextColor());
	}

	private void drawIndicatorLine() {
		NanoVGRenderer.drawRectangle(this.minX, this.lowestPoint - 1, this.maxX, this.lowestPoint - 1 + INDICATOR_LINE_Y, ConfigGui.getBackgroundColor());

		int toggledColor = (ConfigGui.getPrimaryColor() & 0x00FFFFFF) | ((int) this.children.getLast().getToggledOpacity() << 24);
		NanoVGRenderer.drawRectangle(this.minX, this.lowestPoint - 1, this.maxX, this.lowestPoint - 1 + INDICATOR_LINE_Y, toggledColor);

		int hoveredColor = (ConfigGui.getHoveredColor() & 0x00FFFFFF) | ((int) this.children.getLast().getHoveredOpacity() << 24);
		NanoVGRenderer.drawRectangle(this.minX, this.lowestPoint - 1, this.maxX, this.lowestPoint - 1 + INDICATOR_LINE_Y, hoveredColor);
	}

	@Override
	public void render() {
		this.expandingAnimation.update();

		this.calculateCoordinates();
		this.drawMainRectangle();
		this.drawCategoryName();
		this.renderAllFeatures();
		this.drawIndicatorLine();
	}

	/**
	 * Renders all Categories in a GuiGraphics context
	 * @param guiGraphics
	 */
	public static void renderAllCategories(GuiGraphics guiGraphics) {
		NanoVGPiPRenderer.drawNanoVG(guiGraphics, () -> {
			for (Category category : categories) {
				category.render();
			}
		});
	}

	public static void clearCategories() {
		categories.clear();
	}

	@Override
	public Boolean onClick(int button) {
		if (button != GLFW.GLFW_MOUSE_BUTTON_RIGHT) {
			return false;
		}

		this.expanded = !this.expanded;
		this.expandingAnimation = new Animation(this.lowestPoint, this.expanded ? this.getLowestExpandedPoint() : this.getLowestUnexpandedPoint(), 
		ConfigGui.getExpandingAnimationDuration(), value -> this.lowestPoint = value);
		this.expandingAnimation.start();

		ConfigFile.updateConfig();
		return true;
	}

	@Override
	public void onHoverEnter() {
		return;
	}

	@Override
	public void onHoverExit() {
		return;
	}

	@Override
	public Boolean isInHitbox(double x, double y) {
		return (x > this.minX && x < this.maxX && y > this.minY && y < this.maxY);
	}

	@Override
	public Void getValue() { return null; }
 

	private float getLowestExpandedPoint() {
		float lowestExpandedPoint = this.maxY;
		for (Feature child : this.children) {
			float lowestChildPoint = child.getLowestPoint();
			if (lowestChildPoint > lowestExpandedPoint) lowestExpandedPoint = lowestChildPoint;
		}

		return lowestExpandedPoint;
	}

	private float getLowestUnexpandedPoint() { return this.maxY; }

	private void renderAllFeatures() {
		this.sortFeatures();

		for (Feature child : this.children) {
			child.render();
		}
	}

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

	@Override
	public String getName() { return this.name; }

	@Override
	public Boolean getExpanded() { return this.expanded; }

	public float getLowestPoint() { return this.lowestPoint; }

	private void sortFeatures() {
		this.children.sort(Comparator.comparingDouble(Feature::getContentWidth).reversed());
	} 

	public static List<Category> getAllCategories() {
		return categories;
	}

	public void registerChild(Feature child) {
		this.children.add(child);
	}

	public static Category findFirstCategory(String name) {
		for (Category category : categories) {
			if (category.name == name) {
				return category;
			}
		}

		Log.error(String.format("Category \"%s\" doesn't exist", name));
		return null;
	}

	@Override
	public List<Feature> getChildren() { return this.children; }
	
	@Override
	public ConfigGui getParent() { return this.parent; }

	@Override
	public float getContentWidth() {
		return NanoVGRenderer.getStringWidth(this.name, FONT_SIZE, FONT_WEIGHT);
	}
}
  