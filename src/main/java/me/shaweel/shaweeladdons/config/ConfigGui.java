package me.shaweel.shaweeladdons.config;

import me.shaweel.shaweeladdons.config.widgets.Category;
import me.shaweel.shaweeladdons.config.widgets.Feature;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;

public class ConfigGui extends Screen {
	public final int primaryColor = 0xff3c093c;
	public final int backgroundColor = 0xff141414;
	public final int textColor = 0xffffffff;

	private boolean openConfig = false;

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
		this.generalCategory = new Category(this, "General");
		this.dungeonsCategory = new Category(this, "Dungeons");
		this.idekCategory = new Category(this, "Idek atp");

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
}
