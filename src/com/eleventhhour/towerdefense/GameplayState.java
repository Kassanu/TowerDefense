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
	private int playerHealth = 5;
	private int playerMoney = 0;
	private int playerScore = 0;
	
	public GameplayState(int stateId) {
		super();
		this.stateId = stateId;
		this.setLevel(new Level(TowerDefense.width, TowerDefense.height));
		this.towerManager = new TowerManager();
		this.enemyManager = new EnemyManager(this.getLevel());
	}

	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		try {
			this.getLevel().loadMap("res/levels/test.tmx");
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
		
		if (this.playerHealth <= 0) {
			//trigger game over
			System.out.println("GAME OVER");
			System.exit(0);
		}
		
		/**
		 * Handle input
		 * 
		 * Might want to change this to an Input System, will allow multiple input interfaces and key bindings
		 */
		Input i = gc.getInput();
		int mx = i.getMouseX();
		int my = i.getMouseY();
		this.getLevel().setHover(this.getLevel().getTileGridPosition(mx,my));
		if (i.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
			Tile hoverTile = this.getLevel().getHoverTile();
			if (hoverTile.getTileType() == TileType.BUILDABLE)
				if (((BuildableTile)hoverTile).isBuildable())
					this.towerManager.addTower(this.getLevel(), hoverTile);
				else if (!((BuildableTile)hoverTile).isBuildable())
					this.towerManager.removeTower(hoverTile);
		} 
		
		/**
		 * Update game systems.
		 */
		this.level.update(gc, sbg, delta);		
		this.waveManager.update(gc, sbg, this.enemyManager, delta);
		this.enemyManager.update(gc, sbg, this, delta);
		this.towerManager.update(gc, sbg, this, delta);
	}

	@Override
	public int getID() {
		return this.stateId;
	}

	public Level getLevel() {
		return level;
	}

	public void setLevel(Level level) {
		this.level = level;
	}

	public void decreasePlayerHealth() {
		this.playerHealth--;
	}
	
	public TowerManager getTowerManager() {
		return this.towerManager;
	}
	
}
