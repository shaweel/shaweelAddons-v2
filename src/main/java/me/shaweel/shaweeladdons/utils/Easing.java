package me.shaweel.shaweeladdons.utils;

public class Easing {
	/**
	 * Takes linear progress, then returns the progress eased using the ease in cubic algorithm.
	 * @param progress The linear progress from 0-1
	 * @return The progress eased from 0-1
	 */
	public static float easeInCubic(float progress) {
		return progress * progress * progress;
	}

	/**
	 * Takes linear progress, then returns the progress eased using the ease out cubic algorithm.
	 * @param progress The linear progress from 0-1
	 * @return The progress eased from 0-1
	 */
	public static float easeOutCubic(float progress) {
		return (float) Math.pow(progress - 1f, 3) + 1f;
	}
}
