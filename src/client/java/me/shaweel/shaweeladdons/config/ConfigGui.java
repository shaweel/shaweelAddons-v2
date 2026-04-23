package me.shaweel.shaweeladdons.config;

import java.util.List;

import me.shaweel.shaweeladdons.config.widgetTypes.ConfigWidget;
import me.shaweel.shaweeladdons.config.widgets.Button;
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
	//----------Colors----------
	private static int PRIMARY_COLOR = 0xff3c093c;
	private static int BACKGROUND_COLOR = 0xff141414;
	private static int SECONDARY_BACKGROUND_COLOR = 0xff202020;
	private static int HOVERED_COLOR = 0xffffffff;
	private static int TEXT_COLOR = 0xffd0d0d0;

	//----------Padding----------
	//General
	private static float CORNER_X_OFFSET = 7; //The amount of space between the absolute top of the screen and the first Category
	private static float CORNER_Y_OFFSET = 7; //The amount of space between the absolute left of the screen and the first Category

	//Category
	private static float CATEGORY_Y_MARGIN = 3.5f; //The amount of space between the Category's name and the Category's top most and bottom most point
	private static float CATEGORY_X_MARGIN = 2; //The minimum amount of space between the Category's name and the Category's left most and right most point
	private static float CATEGORY_X_PADDING = 15; //The amount of horizontal space between individual Categories

	//Feature
	private static float FEATURE_Y_MARGIN = 3; //The amount of space between the Feature's name and the Feature's top most and bottom most point
	private static float FEATURE_X_MARGIN = 2; //The minimum amount of space between the Feature's name and the Feature's left most and right most point

	//SwitchButton
	private static float SWITCH_SQUARE_PADDING = 1; //The amount of space between the actual switch's rectangle and the square inside that rectangle
	private static float SWITCH_TEXT_PADDING = 2; //The minimum amount of space between the SwitchButton's name and the actual clickable switch next to the name
	private static float SWITCH_Y_MARGIN = 1.5f; //The amount of space between the SwitchButton's name and the SwitchButton container(row inside the Feature)'s top most and bottom most point
	private static float SWITCH_X_MARGIN = 2; //The amount of space between the SwitchButton's content(name and the clickable switch itself) and the SwitchButton container(row inside the Feature)'s left most and right most point

	//Button
	private static float BUTTON_Y_MARGIN = 1; //The amount of space between the actual button and the button container(row inside the Feature)'s top most and bottom most point
	private static float BUTTON_X_MARGIN = 2; //The amount of space between the actual button and the button container(row inside the Feature)'s left most and right most point

	//----------Fonts----------
	private static int CATEGORY_FONT_SIZE = 12;
	private static int FEATURE_FONT_SIZE = 9;
	private static int OPTION_FONT_SIZE = 7;

	private static int CATEGORY_FONT_WEIGHT = 400;
	private static int FEATURE_FONT_WEIGHT = 500;
	private static int OPTION_FONT_WEIGHT = 400;

	//----------Animations----------
	private static float TOGGLE_ANIMATION_DURATION = 50;
	private static float HOVER_ANIMATION_DURATION = 50;
	private static float EXPANDING_ANIMATION_DURATION = 150;

	//----------Miscellaneous----------
	private static float SWITCH_WIDTH = 18;
	private static float MAX_HOVERED_OPACITY = 20;
	private static float CATEGORY_INDICATOR_LINE_SIZE = 2;

	//TODO: Make these configurable in the future.

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

		new Feature("Click GUI", generalCategory);
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
		new Button("Test9", placeholderFeature);
		new Button("Test11", placeholderFeature);
		new Button("Test12", placeholderFeature);
		new Button("Test13", placeholderFeature);
		new Button("Test14", placeholderFeature);
		new Button("oogaboogaomaousdhuojasdo", placeholderFeature);
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

	//----------------GETTER FUNCTIONS----------------
	//----------Colors----------
	public static int getPrimaryColor() { return PRIMARY_COLOR; }
	public static int getBackgroundColor() { return BACKGROUND_COLOR; }
	public static int getSecondaryBackgroundColor() { return SECONDARY_BACKGROUND_COLOR; }
	public static int getHoveredColor() { return HOVERED_COLOR; }
	public static int getTextColor() { return TEXT_COLOR; }

	//----------Padding----------
	//General
	public static float getCornerXOffset() { return CORNER_X_OFFSET; }
	public static float getCornerYOffset() { return CORNER_Y_OFFSET; }

	//Category
	public static float getCategoryYMargin() { return CATEGORY_Y_MARGIN; }
	public static float getCategoryXMargin() { return CATEGORY_X_MARGIN; }
	public static float getCategoryXPadding() { return CATEGORY_X_PADDING; }

	//Feature
	public static float getFeatureYMargin() { return FEATURE_Y_MARGIN; }
	public static float getFeatureXMargin() { return FEATURE_X_MARGIN; }

	//SwitchButton
	public static float getSwitchSquarePadding() { return SWITCH_SQUARE_PADDING; }
	public static float getSwitchTextPadding() { return SWITCH_TEXT_PADDING; }
	public static float getSwitchYMargin() { return SWITCH_Y_MARGIN; }
	public static float getSwitchXMargin() { return SWITCH_X_MARGIN; }

	//Button
	public static float getButtonYMargin() { return BUTTON_Y_MARGIN; }
	public static float getButtonXMargin() { return BUTTON_X_MARGIN; }

	//----------Fonts----------
	public static int getCategoryFontSize() { return CATEGORY_FONT_SIZE; }
	public static int getFeatureFontSize() { return FEATURE_FONT_SIZE; }
	public static int getOptionFontSize() { return OPTION_FONT_SIZE; }

	public static int getCategoryFontWeight() { return CATEGORY_FONT_WEIGHT; }
	public static int getFeatureFontWeight() { return FEATURE_FONT_WEIGHT; }
	public static int getOptionFontWeight() { return OPTION_FONT_WEIGHT; }

	//----------Animations----------
	public static float getToggleAnimationDuration() { return TOGGLE_ANIMATION_DURATION; }
	public static float getHoverAnimationDuration() { return HOVER_ANIMATION_DURATION; }
	public static float getExpandingAnimationDuration() { return EXPANDING_ANIMATION_DURATION; }

	//----------Miscellaneous----------
	public static float getSwitchWidth() { return SWITCH_WIDTH; }
	public static float getMaxHoveredOpacity() { return MAX_HOVERED_OPACITY; }
	public static float getCategoryIndicatorLineSize() { return CATEGORY_INDICATOR_LINE_SIZE; }
}
