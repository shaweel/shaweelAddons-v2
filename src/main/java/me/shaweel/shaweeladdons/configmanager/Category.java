package me.shaweel.shaweeladdons.configmanager;

import java.util.ArrayList;
import java.util.List;

import me.shaweel.shaweeladdons.utils.Text;
import net.minecraft.client.gui.GuiGraphics;

public class Category {
	private static List<String> categories = new ArrayList<>();
	public List<Feature> children = new ArrayList<>();

	private final int margin = 6;
	private final int y = margin;
	private int x;
	public final int xPadding = 20;
	public final int yPadding = 4;
	public final int activatedPadding = 2;
	public final int squareMinX;
	public final int squareMaxX;
	public final int squareMinY;
	public final int squareMaxY;
	public final int textX;
	public final int textY;

	public Category(ConfigGui configGui, GuiGraphics graphics, String name) {
		if (!categories.contains(name)) categories.add(name);
		final int index = categories.indexOf(name);
		this.x = margin * (index + 1);

		for (String category : categories) {
			if (categories.indexOf(category) >= index) break;
			this.x += getWidestStringWidth(configGui) + this.xPadding*2;
		}

		this.squareMinX = this.x;
		this.squareMinY = this.y;
		this.squareMaxX = this.x + this.xPadding*2 + getWidestStringWidth(configGui);
		this.squareMaxY = this.y + this.yPadding*2 + configGui.lineHeight;

		this.textX = (this.squareMaxX+this.squareMinX)/2 - Text.getStringWidth(name)/2;
		this.textY = this.y + this.yPadding;

		graphics.fill(squareMinX, squareMinY, squareMaxX, squareMaxY, configGui.backgroundColor);
		graphics.fill(squareMinX, squareMaxY, squareMaxX, squareMaxY+activatedPadding, configGui.primaryColor);

		Text.drawString(graphics, name, textX, textY, configGui.textColor);
	}

	public void registerChild(Feature child) {
		children.add(child);
	}

	public List<Feature> getChildren() {
		return children;
	}

	private static int getWidestStringWidth(ConfigGui configGui) {
		int widest = 0;

		for (String category : categories) {
			int width = Text.getStringWidth(category);
			if (width > widest) widest = width;
		}
		
		return widest;
	}
}
  