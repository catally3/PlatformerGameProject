package com.platformer;

import com.badlogic.gdx.math.Vector2;

public class DynamicGameObject extends GameObject {

	public final Vector2 velocity;
	
	/** vector for acceleration, change in velocity over time */
	public final Vector2 velocityDelta;
	
	/** create a dynamic game object with center at x,y, width and height, and 0 velocity */
	public DynamicGameObject(float x, float y, float width, float height) {
		super(x, y, width, height);
		// initialize velocity and acceleration vectors to null
		velocity = new Vector2(); 
		velocityDelta = new Vector2();
	}

}
