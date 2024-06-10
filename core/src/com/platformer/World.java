package com.platformer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.badlogic.gdx.math.Vector2;

public class World {

	public interface WorldListener {
		public void jump ();

		public void hit ();
	}

	public static final float WORLD_WIDTH = 10;
	public static final float WORLD_HEIGHT = 15 * 20;
	public static final Vector2 gravity = new Vector2(0, -12);

	public final Avatar ava;
	public final List<Platform> platforms;
	public final WorldListener listener;
	public final Random rand;

	private float heightSoFar;
	private int score;
	private boolean worldRunning = false;
	private boolean gameOver = false;
	private boolean nextLevel = false;

	public World (WorldListener listener) {
		this.ava = new Avatar(5, 1);
		this.platforms = new ArrayList<Platform>();
		this.listener = listener;
		rand = new Random();
		generateLevel();

		this.heightSoFar = 0;
		this.score = 0;
		worldRunning = true;
	}

	private void generateLevel () {
		float y = Platform.PLATFORM_HEIGHT / 2;
		float maxJumpHeight = Avatar.JUMP_VELOCITY * Avatar.JUMP_VELOCITY / (2 * -gravity.y);
		Platform platform = new Platform(false, (WORLD_WIDTH / 2), y);
		platforms.add(platform);
		
		y += (maxJumpHeight - 0.5f);
		y -= rand.nextFloat() * (maxJumpHeight / 3);
		
		while (y < WORLD_HEIGHT - WORLD_WIDTH / 2) {
			boolean moving = ( rand.nextFloat() > 0.8f );
			float x = rand.nextFloat() * (WORLD_WIDTH - Platform.PLATFORM_WIDTH) + Platform.PLATFORM_WIDTH / 2;

			platform = new Platform(moving, x, y);
			platforms.add(platform);

			y += (maxJumpHeight - 0.5f);
			y -= rand.nextFloat() * (maxJumpHeight / 3);
		}

	}

	public void update (float deltaTime, float accelX, boolean up) {
		updateAva(deltaTime, accelX, up);
		updatePlatforms(deltaTime);
		if (!ava.isHit()) checkCollisions();
		checkGameOver();
	}

	private void updateAva (float deltaTime, float accelX, boolean up) {
		if (!ava.isHit() && ava.position.y <= 0.5f) {
			ava.hitPlatform();
		}
		if (!ava.isHit()) {
			ava.velocity.x = -accelX / 10 * Avatar.MOVE_VELOCITY;
			if (up && !ava.isFalling() && !ava.isJumping()) {
				ava.jump();
				listener.jump();
			}
		}
		ava.update(deltaTime);
		heightSoFar = Math.max(ava.position.y, heightSoFar);
	}

	private void updatePlatforms (float deltaTime) {
		int length = platforms.size();
		for (int i = 0; i < length; i++) {
			Platform platform = platforms.get(i);
			platform.update(deltaTime);
			if (platform.isCrumbling() && platform.stateTime > Platform.PLATFORM_CRUMBLE_TIME) {
				platforms.remove(platform);
				length = platforms.size();
			}
		}
	}

	private void checkCollisions () {
		checkPlatformCollisions();
	}

	private void checkPlatformCollisions () {
		//if ava still rising, return immediately
		if (ava.velocity.y > 0) return;
		
		boolean onPlatform = false;
		
		for (Platform p : platforms) {
			if ((ava.position.y > p.position.y) && ava.bounds.overlaps(p.bounds)) {	
				ava.hitPlatform();
				onPlatform = true;
				
				// 50% to crumble platform
				if ((rand.nextFloat() > 0.5f) && !p.isMoving()) p.crumble();
				
				break;
			}
		}
		
		if (ava.velocity.y == 0 && !onPlatform) {
			ava.fall();
		}
	}

	private void checkGameOver () {
		if (heightSoFar - 7.5f > ava.position.y) {
			gameOver = true;
		}
	}
	
	public void setScore(int score) {
		this.score = score;
	}
	public int getScore() {
		return score;
	}
	
	public boolean isWorldRunning() {
		return worldRunning;
	}
	
	public boolean isGameOver() {
		return gameOver;
	}
	
	public boolean isNextLevel() {
		return nextLevel;
	}

}
