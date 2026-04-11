package me.shaweel.shaweeladdons.config;

import java.util.List;

import me.shaweel.shaweeladdons.config.widgetTypes.ConfigWidget;
import me.shaweel.shaweeladdons.config.widgets.Category;
import me.shaweel.shaweeladdons.config.widgets.Feature;
import me.shaweel.shaweeladdons.config.widgets.SwitchButton;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;

public class ConfigGui extends Screen {
	//----------------STATIC VARIABLES----------------
	//Colors
	private static final int PRIMARY_COLOR = 0xff3c093c;
	private static final int BACKGROUND_COLOR = 0xff141414;
	private static final int HOVERED_COLOR = 0xffffffff;
	private static final int TEXT_COLOR = 0xffffffff;

	//Padding
	private static final float CATEGORY_Y_PADDING = 2;
	private static final float FEATURE_Y_PADDING = 2;
	private static final float OPTION_TEXT_VERTICAL_MARGIN = 2;
	private static final float OPTION_HORIZONTAL_MARGIN = 2;
	private static final float SWITCH_VERTICAL_MARGIN = 1.5f;
	private static final float CORNER_OFFSET = 7;
	private static final float CATEGORY_X_PADDING = 15;

	//Fonts
	private static final int CATEGORY_FONT_SIZE = 12;
	private static final int FEATURE_FONT_SIZE = 9;
	private static final int OPTION_FONT_SIZE = 7;

	private static final int CATEGORY_FONT_WEIGHT = 400;
	private static final int FEATURE_FONT_WEIGHT = 500;
	private static final int OPTION_FONT_WEIGHT = 300;

	//Animations
	private static final float FEATURE_TOGGLE_ANIMATION_DURATION = 50;
	private static final float FEATURE_HOVER_ANIMATION_DURATION = 50;
	private static final float EXPANDING_ANIMATION_DURATION = 150;

	//Miscellaneous
	private static final float SWITCH_WIDTH = 20;
	private static final float FEATURE_MAX_HOVERED_OPACITY = 20;
	private static final float CATEGORY_INDICATOR_LINE_SIZE = 3;

	//Make these configurable in the future.

	//The actual class starts here... good luck future me
	private Boolean openConfig = false;

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
		Category generalCategory = new Category("General", this);
		Category dungeonsCategory = new Category("Dungeons", this);
		Category idekCategory = new Category("idek atp", this);

		Feature clickGuiFeature = new Feature("Click GUI", generalCategory);
		Feature placeholderFeature = new Feature("Placeholder", generalCategory);

		new Feature("idk", dungeonsCategory);
		new Feature("potato", dungeonsCategory);

		new Feature("potatoe", idekCategory);
		new Feature("emojis no work :(", idekCategory);

		new SwitchButton("Test", placeholderFeature);
		new SwitchButton("Test2", placeholderFeature);
		new SwitchButton("Test3", placeholderFeature);
		new SwitchButton("Test4", placeholderFeature);
		new SwitchButton("Test5", placeholderFeature);
		new SwitchButton("Test6", placeholderFeature);
		new SwitchButton("Test7", placeholderFeature);
		new SwitchButton("Test8", placeholderFeature);
		new SwitchButton("Test9", placeholderFeature);
		new SwitchButton("Test11", placeholderFeature);
		new SwitchButton("Test12", placeholderFeature);
		new SwitchButton("Test13", placeholderFeature);
		new SwitchButton("Test14", placeholderFeature);
		new SwitchButton("asdsadasdasdsa", clickGuiFeature);
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

	@Override
	public void mouseMoved(double x, double y) {
		Mouse.handleMouseMove(x, y);
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

	//Colors
	public static int getBackgroundColor() { return BACKGROUND_COLOR; }
	public static int getPrimaryColor() { return PRIMARY_COLOR; }
	public static int getTextColor() { return TEXT_COLOR; }
	public static int getHoveredColor() { return HOVERED_COLOR; }

	//Padding
	public static float getCategoryYPadding() { return CATEGORY_Y_PADDING; }
	public static float getFeatureYPadding() { return FEATURE_Y_PADDING; }
	public static float getOptionTextVerticalMargin() { return OPTION_TEXT_VERTICAL_MARGIN; }
	public static float getOptionHorizontalMargin() { return OPTION_HORIZONTAL_MARGIN; }
	public static float getSwitchVerticalMargin() { return SWITCH_VERTICAL_MARGIN; }
	public static float getCornerOffset() { return CORNER_OFFSET; }
	public static float getCategoryXPadding() { return CATEGORY_X_PADDING; }

	//Fonts
	public static int getCategoryFontSize() { return CATEGORY_FONT_SIZE; }
	public static int getFeatureFontSize() { return FEATURE_FONT_SIZE; }
	public static int getOptionFontSize() { return OPTION_FONT_SIZE; }

	public static int getCategoryFontWeight() { return CATEGORY_FONT_WEIGHT; }
	public static int getFeatureFontWeight() { return FEATURE_FONT_WEIGHT; }
	public static int getOptionFontWeight() { return OPTION_FONT_WEIGHT; }

	//Animations
	public static float getFeatureToggleAnimationDuration() { return FEATURE_TOGGLE_ANIMATION_DURATION; }
	public static float getFeatureHoverAnimationDuration() { return FEATURE_HOVER_ANIMATION_DURATION; }
	public static float getExpandingAnimationDuration() { return EXPANDING_ANIMATION_DURATION; }

	//Miscellaneous
	public static float getSwitchWidth() { return SWITCH_WIDTH; }
	public static float getFeatureMaxHoveredOpacity() { return FEATURE_MAX_HOVERED_OPACITY; }
	public static float getCategoryIndicatorLineSize() { return CATEGORY_INDICATOR_LINE_SIZE; }
}
