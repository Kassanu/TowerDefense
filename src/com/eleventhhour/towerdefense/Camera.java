package com.eleventhhour.towerdefense;

import java.util.Vector;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

import com.eleventhhour.towerdefense.Collision.CollisionShape;

/**
 * 
 * The camera class is the viewport for the map.
 * The worldPosition is where in the game world the camera's top left position is currently at.
 * Everything rendered in the game must be offset by this position.
 * 
 * @author Matt Martucciello
 *
 */

public class Camera implements GameObject {
	
	private long ID;
	protected Collidable collidable;
	protected Vector2f screenPosition;
	protected Vector2f worldPosition; //Cameras top,left position in the game world
	protected Vector2f centerPosition; // Cameras center position in the game world
	protected int width; // Cameras width
	protected int height; // Cameras height
	protected int radius;
	protected GameplayState gs;
	
	public Camera (long id, Vector2f screenPosition, Vector2f worldPosition, int width, int height, int radius, GameplayState gs) {
		this.ID = id;
		this.screenPosition = screenPosition;
		this.worldPosition = worldPosition;
		this.centerPosition = new Vector2f(0,0);
		this.width = width;
		this.height = height;
		this.radius = radius;
		this.collidable = new Collidable(this, CollisionShape.RECTANGLE, worldPosition, width, height, radius);
		this.gs = gs;
		this.calcCenterPosition();
	}
	
	/**
	 * Calculates the center position
	 */
	public void calcCenterPosition() {
		this.centerPosition.x = (this.width / 2) + this.worldPosition.x;
		this.centerPosition.y = (this.height / 2) + this.worldPosition.y;
	}
	
	/**
	 * Snaps the camera to the bounds of the play field
	 */
	public void snapToBounds(int width, int height) {
		boolean snapped = false;
		if (this.worldPosition.x < 0) {
			this.worldPosition.x = 0;
			snapped = true;
		}
		else if (this.worldPosition.x + this.width > width) {
			this.worldPosition.x = width - this.width;
			snapped = true;
		}
			
		if (this.worldPosition.y < 0) {
			this.worldPosition.y = 0;
			snapped = true;
		}
		else if (this.worldPosition.y + this.height > height) {
			this.worldPosition.y = height - this.height;
			snapped = true;
		}
		
		if (snapped)
			this.calcCenterPosition();
	}
	
	public boolean isInView(GameObject obj1) {
		Vector2f offset = this.gs.getOffset();
		Rectangle Camrect = new Rectangle((this.worldPosition.x * -1) + offset.x, (this.worldPosition.y * -1) + offset.y, this.width, this.height);
		Rectangle objRect = new Rectangle(obj1.getWorldPosition().x + offset.x, obj1.getWorldPosition().y + offset.y, obj1.getWidth(), obj1.getHeight());
		return Camrect.intersects(objRect);
	}
	
	public void init() {}
	
	public void update(GameContainer gc, StateBasedGame sbg, GameplayState gs, int delta) {
		//this.collidable.update(gc, sbg, gs, delta);
	}

	public void render(GameContainer gc, Graphics g, Vector2f offset) {
		g.draw(new Rectangle((this.screenPosition.x),(this.screenPosition.y), this.width, this.height));
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
	
	/*
	 * This method will not be used for this class.
	 */
	public Vector2f getTilePosition() {
		return new Vector2f(0,0);
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
	
	public Vector2f getOffset() {
		return (this.worldPosition.copy()).scale(TowerDefense.SCALE);
	}

	public void moveCamera(Vector2f move) {
		this.worldPosition = this.worldPosition.sub(move);
		this.calcCenterPosition();
	}
}
