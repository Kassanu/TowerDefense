package com.eleventhhour.towerdefense;

import java.util.ArrayList;
import java.util.Comparator;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

import com.eleventhhour.towerdefense.Collision.CollisionShape;
import com.eleventhhour.towerdefense.Level.TileType;

public abstract class Tile implements GameObject {
	
	private long ID;
	private Image tileImage;
	protected Collidable collidable;
	protected Vector2f tilePosition; //position in the tiles array in the level class (NOT ACTUAL XY COORDINATES)
	protected Vector2f worldPosition;
	protected Vector2f centerPosition;
	protected TileType tileType;
	protected ArrayList<Enemy> enemiesOnTile;
	protected int width;
	protected int height;
	protected int radius;
	
	public Tile(long id, Image tileImage, Vector2f tilePosition, Vector2f worldPosition, TileType tileType) {
		this.ID = id;
		this.tileImage = tileImage;
		this.tilePosition = tilePosition;
		this.worldPosition = worldPosition;
		this.centerPosition = new Vector2f(0,0);
		this.tileType = tileType;
		this.enemiesOnTile = new ArrayList<Enemy>();
		this.width = TowerDefense.TILESIZE;
		this.height = TowerDefense.TILESIZE;
		this.radius = TowerDefense.TILESIZE;
		this.collidable = new Collidable(this, CollisionShape.RECTANGLE, this.worldPosition, TowerDefense.TILESIZE, TowerDefense.TILESIZE, TowerDefense.TILESIZE);
		this.calcCenterPosition();
	}
	
	public void update(GameContainer gc, StateBasedGame sbg, GameplayState gs, int delta) {
		//this.collidable.update(gc, sbg, gs, delta);
		this.enemiesOnTile.clear();
	}
	
	public void render(GameContainer gc, Graphics g, Vector2f offset) {
		//((i * TowerDefense.TILESIZE) - offset.x) * TowerDefense.SCALE, ((j * TowerDefense.TILESIZE) - offset.y) * TowerDefense.SCALE
		this.tileImage.draw((this.worldPosition.x + offset.x) * TowerDefense.SCALE,(this.worldPosition.y + offset.y) * TowerDefense.SCALE,TowerDefense.SCALE);
	}
	
	public Vector2f getPosition() {
		return this.tilePosition;
	}

	public TileType getTileType() {
		return this.tileType;
	}
	
	public void addEnemyToTile(Enemy enemy) {
		this.enemiesOnTile.add(enemy);
	}
	
	public ArrayList<Enemy> getEnemiesOnTile() {
		return this.enemiesOnTile;
	}
	
	@Override
	public String toString() {
		return "Tile [position=" + tilePosition + "]";
	}
	
	public void init() {}
	

	
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
	
}

class TileComparator implements Comparator<Tile> {
    public int compare(Tile t1, Tile t2) {
        return (int) (t1.getId() - t2.getId());
    }
}