package me.shaweel.shaweeladdons.utils;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import com.mojang.blaze3d.platform.NativeImage;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.resources.ResourceLocation;

public class Text {
	private static java.awt.Font getAdwaitaFont() {
		final InputStream fontInputStream = Text.class.getResourceAsStream("/assets/shaweeladdons/adwaitasans.ttf");
		try {
			return java.awt.Font.createFont(Font.TRUETYPE_FONT, fontInputStream);
		} catch (Exception exception) {
			Log.error(String.format("Failed to load font: %s", exception.getMessage()));
		}
		return null;
	}

	private static final java.awt.Font baseFont = getAdwaitaFont();
	private static final int scale = 8;

	private static HashMap<String, ResourceLocation> textureCache = new HashMap<String, ResourceLocation>();

	/**
	 * Draws a shadow-less string with the Adwaita Sans font
	 * @param graphics {@code GuiGraphics}
	 * @param text {@code String}
	 * @param size {@code int}
	 * @param weight {@code float}
	 * @param x {@code int} starts left
	 * @param y {@code int} starts top
	 * @param color {@code int}
	 */
	public static void drawString(GuiGraphics graphics, String text, int size, float weight, int x, int y, int color) {
		final java.awt.Font font = baseFont.deriveFont(Map.of(TextAttribute.SIZE, size*scale, TextAttribute.WEIGHT, weight));

		final BufferedImage dummyBufferedImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
		Graphics2D measure = dummyBufferedImage.createGraphics();
		measure.setFont(font);

		FontMetrics fontMetrics = measure.getFontMetrics();
		int width = fontMetrics.stringWidth(text);
		int height = fontMetrics.getHeight();
		measure.dispose();

		String key = String.format("string_%s_%s_%s", size, weight, text.hashCode());

		ResourceLocation cachedResourceLocation = textureCache.get(key);
		if (cachedResourceLocation != null) {
			graphics.pose().pushMatrix();
			graphics.pose().translate(x, y);
			Log.debug(String.format("%s", 1.0f/scale));
			graphics.pose().scale(1.0f/scale, 1.0f/scale);
			graphics.blit(RenderPipelines.GUI_TEXTURED, cachedResourceLocation, 0, 0, 0, 0, width, height, width, height);
			graphics.pose().popMatrix();
			return;
		}

		BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D graphics2D = bufferedImage.createGraphics();
		graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		graphics2D.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		graphics2D.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
		graphics2D.setFont(font);
		graphics2D.setColor(new Color(color, true));
		graphics2D.drawString(text, 0, fontMetrics.getAscent());
		graphics2D.dispose();

		int[] pixels = bufferedImage.getRGB(0, 0, width, height, null, 0, width);
		NativeImage nativeImage = new NativeImage(NativeImage.Format.RGBA, width, height, false);
		for (int nativeImageY = 0; nativeImageY < height; nativeImageY++) {
			for (int nativeImageX = 0; nativeImageX < width; nativeImageX++) {
				int argb = pixels[nativeImageY * width + nativeImageX];
				int alpha = (argb >>> 24) & 0xFF;
				int red = (argb >> 16) & 0xFF;
				int green = (argb >> 8) & 0xFF;
				int blue = argb & 0xFF;
				nativeImage.setPixelABGR(nativeImageX, nativeImageY, (alpha << 24) | (blue << 16) | (green << 8) | red);
			}
		}

		try {
			File testFile = new File("/home/shaweel/Documents/shaweelAddons-v2/native.png");
			nativeImage.writeToFile(testFile);
		} catch (Exception e) {
			Log.error("fuck my life");
		}

		DynamicTexture texture = new DynamicTexture(() -> "shaweeladdons_" + key, nativeImage);
		ResourceLocation resourceLocation = ResourceLocation.fromNamespaceAndPath("shaweeladdons", key);
		Minecraft.getInstance().getTextureManager().register(resourceLocation, texture);

		graphics.pose().pushMatrix();
		graphics.pose().translate(x, y);
		graphics.pose().scale(1/scale, 1/scale);
		graphics.blit(RenderPipelines.GUI_TEXTURED, resourceLocation, 0, 0, 0, 0, width, height, width, height);
		graphics.pose().popMatrix();
		textureCache.put(key, resourceLocation);
	}

	/**
	 * Returns the width of a string with the Adwaita Sans font
	 * @param text {@code String} 
	 * @param size {@code int}
	 * @param weight {@code float}
	 */
	public static int getStringWidth(String text, int size, float weight) {
		final java.awt.Font font = baseFont.deriveFont(Map.of(TextAttribute.SIZE, size, TextAttribute.WEIGHT, weight));

		final BufferedImage dummyBufferedImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
		Graphics2D measure = dummyBufferedImage.createGraphics();
		measure.setFont(font);

		FontMetrics fontMetrics = measure.getFontMetrics();
		int width = fontMetrics.stringWidth(text);
		measure.dispose();		
		
		return width;
	}
}
