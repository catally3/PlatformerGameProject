package com.platformer;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;

public class GameScreen implements Screen {

	final MyGame game;
	
	Texture avatarImage;
	Texture grassImage;
	//Sound dropSound;
	//Music rainMusic;
	OrthographicCamera camera;
	Rectangle bucket;
	Array<Rectangle> raindrops;
	long lastDropTime;
	int dropsGathered;
	
	public GameScreen(final MyGame game) {
		this.game = game;
		
		// load the images for the droplet and the bucket, 64x64 pixels each
		grassImage = new Texture(Gdx.files.internal("grass.png"));
		avatarImage = new Texture(Gdx.files.internal("avatar.png"));

		// load the drop sound effect and the rain background "music"
		//dropSound = Gdx.audio.newSound(Gdx.files.internal("drop.wav"));
		//rainMusic = Gdx.audio.newMusic(Gdx.files.internal("rain.mp3"));
		//rainMusic.setLooping(true);

		// create the camera and the SpriteBatch
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 1280, 960);

		// create a Rectangle to logically represent the bucket
		bucket = new Rectangle();
		bucket.x = 1280 / 2 - 254 / 2; // center the bucket horizontally
		bucket.y = 20; // bottom left corner of the bucket is 20 pixels above
								// the bottom screen edge
		bucket.width = 254;
		bucket.height = 240;

		// create the raindrops array and spawn the first raindrop
		raindrops = new Array<Rectangle>();
		spawnRaindrop();
	}
	
	private void spawnRaindrop() {
		Rectangle raindrop = new Rectangle();
		raindrop.x = MathUtils.random(0, 1280 - 256);
		raindrop.y = 960;
		raindrop.width = 256;
		raindrop.height = 256;
		raindrops.add(raindrop);
		lastDropTime = TimeUtils.nanoTime();
	}

	@Override
	public void show() {
		// start the playback of the background music
		// when the screen is shown
		//rainMusic.play();
	}

	@Override
	public void render(float delta) {
		// clear the screen with a dark blue color. The
		// arguments to clear are the red, green
		// blue and alpha component in the range [0,1]
		// of the color to be used to clear the screen.
		ScreenUtils.clear(0, 0, 0.2f, 1);

		// tell the camera to update its matrices.
		camera.update();

		// tell the SpriteBatch to render in the
		// coordinate system specified by the camera.
		game.batch.setProjectionMatrix(camera.combined);

		// begin a new batch and draw the bucket and
		// all drops
		game.batch.begin();
		
		//game.font.draw(game.batch, "Drops Collected: " + dropsGathered, 0, 480);
		game.batch.draw(avatarImage, bucket.x, bucket.y, bucket.width, bucket.height);
		for (Rectangle raindrop : raindrops) {
			game.batch.draw(grassImage, raindrop.x, raindrop.y);
		}
		
		game.batch.end();

		// process user input
		if (Gdx.input.isTouched()) {
			Vector3 touchPos = new Vector3();
			touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			camera.unproject(touchPos);
			bucket.x = touchPos.x - 64 / 2;
		}
		
		if (Gdx.input.isKeyPressed(Keys.LEFT))
			bucket.x -= 200 * Gdx.graphics.getDeltaTime();
		
		if (Gdx.input.isKeyPressed(Keys.RIGHT))
			bucket.x += 200 * Gdx.graphics.getDeltaTime();

		// make sure the bucket stays within the screen bounds
		if (bucket.x < 0)
			bucket.x = 0;
		
		if (bucket.x > 1280 - 254)
			bucket.x = 1280 - 254;

		// check if we need to create a new raindrop
		if (TimeUtils.nanoTime() - lastDropTime > 1000000000)
			spawnRaindrop();

		// move the raindrops, remove any that are beneath the bottom edge of
		// the screen or that hit the bucket. In the later case we increase the
		// value our drops counter and add a sound effect.
		Iterator<Rectangle> iter = raindrops.iterator();
		while (iter.hasNext()) {
			Rectangle raindrop = iter.next();
			raindrop.y -= 200 * Gdx.graphics.getDeltaTime();
			if (raindrop.y + 256 < 0)
				iter.remove();
			
			if (raindrop.overlaps(bucket)) {
				dropsGathered++;
				//dropSound.play();
				iter.remove();
			}
			
		}

	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	
	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		grassImage.dispose();
		avatarImage.dispose();
		//dropSound.dispose();
		//rainMusic.dispose();

	}

}
