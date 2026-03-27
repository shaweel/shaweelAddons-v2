package me.shaweel.shaweeladdons.config.NanoVG;

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
import net.minecraft.resources.ResourceLocation;

public class NanoVGRenderer {
	private static long vg;
	private static NVGColor nvgColor = NVGColor.malloc();
	private static final Set<Integer> validWeights = Set.of(100, 200, 300, 400, 500, 600, 700, 800, 900);
	private static int[] adwaitaSans = new int[9];

	private static int loadttf(String ttf) {
		try (InputStream stream = Minecraft.getInstance().getResourceManager()
		.getResource(ResourceLocation.parse(String.format("shaweeladdons:fonts/%s.ttf", ttf))).get().open()) {
			byte[] bytes = stream.readAllBytes();
			ByteBuffer byteBuffer = BufferUtils.createByteBuffer(bytes.length);
			byteBuffer.put(bytes).flip();
			final int font = nvgCreateFontMem(vg, ttf, byteBuffer, true);
			if (font == -1) Log.error(String.format("Failed to load the %s font", ttf));
			return font;
		} catch (Exception exception) {
			Log.error(String.format("Failed to read the %s.ttf file. Exception: ", ttf, exception.getMessage()));
			return -1;
		}
	}

	static {
		vg = nvgCreate(NVG_ANTIALIAS | NVG_STENCIL_STROKES);
		if (vg == -1L || vg == 0L) Log.error("Failed to initialize NanoVG");

		adwaitaSans[0] = loadttf("adwaitasans-100");
		adwaitaSans[1] = loadttf("adwaitasans-200");
		adwaitaSans[2] = loadttf("adwaitasans-300");
		adwaitaSans[3] = loadttf("adwaitasans-400");
		adwaitaSans[4] = loadttf("adwaitasans-500");
		adwaitaSans[5] = loadttf("adwaitasans-600");
		adwaitaSans[6] = loadttf("adwaitasans-700");
		adwaitaSans[7] = loadttf("adwaitasans-800");
		adwaitaSans[8] = loadttf("adwaitasans-900");
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

	private static void applyColor(int color) {
		final float a = ((color >> 24) & 0xFF) / 255f;
		final float r = ((color >> 16) & 0xFF) / 255f;
		final float g = ((color >>  8) & 0xFF) / 255f;
		final float b = ((color      ) & 0xFF) / 255f;
		nvgRGBAf(r, g, b, a, nvgColor);
	}

	public static void drawRect(float minX, float minY, float maxX, float maxY, int color) {
		if (maxX < minX) {
			Log.error("maxX cannot be smaller than minX");
		}
		if (maxY < minY) {
			Log.error("maxY cannot be smaller than minY");
		}
		final float width = maxX-minX;
		final float height = maxY-minY;
		
		applyColor(color);
		nvgBeginPath(vg);
		nvgRect(vg, minX, minY, width, height);
		nvgFillColor(vg, nvgColor);
		nvgFill(vg);
	}

	public static void drawString(String string, float x, float y, int size, int weight, int color) {
		if (!validWeights.contains(weight)) {
			Log.error(String.format("Invalid weight: %s, valid weights: %s", weight, validWeights));
			return;
		}
		
		nvgFontSize(vg, size);
		nvgFontFaceId(vg, adwaitaSans[weight/100-1]);
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
		nvgFontFaceId(vg, adwaitaSans[weight/100-1]);
		float[] bounds = new float[4];
		nvgTextBounds(vg, 0, 0, string, bounds);
		return bounds[2] - bounds[0];
	}
}