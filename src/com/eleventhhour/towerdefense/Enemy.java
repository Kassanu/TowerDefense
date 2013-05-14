package com.eleventhhour.towerdefense;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

import com.eleventhhour.towerdefense.Collision.CollisionShape;
import com.eleventhhour.towerdefense.TileEffect.EffectType;

public class Enemy implements GameObject {
	
	/**
	 * Default settings array for enemies
	 * 
	 * These are loading in from the enemyPref.txt when a map is selected.
	 * 
	 * Each index in the array is for a specific type of enemy. they are as follows:
	 * 
	 * 0 - soldier
	 * 1 - juggernaut
	 * 2 - jeep
	 * 3 - tank
	 * 
	 * Each enemy array has indexes that correspond to an enemies stats, they are as follows:
	 * 0 = health
	 * 1 = speed
	 * 2 = scoreReward
	 * 3 = moneyReward
	 * 4 = spriteGroup (where in the sprite sheet their sprite is.  Each enemy has 4 rows of sprites so their position on the sprite group is always ((4*spriteGroup) + aniType) * tileSize)
	 * 5 = aniTotalDuration
	 */
	private static double DEFAULTVALUES[][];

	private static Image spriteSheet = null;
	
	private long ID; // id of the enemy
	protected Collidable collidable; // used for collision detection
	protected Vector2f worldPosition; // absolute position of the enemy on the map
	protected Vector2f centerPosition; // center position of the enemy on the map
	protected Vector2f tilePosition; // position of the tile that the enemy is on
	// dimensions of the enemy
	protected int width;
	protected int height;
	protected int radius;
	protected int health; // the health of the enemy
	protected float maxSpeed; // the normal speed of the enemy
	protected float currentSpeed; // the current speed of the enemy (less than max speed if enemy is being slowed
	protected int scoreReward; // the score reward the player recieves for killing the enemy
	protected int moneyReward; // money amount the player recieves for killing the enemy
	Vector2f movement; // distance the enemy has moved since the last update
	Waypoint waypoint; // waypoint the enemy is moving towards
	int waypointNumber = 0; // number of the waypoint in the waypoint array
	protected int animationFrame; //current frame being animated
	protected int aniType; //The type of animation up,left,down,right
	protected int aniTotalDuration; //Total duration of animationFrame
	protected int aniCurrentDuration; //current duration of this animationFrame
	protected int spriteGroup; //group on the sprite sheet this enemy gets it's sprite from
	
	public Enemy() {
		this.ID = 0;
		this.worldPosition = null;
		this.tilePosition = null;
		this.width = 0;
		this.height = 0;
		this.radius = 0;
		this.collidable = null;
		this.health = 0;
		this.maxSpeed = 0;
		this.currentSpeed = 0;
		this.scoreReward = 0;
		this.moneyReward = 0;
		this.aniType = 0;
		this.spriteGroup = 0;
		this.animationFrame = 0;
		this.aniType = 0;
		this.aniCurrentDuration = 0;
		this.aniTotalDuration = 0;
		this.waypoint = null;
		if (Enemy.spriteSheet == null) {
			try {
				Enemy.spriteSheet = new Image("res" + File.separator +"enemysheet.png");
			} catch (SlickException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
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
		this.maxSpeed = (float) Enemy.DEFAULTVALUES[type][1];
		this.scoreReward = (int) Enemy.DEFAULTVALUES[type][2];
		this.moneyReward = (int) Enemy.DEFAULTVALUES[type][3];
		this.spriteGroup = (int) Enemy.DEFAULTVALUES[type][4];
		this.animationFrame = 0;
		this.aniType = 0;
		this.aniCurrentDuration = 0;
		this.aniTotalDuration = (int) Enemy.DEFAULTVALUES[type][5];
		this.waypoint = waypoint;
		this.waypointNumber = 0;
		this.calcCenterPosition();
		this.collidable = new Collidable(this, CollisionShape.RECTANGLE, new Vector2f(centerPosition.x - 1, centerPosition.y - 1), 2, 2, 2);
	}
	
	public void update(GameContainer gc, StateBasedGame sbg, GameplayState gs, int delta){
		Tile tile = gs.getLevel().getTileAtGridPosition(this.tilePosition);
		//System.out.println(tile);
		HashMap<Long, TileEffect> effects = tile.getTileEffects();
		this.currentSpeed = this.maxSpeed;
		//System.out.println(effects);
		for (Entry<Long, TileEffect> effect : effects.entrySet()) {
			switch (effect.getValue().getEffectType()) {
				case DAMAGE:
					this.getAttacked((int) effect.getValue().getModifier());
					break;
				case SPEED:
					this.currentSpeed /= (int) effect.getValue().getModifier(); 
					break;
			}
		}
		this.movement = this.waypoint.getCollidable().getCenterPosition().copy();
		this.movement = this.movement.sub(this.centerPosition);	
		// check the movement  of the enemy to determine which way they are facing
		// checking if the aniType matches should ensure that the if will fail quickly and prevent the duration from reseting every tick
		//why are floats so painful?
		if (this.aniType != 2 && this.movement.x > 0.0) {
			this.aniType = 2;
			this.aniCurrentDuration = 0;
		}
		else if (this.aniType != 3 && this.movement.x < 0.0) {
			this.aniType = 3;
			this.aniCurrentDuration = 0;
		}
		else if (this.aniType != 1 && this.movement.y > 0.0) {
			this.aniType = 1;
			this.aniCurrentDuration = 0;
		}
		else if (this.aniType != 0 && this.movement.y < 0.0) {
			this.aniType = 0;
			this.aniCurrentDuration = 0;
		}
		this.movement = this.movement.normalise();
		this.movement = this.movement.scale(this.currentSpeed / delta);
		this.worldPosition = this.worldPosition.add(this.movement);
		this.calcCenterPosition();
		this.tilePosition = gs.getLevel().getTileGridPosition(this.centerPosition).copy();
		this.collidable.update(movement);
		if (this.checkAtWaypoint()) {
			
			//hopefully resolve enemies being off the path and not turning properly?? 
			 this.worldPosition = this.waypoint.getWorldPosition().copy();
			 this.calcCenterPosition();
			
			if (gs.getLevel().isLastWaypoint(this.waypointNumber)) {
				PlayerData.decreaseHealth(1);
				PlayerData.resetMultiplier();
				this.health = 0;
			}
			else {
				this.waypoint = gs.getLevel().requestNextWaypoint(++this.waypointNumber);
			}
		}
		
		//sprite stuff
		this.aniCurrentDuration += delta;
			
		
		if (this.aniCurrentDuration >= this.aniTotalDuration) {
			this.animationFrame = ((this.animationFrame+1) % 4);
			this.aniCurrentDuration = 0;
		}
		
	}
	
	public boolean checkAtWaypoint() {
		return Collision.collide(this, waypoint);
	}
	
	public void render(GameContainer gc, Graphics g, Vector2f offset){
		//render sprite by drawing the spritesheet at the correct position denoted by x = animationFrame * 32, y = ((4*spriteGroup) + aniType) * tileSize
		float x = (this.worldPosition.x + offset.x) * TowerDefense.SCALE;
		float y = (this.worldPosition.y + offset.y) * TowerDefense.SCALE;
		float srcx = this.animationFrame * TowerDefense.TILESIZE;
		float srcy = ((4 * this.spriteGroup) + this.aniType) * TowerDefense.TILESIZE;
		
		g.drawImage(Enemy.spriteSheet,x ,y,x + TowerDefense.TILESIZE, y + TowerDefense.TILESIZE,  srcx, srcy, srcx + TowerDefense.TILESIZE, srcy + TowerDefense.TILESIZE);
		
		//g.draw(new Rectangle((this.worldPosition.x + offset.x) * TowerDefense.SCALE,(this.worldPosition.y + offset.y) * TowerDefense.SCALE, this.width * TowerDefense.SCALE, this.height * TowerDefense.SCALE));
	}
	
	public void getAttacked(int damage) {
		this.health -= damage;
		if (this.isDead())
			this.onDeath();
	}
	
	public boolean isDead() {
		return this.health <= 0;
	}
	
	public void onDeath() {
		PlayerData.increaseMoney(this.moneyReward);
		PlayerData.increaseScore(this.scoreReward);
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
		return "Enemy [_ID=" + ID + ", health=" + health + ", speed=" + maxSpeed
				+ ", scoreReward=" + scoreReward + ", worldPosition=" + worldPosition
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

	public int getHealth() {
		return this.health;
	}
	
	/**
	 * setDefaults -
	 * 
	 * Sets the DEFAULTVALUES array from the supplied array list.
	 * 
	 * @param prefList
	 */
	public static void setDefaults(ArrayList<double[]> prefList) {
		Enemy.DEFAULTVALUES = new double[prefList.size()][6];
		
		for (int i = 0; i < Enemy.DEFAULTVALUES.length; i++) {
			Enemy.DEFAULTVALUES[i] = prefList.get(i);
		}
		
	}
	
}

class EnemyHealthComparator implements Comparator<Enemy> {
    public int compare(Enemy e1, Enemy e2) {
        return (int) (e1.getHealth() - e2.getHealth());
    }
}
