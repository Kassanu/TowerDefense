package com.eleventhhour.towerdefense;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

/**
 * Tile effects are status for a tile.  They will be applied to all enemies on a tile.
 * 
 * Effects can range from damage, to modifing stats.
 * 
 * @author Matt
 *
 */
public interface ITileEffect {

	public void update(GameContainer gc, StateBasedGame sbg, GameplayState gs, int delta);
	public void render(GameContainer gc, Graphics g, Vector2f offset);
	public void calcCenterPosition();
	public long getId();
	public Vector2f getWorldPosition(); //x,y coords in the game world
	public Vector2f getCenterPosition();
	public Vector2f getTilePosition();  //x,y coords of the tile
	public int getWidth();
	public int getHeight();
	public int getRadius();
	//public EffectType 	
	
}
