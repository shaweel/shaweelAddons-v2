package me.shaweel.shaweeladdons.config.widgets;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.lwjgl.glfw.GLFW;

import me.shaweel.shaweeladdons.config.ConfigFile;
import me.shaweel.shaweeladdons.config.ConfigGui;
import me.shaweel.shaweeladdons.config.widgetTypes.ConfigWidget;
import me.shaweel.shaweeladdons.config.widgetTypes.ExpandableConfigWidget;
import me.shaweel.shaweeladdons.utils.Log;
import me.shaweel.shaweeladdons.utils.Animation;
import me.shaweel.shaweeladdons.utils.NanoVG.NanoVGPiPRenderer;
import me.shaweel.shaweeladdons.utils.NanoVG.NanoVGRenderer;
import net.minecraft.client.gui.GuiGraphics;

public class Category implements ConfigWidget<ConfigGui, Void>, ExpandableConfigWidget {
	private static List<Category> categories = new ArrayList<>();
	public List<Feature> children = new ArrayList<>();

	private final ConfigGui parent;
	private final String name;
	private int id;

	private float minX;
	private float maxX;
	private float minY;
	private float maxY;

	private float textX;
	private float textY;

	private float lowestPoint = Float.POSITIVE_INFINITY;

	private Animation expandingAnimation = new Animation(0, 0, 0, null);

	private Boolean expanded = false;

	@Override
	public void calculateCoordinates() {
		this.id = categories.indexOf(this);

		this.minY = ConfigGui.getCornerYOffset();
		this.maxY = this.minY + ConfigGui.getCategoryYMargin()*2 + ConfigGui.getCategoryFontSize();

		this.minX = ConfigGui.getCornerXOffset();

		for (int i = 0; i < this.id; i++) {
			this.minX += this.parent.getWidestContentWidth() + ConfigGui.getCategoryXPadding();
		}

		this.maxX = this.minX + this.parent.getWidestContentWidth();

		this.textX = (this.maxX+this.minX)/2 - NanoVGRenderer.getStringWidth(this.name, ConfigGui.getCategoryFontSize(), ConfigGui.getCategoryFontWeight())/2;
		this.textY = this.minY + ConfigGui.getCategoryYMargin();

		if (this.expanded && !this.expandingAnimation.isRunning()) {
			this.lowestPoint = this.getLowestExpandedPoint();
		} else if (!this.expanded && !this.expandingAnimation.isRunning()) {
			this.lowestPoint = this.getLowestUnexpandedPoint();
		}
	}
	
	public Category(String name, ConfigGui parent) {
		this.name = name;
		this.parent = parent;
		this.expanded = (boolean) ConfigFile.readFromConfig(name + ".expanded", true);

		Boolean alreadyExists = false;

		for (Category category : categories) {
			if (category.name.equals(this.name)) alreadyExists = true;
		}

		if (alreadyExists) {
			Log.error("You've made a duplicate Category, this is highly discouraged. EXPECT EVERYTHING TO BREAK!");
		}

		categories.add(this);
		this.id = categories.indexOf(this);
	}

	private void renderMainRectangle() {
		NanoVGRenderer.renderRectangle(this.minX, this.minY, this.maxX, this.maxY, ConfigGui.getBackgroundColor());
	}

	private void renderCategoryName() {
		NanoVGRenderer.renderString(this.name, this.textX, this.textY, ConfigGui.getCategoryFontSize(), ConfigGui.getCategoryFontWeight(), ConfigGui.getTextColor());
	}

	private void renderIndicatorLine() {
		NanoVGRenderer.renderRectangle(this.minX, this.lowestPoint, this.maxX, this.lowestPoint + ConfigGui.getCategoryIndicatorLineSize(), ConfigGui.getBackgroundColor());

		int toggledColor = (ConfigGui.getPrimaryColor() & 0x00FFFFFF) | ((int) this.children.getLast().getToggledOpacity() << 24);
		NanoVGRenderer.renderRectangle(this.minX, this.lowestPoint, this.maxX, this.lowestPoint + ConfigGui.getCategoryIndicatorLineSize(), toggledColor);

		int hoveredColor = (ConfigGui.getHoveredColor() & 0x00FFFFFF) | ((int) this.children.getLast().getHoveredOpacity() << 24);
		NanoVGRenderer.renderRectangle(this.minX, this.lowestPoint, this.maxX, this.lowestPoint + ConfigGui.getCategoryIndicatorLineSize(), hoveredColor);
	}

	@Override
	public void render() {
		this.expandingAnimation.update();

		this.calculateCoordinates();
		this.renderMainRectangle();
		this.renderCategoryName();
		this.renderAllFeatures();
		this.renderIndicatorLine();
	}

	/**
	 * Renders all Categories in a GuiGraphics context
	 * @param guiGraphics
	 */
	public static void renderAllCategories(GuiGraphics guiGraphics) {
		NanoVGPiPRenderer.renderNanoVG(guiGraphics, () -> {
			for (Category category : categories) {
				category.render();
			}
		});
	}

	public static void clearCategories() {
		categories.clear();
	}

	@Override
	public Boolean onClick(int button) {
		if (button != GLFW.GLFW_MOUSE_BUTTON_RIGHT) {
			return false;
		}

		this.expanded = !this.expanded;
		this.expandingAnimation = new Animation(this.lowestPoint, this.expanded ? this.getLowestExpandedPoint() : this.getLowestUnexpandedPoint(), 
		ConfigGui.getExpandingAnimationDuration(), value -> this.lowestPoint = value);
		this.expandingAnimation.start();

		ConfigFile.updateConfig();
		return true;
	}

	@Override
	public void onHoverEnter() {
		return;
	}

	@Override
	public void onHoverExit() {
		return;
	}

	@Override
	public Boolean isInHitbox(double x, double y) {
		return (x > this.minX && x < this.maxX && y > this.minY && y < this.maxY);
	}

	@Override
	public Void getValue() { return null; }
 

	private float getLowestExpandedPoint() {
		float lowestExpandedPoint = this.maxY;
		for (Feature child : this.children) {
			float lowestChildPoint = child.getLowestPoint();
			if (lowestChildPoint > lowestExpandedPoint) lowestExpandedPoint = lowestChildPoint;
		}

		return lowestExpandedPoint;
	}

	private float getLowestUnexpandedPoint() { return this.maxY; }

	private void renderAllFeatures() {
		this.sortFeatures();

		for (Feature child : this.children) {
			child.render();
		}
	}

	@Override
	public float getMinX() { return this.minX; }

	@Override
	public float getMaxX() { return this.maxX; }

	@Override
	public float getMinY() { return this.minY; }

	@Override
	public float getMaxY() { return this.maxY; }

	public float getTextX() { return this.textX; }
	public float getTextY() { return this.textY; }

	@Override
	public String getName() { return this.name; }

	@Override
	public Boolean getExpanded() { return this.expanded; }

	public float getLowestPoint() { return this.lowestPoint; }

	private void sortFeatures() {
		this.children.sort(Comparator.comparingDouble(Feature::getContentWidth).reversed());
	} 

	public static List<Category> getAllCategories() {
		return categories;
	}

	public void registerChild(Feature child) {
		this.children.add(child);
	}

	public static Category findFirstCategory(String name) {
		for (Category category : categories) {
			if (category.name == name) {
				return category;
			}
		}

		Log.error(String.format("Category \"%s\" doesn't exist", name));
		return null;
	}

	@Override
	public List<Feature> getChildren() { return this.children; }
	
	@Override
	public ConfigGui getParent() { return this.parent; }

	@Override
	public float getContentWidth() {
		return NanoVGRenderer.getStringWidth(this.name, ConfigGui.getCategoryFontSize(), ConfigGui.getCategoryFontWeight()) + ConfigGui.getCategoryXMargin() * 2;
	}

	public Feature getChildById(int id) {
		for (Feature child : this.children) {
			if (child.getId() == id) return child;
		}
		
		return null;
	}

	@Override
	public int getId() {
		return this.id;
	}

	@Override
	public void setId(int newId) {
		String caller = Thread.currentThread().getStackTrace()[2].getClassName();
		if (caller.equals(this.parent.getClass().getName())) {
			Log.error("The id of a ConfigWidget can only be set by itself or it's parent.");
			return;
		}

		this.id = newId;
	}
}
  