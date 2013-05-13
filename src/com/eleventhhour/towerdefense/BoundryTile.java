package com.eleventhhour.towerdefense;

import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Vector2f;

import com.eleventhhour.towerdefense.Level.TileType;

// tile that exists on the edge of the map and is not part of the enemy path, non-buildable
public class BoundryTile extends Tile {

	public BoundryTile(long id, Image tileImage, Vector2f tilePosition, Vector2f worldPosition, TileType tileType) {
		super(id, tileImage, tilePosition, worldPosition, tileType);
	}

}
