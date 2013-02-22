package com.eleventhhour.towerdefense;

import java.io.FileNotFoundException;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class GameplayState extends BasicGameState {
	
	private int stateId;
	private Level level;
	private TowerManager towerManager;
	
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
			this.towerManager.addTower(tile);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		this.level.render(gc, sbg, g);
		this.towerManager.render(gc, sbg, g);
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		this.level.update(gc, sbg, delta);
		this.towerManager.update(gc, sbg, delta);
	}

	@Override
	public int getID() {
		return this.stateId;
	}
	
	
	
}
