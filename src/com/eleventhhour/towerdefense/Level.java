package com.eleventhhour.towerdefense;

import java.io.File;
import java.io.FileNotFoundException;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.tiled.TiledMap;

public class Level {
	
	public Tile[][] tiles;
	public int width, height, tileWidth = 32, tileHeight = 32;
	public Vector2f hoverTile = new Vector2f(-1,-1);
	
	public Level(int widht, int height) {
		this.width = width;
		this.height = height;
	}
	
	public void loadMap(String path) throws FileNotFoundException {
		File level = new File(path);
		if (!level.exists()) {
			throw new FileNotFoundException("Map not found!");
		}
		
		TiledMap tiledmap = null;
		
		try {
			tiledmap = new TiledMap(path);
		} catch (SlickException e) {
			e.printStackTrace();
		}
		
		int mapWidth = tiledmap.getWidth();
		int mapHeight = tiledmap.getHeight();
		System.out.println("");
		this.tiles = new Tile[mapWidth][mapHeight];
		
		for (int i = 0; i < mapWidth; i++) {
			for (int j = 0; j < mapHeight; j++) {
				int tileID = tiledmap.getTileId(i, j, 0);
				String buildable = tiledmap.getTileProperty(tileID, "buildable", "false");
				this.tiles[i][j] = new Tile((buildable.equals("true")?true:false), tiledmap.getTileImage(i, j, 0), new Vector2f(i,j));
			}
		}
		
	}
	
	public void update(GameContainer gc, StateBasedGame sbg, int delta) {
		Input i = gc.getInput();
		int mx = i.getMouseX();
		int my = i.getMouseY();
		this.hoverTile = this.getTilePosition(mx, my);
	}

	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		for (int i = 0; i < this.tiles.length; i++) {
			for (int j = 0; j < this.tiles[i].length; j++) {
				this.tiles[i][j].render(gc, g, i * this.tileWidth * TowerDefense.SCALE, j * this.tileHeight * TowerDefense.SCALE);
				if (i == this.hoverTile.x && j == this.hoverTile.y) {
					if (this.tiles[i][j].isBuildable())
						g.setColor(Color.blue);
					else
						g.setColor(Color.red);
					g.drawRect(i * this.tileWidth * TowerDefense.SCALE, j * this.tileHeight * TowerDefense.SCALE, this.tileWidth * TowerDefense.SCALE - 1, this.tileHeight * TowerDefense.SCALE - 1);
				}
					
			}
		}
	}
	
	public Tile getTile(int x, int y) {
		if (this.tiles == null)
			return null;
		return this.tiles[x / (this.tileWidth * TowerDefense.SCALE)][y / (this.tileHeight * TowerDefense.SCALE)];
	}
	
	public Vector2f getTilePosition(int x, int y) {
		return new Vector2f(x / (this.tileWidth * TowerDefense.SCALE),y / (this.tileHeight * TowerDefense.SCALE));
	}
	
	public Vector2f getTileXYPosition(Tile tile) {
		return null;
	}
}
