package com.eleventhhour.towerdefense;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

public class Waypoint implements GameObject {
	
	private long ID;
	protected ShapeType shapeType = ShapeType.CIRCLE;
	protected Vector2f worldPosition;
	protected Vector2f tilePosition;
	protected int width;
	protected int height;
	protected int radius;
	
	public Waypoint(long id, Vector2f worldPosition, Vector2f tilePosition, int width, int height, int radius, ShapeType shapeType) {
		this.ID = id;
		this.worldPosition = worldPosition;
		this.tilePosition = tilePosition;
		this.width = width;
		this.height = height;
		this.radius = radius;
		this.shapeType = shapeType;
	}
	
	public void init() {}
	
	public void update(GameContainer gc, StateBasedGame sbg, GameplayState gs, int delta) {}

	public void render(GameContainer gc, Graphics g) {}

	public long getId() {
		return this.ID;
	}

	public Vector2f getWorldPosition() {
		return this.worldPosition;
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

	public ShapeType getShape() {
		return this.shapeType;
	}



}
