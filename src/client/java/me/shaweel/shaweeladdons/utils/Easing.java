package me.shaweel.shaweeladdons.utils;

public enum Easing {
	EASE_IN_QUAD {
		@Override
		public float get(float progress) {
			return progress * progress;
		}
	},
	EASE_OUT_QUAD {
		@Override
		public float get(float progress) {
			return 1f - (1f - progress) * (1f - progress);
		}
	};

	public abstract float get(float progress);
}