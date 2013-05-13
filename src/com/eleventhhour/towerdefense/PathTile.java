package com.eleventhhour.towerdefense;

import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Vector2f;

import com.eleventhhour.towerdefense.Level.TileType;

// tile that is part of the enemy path, non-buildable
public class PathTile extends Tile {
	
	int pathPostion = 0;
	
	public PathTile(long id, Image tileImage, Vector2f tilePosition, Vector2f worldPosition, TileType tileType) {
		super(id, tileImage, tilePosition, worldPosition, tileType);
	}
	
	public void setPathPosition(int pathPosition) {
		this.pathPostion = pathPosition;
	}
	
	public int getPathPosition() {
		return this.pathPostion;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "PathTile [pathPostion=" + pathPostion + "]";
	}
	
	
	
}
