package com.eleventhhour.towerdefense;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;

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
				//System.out.println("Level: " + i + "," + j);
				//System.out.println("Tile: " + this.tiles[i][j].getPosition().toString());
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
		ArrayList<Vector2f> visitedNodes = new ArrayList<Vector2f>();
		ArrayList<Vector2f> listPath = new ArrayList<Vector2f>();
		Vector2f current = this.startpoint;
		Vector2f[] walkVectors = {new Vector2f(0,-1), new Vector2f(1,0), new Vector2f(0,1), new Vector2f(-1,0)};
		
		
		while (!current.equals(this.endpoint)) {
			boolean pathFound = false;
			for (Vector2f dir : walkVectors) {
				Vector2f workingCurrent = current.copy().add(dir);
				if (pathFound) {
					continue;
				}
				
				Tile tile = this.getTileAt((int)workingCurrent.x, (int)workingCurrent.y);
				
				//check if tile is a path, and if true check if we have visited this tile.
				//if we haven't visited the tile add the tile to listPath and set pathfound to true
				if (tile.getTileType() == TileType.PATH) {
					if (!visitedNodes.contains(workingCurrent)) {
						visitedNodes.add(workingCurrent.copy());
						listPath.add(workingCurrent.copy());
						pathFound = true;
						current = workingCurrent.copy();
					}
				}
			}
		}
		
		
		this.path = listPath.toArray(new Vector2f[listPath.size()]);
		System.out.println(Arrays.toString(this.path));
	}
	
	public void update(GameContainer gc, StateBasedGame sbg, int delta) {

	}

	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		for (int i = 0; i < this.tiles.length; i++) {
			for (int j = 0; j < this.tiles[i].length; j++) {
				this.tiles[i][j].render(gc, g, i * TowerDefense.SCALEDTILESIZE, j * TowerDefense.SCALEDTILESIZE);
				if (i == this.hoverTile.x && j == this.hoverTile.y) {
					if (this.tiles[i][j].getTileType() == TileType.BUILDABLE && ((BuildableTile) this.tiles[i][j]).isBuildable())
						g.setColor(Color.blue);
					else
						g.setColor(Color.red);
					g.drawRect(i * this.tileWidth * TowerDefense.SCALE, j * TowerDefense.SCALEDTILESIZE, this.tileWidth * TowerDefense.SCALE - 1, TowerDefense.SCALEDTILESIZE - 1);
				}
				
				if (i == this.startpoint.x && j == this.startpoint.y) {
					g.setColor(new Color(0,127,4));
					//g.drawRect(i * this.tileWidth * TowerDefense.SCALE, j * this.tileHeight * TowerDefense.SCALE, this.tileWidth * TowerDefense.SCALE - 1, this.tileHeight * TowerDefense.SCALE - 1);
					g.drawRoundRect(i * TowerDefense.SCALEDTILESIZE, j * TowerDefense.SCALEDTILESIZE, TowerDefense.SCALEDTILESIZE - 1, TowerDefense.SCALEDTILESIZE - 1,64);
				}
				
				if (i == this.endpoint.x && j == this.endpoint.y) {
					g.setColor(new Color(255,0,0));
					//g.drawRect(i * this.tileWidth * TowerDefense.SCALE, j * this.tileHeight * TowerDefense.SCALE, this.tileWidth * TowerDefense.SCALE - 1, this.tileHeight * TowerDefense.SCALE - 1);
					g.drawRoundRect(i * TowerDefense.SCALEDTILESIZE, j * TowerDefense.SCALEDTILESIZE, TowerDefense.SCALEDTILESIZE - 1, TowerDefense.SCALEDTILESIZE - 1,64);
				}
			}
		}
	}
	
	/**
	 * getTileAt
	 * 
	 * @param x - x position in tile grid
	 * @param y - y position in tile grid
	 * @return returns the tile at x,y in the grid
	 */
	public Tile getTileAt(int x, int y) {
		if (this.tiles == null || this.tiles.length < x || this.tiles[0].length < y)
			return null;
		
		return this.tiles[x][y];
	}
	
	/**
	 * getTile
	 * @param x - x position on map
	 * @param y - y position on map
	 * @return returns the tile that is on the map at x, y
	 */
	public Tile getTile(int x, int y) {
		if (this.tiles == null)
			return null;
		
		return this.tiles[(int)(x / TowerDefense.SCALEDTILESIZE)][(int)(y / TowerDefense.SCALEDTILESIZE)];
	}
	
	public Vector2f getTilePosition(Vector2f vec) {
		System.out.println(vec.toString());
		return this.getTilePosition((int) vec.x, (int) vec.y);
	}
	
	public Vector2f getTilePosition(int x, int y) {
		return new Vector2f(x / TowerDefense.SCALEDTILESIZE,y / TowerDefense.SCALEDTILESIZE);
	}
	
	public Vector2f getTileXYPosition(Vector2f vec) {
		return this.getTileXYPosition((int) vec.x, (int) vec.y);
	}
	
	public Vector2f getTileXYPosition(Tile tile) {
		return this.getTileXYPosition((int) tile.position.x, (int) tile.position.y);
	}

	public Vector2f getTileXYPosition(int x, int y) {
		return new Vector2f(x * TowerDefense.SCALEDTILESIZE,y * TowerDefense.SCALEDTILESIZE);
	}
	
	public Vector2f getCenter(Vector2f vec) {
		return this.getCenter((int)vec.x, (int)vec.y);
	}
	
	public Vector2f getCenter(int x, int y) {
		Vector2f pos = this.getTileXYPosition(x, y);
		return new Vector2f(pos.x + TowerDefense.SCALEDTILESIZE / 2, pos.y + TowerDefense.SCALEDTILESIZE / 2);
	}
	
	public void setHover(Vector2f hoverTile) {
		this.hoverTile = hoverTile;		
	}
	
	public Vector2f getHover() {
		return this.hoverTile;
	}
	
	public Tile getHoverTile() {
		return this.tiles[(int) this.hoverTile.x][(int) this.hoverTile.y];
	}

	public Vector2f requestNextWaypoint(int waypointNumber) {
		return this.path[waypointNumber].copy();		
	}
	
}
