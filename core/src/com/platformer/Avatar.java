package com.platformer;

public class Avatar extends DynamicGameObject {

	public static final int STATE_JUMP = 0;
	public static final int STATE_FALL = 1;
	public static final int STATE_HIT = 2;
	public static final float JUMP_VELOCITY = 11;
	public static final float MOVE_VELOCITY = 20;
	public static final float WIDTH = 0.8f;
	public static final float HEIGHT = 0.8f;

	int state;
	float stateTime;
	
	public Avatar(float x, float y) {
		super(x, y, WIDTH, HEIGHT);
		state = STATE_FALL;
		stateTime = 0;
	}
	
	public void update (float deltaTime) {
		velocity.add(World.gravity.x * deltaTime, World.gravity.y * deltaTime);
		position.add(velocity.x * deltaTime, velocity.y * deltaTime);
		bounds.x = position.x - bounds.width / 2;
		bounds.y = position.y - bounds.height / 2;

		if (velocity.y > 0 && state != STATE_HIT) {
			if (state != STATE_JUMP) {
				state = STATE_JUMP;
				stateTime = 0;
			}
		}

		if (velocity.y < 0 && state != STATE_HIT) {
			if (state != STATE_FALL) {
				state = STATE_FALL;
				stateTime = 0;
			}
		}

		if (position.x < 0) position.x = World.WORLD_WIDTH;
		if (position.x > World.WORLD_WIDTH) position.x = 0;

		stateTime += deltaTime;
	}
	
	public void hitPlatform () {
		velocity.y = JUMP_VELOCITY;
		state = STATE_JUMP;
		stateTime = 0;
	}

}
