package com.eleventhhour.towerdefense;

import java.util.ArrayList;

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
	protected ArrayList<Enemy> enemiesOnTile;
	
	public Tile(Image tileImage, Vector2f position, TileType tileType) {
		this.tileImage = tileImage;
		this.position = position;
		this.tileType = tileType;
		this.enemiesOnTile = new ArrayList<Enemy>();
	}
	
	public void update(GameContainer gc, StateBasedGame sbg, int delta) {
		this.enemiesOnTile.clear();
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
	
	public void addEnemyToTile(Enemy enemy) {
		this.enemiesOnTile.add(enemy);
	}
	
	public ArrayList<Enemy> getEnemiesOnTile() {
		return this.enemiesOnTile;
	}
	
	@Override
	public String toString() {
		return "Tile [position=" + position + "]";
	}
	
}
