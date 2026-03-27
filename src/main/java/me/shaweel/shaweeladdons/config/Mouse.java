package me.shaweel.shaweeladdons.config;

import me.shaweel.shaweeladdons.config.widgets.Category;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.MouseButtonEvent;

public class Mouse {
	/**
	 * Handles a mouseClicked event from a Screen.
	 * @param screen
	 * @param event
	 * @param consumed
	 * @return <code>boolean</code> Whether the click was consumed or not
	 */
	public static boolean handleMouseClick(Screen screen, MouseButtonEvent event, boolean consumed) {
		if (consumed) return true;

		if (event.button() != 1) return false;

		Category hoveredCategory = null;
		for (Category category : Category.getAllCategories()) {
			if (category.isInside(event.x(), event.y())) hoveredCategory = category;
		}

		if (hoveredCategory == null) return false;

		hoveredCategory.toggleExpand();

		return true;
	}
}
