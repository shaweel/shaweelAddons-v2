package me.shaweel.shaweeladdons.config;

import java.util.List;

import me.shaweel.shaweeladdons.config.widgets.Category;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.MouseButtonEvent;

public class Mouse {
	/**
	 * Gets the hovered ConfigWidget in x and y
	 * @param x
	 * @param y
	 * @return The hovered ConfigWidget, null if there isn't any
	 */
	private static ConfigWidget<?, ?> getHoveredConfigWidget(double x, double y) {
		return getHoveredConfigWidget(Category.getAllCategories(), x, y);
	}

	private static ConfigWidget<?, ?> getHoveredConfigWidget(List<? extends ConfigWidget<?, ?>> widgets, double x, double y) {
		if (widgets == null) return null;
		
		for (ConfigWidget<?, ?> widget : widgets) {
			if (widget.isInHitbox(x, y)) {
				return widget;
			}

			ConfigWidget<?, ?> foundWidget = getHoveredConfigWidget(widget.getChildren(), x, y);
			if (foundWidget != null) {
				return foundWidget;
			}
		}

		return null;
	}

	/**
	 * Handles a mouseClicked event from a Screen.
	 * @param screen
	 * @param event
	 * @param consumed
	 * @return <code>boolean</code> Whether the click was consumed or not
	 */
	public static boolean handleMouseClick(Screen screen, MouseButtonEvent event, boolean consumed) {
		if (consumed) return true;

		ConfigWidget<?, ?> hoveredConfigWidget = getHoveredConfigWidget(event.x(), event.y());

		if (hoveredConfigWidget != null) {
			return hoveredConfigWidget.onClick(event.button());
		}
		return false;
	}
}
