package me.shaweel.shaweeladdons.config;

import me.shaweel.shaweeladdons.config.widgets.Category;
import me.shaweel.shaweeladdons.config.widgets.Feature;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;

public class ConfigGui extends Screen {
	public static ConfigGui instance;

	public final int primaryColor = 0xffffcfda;
	public final int backgroundColor = 0xff141414;
	public final int seperatorColor = 0xff2a2a2a;
	public final int textColor = 0xffffffff;

	private Category generalCategory;
	private Category dungeonsCategory;
	private Category idekCategory;

	public ConfigGui() {
		super(Component.literal("shaweelAddonsConfigGui"));
	}

	@Override
	protected void init() {
		super.init();
		
		Category.clearCategories();
		this.generalCategory = new Category("General");
		this.dungeonsCategory = new Category("Dungeons");
		this.idekCategory = new Category("Idek atp");

		new Feature("potato1", this.generalCategory);
		new Feature("potato2", this.generalCategory);

		new Feature("potato3", this.dungeonsCategory);
		new Feature("potato4", this.dungeonsCategory);

		new Feature("potato5", this.idekCategory);
		new Feature("potato6", this.idekCategory);
	}

	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
		guiGraphics.fill(0, 0, this.width, this.height, 0x00000000);
		Category.renderAll(this, guiGraphics);
		super.render(guiGraphics, mouseX, mouseY, delta);
	}

	@Override
	public boolean mouseClicked(MouseButtonEvent event, boolean consumed) {
		consumed = Mouse.handleMouseClick(this, event, consumed);
		return super.mouseClicked(event, consumed);
	}
}
