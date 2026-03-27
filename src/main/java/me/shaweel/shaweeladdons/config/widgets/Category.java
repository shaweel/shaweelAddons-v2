package me.shaweel.shaweeladdons.config.widgets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.shaweel.shaweeladdons.config.ConfigGui;
import me.shaweel.shaweeladdons.config.NanoVG.NanoVGPiPRenderer;
import me.shaweel.shaweeladdons.config.NanoVG.NanoVGRenderer;
import net.minecraft.client.gui.GuiGraphics;

public class Category {
	private static List<Category> categories = new ArrayList<>();
	public List<Feature> children = new ArrayList<>();

	private final String name;
	private final float index;

	private static final int fontSize = 9;
	private static final int fontWeight = 700;

	private static final float margin = 6;
	private static final float xPadding = 40;
	private static final float yPadding = 4;
	private static final float activatedPadding = 2;

	private final float y = margin;
	private float x;

	private static HashMap<String, Boolean> expandedMap = new HashMap<>();
	private boolean expanded = false;

	private float squareMinX;
	private float squareMaxX;
	private final float squareMinY = y;
	private float squareMaxY;

	private float textX;
	private float textY;

	public Category(String name) {
		this.name = name;
		this.expanded = expandedMap.getOrDefault(name, false);

		boolean alreadyExists = false;

		for (Category category : categories) {
			if (category.name.equals(this.name)) alreadyExists = true;
		}

		if (!alreadyExists) categories.add(this);
		this.index = categories.indexOf(this);
	}

	public void render(ConfigGui configGui) {
		this.x = margin * (index + 1);

		for (Category category : categories) {
			if (categories.indexOf(category) >= index) break;
			this.x += getWidestStringWidth(configGui) + xPadding;
		}

		this.squareMinX = this.x;
		this.squareMaxX = this.x + xPadding + getWidestStringWidth(configGui);
		this.squareMaxY = this.y + yPadding*2 + fontSize;

		this.textX = (this.squareMaxX+this.squareMinX)/2 - NanoVGRenderer.getStringWidth(this.name, fontSize, fontWeight)/2;
		this.textY = this.y + yPadding;

		NanoVGRenderer.drawRect(squareMinX, squareMinY, squareMaxX, squareMaxY, configGui.backgroundColor);

		if (expanded) {
			renderAllFeatures(configGui);
		} else {
			NanoVGRenderer.drawRect(squareMinX, squareMaxY, squareMaxX, squareMaxY+activatedPadding, configGui.primaryColor);
		}

		NanoVGRenderer.drawString(this.name, textX, textY, fontSize, fontWeight, configGui.textColor);
	}

	public static void renderAll(ConfigGui configGui, GuiGraphics guiGraphics) {
		NanoVGPiPRenderer.drawNanoVG(guiGraphics, () -> renderPip(configGui));
	}

	private static void renderPip(ConfigGui configGui) {
		for (Category category : categories) {
			category.render(configGui);
		}
	}

	public static void clearCategories() {
		for (Category category : categories) {
			expandedMap.put(category.name, category.expanded);
		}
		categories.clear();
	}

	public void toggleExpand() {
		expanded = !expanded;
	}

	private void renderAllFeatures(ConfigGui configGui) {
		float lastY = this.squareMaxY - 1;
		for (Feature child : this.children) {
			child.render(configGui, lastY);
			lastY = child.getSquareMaxY() - 1;
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
		return xPadding;
	}

	public float getYPadding() {
		return yPadding;
	}

	public float getTextX() {
		return this.textX;
	}

	public float getTextY() {
		return this.textY;
	}

	public String getName() {
		return this.name;
	}

	public boolean isInside(double x, double y) {
		return (x > this.squareMinX && x < this.squareMaxX && y > this.squareMinY && y < this.squareMaxY);
	}

	public static List<Category> getAllCategories() {
		return categories;
	}

	public void registerChild(Feature child) {
		this.children.add(child);
	}

	public List<Feature> getChildren() {
		return this.children;
	}

	private static float getWidestStringWidth(ConfigGui configGui) {
		float widest = 0;

		for (Category category : categories) {
			float width = NanoVGRenderer.getStringWidth(category.name, fontSize, fontWeight);
			if (width > widest) widest = width;
		}
		
		return widest;
	}
}
  