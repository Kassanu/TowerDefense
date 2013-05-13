package com.eleventhhour.towerdefense;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

import com.eleventhhour.towerdefense.Collision.CollisionShape;

public class Bullet implements GameObject {

	private long ID; // id of the bullet
	protected Collidable target; // used for collision detection
	protected Collidable collidable; // used for collision detection
	protected Vector2f worldPosition; // absolute position of the bullet on the map
	protected Vector2f centerPosition; // absolute center position of the bullet on the map
	protected Vector2f tilePosition; // position of the tile that the bullet is on
	// dimensions of the bullet
	protected int width; 
	protected int height;
	protected int radius;
	public float speed; // speed at which the bullet moves
	public int health, life;
	protected Vector2f movement;
	protected Tile targetTile; // tile that the bullet is targeting
	private int damage; // amount of damage done by the bullet
	
	public Bullet() {
		this.ID = 0;
		this.worldPosition = null;
		this.tilePosition = null;
		this.width = 4;
		this.height = 4;
		this.radius = 2;
		this.speed = 100;
		this.health = 0;
		this.life = 0;
		this.target = null;
		this.collidable = null;
		this.targetTile = null;
		this.damage = 0;
	}
	
	/**
	 * This constructor is just for testing, when you allocate an enemy from the pool
	 * use the setProperties method to set the enemy before adding it to the field
	 */
	public Bullet(long id, Vector2f worldPosition, Vector2f tilePosition, int width, int height, int radius, Vector2f target, Tile targetTile, int damage) {
		this.ID = id;
		this.worldPosition = worldPosition;
		this.centerPosition = new Vector2f(0,0);
		this.tilePosition = tilePosition;
		this.width = width;
		this.height = height;
		this.radius = radius;
		this.health = 1;
		this.life = 1000; //1 second
		this.calcCenterPosition();
		this.collidable = new Collidable(this, CollisionShape.RECTANGLE, new Vector2f(centerPosition.x - 2, centerPosition.y - 2), 4, 4, 2);
		this.target = new Collidable(this, CollisionShape.RECTANGLE, new Vector2f(target.x - 2, target.y - 2), 4, 4, 2);
		this.targetTile = targetTile;
		this.damage = damage;
	}
	
	public void init(long id, Vector2f worldPosition, Vector2f target, Tile targetTile, int damage) {
		this.ID = id;
		this.worldPosition = worldPosition;
		this.centerPosition = new Vector2f(0,0);
		this.health = 1;
		this.life = 1000; //1 second
		this.calcCenterPosition();
		this.collidable = new Collidable(this, CollisionShape.RECTANGLE, new Vector2f(this.centerPosition.x - 2, this.centerPosition.y - 2), 4, 4, 2);
		this.target = new Collidable(this, CollisionShape.RECTANGLE, new Vector2f(target.x - 2, target.y - 2), 4, 4, 2);
		this.targetTile = targetTile;
		this.damage = damage;
	}
	
	public void update(GameContainer gc, StateBasedGame sbg, GameplayState gs, int delta) {
		this.life -= delta;
		this.movement = this.target.getCollidable().getCenterPosition().copy();
		this.movement = this.movement.sub(this.centerPosition);
		this.movement = this.movement.normalise();
		this.movement = this.movement.scale(this.speed / delta);
		this.worldPosition = this.worldPosition.add(this.movement);
		this.calcCenterPosition();
		this.collidable.update(movement);
		if (this.life <= 0 || Collision.collide(this, this.target)) {
			this.health = 0;
			this.targetTile.addEffect(new TileEffect(TileEffect.EffectType.DAMAGE, this.damage, 200, this.targetTile));
		}
	}

	public void render(GameContainer gc, Graphics g, Vector2f offset) {
		g.setColor(Color.blue);
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

	public boolean isDead() {
		return this.health <= 0;
	}
}
