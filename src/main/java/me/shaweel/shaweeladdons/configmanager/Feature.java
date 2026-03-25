package me.shaweel.shaweeladdons.configmanager;

import me.shaweel.shaweeladdons.utils.Text;
import net.minecraft.client.gui.GuiGraphics;

public class Feature {
	private String name;
	private Category parent;

	private static final int fontSize = 7;
	private static final float fontWeight = 2.0f;

	private int squareMinX;
	private int squareMaxX;
	private int squareMinY;
	private int squareMaxY;

	private int textX;
	private int textY;

	public Feature(String name, Category parent) {
		this.name = name;
		this.parent = parent;

		this.parent.registerChild(this);
	}

	public void render(ConfigGui configGui, GuiGraphics graphics, int y) {
		this.squareMinX = parent.getSquareMinX();
		this.squareMaxX = parent.getSquareMaxX();
		this.squareMinY = y;
		this.squareMaxY = y + this.parent.getYPadding()*2 + fontSize;

		this.textX = (this.squareMaxX+this.squareMinX)/2 - Text.getStringWidth(this.name, fontSize, fontWeight)/2;
		this.textY = y + this.parent.getYPadding();

		graphics.fill(this.squareMinX, this.squareMinY, this.squareMaxX, this.squareMaxY, configGui.backgroundColor);
		Text.drawString(graphics, this.name, fontSize, fontWeight, this.textX, this.textY, configGui.textColor);
	}

	public int getSquareMinX() {
		return squareMinX;
	}

	public int getSquareMaxX() {
		return squareMaxX;
	}

	public int getSquareMinY() {
		return squareMinY;
	}

	public int getSquareMaxY() {
		return squareMaxY;
	}

	public int getTextX() {
		return textX;
	}

	public int getTextY() {
		return textY;
	}

	public String getName() {
		return name;
	}
}
