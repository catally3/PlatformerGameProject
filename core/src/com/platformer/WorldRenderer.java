package com.platformer;


import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class WorldRenderer {
	static final float FRUSTUM_WIDTH = 10;
	static final float FRUSTUM_HEIGHT = 15;
	World world;
	OrthographicCamera cam;
	SpriteBatch batch;

	public WorldRenderer (SpriteBatch batch, World world) {
		this.world = world;
		this.cam = new OrthographicCamera(FRUSTUM_WIDTH, FRUSTUM_HEIGHT);
		this.cam.position.set(FRUSTUM_WIDTH / 2, FRUSTUM_HEIGHT / 2, 0);
		this.batch = batch;
	}

	public void render () {
		if (world.ava.position.y > cam.position.y) cam.position.y = world.ava.position.y;
		cam.update();
		batch.setProjectionMatrix(cam.combined);
		renderBackground();
		renderObjects();
	}

	public void renderBackground () {
		batch.disableBlending();
		batch.begin();
		batch.draw(Assets.backgroundRegion, cam.position.x - FRUSTUM_WIDTH / 2, cam.position.y - FRUSTUM_HEIGHT / 2, FRUSTUM_WIDTH,
			FRUSTUM_HEIGHT);
		batch.end();
	}

	public void renderObjects () {
		batch.enableBlending();
		batch.begin();
		renderAva();
		renderPlatforms();
		batch.end();
	}

	private void renderAva() {
		TextureRegion keyFrame;
		if (world.ava.isHit()) {
			keyFrame = Assets.avaHit;
		}	
		else if (world.ava.isFalling()) {
			keyFrame = Assets.avaFall;
		}
		else if (world.ava.isJumping()) {
			keyFrame = Assets.avaJump;
		}
		else {
			keyFrame = Assets.avaDefault;
		}
		float side = world.ava.velocity.x < 0 ? -1 : 1;
		if (side < 0)
			batch.draw(keyFrame, world.ava.position.x + 0.5f, world.ava.position.y - 0.5f, side, 1);
		else
			batch.draw(keyFrame, world.ava.position.x - 0.5f, world.ava.position.y - 0.5f, side, 1);
	}

	private void renderPlatforms () {
		for (Platform p : world.platforms) {
			TextureRegion keyFrame;
			if (p.isCrumbling()) {
				if (p.stateTime > (Platform.PLATFORM_CRUMBLE_TIME * 2.0f / 3.0f)) 
					keyFrame = Assets.platformCrumble2; 
				else if (p.stateTime > (Platform.PLATFORM_CRUMBLE_TIME / 3.0f))
					keyFrame = Assets.platformCrumble1;
				else 
					keyFrame = Assets.platformCrumble0;
			}
			else if (p.canCrumble())
				keyFrame = Assets.platformCrumble0; // this will be static cracked platform texture
			else
				keyFrame = Assets.platform; // default platform texture
			batch.draw(keyFrame, p.position.x - 1, p.position.y - 0.25f, 2, 0.5f);
		}
	}
}
