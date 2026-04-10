package me.shaweel.shaweeladdons.utils;

import java.util.function.Consumer;


public class Animation {
	private final float DURATION;
	private final Consumer<Float> setter;

	private float goal;
	private float value;
	private float lastValue;

	private long toggleTime;

	private boolean running;
	
	public Animation(float start, float goal, float duration, Consumer<Float> setter) {
		this.toggleTime = System.currentTimeMillis();
		this.value = start;
		this.lastValue = start;
		this.DURATION = duration;
		this.goal = goal;
		this.setter = setter;
		this.running = false;
	}

	public void start() {
		this.running = true;
		this.toggleTime = System.currentTimeMillis();
		this.lastValue = this.value;
	}

	public void stop() {
		this.running = false;
	}

	public boolean isRunning() {
		return this.running;
	}

	public void update() {
		if (!this.running) return;

		final long elapsed = System.currentTimeMillis() - toggleTime;
		final float progress = Math.min(elapsed / DURATION, 1f);

		if (progress >= 1) {
			this.value = this.goal;
			this.running = false;
			setter.accept(this.value);
			return;
		}

		this.value = this.lastValue + (this.goal - this.lastValue) * progress;
		setter.accept(this.value);
	}
}
