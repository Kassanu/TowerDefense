package com.eleventhhour.towerdefense;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

public class Enemy {
	
	/**
	 * Default settings array for enemies
	 * 
	 * Each index in the array is for a specific type of enemy.
	 * 
	 * 
	 */
	private static final double DEFAULTVALUES[][] = {
		{20.0,5,100.0},
		{20.0,0.5,100.0}
	};
	
	private int _ID;
	public int health;
	public float speed;
	public int reward;
	Vector2f position;
	Vector2f movement;
	Vector2f waypoint;
	int waypointNumber = 0;
	//Image image;
	
	public Enemy(int type, Vector2f position, Vector2f waypoint) {
		this.health = (int) Enemy.DEFAULTVALUES[type][0];
		this.speed = (float) Enemy.DEFAULTVALUES[type][1];
		this.reward = (int) Enemy.DEFAULTVALUES[type][2];;
		this.position = position;
		this.waypoint = waypoint;
	}
	
	public void update(GameContainer gc, StateBasedGame sbg, Level level, int delta){
		this.movement = this.waypoint.copy();
		//System.out.println(this.movement.toString());
		this.movement = this.movement.sub(this.position);
		//System.out.println(this.movement.toString());
		this.movement = this.movement.normalise();
		//System.out.println(this.movement.toString());
		this.movement = this.movement.scale(this.speed / delta);
		//System.out.println(this.movement.toString());
		this.position = this.position.add(this.movement);
		//System.out.println(this.movement.toString());
		//System.out.println(this.position.toString());
		if (this.checkAtWaypoint()) {
			this.waypoint = level.getCenter(level.requestNextWaypoint(++waypointNumber));
		}
	}
	
	public boolean checkAtWaypoint() {
		int leftEnemy, leftWaypoint;
	    int rightEnemy, rightWaypoint;
	    int topEnemy, topWaypoint;
	    int bottomEnemy, bottomWaypoint;
	    leftEnemy = (int) this.position.x;
	    rightEnemy = (int) (this.position.x + 2);
	    topEnemy = (int) this.position.y;
	    bottomEnemy = (int) (this.position.y + 2);
	    leftWaypoint = (int) this.waypoint.x;
	    rightWaypoint = (int) (this.waypoint.x + 5);
	    topWaypoint = (int) this.waypoint.y;
	    bottomWaypoint = (int) (this.waypoint.y + 5);
	    
	  //If any of the sides from A are outside of B
	    if( bottomEnemy <= topWaypoint ) {
	    
	        return false;
	    }
	    
	    if( topEnemy >= bottomWaypoint )
	    {
	        return false;
	    }
	    
	    if( rightEnemy <= leftWaypoint )
	    {
	        return false;
	    }
	    
	    if( leftEnemy >= rightWaypoint )
	    {
	        return false;
	    }
	    
		return true;
	}
	
	public void render(GameContainer gc, Graphics g){
		g.drawRect(this.position.x - (TowerDefense.SCALEDTILESIZE / 2), this.position.y - (TowerDefense.SCALEDTILESIZE / 2), TowerDefense.SCALEDTILESIZE, TowerDefense.SCALEDTILESIZE);
		g.drawRect(this.position.x, this.position.y, 2, 2);
		
	}
	
}
