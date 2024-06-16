package com.platformer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.platformer.World.WorldListener;

public class GameScreen extends ScreenAdapter {
	MyGame game;

	boolean gameRunning = false;
	boolean gamePaused = false;
	boolean levelEnded = false;
	boolean gameOver = false;
	
	OrthographicCamera guiCam;
	Vector3 touchPoint;
	World world;
	WorldListener worldListener;
	WorldRenderer renderer;
	
	/** pause button boundaries */
	Rectangle pauseBounds;
	
	/** resume button boundaries */
	Rectangle resumeBounds;
	
	/** quit button boundaries */
	Rectangle quitBounds;
	
	int lastScore;
	String scoreString;

	GlyphLayout glyphLayout = new GlyphLayout();

	public GameScreen (MyGame game) {
		this.game = game;

		guiCam = new OrthographicCamera(320, 480);
		guiCam.position.set(320 / 2, 480 / 2, 0);
		touchPoint = new Vector3();
		
		worldListener = new WorldListener() {
			@Override
			public void jump () {
				Assets.playSound(Assets.jumpSound);
			}

			@Override
			public void hit () {
				Assets.playSound(Assets.hitSound);
			}
		};
		
		world = new World(worldListener);
		renderer = new WorldRenderer(game.batch, world);
		pauseBounds = new Rectangle(320 - 64, 480 - 64, 64, 64);
		resumeBounds = new Rectangle(160 - 96, 240, 192, 36);
		quitBounds = new Rectangle(160 - 96, 240 - 36, 192, 36);
		lastScore = 0;
		scoreString = "SCORE: 0";
	}

	public void update (float deltaTime) {
		if (deltaTime > 0.1f) deltaTime = 0.1f;

		if (gameRunning) updateRunning(deltaTime);
		else if (gamePaused) updatePaused();
		else if (levelEnded) updateLevelEnd();
		else if (gameOver) updateGameOver();
		else if (Gdx.input.justTouched() || Gdx.input.isKeyJustPressed(Keys.ANY_KEY)) {
			gameRunning = true;
		}
	}

	private void updateRunning (float deltaTime) {
		if (Gdx.input.justTouched()) {
			guiCam.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));

			if (pauseBounds.contains(touchPoint.x, touchPoint.y)) {
				Assets.playSound(Assets.clickSound);
				gamePaused = true; gameRunning = false;
				return;
			}
		}
		
		float accel = 0;
		boolean up = false;
		if (Gdx.input.isKeyPressed(Keys.DPAD_LEFT)) accel = 5f;
		if (Gdx.input.isKeyPressed(Keys.DPAD_RIGHT)) accel = -5f;
		up = Gdx.input.isKeyPressed(Keys.DPAD_UP) || Gdx.input.isKeyPressed(Keys.SPACE);
		world.update(deltaTime, accel, up);
		
		if (world.getScore() != lastScore) {
			lastScore = world.getScore();
			scoreString = "SCORE: " + lastScore;
		}
		if (world.isNextLevel()) {
			game.setScreen(new WinScreen(game));
		}
		if (world.isGameOver()) {
			gameOver = true; gameRunning = false;
			scoreString = "SCORE: " + lastScore;
		}
	}

	private void updatePaused () {
		if (Gdx.input.justTouched()) {
			guiCam.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));

			if (resumeBounds.contains(touchPoint.x, touchPoint.y)) {
				Assets.playSound(Assets.clickSound);
				gameRunning = true; gamePaused = false;
				return;
			}

			if (quitBounds.contains(touchPoint.x, touchPoint.y)) {
				Assets.playSound(Assets.clickSound);
				game.setScreen(new MainMenuScreen(game));
				return;
			}
		}
	}

	private void updateLevelEnd () {
		if (Gdx.input.justTouched()) {
			world = new World(worldListener);
			renderer = new WorldRenderer(game.batch, world);
			//world.setScore(lastScore);
			gameRunning = false; levelEnded = false;
		}
	}

	private void updateGameOver () {
		if (Gdx.input.justTouched() || Gdx.input.isKeyJustPressed(Keys.ANY_KEY)) {
			game.setScreen(new MainMenuScreen(game));
		}
	}

	public void draw () {
		GL20 gl = Gdx.gl;
		gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		renderer.render();

		guiCam.update();
		game.batch.setProjectionMatrix(guiCam.combined);
		game.batch.enableBlending();
		game.batch.begin();
		if (gameRunning) presentRunning();
		else if (gamePaused) presentPaused();
		else if (gameOver) presentGameOver();
		else presentReady();
		game.batch.end();
	}

	private void presentReady () {
		game.batch.draw(Assets.ready, 160 - 192 / 2, 240 - 32 / 2, 192, 32);
	}

	private void presentRunning () {
		game.batch.draw(Assets.pause, 320 - 64, 480 - 64, 64, 64);
		Assets.font.draw(game.batch, scoreString, 16, 480 - 20);
	}

	private void presentPaused () {
		game.batch.draw(Assets.pauseMenu, 160 - 192 / 2, 240 - 96 / 2, 192, 96);
		Assets.font.draw(game.batch, scoreString, 16, 480 - 20);
	}

	private void presentGameOver () {
		game.batch.draw(Assets.gameOver, 160 - 160 / 2, 240 - 96 / 2, 160, 96);
		glyphLayout.setText(Assets.font, scoreString);
		Assets.font.draw(game.batch, scoreString, 160 - glyphLayout.width / 2, 480 - 20);
	}

	@Override
	public void render (float delta) {
		update(delta);
		draw();
	}

	@Override
	public void pause () {
		if (gameRunning) {
			gamePaused = true; gameRunning = false;
		}
	}
}
