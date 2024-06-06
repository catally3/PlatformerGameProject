package com.platformer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

public class MainMenuScreen extends ScreenAdapter {
	MyGame game;
	OrthographicCamera guiCam;
	
	/** this is the boundaries of the mute sound button*/
	Rectangle soundBounds;
	
	/** this is the boundaries of the play button*/
	Rectangle playBounds;
	
	Vector3 touchPoint;

	public MainMenuScreen (MyGame game) {
		this.game = game;

		guiCam = new OrthographicCamera(320, 480);
		guiCam.position.set(320 / 2, 480 / 2, 0);
		soundBounds = new Rectangle(0, 0, 64, 64);
		playBounds = new Rectangle(160 - 150, 200 + 18, 300, 36);
		touchPoint = new Vector3();
	}

	public void update () {
		if (Gdx.input.justTouched()) {
			guiCam.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));

			//if the touchpoint is within the play button, begin gamescreen
			if (playBounds.contains(touchPoint.x, touchPoint.y)) {
				Assets.playSound(Assets.clickSound);
				game.setScreen(new GameScreen(game));
				return;
			}
			if (soundBounds.contains(touchPoint.x, touchPoint.y)) {
				Assets.playSound(Assets.clickSound);
			}
		}
	}

	public void draw () {
		GL20 gl = Gdx.gl;
		gl.glClearColor(1, 0, 0, 1);
		gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		guiCam.update();
		game.batch.setProjectionMatrix(guiCam.combined);

		game.batch.disableBlending();
		game.batch.begin();
		game.batch.draw(Assets.backgroundRegion, 0, 0, 320, 480);
		game.batch.end();

		game.batch.enableBlending();
		game.batch.begin();
		game.batch.draw(Assets.logo, 160 - 274 / 2, 480 - 10 - 142, 274, 142);
		game.batch.draw(Assets.mainMenu, 10, 200 - 110 / 2, 300, 110);
		game.batch.draw(Assets.soundOn, 0, 0, 64, 64);
		game.batch.end();	
	}

	@Override
	public void render (float delta) {
		update();
		draw();
	}

	@Override
	public void pause () {
	}
}
