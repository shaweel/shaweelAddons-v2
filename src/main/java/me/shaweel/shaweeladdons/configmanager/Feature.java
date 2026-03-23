package me.shaweel.shaweeladdons.configmanager;


import me.shaweel.shaweeladdons.utils.Text;
import net.minecraft.client.gui.GuiGraphics;

public class Feature {
	private String name;
	private Category parent;

	public Feature(ConfigGui configGui, GuiGraphics graphics, String name, Category parent) {
		this.name = name;
		this.parent = parent;
		parent.registerChild(this);
	}

	public void render(ConfigGui configGui, GuiGraphics graphics, int y) {
		graphics.fill(parent.squareMinX, y, parent.squareMaxX, y+parent.yPadding*2+configGui.lineHeight, configGui.backgroundColor);
		Text.drawString(graphics, this.name, parent.textX, y+parent.yPadding, configGui.textColor);
	}
}
