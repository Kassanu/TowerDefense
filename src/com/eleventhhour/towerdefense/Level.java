package com.eleventhhour.towerdefense;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

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
	public Vector2f startpoint = null, endpoint = null;
	public int width, height, tileWidth = 32, tileHeight = 32;
	public Vector2f hoverTile = new Vector2f(-1,-1);
	public Vector2f[] path = null;
	
	public enum TileType {BUILDABLE, PATH, BOUNDRY};
	
	public Level(int widht, int height) {
		this.width = width;
		this.height = height;
	}
	
	public void loadMap(String path) throws Exception {
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
		
		this.tiles = new Tile[mapWidth][mapHeight];
		
		for (int i = 0; i < mapWidth; i++) {
			for (int j = 0; j < mapHeight; j++) {
				int tileID = tiledmap.getTileId(i, j, 0);
				String type = tiledmap.getTileProperty(tileID, "type", "false");
				
				if (type.equals("buildable")) {
					//create buildabletile
					this.tiles[i][j] = new BuildableTile(tiledmap.getTileImage(i, j, 0), new Vector2f(i,j), TileType.BUILDABLE);
				}
				else if (type.equals("path")) {
					//create PathTile
					this.tiles[i][j] = new PathTile(tiledmap.getTileImage(i, j, 0), new Vector2f(i,j), TileType.PATH);
				}
				else if (type.equals("boundry")) {
					//create BoundryTile
					this.tiles[i][j] = new BoundryTile(tiledmap.getTileImage(i, j, 0), new Vector2f(i,j), TileType.BOUNDRY);
				}
				
				//this.tiles[i][j] = new Tile((buildable.equals("true")?true:false), tiledmap.getTileImage(i, j, 0), new Vector2f(i,j));
				
				//determine if start or end waypoint
				tileID = tiledmap.getTileId(i, j, 1);
				
				String waypoint = tiledmap.getTileProperty(tileID, "waypoint", "false");
				
				if (waypoint.equals("true")) {
					waypoint = tiledmap.getTileProperty(tileID, "start", "false");
					if (waypoint.equals("true")) {
						this.startpoint = new Vector2f(i,j);
					}
					else {
						waypoint = tiledmap.getTileProperty(tileID, "end", "false");
						if (waypoint.equals("true")) {
							this.endpoint = new Vector2f(i,j);
						}
					}
				}
				
			}
		}
		
		if (this.startpoint == null || this.endpoint == null)
			throw new Exception("Levels must have a start and end point");
		
		((BuildableTile) this.tiles[2][5]).addTower(new MGtower(this.tiles[2][5]));
		System.out.println(getTileXYPosition(this.tiles[2][5]).toString());
		findPath();
	}
	
	/**
	 * findPath
	 * 
	 * -will determine the path that enemies will take.  Uses modified Dijkstra algorithm to visit each tile starting from the starting point to determine the path the
	 * enemies will take to reach the end of the map.
	 * first start from the start point
	 * 
	 */
	public void findPath() {
		
	}
	
	public void update(GameContainer gc, StateBasedGame sbg, int delta) {
		Input i = gc.getInput();
		int mx = i.getMouseX();
		int my = i.getMouseY();
		System.out.println("MX: " + mx);
		System.out.println("MY: " + my);
		this.hoverTile = this.getTilePosition(mx, my);
	}

	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		for (int i = 0; i < this.tiles.length; i++) {
			for (int j = 0; j < this.tiles[i].length; j++) {
				this.tiles[i][j].render(gc, g, i * this.tileWidth * TowerDefense.SCALE, j * this.tileHeight * TowerDefense.SCALE);
				if (i == this.hoverTile.x && j == this.hoverTile.y) {
					if (this.tiles[i][j].getTileType() == TileType.BUILDABLE && ((BuildableTile) this.tiles[i][j]).isBuildable())
						g.setColor(Color.blue);
					else
						g.setColor(Color.red);
					g.drawRect(i * this.tileWidth * TowerDefense.SCALE, j * this.tileHeight * TowerDefense.SCALE, this.tileWidth * TowerDefense.SCALE - 1, this.tileHeight * TowerDefense.SCALE - 1);
				}
				
				if (i == this.startpoint.x && j == this.startpoint.y) {
					g.setColor(new Color(0,127,4));
					//g.drawRect(i * this.tileWidth * TowerDefense.SCALE, j * this.tileHeight * TowerDefense.SCALE, this.tileWidth * TowerDefense.SCALE - 1, this.tileHeight * TowerDefense.SCALE - 1);
					g.drawRoundRect(i * this.tileWidth * TowerDefense.SCALE, j * this.tileHeight * TowerDefense.SCALE, this.tileWidth * TowerDefense.SCALE - 1, this.tileHeight * TowerDefense.SCALE - 1,64);
				}
				
				if (i == this.endpoint.x && j == this.endpoint.y) {
					g.setColor(new Color(255,0,0));
					//g.drawRect(i * this.tileWidth * TowerDefense.SCALE, j * this.tileHeight * TowerDefense.SCALE, this.tileWidth * TowerDefense.SCALE - 1, this.tileHeight * TowerDefense.SCALE - 1);
					g.drawRoundRect(i * this.tileWidth * TowerDefense.SCALE, j * this.tileHeight * TowerDefense.SCALE, this.tileWidth * TowerDefense.SCALE - 1, this.tileHeight * TowerDefense.SCALE - 1,64);
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
		return new Vector2f(tile.position.x * (this.tileWidth * TowerDefense.SCALE),tile.position.y * (this.tileHeight * TowerDefense.SCALE));
	}
}
