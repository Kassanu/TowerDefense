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
	private WaveManager waveManager;
	private EnemyManager enemyManager;
	
	public GameplayState(int stateId) {
		super();
		this.stateId = stateId;
		this.level = new Level(TowerDefense.width, TowerDefense.height);
		this.towerManager = new TowerManager();
		this.enemyManager = new EnemyManager(this.level);
	}

	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		try {
			this.level.loadMap("res/levels/test2.tmx");
			this.waveManager = new WaveManager(1);
			System.out.println(this.waveManager);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		this.level.render(gc, sbg, g);
		this.towerManager.render(gc, sbg, g);
		this.enemyManager.render(gc, sbg, g);
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
		this.waveManager.update(gc, sbg, this.enemyManager, delta);
		this.enemyManager.update(gc, sbg, delta);
	}

	@Override
	public int getID() {
		return this.stateId;
	}
	
	
	
}
