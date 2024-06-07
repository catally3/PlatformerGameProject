package com.platformer;

public class Avatar extends DynamicGameObject {

	public static final float JUMP_VELOCITY = 11;
	public static final float MOVE_VELOCITY = 20;
	public static final float WIDTH = 0.8f;
	public static final float HEIGHT = 0.8f;

	private boolean hit = false;
	private boolean falling = false;
	private boolean jumping = false;
	float stateTime;
	
	public Avatar(float x, float y) {
		super(x, y, WIDTH, HEIGHT);
		stateTime = 0;
	}
	
	public void update (float deltaTime) {
		if (falling || jumping) {
			velocity.add(0, World.gravity.y * deltaTime);
			position.add(0, velocity.y * deltaTime);
			bounds.y = position.y - bounds.height / 2;
		}
		velocity.add(World.gravity.x * deltaTime, 0);
		position.add(velocity.x * deltaTime, 0);
		bounds.x = position.x - bounds.width / 2;
		

		if (velocity.y > 0 && !hit && !jumping) {
			falling = false;
			jumping = true;
			stateTime = 0;
		}
		else if (velocity.y < 0 && !hit && !falling) {
			falling = true;
			jumping = false;
			stateTime = 0;
		}
		else if (velocity.y == 0 && !hit) {
			falling = false;
			jumping = false;
			stateTime = 0;
		}

		// if going off the screen negative x, reset x to 0
		if (position.x < 0)	position.x = 0;
		// if going off the screen positive x, reset x to world width
		if (position.x > World.WORLD_WIDTH) position.x = World.WORLD_WIDTH;
		// only increment stateTime if currently jumping or falling
		if (jumping || falling) stateTime += deltaTime;
	}
	
	public void jump() {
		velocity.y = JUMP_VELOCITY;
		jumping = true;
		falling = false;
		stateTime = 0;
	}
	
	public void fall() {
		velocity.y = (World.gravity.y * 0.016f); // gravity times estimated deltaTime to match normal fall speed
		jumping = false;
		falling = true;
		stateTime = 0;
	}
	
	public void hitPlatform () {
		velocity.y = 0;
		falling = false;
		jumping = false;
		stateTime = 0;
	}
	
	public boolean isHit () {
		return hit;
	}
	
	public boolean isJumping() {
		return jumping;
	}
	
	public boolean isFalling() {
		return falling;
	}

}
