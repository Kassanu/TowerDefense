package com.eleventhhour.towerdefense;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

public class Tile {
	
	public boolean isBuildable;
	public Tower tower = null;
	public Image tileImage;
	public int tileSize = 32;
	public Vector2f position;
	
	public Tile(boolean isBuildable, Image tileImage, Vector2f position) {
		this.isBuildable = isBuildable;
		this.tileImage = tileImage;
		this.position = position;
	}
	
	public void update(GameContainer gc, StateBasedGame sbg, int delta) {
		
	}
	
	public void render(GameContainer gc, Graphics g, int x, int y) {
		this.tileImage.draw(x,y,TowerDefense.SCALE);
	}
	
	public void removeTower() {
		this.isBuildable = false;
		this.tower = null;
	}
	
	public void addTower(Tower tower) {
		this.isBuildable = false;
		this.tower = tower;
	}
	
	public Tower getTower() {
		return this.tower;
	}
	
	public boolean isBuildable() {
		return this.isBuildable;
	}

	public Vector2f getPosition() {
		return this.position;
	}

}
