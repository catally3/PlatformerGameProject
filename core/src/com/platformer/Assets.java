package com.platformer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Assets {
	public static Texture background;
	public static TextureRegion backgroundRegion;
	public static Texture items;
	public static TextureRegion mainMenu;
	public static TextureRegion pauseMenu;
	public static TextureRegion ready;
	public static TextureRegion gameOver;
	public static TextureRegion highScoresRegion;
	public static TextureRegion logo;
	public static TextureRegion soundOn;
	public static TextureRegion soundOff;
	public static TextureRegion arrow;
	public static TextureRegion pause;
	public static TextureRegion avaDefault;
	public static TextureRegion avaHit;
	public static TextureRegion avaJump;
	public static TextureRegion avaFall;
	public static TextureRegion platform;
	public static TextureRegion platformCrumble0;
	public static TextureRegion platformCrumble1;
	public static TextureRegion platformCrumble2;
	public static BitmapFont font;

	public static Boolean soundEnabled = true;
	public static Music music;
	public static Sound jumpSound;
	public static Sound hitSound;
	public static Sound clickSound;

	public static Texture loadTexture (String file) {
		return new Texture(Gdx.files.internal(file));
	}

	public static void load () {
		background = loadTexture("background.png");
		backgroundRegion = new TextureRegion(background, 0, 0, 320, 480);

		items = loadTexture("items.png");
		mainMenu = new TextureRegion(items, 0, 224, 300, 110);
		pauseMenu = new TextureRegion(items, 224, 128, 192, 96);
		ready = new TextureRegion(items, 320, 224, 192, 32);
		gameOver = new TextureRegion(items, 352, 256, 160, 96);
		highScoresRegion = new TextureRegion(Assets.items, 0, 257, 300, 110 / 3);
		logo = new TextureRegion(items, 0, 352, 274, 142);
		soundOff = new TextureRegion(items, 0, 0, 64, 64);
		soundOn = new TextureRegion(items, 64, 0, 64, 64);
		arrow = new TextureRegion(items, 0, 64, 64, 64);
		pause = new TextureRegion(items, 64, 64, 64, 64);
		avaDefault = new TextureRegion(items, 160, 128, 32, 32); 
		avaHit = new TextureRegion(items, 128, 128, 32, 32);
		avaJump = new TextureRegion(items, 0, 128, 32, 32); // first frame of jump
		avaFall = new TextureRegion(items, 64, 128, 32, 32); // first frame of fall
		platform = new TextureRegion(items, 64, 160, 64, 16);
		platformCrumble0 = new TextureRegion(items, 64, 176, 64, 16); // first frame of crumble
		platformCrumble1 = new TextureRegion(items, 64, 192, 64, 16);
		platformCrumble2 = new TextureRegion(items, 64, 208, 64, 16);
		
		font = new BitmapFont(Gdx.files.internal("font.fnt"), Gdx.files.internal("font.png"), false);

		music = Gdx.audio.newMusic(Gdx.files.internal("music.mp3"));
		music.setLooping(true);
		music.setVolume(0.5f);
		music.play();
		jumpSound = Gdx.audio.newSound(Gdx.files.internal("jump.wav"));
		hitSound = Gdx.audio.newSound(Gdx.files.internal("hit.wav"));
		clickSound = Gdx.audio.newSound(Gdx.files.internal("click.wav"));
	}

	public static void playSound (Sound sound) {
		if (soundEnabled) sound.play(1);
	}
}
