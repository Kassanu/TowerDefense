package com.eleventhhour.towerdefense;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

import com.eleventhhour.towerdefense.GameObject.ShapeType;

public class Camera implements GameObject {
	
	private long ID;
	protected ShapeType shapeType = ShapeType.CIRCLE;
	protected Vector2f worldPosition; //Cameras top,left position
	protected Vector2f centerPosition; // Cameras center position
	protected int width; // Cameras width
	protected int height; // Cameras height
	protected int radius;
	
	public Camera (long id, Vector2f worldPosition, int width, int height, int radius, ShapeType shapeType) {
		this.ID = id;
		this.worldPosition = worldPosition;
		this.centerPosition = new Vector2f(0,0);
		this.width = width;
		this.height = height;
		this.radius = radius;
		this.shapeType = shapeType;
		this.calcCameraCenterPosition();
	}
	
	/**
	 * Calculates the center position of the camera
	 */
	public void calcCameraCenterPosition() {
		float cx, cy;
		cx = (this.width / 2) + this.worldPosition.x;
		cy = (this.height / 2) + this.worldPosition.y;
		this.centerPosition.x = cx;
		this.centerPosition.y = cy;
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
			this.calcCameraCenterPosition();
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

	public ShapeType getShape() {
		return this.shapeType;
	}
}
