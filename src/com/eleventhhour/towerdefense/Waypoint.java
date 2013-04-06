package com.eleventhhour.towerdefense;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

import com.eleventhhour.towerdefense.Collision.CollisionShape;

public class Waypoint implements GameObject {
	
	private long ID;
	protected Collidable collidable;
	protected Vector2f worldPosition;
	protected Vector2f centerPosition;
	protected Vector2f tilePosition;
	protected int width;
	protected int height;
	protected int radius;
	
	public Waypoint(long id, Vector2f worldPosition, Vector2f tilePosition, int width, int height, int radius) {
		this.ID = id;
		this.worldPosition = worldPosition;
		this.centerPosition = new Vector2f(0,0);
		this.tilePosition = tilePosition;
		this.width = width;
		this.height = height;
		this.radius = radius;
		this.calcCenterPosition();
		this.collidable = new Collidable(this, CollisionShape.RECTANGLE, new Vector2f(centerPosition.x - 3, centerPosition.y - 3), 2, 2, 1);
	}
	
	public void update(GameContainer gc, StateBasedGame sbg, GameplayState gs, int delta) {}

	public void render(GameContainer gc, Graphics g, Vector2f offset) {
		g.setColor(Color.white);
		this.collidable.render(gc, g, offset);
	}
	
	/**
	 * Calculates the center position
	 */
	public void calcCenterPosition() {
		this.centerPosition.x = (this.width / 2) + this.worldPosition.x;
		this.centerPosition.y = (this.height / 2) + this.worldPosition.y;
	}

	public long getId() {
		return this.ID;
	}

	public Vector2f getWorldPosition() {
		return this.worldPosition;
	}
	
	public Vector2f getCenterPosition() {
		return this.centerPosition;
	}
	
	public Vector2f getTilePosition() {
		return this.tilePosition;
	}

	public int getWidth() {
		return this.width;
	}

	public int getHeight() {
		return this.height;
	}

	public int getRadius() {
		return this.radius;
	}

	public Collidable getCollidable() {
		return this.collidable;
	}
	
	public String toString() {
		return "Waypoint [ID=" + ID + ", collidabe=" + collidable
				+ ", worldPosition=" + worldPosition + ", tilePosition="
				+ tilePosition + ", width=" + width + ", height=" + height
				+ ", radius=" + radius + "]";
	}

}
