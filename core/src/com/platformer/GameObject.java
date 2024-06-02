package com.platformer;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class GameObject {

	public final Vector2 position; // this is the center of the object
	public final Rectangle bounds; // this is the outer bounds of the object
	
	/** create a new GameObject with center at x,y and dimensions of width, height */
	public GameObject(float x, float y, float width, float height) {
		this.position = new Vector2(x, y);
		this.bounds = new Rectangle(x - width/2, y - height/2, width, height);
	}

}
