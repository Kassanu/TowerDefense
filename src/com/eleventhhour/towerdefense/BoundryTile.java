package com.eleventhhour.towerdefense;

import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Vector2f;

import com.eleventhhour.towerdefense.Level.TileType;

public class BoundryTile extends Tile {

	public BoundryTile(Image tileImage, Vector2f tilePosition, Vector2f worldPosition, TileType tileType) {
		super(tileImage, tilePosition, worldPosition, tileType);
	}

}
