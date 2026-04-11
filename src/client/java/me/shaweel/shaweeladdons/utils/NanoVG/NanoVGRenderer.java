package me.shaweel.shaweeladdons.utils.NanoVG;

import static org.lwjgl.nanovg.NanoVG.*;
import static org.lwjgl.nanovg.NanoVGGL3.*;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Set;
import org.lwjgl.BufferUtils;
import org.lwjgl.nanovg.NVGColor;

import com.mojang.blaze3d.platform.Window;

import me.shaweel.shaweeladdons.utils.Log;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;

public class NanoVGRenderer {
	private static long vg;
	private static NVGColor nvgColor = NVGColor.malloc();
	private static final Set<Integer> validWeights = Set.of(100, 200, 300, 400, 500, 600, 700, 800, 900);
	private static int[] notoSerif = new int[9];

	private static int loadttf(String ttf) {
		try (InputStream stream = Minecraft.getInstance().getResourceManager()
		.getResource(Identifier.parse(String.format("shaweeladdons:fonts/%s.ttf", ttf))).get().open()) {
			byte[] bytes = stream.readAllBytes();
			ByteBuffer byteBuffer = BufferUtils.createByteBuffer(bytes.length);
			byteBuffer.put(bytes).flip();

			final int font = nvgCreateFontMem(vg, ttf, byteBuffer, true);
			if (font == -1) Log.error(String.format("Failed to load the %s font", ttf));
			return font;
		} catch (Exception exception) {
			Log.error(String.format("Failed to read the %s.ttf file. Exception: %s", ttf, exception.getMessage()));
			return -1;
		}
	}

	static {
		vg = nvgCreate(NVG_ANTIALIAS | NVG_STENCIL_STROKES);
		if (vg == -1L || vg == 0L) Log.error("Failed to initialize NanoVG");

		notoSerif[0] = loadttf("noto_serif/100");
		notoSerif[1] = loadttf("noto_serif/200");
		notoSerif[2] = loadttf("noto_serif/300");
		notoSerif[3] = loadttf("noto_serif/400");
		notoSerif[4] = loadttf("noto_serif/500");
		notoSerif[5] = loadttf("noto_serif/600");
		notoSerif[6] = loadttf("noto_serif/700");
		notoSerif[7] = loadttf("noto_serif/800");
		notoSerif[8] = loadttf("noto_serif/900");
	}
	
	public static void beginFrame() {
		final Window window = Minecraft.getInstance().getWindow();
		final float guiScale = window.getGuiScale();
		final int width = window.getWidth();
		final int height = window.getHeight();

		nvgBeginFrame(vg, width/guiScale, height/guiScale, guiScale);
		nvgTextAlign(vg, NVG_ALIGN_LEFT | NVG_ALIGN_TOP);
	}

	public static void endFrame() {
		nvgEndFrame(vg);
	}

	public static void applyScissor(float minX, float minY, float maxX, float maxY) {
		final WidthHeight widthHeight = getWidthAndHeight(minX, minY, maxX, maxY);
		final float width = widthHeight.width;
		final float height = widthHeight.height;

		nvgScissor(vg, minX, minY, width, height);
	}

	public static void resetScissor() {
		nvgResetScissor(vg);
	}

	public record WidthHeight(float width, float height) {}

	private static WidthHeight getWidthAndHeight(float minX, float minY, float maxX, float maxY) {
		if (maxX < minX) {
			Log.error(String.format("maxX cannot be smaller than minX (%s < %s)", maxX, minX));
			return new WidthHeight(0, 0);
		}
		if (maxY < minY) {
			Log.error(String.format("maxY cannot be smaller than minY (%s < %s)", maxY, minY));
			return new WidthHeight(0, 0);
		}
		final float width = maxX-minX;
		final float height = maxY-minY;

		return new WidthHeight(width, height);
	}

	private static void applyColor(int color) {
		final float a = ((color >> 24) & 0xFF) / 255f;
		final float r = ((color >> 16) & 0xFF) / 255f;
		final float g = ((color >>  8) & 0xFF) / 255f;
		final float b = ((color      ) & 0xFF) / 255f;
		nvgRGBAf(r, g, b, a, nvgColor);
	}

	public static void drawRectangle(float minX, float minY, float maxX, float maxY, int color) {
		final WidthHeight widthHeight = getWidthAndHeight(minX, minY, maxX, maxY);
		final float width = widthHeight.width;
		final float height = widthHeight.height;
		
		applyColor(color);
		nvgBeginPath(vg);
		nvgRect(vg, minX, minY, width, height);
		nvgFillColor(vg, nvgColor);
		nvgFill(vg);
		nvgClosePath(vg);
	}

	public static void drawRectangle(float minX, float minY, float maxX, float maxY, float radius, int color) {
		final WidthHeight widthHeight = getWidthAndHeight(minX, minY, maxX, maxY);
		final float width = widthHeight.width;
		final float height = widthHeight.height;
		
		applyColor(color);
		nvgBeginPath(vg);
		nvgRoundedRect(vg, minX, minY, width, height, radius);
		nvgFillColor(vg, nvgColor);
		nvgFill(vg);
		nvgClosePath(vg);
	}

	public static void drawRectangle(float minX, float minY, float maxX, float maxY, 
		float radiusTopLeft, float radiusTopRight, float radiusBottomLeft, float radiusBottomRight, int color) {
		final WidthHeight widthHeight = getWidthAndHeight(minX, minY, maxX, maxY);
		final float width = widthHeight.width;
		final float height = widthHeight.height;
		
		applyColor(color);
		nvgBeginPath(vg);
		nvgRoundedRectVarying(vg, minX, minY, width, height, radiusTopLeft, radiusTopRight, radiusBottomRight, radiusBottomLeft);
		nvgFillColor(vg, nvgColor);
		nvgFill(vg);
		nvgClosePath(vg);
	}

	public static void drawCircle(float x, float y, float r, int color) {
		x += r;
		y += r;
		
		applyColor(color);
		nvgBeginPath(vg);
		nvgCircle(vg, x, y, r);
		nvgFillColor(vg, nvgColor);
		nvgFill(vg);
		nvgClosePath(vg);
	}

	public static void drawString(String string, float x, float y, int size, int weight, int color) {
		if (!validWeights.contains(weight)) {
			Log.error(String.format("Invalid weight: %s, valid weights: %s", weight, validWeights));
			return;
		}
		
		nvgFontSize(vg, size);
		nvgFontFaceId(vg, notoSerif[weight/100-1]);
		applyColor(color);
		nvgFillColor(vg, nvgColor);
		nvgText(vg, x, y, string);
	}

	public static float getStringWidth(String string, int size, int weight) {		
		if (!validWeights.contains(weight)) {
			Log.error(String.format("Invalid weight: %s, valid weights: %s", weight, validWeights));
			return 0;
		}

		nvgFontSize(vg, size);
		nvgFontFaceId(vg, notoSerif[weight/100-1]);
		float[] bounds = new float[4];
		nvgTextBounds(vg, 0, 0, string, bounds);
		
		return bounds[2] - bounds[0];
	}
}