package me.shaweel.shaweeladdons.utils;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FontDescription;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;

public class Text {
	private static final Font font = Minecraft.getInstance().font;
	private static final float baseTextSize = 9.0f;

	/**
	 * Draws a shadow-less string with the Adwaita Sans font with 700 weight
	 * @param graphics {@code GuiGraphics}
	 * @param text {@code String}
	 * @param x {@code int} starts left
	 * @param y {@code int} starts top
	 * @param color {@code int}
	 */
	public static void drawString(GuiGraphics graphics, String text, float size, int x, int y, int color) {
		final ResourceLocation ttfResource = ResourceLocation.fromNamespaceAndPath("shaweeladdons", "adwaitasans");
		final FontDescription fontDescription = new FontDescription.Resource(ttfResource);
		Component component = Component.literal(text).withStyle(Style.EMPTY.withFont(fontDescription));

		float scale = size / baseTextSize;

		graphics.pose().pushMatrix();
		graphics.pose().translate(x, y);
		graphics.pose().scale(scale, scale);
		graphics.drawString(font, component, 0, 0, color, false);
		graphics.pose().popMatrix();
	}

	/**
	 * Returns the width of a string with the Adwaita Sans font with 700 weight
	 */
	public static int getStringWidth(String text, float size) {
		final ResourceLocation ttfResource = ResourceLocation.fromNamespaceAndPath("shaweeladdons", "adwaitasans");
		final FontDescription fontDescription = new FontDescription.Resource(ttfResource);
		Component categoryText = Component.literal(text).withStyle(Style.EMPTY.withFont(fontDescription));
		return font.width(categoryText);
	}
}
