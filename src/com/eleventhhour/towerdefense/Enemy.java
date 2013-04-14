package com.eleventhhour.towerdefense;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

import com.eleventhhour.towerdefense.Collision.CollisionShape;

public class Enemy implements GameObject {
	
	/**
	 * Default settings array for enemies
	 * 
	 * Each index in the array is for a specific type of enemy.
	 * 
	 * 
	 */
	private static final double DEFAULTVALUES[][] = {
		{20.0,20,100.0},
		{20.0,40,100.0}
	};
	
	private long ID;
	protected Collidable collidable;
	protected Vector2f worldPosition;
	protected Vector2f centerPosition;
	protected Vector2f tilePosition;
	protected int width;
	protected int height;
	protected int radius;
	public int health;
	public float speed;
	public int reward;
	Vector2f movement;
	Waypoint waypoint;
	int waypointNumber = 0;
	//Image image;
	
	public Enemy() {
		this.ID = 0;
		this.worldPosition = null;
		this.tilePosition = null;
		this.width = 0;
		this.height = 0;
		this.radius = 0;
		this.collidable = null;
		this.health = 0;
		this.speed = 0;
		this.reward = 0;
		this.waypoint = null;
	}
	
	/**
	 * This constructor is just for testing, when you allocate an enemy from the pool
	 * use the setProperties method to set the enemy before adding it to the field
	 */
	public Enemy(long id, int type, Vector2f worldPosition, Vector2f tilePosition, int width, int height, int radius, CollisionShape collisionShape, Waypoint waypoint) {
		this.ID = id;
		this.worldPosition = worldPosition;
		this.centerPosition = new Vector2f(0,0);
		this.tilePosition = tilePosition;
		this.width = width;
		this.height = height;
		this.radius = radius;
		this.health = (int) Enemy.DEFAULTVALUES[type][0];
		this.speed = (float) Enemy.DEFAULTVALUES[type][1];
		this.reward = (int) Enemy.DEFAULTVALUES[type][2];
		this.waypoint = waypoint;
		this.calcCenterPosition();
		this.collidable = new Collidable(this, CollisionShape.RECTANGLE, new Vector2f(centerPosition.x - 2, centerPosition.y - 2), 4, 4, 2);
	}
	
	public void init(long id, int type, Vector2f worldPosition, Vector2f tilePosition, int width, int height, int radius, Waypoint waypoint) {
		this.ID = id;
		this.worldPosition = worldPosition;
		this.centerPosition = new Vector2f(0,0);
		this.tilePosition = tilePosition;
		this.width = width;
		this.height = height;
		this.radius = radius;
		this.health = (int) Enemy.DEFAULTVALUES[type][0];
		this.speed = (float) Enemy.DEFAULTVALUES[type][1];
		this.reward = (int) Enemy.DEFAULTVALUES[type][2];;
		this.waypoint = waypoint;
		this.waypointNumber = 0;
		this.calcCenterPosition();
		this.collidable = new Collidable(this, CollisionShape.RECTANGLE, new Vector2f(centerPosition.x - 2, centerPosition.y - 2), 4, 4, 2);
	}
	
	public void update(GameContainer gc, StateBasedGame sbg, GameplayState gs, int delta){
		this.movement = this.waypoint.getCollidable().getCenterPosition().copy();
		this.movement = this.movement.sub(this.centerPosition);
		this.movement = this.movement.normalise();
		this.movement = this.movement.scale(this.speed / delta);
		this.worldPosition = this.worldPosition.add(this.movement);
		this.calcCenterPosition();
		this.collidable.update(movement);
		if (this.checkAtWaypoint()) {
			if (gs.getLevel().isLastWaypoint(this.waypointNumber)) {
				gs.decreasePlayerHealth();
				this.health = 0;
			}
			else {
				this.waypoint = gs.getLevel().requestNextWaypoint(++this.waypointNumber);
			}
		}
	}
	
	public boolean checkAtWaypoint() {
		return Collision.collide(this, waypoint);
	}
	
	public void render(GameContainer gc, Graphics g, Vector2f offset){
		g.setColor(Color.red);
		this.collidable.render(gc, g, offset);
		g.draw(new Rectangle((this.worldPosition.x + offset.x) * TowerDefense.SCALE,(this.worldPosition.y + offset.y) * TowerDefense.SCALE, this.width * TowerDefense.SCALE, this.height * TowerDefense.SCALE));
	}
	
	public void getAttacked(int damage) {
		this.health -= damage;
	}
	
	public boolean isDead() {
		return this.health <= 0;
	}
	
	/**
	 * Calculates the center position
	 */
	public void calcCenterPosition() {
		this.centerPosition.x = (this.width / 2) + this.worldPosition.x;
		this.centerPosition.y = (this.height / 2) + this.worldPosition.y;
	}

	@Override
	public String toString() {
		return "Enemy [_ID=" + ID + ", health=" + health + ", speed=" + speed
				+ ", reward=" + reward + ", worldPosition=" + worldPosition
				+ ", movement=" + movement + ", waypoint=" + waypoint
				+ ", waypointNumber=" + waypointNumber + "]";
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
	
}
