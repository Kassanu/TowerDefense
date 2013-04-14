package com.eleventhhour.towerdefense;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

import com.eleventhhour.towerdefense.Level.TileType;

public class BuildableTile extends Tile {
	
	Tower tower = null;
	
	public BuildableTile(Image tileImage, Vector2f tilePosition, Vector2f worldPosition, TileType tileType) {
		super(tileImage, tilePosition, worldPosition, tileType);
	}
	
	public void addTower(Tower t) {
		this.tower = t;
	}
	
	public void removeTower() {
		this.tower = null;
	}
	
	public Tower getTower() {
		return this.tower;
	}
	
	public boolean isBuildable() {
		return this.tower == null;
	}

}
