package com.eleventhhour.towerdefense;

import java.io.File;
import java.io.FileNotFoundException;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.KeyListener;
import org.newdawn.slick.MouseListener;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import com.eleventhhour.towerdefense.GameplayState.GameState;
import com.eleventhhour.towerdefense.Level.TileType;

public class GameplayState extends BasicGameState implements MouseListener, KeyListener {
	
	private int stateId;
	private Level level;
	private TowerManager towerManager;
	private WaveManager waveManager;
	private EnemyManager enemyManager;
	private Camera camera;
	public static enum GameState {NORMAL, PAUSED, WIN, LOSE, PLACE};
	public static GameState currentState = GameState.NORMAL;
	public GameGUI gui;
	public gameOverGUI gameOverUI;
	/*
	 * gameStates
	 * 
	 * NORMAL - Gameplay is normal, everything updates as usual
	 * PAUSED - Gameplay is paused all managers cease updating
	 * WIN - Player has completed the level, show a pop up with score and option to go back to main menu
	 * LOSE - Player has lost the level, show pop up with score and option to go back to main menu
	 * PLACE - Player is placing a tower,  render tower at players cursor position to show placement
	 */
	
	public GameplayState(int stateId) {
		super();
		this.stateId = stateId;
		
	}

	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {	}
	
	@Override
	public void enter(GameContainer gc, StateBasedGame sbg) throws SlickException {
		GameplayState.currentState = GameState.NORMAL;
		try {
			this.camera = new Camera(0,new Vector2f(0,00), new Vector2f(0,0), 20 * TowerDefense.TILESIZE, 16 * TowerDefense.TILESIZE, 0, this);
			this.setLevel(new Level(this, gc.getWidth(), gc.getHeight(), new Vector2f(0,00)));
			this.towerManager = new TowerManager();
			this.enemyManager = new EnemyManager(this.getLevel());
			this.level.loadMap("res" + File.separator +"levels" + File.separator +"level"+PlayerData.level+ File.separator +"map.tmx");
			this.waveManager = new WaveManager(PlayerData.level);
			this.gui = new GameGUI(this);
			this.gameOverUI = new gameOverGUI(sbg);
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
	
	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		Vector2f offset = this.getOffset();
		this.level.render(gc, sbg, g, this, offset);
		this.towerManager.render(gc, sbg, g, offset);
		this.enemyManager.render(gc, sbg, g, offset);
		this.gui.render(gc, sbg, g);
		switch (GameplayState.currentState) {
			case WIN:
			case LOSE:
				this.gameOverUI.render(gc, sbg, g);
			break;
		}
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		
		Input i = gc.getInput();
		int mx = i.getMouseX();
		int my = i.getMouseY();
		this.getLevel().setHover(this.getLevel().getTileGridPosition(mx,my));
		
		switch (GameplayState.currentState) {
			case PLACE:
				if (i.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
					if(my <= 512){
						Tile hoverTile = this.getLevel().getHoverTile();
						if (hoverTile.getTileType() == TileType.BUILDABLE){
							if (((BuildableTile)hoverTile).isBuildable()){
								if(this.gui.buildS && this.towerManager.addTower(this.level, hoverTile, "ST")){
									GameplayState.currentState = GameState.NORMAL;
									this.gui.resetButtons();
								}
								else if(this.gui.buildR && this.towerManager.addTower(this.level, hoverTile, "RT")){
									GameplayState.currentState = GameState.NORMAL;
									this.gui.resetButtons();
								}
								else if(this.gui.buildM && this.towerManager.addTower(this.level, hoverTile, "MGT")){
									GameplayState.currentState = GameState.NORMAL;
									this.gui.resetButtons();
								}
							}
						}
					}
				}
			case NORMAL:
				this.camera.update(gc, sbg, this, delta);
				this.level.update(gc, sbg, delta);		
				this.waveManager.update(gc, sbg, this.enemyManager, delta);
				this.enemyManager.update(gc, sbg, this, delta);
				this.towerManager.update(gc, sbg, this, delta);
				this.gui.update(gc, sbg, delta);
				if (i.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
					if(my <= 512){
						Tile hoverTile = this.getLevel().getHoverTile();
						if (hoverTile.getTileType() == TileType.BUILDABLE) {
							this.gui.towerselect = ((BuildableTile)hoverTile).getTower();
						}
						else {
							this.gui.towerselect = null;
						}
					}
				}
				break;
			case PAUSED:
				this.gui.update(gc, sbg, delta);
				break;
			case WIN:
				this.gameOverUI.setWin(true);
				break;
			case LOSE:
				this.gameOverUI.setWin(false);
				break;
		}
		if (PlayerData.health <= 0) {
			GameplayState.currentState = GameState.LOSE;
		}
		else if (this.waveManager.isFinished() && this.enemyManager.isFinished())
			GameplayState.currentState = GameState.WIN;
		
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
	
	public TowerManager getTowerManager() {
		return this.towerManager;
	}
	
	public void keyPressed(int key, char c){
		this.gui.keyPressed(key, c);
	}	
	
	public void mouseClicked(int button, int x, int y, int clickCount) {
		switch (GameplayState.currentState) {
		case PAUSED:
		case PLACE:
		case NORMAL:
			this.gui.mouseClicked(button, x, y, clickCount);
			break;
		case WIN:
		case LOSE:
			this.gameOverUI.mouseClicked(button, x, y, clickCount);
			break;
	}
	}
	
	/**
	 * getOffset -
	 * 
	 * Calculates the offset of all renderable objects which is the sum of the
	 * cameras position and the levels position.
	 * 
	 * @return A Vector2f of the current render offset
	 */
	public Vector2f getOffset() {
		Vector2f cameraPos = this.camera.getOffset();
		Vector2f levelPos = this.level.getOffset();
		return new Vector2f((cameraPos.x + levelPos.x), (cameraPos.y + levelPos.y));
	}

	public Camera getCamera() {
		return this.camera;
	}

	public static GameState getCurrentState() {
		return GameplayState.currentState;
	}
	
}
