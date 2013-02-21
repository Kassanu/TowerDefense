package com.eleventhhour.towerdefense;

import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Vector2f;

import com.eleventhhour.towerdefense.Level.TileType;

public class BuildableTile extends Tile {
	
	Tower tower = null;
	
	public BuildableTile(Image tileImage, Vector2f position, TileType tileType) {
		super(tileImage, position, tileType);
	}
	
	public void addTower(Tower t) {
		this.tower = t;
	}
	
	public void removeTower() {
		this.tower = null;
	}
	
	public boolean isBuildable() {
		return this.tower == null;
	}
}
