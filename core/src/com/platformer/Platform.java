package com.platformer;

public class Platform extends DynamicGameObject {

	public static final float PLATFORM_WIDTH = 1.75f;
	public static final float PLATFORM_HEIGHT = 0.5f;
	public static final float PLATFORM_CRUMBLE_TIME = 0.2f * 3;
	public static final float PLATFORM_VELOCITY = 2;

	private boolean moving = false;
	private boolean crumble = false;
	private boolean crumbling = false;
	float stateTime;

	public Platform (boolean moving, boolean crumble, float x, float y) {
		super(x, y, PLATFORM_WIDTH, PLATFORM_HEIGHT);
		this.moving = moving;
		this.crumble = crumble;
		this.crumbling = false;
		this.stateTime = 0;
		if (moving) {
			velocity.x = PLATFORM_VELOCITY;
		}
	}

	public void update (float deltaTime) {
		if (moving) {
			position.add(velocity.x * deltaTime, 0);
			bounds.x = position.x - PLATFORM_WIDTH / 2;
			bounds.y = position.y - PLATFORM_HEIGHT / 2;

			if (position.x < PLATFORM_WIDTH / 2) {
				velocity.x = -velocity.x;
				position.x = PLATFORM_WIDTH / 2;
			}
			if (position.x > World.WORLD_WIDTH - PLATFORM_WIDTH / 2) {
				velocity.x = -velocity.x;
				position.x = World.WORLD_WIDTH - PLATFORM_WIDTH / 2;
			}
		}

		stateTime += deltaTime;
	}

	public void crumble() {
		if (crumble && !crumbling) {
			crumbling = true;
			stateTime = 0;
		}
	}
	
	public boolean isMoving() {
		return moving;
	}
	
	public boolean canCrumble() {
		return crumble;
	}
	
	public boolean isCrumbling() {
		return crumbling;
	}

}
