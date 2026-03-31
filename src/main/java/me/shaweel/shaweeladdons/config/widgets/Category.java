package me.shaweel.shaweeladdons.config.widgets;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.glfw.GLFW;

import me.shaweel.shaweeladdons.config.ConfigFile;
import me.shaweel.shaweeladdons.config.ConfigGui;
import me.shaweel.shaweeladdons.config.ConfigWidget;
import me.shaweel.shaweeladdons.utils.Easing;
import me.shaweel.shaweeladdons.utils.Log;
import me.shaweel.shaweeladdons.utils.NanoVG.NanoVGPiPRenderer;
import me.shaweel.shaweeladdons.utils.NanoVG.NanoVGRenderer;
import net.minecraft.client.gui.GuiGraphics;

public class Category extends ConfigWidget<ConfigGui, Void> {
	private static List<Category> categories = new ArrayList<>();
	public List<Feature> children = new ArrayList<>();

	private final ConfigGui parent;
	private final String name;
	private final float index;

	private static final int FONT_SIZE = 12;
	private static final int FONT_WEIGHT = 700;

	private static final float MARGIN = 7f;
	private static final float X_PADDING = 30;
	private static final float Y_PADDING = 3;
	private static final float INDICATOR_LINE_Y = 2;
	private static final float ANIMATION_DURATION = 300;

	private float y;
	private float x;

	private float squareMinX;
	private float squareMaxX;
	private float squareMinY;
	private float squareMaxY;

	private float textX;
	private float textY;

	private float lowestPoint = Float.POSITIVE_INFINITY;

	private long lastExpandTime;
	private float lastLowestPoint;
	private float expandGoal;

	private Boolean expanded = false;
	private Boolean expanding = false;
	private Boolean collapsing = false;

	private void calculateCoordinates() {
		this.x = MARGIN * (index + 1);

		for (Category category : categories) {
			if (categories.indexOf(category) >= this.index) break;
			this.x += this.parent.getWidestContentWidth() + X_PADDING;
		}

		this.y = MARGIN;

		this.squareMinX = this.x;
		this.squareMaxX = this.x + X_PADDING + this.parent.getWidestContentWidth();
		this.squareMinY = this.y;
		this.squareMaxY = this.y + Y_PADDING*2 + FONT_SIZE;

		this.textX = (this.squareMaxX+this.squareMinX)/2 - NanoVGRenderer.getStringWidth(this.name, FONT_SIZE, FONT_WEIGHT)/2;
		this.textY = this.y + Y_PADDING;
	}
	
	public Category(String name, ConfigGui parent) {
		this.name = name;
		this.parent = parent;
		this.expanded = Boolean.TRUE.equals(ConfigFile.readFromConfig(name + ".expanded"));

		Boolean alreadyExists = false;

		for (Category category : categories) {
			if (category.name.equals(this.name)) alreadyExists = true;
		}

		if (alreadyExists) {
			Log.error("You've made a duplicate Category, this is highly discouraged. EXPECT EVERYTHING TO BREAK!");
		}

		categories.add(this);
		this.index = categories.indexOf(this);

		this.calculateCoordinates();
	}

	/**
	 * @return The widest string width in all of the Category
	 */
	/*
	private statc float getWidestStringWidth() {
		//TODO refactor later
		float widest = 0;

		for (Category category : categories) {
			for (Feature feature : category.children) {
				float width = feature.getStringWidth(feature.getName());
				if (width > widest) widest = width;
			}
			float width = category.getStringWidth(category.name);
			if (width > widest) widest = width;
		}
		
		return widest;
	} */

	private void expandOrCollapse() {
		final long elapsed = System.currentTimeMillis() - lastExpandTime;
		final float progress = Math.min(elapsed / ANIMATION_DURATION, 1f);

		final float easedProgress;
		if (this.expanding) {
			easedProgress = Easing.easeInCubic(progress);
		} else if (this.collapsing) {
			easedProgress = Easing.easeOutCubic(progress);
		} else {
			Log.error("Unexpected scenario, expandOrCollapse() was called when neither expanding nor collapsing are true");
			return;
		}

		if (progress >= 1) {
			this.expanded = expanding;
			this.expanding = false;
			this.collapsing = false;
			lowestPoint = expandGoal;
		}

		this.lowestPoint = this.lastLowestPoint + (this.expandGoal - this.lastLowestPoint) * easedProgress;
	}

	private void drawMainRectangle() {
		NanoVGRenderer.drawRect(this.squareMinX, this.squareMinY, this.squareMaxX, this.squareMaxY, this.parent.backgroundColor);
	}

	private void drawCategoryName() {
		NanoVGRenderer.drawString(this.name, this.textX, this.textY, FONT_SIZE, FONT_WEIGHT, this.parent.textColor);
	}

	private void drawIndicatorLine() {
		NanoVGRenderer.drawRect(this.squareMinX, this.lowestPoint - 1, this.squareMaxX, this.lowestPoint - 1 + INDICATOR_LINE_Y,
			this.parent.primaryColor);
	}

	private void render() {
		calculateCoordinates();

		drawMainRectangle();
		drawCategoryName();

		if (expanding || collapsing) {
			expandOrCollapse();
		} else if (!expanded) {
			this.lowestPoint = getLowestUnexpandedPoint();
		} else if (expanded) {
			this.lowestPoint = getLowestExpandedPoint();
		}

		if (expanding || collapsing || expanded) {
			renderAllFeatures();
		}

		drawIndicatorLine();
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

		this.lastExpandTime = System.currentTimeMillis();
		this.lastLowestPoint = lowestPoint;

		if (expanded && !collapsing || expanding) {
			this.expandGoal = this.squareMaxY;
			this.expanding = false;
			this.collapsing = true;
		} else {
			this.expandGoal = getLowestExpandedPoint();
			this.expanding = true;
			this.collapsing = false;
		}

		ConfigFile.updateConfig();
		return true;
	}

	@Override
	public Boolean isInHitbox(double x, double y) {
		return (x > this.squareMinX && x < this.squareMaxX && y > this.squareMinY && y < this.squareMaxY);
	}

	@Override
	public Void getValue() {
		return null;
	}

	@Override
	public Boolean getExpanded() {
		return this.expanded && !this.collapsing || this.expanding;
	}

	private float getLowestExpandedPoint() {
		float lowestExpandedPoint = this.squareMaxY;
		for (Feature child : children) {
			float lowestChildPoint = child.getSquareMaxY();
			if (lowestChildPoint > lowestExpandedPoint) lowestExpandedPoint = lowestChildPoint;
		}

		return lowestExpandedPoint;
	}

	private float getLowestUnexpandedPoint() {
		return this.squareMaxY;
	}

	private void renderAllFeatures() {
		for (Feature child : this.children) {
			child.render();
		}
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

	public float getXPadding() {
		return X_PADDING;
	}

	public float getYPadding() {
		return Y_PADDING;
	}

	public float getTextX() {
		return this.textX;
	}

	public float getTextY() {
		return this.textY;
	}

	@Override
	public String getName() {
		return this.name;
	}

	public float getLowestPoint() {
		return this.lowestPoint;
	}

	public static List<Category> getAllCategories() {
		return categories;
	}

	public void registerChild(Feature child) {
		this.children.add(child);
	}

	@Override
	public List<Feature> getChildren() {
		return this.children;
	}
	
	@Override
	public ConfigGui getParent() {
		return this.parent;
	}

	@Override
	public float getContentWidth() {
		return NanoVGRenderer.getStringWidth(this.name, FONT_SIZE, FONT_WEIGHT);
	}
}
  