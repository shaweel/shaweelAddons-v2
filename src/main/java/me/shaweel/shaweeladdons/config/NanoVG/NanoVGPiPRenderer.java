package me.shaweel.shaweeladdons.config.NanoVG;


import org.joml.Matrix3x2f;
import org.lwjgl.opengl.GL33C;

import com.mojang.blaze3d.opengl.DirectStateAccess;
import com.mojang.blaze3d.opengl.GlConst;
import com.mojang.blaze3d.opengl.GlDevice;
import com.mojang.blaze3d.opengl.GlStateManager;
import com.mojang.blaze3d.opengl.GlTexture;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.GpuTexture;
import com.mojang.blaze3d.textures.GpuTextureView;
import com.mojang.blaze3d.vertex.PoseStack;

import me.shaweel.shaweeladdons.utils.Log;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.render.pip.PictureInPictureRenderer;
import net.minecraft.client.gui.render.state.pip.PictureInPictureRenderState;
import net.minecraft.client.renderer.MultiBufferSource;
import net.fabricmc.fabric.api.client.rendering.v1.SpecialGuiElementRegistry;

public class NanoVGPiPRenderer extends PictureInPictureRenderer<NanoVGPiPRenderer.NanoVGRenderState> {
	public NanoVGPiPRenderer(MultiBufferSource.BufferSource buffer) { super(buffer); }

	@Override public float getTranslateY(int height, int windowScaleFactor) { return height / 2f; }
	@Override public Class<NanoVGRenderState> getRenderStateClass() { return NanoVGRenderState.class; }
	@Override public String getTextureLabel() { return "shaweeladdons-nanovg-renderer"; }

	@Override public void renderToTexture(NanoVGRenderState state, PoseStack poseStack) {
		final GpuTextureView colorTexture = RenderSystem.outputColorTextureOverride;
		if (colorTexture == null) {
			Log.error("Failed to get RenderSystem.outputColorTextureOverride");
			return;
		}
		
		final GlDevice device = RenderSystem.getDevice() instanceof GlDevice d ? d : null;
		if (device == null) {
			Log.error("Failed to get RenderSystem.device()");
			return;
		}

		final DirectStateAccess bufferManager = device.directStateAccess();
		if (bufferManager == null) {
			Log.error("Failed to get device.directStateAccess()");
			return;
		}

		final GpuTexture rawDepth = RenderSystem.outputDepthTextureOverride != null
		 ? RenderSystem.outputDepthTextureOverride.texture() : null;

		if (!(rawDepth instanceof GlTexture glDepthTexture)) {
			Log.error("Failed to get the GL depth texture");
			return;
		};

		final int width = colorTexture.getWidth(0);
		final int height = colorTexture.getHeight(0);

		final GpuTexture rawColor = colorTexture.texture();
		if (!(rawColor instanceof GlTexture glColorTexture)) {
			Log.error("Failed to get the GL color texture");
			return;
		}

		final int framebuffer = glColorTexture.getFbo(bufferManager, glDepthTexture);
		if (framebuffer != 0) {
			GlStateManager._glBindFramebuffer(GlConst.GL_FRAMEBUFFER, framebuffer);
			GlStateManager._viewport(0, 0, width, height);
		} else {
			Log.error("Failed to get framebuffer");
		}

		GL33C.glBindSampler(0, 0);
		NanoVGRenderer.beginFrame();
		state.render();
		NanoVGRenderer.endFrame();

		GlStateManager._disableDepthTest();
		GlStateManager._disableCull();
		GlStateManager._enableBlend();
		GlStateManager._blendFuncSeparate(GlConst.GL_SRC_ALPHA, GlConst.GL_ONE_MINUS_SRC_ALPHA, GlConst.GL_ONE, GlConst.GL_ZERO);
	}

	public static void initialize() {
		SpecialGuiElementRegistry.register(context -> new NanoVGPiPRenderer(context.vertexConsumers()));
	}

	public record NanoVGRenderState(int x, int y, int width, int height,
	ScreenRectangle scissor, ScreenRectangle boundsRectangle, Runnable content) 
	implements PictureInPictureRenderState {
		@Override public float scale() { return 1f; }
		@Override public int x0() { return x; }
		@Override public int y0() { return y; }
		@Override public int x1() { return x + width; }
		@Override public int y1() { return y + height; }
		@Override public ScreenRectangle scissorArea() { return scissor; }
		@Override public ScreenRectangle bounds() { return boundsRectangle; }
		public void render() { content.run(); }
	}

	public static void drawNanoVG(GuiGraphics guiGraphics, Runnable content) {		
		final int width = guiGraphics.guiWidth();
		final int height = guiGraphics.guiHeight();

		final ScreenRectangle scissor = guiGraphics.scissorStack.peek();
		final Matrix3x2f pose = new Matrix3x2f(guiGraphics.pose());
		ScreenRectangle boundsRectangle = new ScreenRectangle(0, 0, width, height).transformMaxBounds(pose);

		if (boundsRectangle.width() <= 0 || boundsRectangle.height() <= 0) { boundsRectangle = null; }
		if (scissor != null) { boundsRectangle = scissor.intersection(boundsRectangle); }

		final NanoVGRenderState state = new NanoVGRenderState(0, 0, width, height, scissor, boundsRectangle, content);
		guiGraphics.guiRenderState.submitPicturesInPictureState(state);
	}
}
