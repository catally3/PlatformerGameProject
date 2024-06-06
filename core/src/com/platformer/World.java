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
	public static final int WORLD_STATE_RUNNING = 0;
	public static final int WORLD_STATE_NEXT_LEVEL = 1;
	public static final int WORLD_STATE_GAME_OVER = 2;
	public static final Vector2 gravity = new Vector2(0, -12);

	public final Avatar ava;
	public final List<Platform> platforms;
	public final WorldListener listener;
	public final Random rand;

	public float heightSoFar;
	public int score;
	public int state;

	public World (WorldListener listener) {
		this.ava = new Avatar(5, 1);
		this.platforms = new ArrayList<Platform>();
		this.listener = listener;
		rand = new Random();
		generateLevel();

		this.heightSoFar = 0;
		this.score = 0;
		this.state = WORLD_STATE_RUNNING;
	}

	private void generateLevel () {
		float y = Platform.PLATFORM_HEIGHT / 2;
		float maxJumpHeight = Avatar.JUMP_VELOCITY * Avatar.JUMP_VELOCITY / (2 * -gravity.y);
		while (y < WORLD_HEIGHT - WORLD_WIDTH / 2) {
			int type = rand.nextFloat() > 0.8f ? Platform.TYPE_MOVING : Platform.TYPE_STATIC;
			float x = rand.nextFloat() * (WORLD_WIDTH - Platform.PLATFORM_WIDTH) + Platform.PLATFORM_WIDTH / 2;

			Platform platform = new Platform(type, x, y);
			platforms.add(platform);

			y += (maxJumpHeight - 0.5f);
			y -= rand.nextFloat() * (maxJumpHeight / 3);
		}

	}

	public void update (float deltaTime, float accelX) {
		updateAva(deltaTime, accelX);
		updatePlatforms(deltaTime);
		if (!ava.isHit()) checkCollisions();
		checkGameOver();
	}

	private void updateAva (float deltaTime, float accelX) {
		if (!ava.isHit() && ava.position.y <= 0.5f) ava.hitPlatform();
		if (!ava.isHit()) ava.velocity.x = -accelX / 10 * Avatar.MOVE_VELOCITY;
		ava.update(deltaTime);
		heightSoFar = Math.max(ava.position.y, heightSoFar);
	}

	private void updatePlatforms (float deltaTime) {
		int len = platforms.size();
		for (int i = 0; i < len; i++) {
			Platform platform = platforms.get(i);
			platform.update(deltaTime);
			if (platform.state == Platform.STATE_PULVERIZING && platform.stateTime > Platform.PLATFORM_PULVERIZE_TIME) {
				platforms.remove(platform);
				len = platforms.size();
			}
		}
	}

	private void checkCollisions () {
		checkPlatformCollisions();
	}

	private void checkPlatformCollisions () {
		if (ava.velocity.y > 0) return;

		int len = platforms.size();
		for (int i = 0; i < len; i++) {
			Platform platform = platforms.get(i);
			if (ava.position.y > platform.position.y) {
				if (ava.bounds.overlaps(platform.bounds)) {
					ava.hitPlatform();
					listener.jump();
					if (rand.nextFloat() > 0.5f) {
						platform.pulverize();
					}
					break;
				}
			}
		}
	}

	private void checkGameOver () {
		if (heightSoFar - 7.5f > ava.position.y) {
			state = WORLD_STATE_GAME_OVER;
		}
	}

}
