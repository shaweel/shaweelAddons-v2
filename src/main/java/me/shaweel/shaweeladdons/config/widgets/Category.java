package me.shaweel.shaweeladdons.config.widgets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.shaweel.shaweeladdons.config.ConfigGui;
import me.shaweel.shaweeladdons.utils.Text;
import net.minecraft.client.gui.GuiGraphics;

public class Category {
	private static List<Category> categories = new ArrayList<>();
	public List<Feature> children = new ArrayList<>();

	private final String name;
	private final int index;

	private static final int fontSize = 9;
	private static final float fontWeight = 2.0f;

	private static final int margin = 6;
	private static final int xPadding = 40;
	private static final int yPadding = 4;
	private static final int activatedPadding = 2;

	private final int y = margin;
	private int x;

	private static HashMap<String, Boolean> expandedMap = new HashMap<>();
	private boolean expanded = false;

	private int squareMinX;
	private int squareMaxX;
	private final int squareMinY = y;
	private int squareMaxY;

	private int textX;
	private int textY;

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

	public void render(ConfigGui configGui, GuiGraphics graphics) {
		this.x = margin * (index + 1);

		for (Category category : categories) {
			if (categories.indexOf(category) >= index) break;
			this.x += getWidestStringWidth(configGui) + xPadding;
		}

		this.squareMinX = this.x;
		this.squareMaxX = this.x + xPadding + getWidestStringWidth(configGui);
		this.squareMaxY = this.y + yPadding*2 + fontSize;

		this.textX = (this.squareMaxX+this.squareMinX)/2 - Text.getStringWidth(this.name, fontSize, fontWeight)/2;
		this.textY = this.y + yPadding;

		graphics.fill(squareMinX, squareMinY, squareMaxX, squareMaxY, configGui.backgroundColor);

		if (expanded) {
			renderAllFeatures(configGui, graphics);
		} else {
			graphics.fill(squareMinX, squareMaxY, squareMaxX, squareMaxY+activatedPadding, configGui.primaryColor);
		}

		Text.drawString(graphics, this.name, fontSize, fontWeight, textX, textY, configGui.textColor);
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

	private void renderAllFeatures(ConfigGui configGui, GuiGraphics graphics) {
		int lastY = this.squareMaxY;
		for (Feature child : this.children) {
			child.render(configGui, graphics, lastY);
			lastY = child.getSquareMaxY();
		}
	}

	public int getSquareMinX() {
		return this.squareMinX;
	}

	public int getSquareMaxX() {
		return this.squareMaxX;
	}

	public int getSquareMinY() {
		return this.squareMinY;
	}

	public int getSquareMaxY() {
		return this.squareMaxY;
	}

	public int getXPadding() {
		return xPadding;
	}

	public int getYPadding() {
		return yPadding;
	}

	public int getTextX() {
		return this.textX;
	}

	public int getTextY() {
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

	private static int getWidestStringWidth(ConfigGui configGui) {
		int widest = 0;

		for (Category category : categories) {
			int width = Text.getStringWidth(category.name, fontSize, fontWeight);
			if (width > widest) widest = width;
		}
		
		return widest;
	}
}
  