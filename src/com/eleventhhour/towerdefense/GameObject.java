package com.eleventhhour.towerdefense;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

/**
 * GameObject --
 * 
 * This is the interface that all game objects should come from.  It sets up the basic functions needed for those classes.
 * @author Matt Martucciello
 *
 */

public interface GameObject {
	
	public enum ShapeType {
		RECTANGLE, CIRCLE
	}
	public void update(GameContainer gc, StateBasedGame sbg, GameplayState gs, int delta);
	public void render(GameContainer gc, Graphics g);
	public void init();
	public long getId();
	public Vector2f getWorldPosition(); //x,y coords in the game world
	public Vector2f getTilePosition();  //x,y coords of the tile
	public int getWidth();
	public int getHeight();
	public int getRadius();
	public ShapeType getShape();
	
}


