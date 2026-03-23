package me.shaweel.shaweeladdons.configmanager;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class ConfigGui extends Screen {
	public static ConfigGui instance;

	public final int primaryColor = 0xffffcfda;
	public final int backgroundColor = 0xff141414;
	public final int textColor = 0xffffffff;
	public final int lineHeight = 9;

	public final Font font = Minecraft.getInstance().font;

	public ConfigGui() {
		super(Component.literal("shaweelAddonsConfigGui"));
	}

	@Override
	protected void init() {
		super.init();
	}

	@Override
	public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
		graphics.fill(0, 0, this.width, this.height, 0x00000000);
		Category generalCategory = new Category(this, graphics, "General");
		new Feature(this, graphics, "Feature1", generalCategory);
		new Category(this, graphics, "Dungeons");
		new Category(this, graphics, "Idek atp");
		super.render(graphics, mouseX, mouseY, delta);
	}
}
