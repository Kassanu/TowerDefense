package com.eleventhhour.towerdefense;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

import com.eleventhhour.towerdefense.Level.TileType;

public abstract class Tile {
	
	public Image tileImage;
	public int tileSize = 32;
	public Vector2f position; //position in the tiles array in the level class (NOT ACTUAL XY COORDINATES)
	public TileType tileType;
	
	public Tile(Image tileImage, Vector2f position, TileType tileType) {
		this.tileImage = tileImage;
		this.position = position;
		this.tileType = tileType;
	}
	
	public void update(GameContainer gc, StateBasedGame sbg, int delta) {
		
	}
	
	public void render(GameContainer gc, Graphics g, int x, int y) {
		this.tileImage.draw(x,y,TowerDefense.SCALE);
	}
	
	public Vector2f getPosition() {
		return this.position;
	}

	public TileType getTileType() {
		return this.tileType;
	}

}
