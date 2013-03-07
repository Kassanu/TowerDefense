package com.eleventhhour.towerdefense;

import java.io.FileNotFoundException;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import com.eleventhhour.towerdefense.Level.TileType;

public class GameplayState extends BasicGameState {
	
	private int stateId;
	private Level level;
	private TowerManager towerManager;
	public Enemy testEnemy;
	
	
	public GameplayState(int stateId) {
		super();
		this.stateId = stateId;
		this.level = new Level(TowerDefense.width, TowerDefense.height);
		this.towerManager = new TowerManager();
	}

	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		try {
			this.level.loadMap("res/levels/test.tmx");
			Tile tile = this.level.getTileAt(2,5);
			System.out.println("tile: " + tile.getPosition().toString());
			//this.towerManager.addTower(tile);
			Vector2f enemyStart = this.level.getCenter(this.level.startpoint);
			Vector2f enemyWaypoint = this.level.getCenter(this.level.path[0]); 
			System.out.println(enemyStart.toString());
			System.out.println(enemyWaypoint.toString());
			this.testEnemy = new Enemy(0,enemyStart, enemyWaypoint);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		this.level.render(gc, sbg, g);
		this.towerManager.render(gc, sbg, g);
		this.testEnemy.render(gc, g);
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		
		/**
		 * Handle input
		 * 
		 * Might want to change this to an Input System, will allow multiple input interfaces and key bindings
		 */
		Input i = gc.getInput();
		int mx = i.getMouseX();
		int my = i.getMouseY();
		this.level.setHover(this.level.getTilePosition(mx,my));
		if (i.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
			Tile hoverTile = this.level.getHoverTile();
			if (hoverTile.getTileType() == TileType.BUILDABLE)
				if (((BuildableTile)hoverTile).isBuildable())
					this.towerManager.addTower(hoverTile);
				else if (!((BuildableTile)hoverTile).isBuildable())
					this.towerManager.removeTower(hoverTile);
		} 
		
		/**
		 * Update game systems.
		 */
		this.level.update(gc, sbg, delta);
		this.towerManager.update(gc, sbg, delta);
		this.testEnemy.update(gc, sbg, this.level, delta);
	}

	@Override
	public int getID() {
		return this.stateId;
	}
	
	
	
}
