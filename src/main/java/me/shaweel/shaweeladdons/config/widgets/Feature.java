package me.shaweel.shaweeladdons.config.widgets;

import me.shaweel.shaweeladdons.config.ConfigGui;
import me.shaweel.shaweeladdons.config.NanoVG.NanoVGRenderer;

public class Feature {
	private String name;
	private Category parent;

	private static final int fontSize = 7;
	private static final int fontWeight = 700;
	private static final float seperatorSize = 0.5f;
	private static final float seperatorPadding = 3f;

	private float seperatorMinX;
	private float seperatorMaxX;
	private float seperatorMinY;
	private float seperatorMaxY;

	private float squareMinX;
	private float squareMaxX;
	private float squareMinY;
	private float squareMaxY;

	private float textX;
	private float textY;

	public Feature(String name, Category parent) {
		this.name = name;
		this.parent = parent;

		this.parent.registerChild(this);
	}

	public void render(ConfigGui configGui, float y) {
		this.seperatorMinY = y;
		this.seperatorMaxY = y + seperatorSize;

		this.squareMinX = parent.getSquareMinX();
		this.squareMaxX = parent.getSquareMaxX();
		this.squareMinY = seperatorMaxY;
		this.squareMaxY = seperatorMaxY + this.parent.getYPadding()*2 + fontSize;

		this.seperatorMinX = squareMinX + seperatorPadding;
		this.seperatorMaxX = squareMaxX - seperatorPadding;

		this.textX = (this.squareMaxX+this.squareMinX)/2 - NanoVGRenderer.getStringWidth(this.name, fontSize, fontWeight)/2;
		this.textY = y + this.parent.getYPadding();

		NanoVGRenderer.drawRect(this.seperatorMinX, this.seperatorMinY, this.seperatorMaxX, this.seperatorMaxY, 
			configGui.seperatorColor);
		NanoVGRenderer.drawRect(this.squareMinX, this.squareMinY, this.squareMaxX, this.squareMaxY, configGui.backgroundColor);
		NanoVGRenderer.drawString(this.name, this.textX, this.textY, fontSize, fontWeight, configGui.textColor);
	}

	public float getSquareMinX() {
		return squareMinX;
	}

	public float getSquareMaxX() {
		return squareMaxX;
	}

	public float getSquareMinY() {
		return squareMinY;
	}

	public float getSquareMaxY() {
		return squareMaxY;
	}

	public float getTextX() {
		return textX;
	}

	public float getTextY() {
		return textY;
	}

	public String getName() {
		return name;
	}
}
