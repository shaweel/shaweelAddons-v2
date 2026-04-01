package me.shaweel.shaweeladdons.config;

import java.util.List;

import me.shaweel.shaweeladdons.config.widgets.Category;
import me.shaweel.shaweeladdons.config.widgets.Feature;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;

public class ConfigGui extends Screen {
	private static final int PRIMARY_COLOR = 0xff3c093c;
	private static final int BACKGROUND_COLOR = 0xff141414;
	private static final int TEXT_COLOR = 0xffffffff;

	private static final float X_PADDING = 15;
	private static final float Y_PADDING = 3;

	private Boolean openConfig = false;

	private Category generalCategory;
	private Category dungeonsCategory;
	private Category idekCategory;

	public ConfigGui() {
		super(Component.literal("shaweelAddonsConfigGui"));

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (!this.openConfig || client.screen != null) return;
			this.openConfig = false;
			client.setScreen(this);
		});
	}

	public void open() {
		this.openConfig = true;
	}

	@Override
	protected void init() {
		super.init();
		
		Category.clearCategories();
		this.generalCategory = new Category("General", this);
		this.dungeonsCategory = new Category("Dungeons", this);
		this.idekCategory = new Category("idek atp", this);

		new Feature("Click GUI", this.generalCategory);
		new Feature("Placeholder", this.generalCategory);

		new Feature("idk", this.dungeonsCategory);
		new Feature("potato", this.dungeonsCategory);

		new Feature("potatoe", this.idekCategory);
		new Feature("emojis no work :(", this.idekCategory);
	}

	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
		guiGraphics.fill(0, 0, this.width, this.height, 0x00000000);
		Category.renderAllCategories(guiGraphics);
		super.render(guiGraphics, mouseX, mouseY, delta);
	}

	@Override
	public boolean mouseClicked(MouseButtonEvent event, boolean consumed) {
		consumed = Mouse.handleMouseClick(this, event, consumed);
		return super.mouseClicked(event, consumed);
	}

	public float getWidestContentWidth() {
		return getWidestContentWidth(Category.getAllCategories());
	}

	private float getWidestContentWidth(List<? extends ConfigWidget<?, ?>> widgets) {
		if (widgets == null) {
			return Float.NEGATIVE_INFINITY;
		}
		
		float widest = 0;

		for (ConfigWidget<?, ?> widget : widgets) {
			float width = Math.max(widget.getContentWidth(), getWidestContentWidth(widget.getChildren()));
			if (width > widest) {
				widest = width;
			}
		}

		return widest;
	}

	public static float getXPadding() {
		return X_PADDING;
	}

	public static float getYPadding() {
		return Y_PADDING;
	}

	public static int getBackgroundColor() {
		return BACKGROUND_COLOR;
	}

	public static int getPrimaryColor() {
		return PRIMARY_COLOR;
	}

	public static int getTextColor() {
		return TEXT_COLOR;
	}
}
