package com.eleventhhour.towerdefense;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.tiled.TiledMap;

import com.eleventhhour.towerdefense.Collision.CollisionShape;

public class Level {
	
	public Tile[][] tiles;// 2-d array of tiles on the map
	public Vector2f startpoint = null, endpoint = null, screenPosition = null; // positions of the screen and start and end point of the map
	public int width, height; // dimensions of the map
	public Vector2f hoverTile = new Vector2f(-1,-1); // position of the current hover tile
	public Waypoint[] waypoints = null; // array of the waypoints for the enemies to move towards
	public Vector2f[] path = null; // array of positions of the tiles in the enemy path
	public GameplayState gs; // reference to the game state that uses this class
	public enum TileType {BUILDABLE, PATH, BOUNDRY}; // different types of tiles
	
	public Level(GameplayState gs, int width, int height, Vector2f screenPosition) {
		this.gs = gs;
		this.width = width;
		this.height = height;
		this.screenPosition = screenPosition;
	}
	
	// loads the map data
	public void loadMap(String path) throws Exception {
		File level = new File(path);
		// if the given file path is not a level
		if (!level.exists()) {
			throw new FileNotFoundException("Map not found!");
		}
		
		TiledMap tiledmap = null;
		
		try {
			tiledmap = new TiledMap(path); // create new tiled map from the path
		} catch (SlickException e) {
			e.printStackTrace();
		}
		
		// get the dimensions of the tiled map to create the 2-d array of the tiles in the map
		int mapWidth = tiledmap.getWidth();
		int mapHeight = tiledmap.getHeight();
		
		this.tiles = new Tile[mapWidth][mapHeight];
		long tileId = 0;
		for (int i = 0; i < mapWidth; i++) {
			for (int j = 0; j < mapHeight; j++) {
				// read the map file and create the map
				// each tile in the map has different variables to signify different things such as tile type
				int tileID = tiledmap.getTileId(i, j, 0);
				String type = tiledmap.getTileProperty(tileID, "type", "false");
				// create the map from different tile types
				if (type.equals("buildable")) {
					//create buildabletile
					this.tiles[i][j] = new BuildableTile(tileId++, tiledmap.getTileImage(i, j, 0), new Vector2f(i,j), this.getTileWorldPosition(i, j), TileType.BUILDABLE);
				}
				else if (type.equals("path")) {
					//create PathTile
					this.tiles[i][j] = new PathTile(tileId++, tiledmap.getTileImage(i, j, 0), new Vector2f(i,j), this.getTileWorldPosition(i, j), TileType.PATH);
				}
				else if (type.equals("boundry")) {
					//create BoundryTile
					this.tiles[i][j] = new BoundryTile(tileId++, tiledmap.getTileImage(i, j, 0), new Vector2f(i,j), this.getTileWorldPosition(i, j), TileType.BOUNDRY);
				}
				//determine if start or end waypoint
				tileID = tiledmap.getTileId(i, j, 1);
				
				String waypoint = tiledmap.getTileProperty(tileID, "waypoint", "false");
				// determine if its a waypoint, and if its the end or starting waypoint
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
		
		// throw an exception if map does not have a start and an end point
		if (this.startpoint == null || this.endpoint == null)
			throw new Exception("Levels must have a start and end point");
		// after map data is loaded properly, create the enemy path
		findPath();
	}
	
	/**
	 * findPath
	 * 
	 * - will determine the path that enemies will take.
	 * - Uses modified Dijkstra algorithm to visit each tile starting from the starting point
	 * 	 to determine the path the enemies will take to reach the end of the map.
	 * - starts from the start point
	 * 
	 */
	public void findPath() {
		ArrayList<Vector2f> visitedNodes = new ArrayList<Vector2f>();
		ArrayList<Waypoint> listWaypoints = new ArrayList<Waypoint>();
		ArrayList<Vector2f> listPath = new ArrayList<Vector2f>();
		Vector2f current = this.startpoint;
		visitedNodes.add(current.copy());
		listPath.add(current.copy());
		long waypointId = 0;
		Vector2f lastDirection = null; //the last direction of walking.  When this changes we are at a corner and it is time to put a waypoint
		Vector2f[] walkVectors = {new Vector2f(0,-1), new Vector2f(1,0), new Vector2f(0,1), new Vector2f(-1,0)};
		
		while (!current.equals(this.endpoint)) {
			boolean pathFound = false;
			for (Vector2f dir : walkVectors) {
				if (pathFound) {
					continue; //c ontinue through the loop no need to keep checking.
				}
				Vector2f workingCurrent = current.copy().add(dir); // the workingCurrent is the node we are looking at.
				
				Tile tile = this.getTileAtGridPosition(workingCurrent);
				
				// check if tile is a path, and if true check if we have visited this tile.
				// if we haven't visited the tile add the tile to listPath and set pathfound to true
				if (tile.getTileType() == TileType.PATH) {
					if (!visitedNodes.contains(workingCurrent)) {
						visitedNodes.add(workingCurrent.copy());
						// if lastDirection isn't null (i.e) we haven't gotten to our first corner yet
						// AND last direction isn't equal to the new direction we found add it to the way points
						// What this means is that we have found a corner.
						if (!(lastDirection == null) && !lastDirection.equals(dir)) {
							listWaypoints.add(new Waypoint(waypointId++, this.getTileWorldPosition(current.x, current.y), this.getTileGridPosition(current), (TowerDefense.TILESIZE), (TowerDefense.TILESIZE), 0));
						}
						else if (workingCurrent.equals(this.endpoint)) {
							// FIXME:	This line needs to be here to add the end point to the waypoints
							//			This probably should be fixed if there is time and we figure out a better way to do this.
							listWaypoints.add(new Waypoint(waypointId++, this.getTileWorldPosition(workingCurrent.x, workingCurrent.y), this.getTileGridPosition(workingCurrent), (TowerDefense.TILESIZE), (TowerDefense.TILESIZE), 0)); 
						}
						listPath.add(workingCurrent.copy());
						((PathTile) tile).setPathPosition(listPath.size() - 1);
						lastDirection = dir.copy();
						pathFound = true;
						current = workingCurrent.copy();
					}
				}
			}
		}
		
		
		this.waypoints = listWaypoints.toArray(new Waypoint[listWaypoints.size()]);
		this.path = listPath.toArray(new Vector2f[listPath.size()]);
	}
	
	// updates the tiles in the level
	public void update(GameContainer gc, StateBasedGame sbg, int delta) {
		//Tile temp = this.getTileAtGridPosition(this.hoverTile);
		//System.out.println(temp.getEnemiesOnTile().toString());
		for (int i = 0; i < this.tiles.length; i++) {
			for (int j = 0; j < this.tiles[i].length; j++) {
				this.tiles[i][j].update(gc, sbg, gs, delta);
			}
		}
	}

	// renders all the tiles that are visible
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g, GameplayState gs, Vector2f offset) throws SlickException {
		for (int i = 0; i < this.tiles.length; i++) {
			for (int j = 0; j < this.tiles[i].length; j++) {
				if (gs.getCamera().isInView(this.tiles[i][j]))
					this.tiles[i][j].render(gc, g, offset);
				if (i == (int)this.hoverTile.x && j == (int)this.hoverTile.y) {
					if (this.tiles[i][j].getTileType() == TileType.BUILDABLE && ((BuildableTile) this.tiles[i][j]).isBuildable())
						g.setColor(Color.blue);
					else
						g.setColor(Color.red);
					g.drawRect(((i * TowerDefense.TILESIZE) + offset.x) * TowerDefense.SCALE, ((j * TowerDefense.TILESIZE) + offset.y) * TowerDefense.SCALE, TowerDefense.TILESIZE * TowerDefense.SCALE - 1, (TowerDefense.TILESIZE * TowerDefense.SCALE) - 1);
				}
				/*
				if (i == this.startpoint.x && j == this.startpoint.y) {
					g.setColor(new Color(0,127,4));
					g.drawRoundRect(((i * TowerDefense.TILESIZE) + offset.x) * TowerDefense.SCALE, ((j * TowerDefense.TILESIZE) + offset.y) * TowerDefense.SCALE, (TowerDefense.TILESIZE * TowerDefense.SCALE) - 1, (TowerDefense.TILESIZE * TowerDefense.SCALE) - 1,64);
				}
				
				if (i == this.endpoint.x && j == this.endpoint.y) {
					g.setColor(new Color(255,0,0));
					g.drawRoundRect(((i * TowerDefense.TILESIZE) + offset.x) * TowerDefense.SCALE, ((j * TowerDefense.TILESIZE) + offset.y) * TowerDefense.SCALE, (TowerDefense.TILESIZE * TowerDefense.SCALE) - 1, (TowerDefense.TILESIZE * TowerDefense.SCALE) - 1,64);
				}*/
			}
		}
		
		/*for (Waypoint waypoint : this.waypoints) {
			waypoint.render(gc, g, offset);
		}*/
		
	}
	
	/**
	 * getTileAtGridPosition
	 * 
	 * Returns the tile at the position in the grid passed.
	 * 
	 * X,Y corresponds to the tile's GRID POSITION not WORLD POSITION.
	 * To find a tile at a world position use getTileAtWorldPosition
	 * 
	 * @param x - x position in tile grid
	 * @param y - y position in tile grid
	 * @return returns the tile at x,y in the grid
	 */
	public Tile getTileAtGridPosition(int x, int y) {
		if (this.tiles == null || this.tiles.length < x || this.tiles[0].length < y)
			return null;
		
		return this.tiles[x][y];
	}
	
	/**
	 * getTileAtGridPosition
	 * 
	 * Returns the tile at the position in the grid passed.
	 * 
	 * pos corresponds to the tile's GRID POSITION not WORLD POSITION.
	 * To find a tile at a world position use getTileAtWorldPosition
	 * 
	 * @param pos - GRID POSITION of tile you wish to get
	 * @return returns the tile at x,y in the grid
	 */
	public Tile getTileAtGridPosition(Vector2f pos) {
		if (this.tiles == null || this.tiles.length < pos.x || this.tiles[0].length < pos.y)
			return null;
		
		return this.getTileAtGridPosition((int)pos.x,(int)pos.y);
	}
	
	/**
	 * getTileAtWorldPosition
	 * 
	 * Returns the tile at the world position on the map.
	 * 
	 * X,Y corresponds to the tile's WORLD POSITION not GRID POSITION
	 * To find a tile at a grid position use getTileAtGridPosition
	 * 
	 * @param x - x position on map
	 * @param y - y position on map
	 * @return returns the tile that is on the map at x, y
	 */
	public Tile getTileAtWorldPosition(float x, float y) {
		if (this.tiles == null)
			return null;
		
		return this.getTileAtGridPosition((int)(x / (TowerDefense.TILESIZE * TowerDefense.SCALE)), (int)(y / (TowerDefense.TILESIZE * TowerDefense.SCALE)));
	}
	
	/**
	 * getTileAtWorldPosition
	 * 
	 * Returns the tile at the world position on the map.
	 * 
	 * pos corresponds to the tile's WORLD POSITION not GRID POSITION
	 * To find a tile at a grid position use getTileAtGridPosition
	 * 
	 * @param pos - Vector2f of the WORLD POSITION you wish to get the tile at
	 * @return returns the tile that is on the map at x, y
	 */
	public Tile getTileAtWorldPosition(Vector2f pos) {
		if (this.tiles == null)
			return null;
		
		return this.getTileAtWorldPosition(pos.x,pos.y);
	}
	
	/**
	 * getTileWorldPosition
	 * 
	 * Returns the tiles TOP LEFT WORLD POSITION in vector form
	 * 
	 * @param x - x GRID POSITION of tile
	 * @param y - y GRID POSITION of tile
	 * @return a Vector of the tile's TOP LEFT WORLD POSITION
	 */
	public Vector2f getTileWorldPosition(float x, float y) {
		return new Vector2f(x * (TowerDefense.TILESIZE),y * (TowerDefense.TILESIZE));
	}
	
	/**
	 * getTileWorldPosition
	 * 
	 * Returns the tiles TOP LEFT WORLD POSITION in vector form
	 * 
	 * @param tile - tile to find position of
	 * @return a Vector of the tile's TOP LEFT WORLD POSITION
	 */
	public Vector2f getTileWorldPosition(Tile tile) {
		return this.getTileWorldPosition(tile.tilePosition.x,tile.tilePosition.y);
	}
	
	/**
	 * getTileGridPosition
	 * 
	 * Returns a tiles GRID POSITION from the passed WORLD POSITION
	 * 
	 * @param x - WORLD POSITION to find tile's GRID POSITION
	 * @param y - WORLD POSITION to find tile's GRID POSITION
	 * @return a vector of the tile's GRID POSITION
	 */
	public Vector2f getTileGridPosition(float x, float y) {
		Vector2f offset = this.gs.getOffset();
		return new Vector2f((x - (offset.x * TowerDefense.SCALE)) / (TowerDefense.TILESIZE * TowerDefense.SCALE),(y - (offset.y * TowerDefense.SCALE)) / (TowerDefense.TILESIZE * TowerDefense.SCALE));
	}
	
	/**
	 * getTileGridPosition
	 * 
	 * Returns a tiles GRID POSITION from the passed WORLD POSITION
	 * 
	 * @param pos - WORLD POSITION to find tile's GRID POSITION
	 * @return a vector of the tile's GRID POSITION
	 */
	public Vector2f getTileGridPosition(Vector2f pos) {
		return this.getTileGridPosition(pos.x, pos.y);
	}
	
	/**
	 * getCenter
	 * 
	 * This method calculates the tiles center WORLD POSITION
	 * from it's GRID POSITION
	 * 
	 * @param x - GRID POSITION of tile
	 * @param y - GRID POSITION of tile
	 * @return a vector containing the center of the tile IN WORLD POSITION
	 */
	public Vector2f getCenter(int x, int y) {
		Vector2f pos = this.getTileWorldPosition(x, y);
		return new Vector2f(pos.x + (TowerDefense.TILESIZE) / 2, pos.y + (TowerDefense.TILESIZE) / 2);
	}
	
	// sets the hover tile given the position of a tile
	public void setHover(Vector2f hoverTile) {
		this.hoverTile = hoverTile;		
	}
	
	// returns the position current hover tile
	public Vector2f getHover() {
		return this.hoverTile;
	}
	
	// returns the Tile that is being hovered over
	public Tile getHoverTile() {
		return this.tiles[(int) this.hoverTile.x][(int) this.hoverTile.y];
	}

	// returns the waypoint requested
	public Waypoint requestNextWaypoint(int waypointNumber) {
		return this.waypoints[waypointNumber];		
	}

	// returns a Tile array that represents the path tiles that are within the range of a given tower's position
	public Tile[] getAttackableTiles(Vector2f position, int range) {
		ArrayList<Tile> attackable = new ArrayList<Tile>();
		int from = Math.abs(range) * -1;
		int to = Math.abs(range); // prevents given a negative range;
		for (int i = from; i <= to; i++) {
			for ( int j = from; j <= to; j++) {
				if ((i+(int)position.x) > 0 && (i+(int)position.x < this.tiles.length) && (j+(int)position.y > 0) && (j+(int)position.y < this.tiles[0].length)) {
					Tile temp = this.getTileAtGridPosition(i+(int)position.x, j+(int)position.y);
					if (temp.getTileType() == TileType.PATH)
						attackable.add(temp);
				}
			}
		}
		
		Collections.sort(attackable, new Comparator<Tile>() {
		    public int compare(Tile tile1, Tile tile2) {
				return ((PathTile)tile1).getPathPosition() - ((PathTile)tile2).getPathPosition();   
		    }
		});
					
		return attackable.toArray(new Tile[attackable.size()]);
	}

	// adds an enemy to the enemy array of the tile that it is on
	public void addEnemyToTile(Enemy enemy) {
		Tile temp = this.getTileAtWorldPosition(enemy.getCenterPosition());
		if (temp != null) {
			temp.addEnemyToTile(enemy);
		}
	}

	public boolean isLastWaypoint(int waypointNumber) {
		return waypointNumber == this.waypoints.length - 1;
	}

	public Waypoint getWaypoint(int i) {
		return this.waypoints[0];
	}

	public Vector2f getOffset() {
		return (this.screenPosition.copy()).scale(TowerDefense.SCALE);
	}
	
}
