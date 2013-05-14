package com.eleventhhour.towerdefense;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

import com.eleventhhour.towerdefense.Collision.CollisionShape;
import com.eleventhhour.towerdefense.Level.TileType;

public abstract class Tile implements GameObject {
	
	private long ID; // id of the tile
	private Image tileImage; // image of the tile
	protected Collidable collidable; // for collision detection
	protected Vector2f tilePosition; //position in the tiles array in the level class (NOT ACTUAL XY COORDINATES)
	protected Vector2f worldPosition; // absolute position of the tile on the map
	protected Vector2f centerPosition; // center position of the tile
	protected TileType tileType; // the tile type of this tile
	protected ArrayList<Enemy> enemiesOnTile; // arraylist of the enemies that are currently on this tile
	protected HashMap<Long,TileEffect> tileEffects; // hash map of the effects active on this tile
	public ArrayList<Long> tileEffectsToBeRemoved; // effects to be removed from the tile
	// dimensions of the tile
	protected int width;
	protected int height;
	protected int radius;
	
	// initializes the variables in this class
	public Tile(long id, Image tileImage, Vector2f tilePosition, Vector2f worldPosition, TileType tileType) {
		this.ID = id;
		this.tileImage = tileImage;
		this.tilePosition = tilePosition;
		this.worldPosition = worldPosition;
		this.centerPosition = new Vector2f(0,0);
		this.tileType = tileType;
		this.enemiesOnTile = new ArrayList<Enemy>();
		this.tileEffects = new HashMap<Long,TileEffect>();
		this.tileEffectsToBeRemoved = new ArrayList<Long>();
		this.width = TowerDefense.TILESIZE;
		this.height = TowerDefense.TILESIZE;
		this.radius = TowerDefense.TILESIZE;
		this.collidable = new Collidable(this, CollisionShape.RECTANGLE, this.worldPosition, TowerDefense.TILESIZE, TowerDefense.TILESIZE, TowerDefense.TILESIZE);
		this.calcCenterPosition();
	}
	
	// updates the components in this class
	public void update(GameContainer gc, StateBasedGame sbg, GameplayState gs, int delta) {
		//this.collidable.update(gc, sbg, gs, delta);
		for (Entry<Long, TileEffect> effect : this.tileEffects.entrySet()) {
			effect.getValue().update(gc, sbg, gs, delta);
			if (effect.getValue().isOver()) {
	        	this.tileEffectsToBeRemoved.add(effect.getKey());
			}
		}
		if (!this.tileEffectsToBeRemoved.isEmpty()) {
			for (Long eId : this.tileEffectsToBeRemoved)
				this.removeEffect(eId);
			this.tileEffectsToBeRemoved.clear();
		}
		this.enemiesOnTile.clear();
	}
	
	// renders the components of the tile
	public void render(GameContainer gc, Graphics g, Vector2f offset) {
		//((i * TowerDefense.TILESIZE) - offset.x) * TowerDefense.SCALE, ((j * TowerDefense.TILESIZE) - offset.y) * TowerDefense.SCALE
		this.tileImage.draw((this.worldPosition.x + offset.x) * TowerDefense.SCALE,(this.worldPosition.y + offset.y) * TowerDefense.SCALE,TowerDefense.SCALE);
		for (Entry<Long, TileEffect> effect : this.tileEffects.entrySet()) {
			effect.getValue().render(gc, g, offset);
		}
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
	
	public HashMap<Long, TileEffect> getTileEffects() {
		return this.tileEffects;
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

	public void addEffect(TileEffect tileEffect) {
		this.tileEffects.put(tileEffect.getId(), tileEffect);		
	}
	
	public void removeEffect(Long eId) {
		this.tileEffects.remove(eId);
	}
	
}

class TileComparator implements Comparator<Tile> {
    public int compare(Tile t1, Tile t2) {
        return (int) (t1.getId() - t2.getId());
    }
}