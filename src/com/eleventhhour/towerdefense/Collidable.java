package com.eleventhhour.towerdefense;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

import com.eleventhhour.towerdefense.Collision.CollisionShape;

public class Collidable implements GameObject {
	
	private long ID;
	protected CollisionShape collisionShape;
	protected GameObject parentGameObject;
	protected Vector2f worldPosition;
	protected Vector2f tilePosition = null; //not needed for this class
	protected Vector2f centerPosition;
	protected int width,height,radius;
	
	public Collidable(GameObject parent, CollisionShape shape, Vector2f position, int width, int height, int radius) {
		this.parentGameObject = parent;
		this.collisionShape = shape;
		this.worldPosition = position;
		this.centerPosition = new Vector2f(0,0);
		this.width = width;
		this.height = height;
		this.radius = radius;
		this.calcCenterPosition();
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, GameplayState gs, int delta) { }
	
	public void update(Vector2f movement) {
		this.worldPosition = this.worldPosition.add(movement);
		this.calcCenterPosition();
	}
	
	@Override
	public void render(GameContainer gc, Graphics g) {
		if (this.collisionShape == CollisionShape.CIRCLE)
			g.draw(new Circle(this.centerPosition.x,this.centerPosition.y,this.radius));
		else
			g.draw(new Rectangle(this.worldPosition.x,this.worldPosition.y, this.width, this.height));
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
		return this;
	}

	public CollisionShape getShape() {
		return this.collisionShape;
	}
	
}
