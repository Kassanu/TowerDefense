package com.eleventhhour.towerdefense;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.state.StateBasedGame;

public class Tile {
	
	public boolean isBuildable;
	public Tower tower = null;
	public Image tileImage;
	public int tileSize = 32;
	
	public Tile(boolean isBuildable, Image tileImage) {
		this.isBuildable = isBuildable;
		this.tileImage = tileImage;
	}
	
	public void update(GameContainer gc, StateBasedGame sbg, int delta) {
		
	}
	
	public void render(GameContainer gc, Graphics g, int x, int y) {
		g.drawImage(this.tileImage, x, y);
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
}
