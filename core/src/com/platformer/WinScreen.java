package com.platformer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Align;

public class WinScreen extends ScreenAdapter {
	MyGame game;
	OrthographicCamera cam;
	TextureRegion princess;
	String[] messages = { "Princess: Oh dear!\n What have you done?",
						  "Bob: I came to \nrescue you!",
						  "Princess: you are\n mistaken\nI need no rescueing",
						  "Bob: So all this \nwork for nothing?",
						  "Princess: I have \ncake and tea!\nWould you like some?",
						  "Bob: I'd be my \npleasure!",
						  "And they ate cake\nand drank tea\nhappily ever \nafter\n\n\n\n\n\n\nKära Emma!\nDu är fantastisk!\nDu blev ferdig\n med spelet!"
			};
	int currentMessage = 0;
	
	public WinScreen(MyGame game) {
		this.game = game;
		cam = new OrthographicCamera();
		cam.setToOrtho(false, 320, 480);
		princess = new TextureRegion(Assets.arrow.getTexture(), 210, 122, -40, 38);
	}
	
	@Override
	public void render(float delta) {
		if(Gdx.input.justTouched()) {
			currentMessage++;
			if(currentMessage == messages.length) {
				currentMessage--;
				game.setScreen(new MainMenuScreen(game));
			}
		}
		
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		cam.update();
		game.batch.setProjectionMatrix(cam.combined);
		game.batch.begin();
		game.batch.draw(Assets.backgroundRegion, 0, 0);
		Assets.font.draw(game.batch, messages[currentMessage], 0, 400, 320, Align.center, false);
		game.batch.draw(princess,150, 200);
		game.batch.end();
	}
}
