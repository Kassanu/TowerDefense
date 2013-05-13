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

import com.eleventhhour.towerdefense.Level.TileType;

public class GameplayState extends BasicGameState implements MouseListener, KeyListener {
	
	private int stateId; // id number of this game state
	private Level level; // class that contains the data for the level that the player selects
	private TowerManager towerManager; // manages tower updating, rendering, and manipulation
	private WaveManager waveManager; // manages wave creation and timing
	private EnemyManager enemyManager; // manages enemy updating, rendering, and manipulation
	private Camera camera; // controls what parts of the game are shown on the game screen
	public static enum GameState {NORMAL, PAUSED, WIN, LOSE, PLACE}; // different states within this game state
	public GameState currentState = GameState.NORMAL; // the current state of this game state
	public GameGUI gui; // the GUI of this game state
	public gameOverGUI gameOverUI; // the GUI used when the game ends (win / lose)
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
	// initializes the variables of this game state when the game enters this state
	public void enter(GameContainer gc, StateBasedGame sbg) throws SlickException {
		this.currentState = GameState.NORMAL;
		try {
			this.camera = new Camera(0,new Vector2f(0,00), new Vector2f(0,0), 20 * TowerDefense.TILESIZE, 16 * TowerDefense.TILESIZE, 0, this);
			this.setLevel(new Level(this, gc.getWidth(), gc.getHeight(), new Vector2f(0,00)));
			this.towerManager = new TowerManager();
			this.enemyManager = new EnemyManager(this.getLevel());
			this.level.loadMap("res" + File.separator +"levels" + File.separator +"level"+PlayerData.level+ File.separator +"map.tmx");
			this.waveManager = new WaveManager(PlayerData.level);
			this.gui = new GameGUI(this, sbg);
			this.gameOverUI = new gameOverGUI(sbg);
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
	
	@Override
	// renders the components of this game state
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		Vector2f offset = this.getOffset();
		this.level.render(gc, sbg, g, this, offset);
		this.towerManager.render(gc, sbg, g, offset);
		this.enemyManager.render(gc, sbg, g, offset);
		this.gui.render(gc, sbg, g);
		switch (this.currentState) {
			case WIN:
			case LOSE:
				this.gameOverUI.render(gc, sbg, g);
			break;
		}
	}

	@Override
	// updates all the components of this game state
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		// Input used to detect where the mouse cursor is
		Input i = gc.getInput();
		int mx = i.getMouseX();
		int my = i.getMouseY();
		// sets the hover tile as the tile that the mouse is currently hovering over
		this.getLevel().setHover(this.getLevel().getTileGridPosition(mx,my));
		// this game state does different things when this game state is in different states
		switch (this.currentState) {
			// this state is used when the player is trying to build(place) a tower
			case PLACE:
				// if the player clicks the left mouse button
				if (i.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
					// and it's on the map
					if(my <= 512){
						// set the hover tile
						Tile hoverTile = this.getLevel().getHoverTile();
						// if the hover tile is a buildable tile type
						if (hoverTile.getTileType() == TileType.BUILDABLE){
							// and is available to build on
							if (((BuildableTile)hoverTile).isBuildable()){
								// attempt to build a tower, depending on the tower type
								if(this.gui.buildS && this.towerManager.addTower(this.level, hoverTile, "ST")){
									this.currentState = GameState.NORMAL;
									this.gui.resetButtons();
								}
								else if(this.gui.buildR && this.towerManager.addTower(this.level, hoverTile, "RT")){
									this.currentState = GameState.NORMAL;
									this.gui.resetButtons();
								}
								else if(this.gui.buildM && this.towerManager.addTower(this.level, hoverTile, "MGT")){
									this.currentState = GameState.NORMAL;
									this.gui.resetButtons();
								}
							}
						}
					}
				}
			// normal state is where nothing significant is happening, and we want everything to update and render
			// place state falls through to normal state because it is like a special case of the normal state
			case NORMAL:
				// update each of the components of this game state
				this.camera.update(gc, sbg, this, delta);
				this.level.update(gc, sbg, delta);		
				this.waveManager.update(gc, sbg, this.enemyManager, delta);
				this.enemyManager.update(gc, sbg, this, delta);
				this.towerManager.update(gc, sbg, this, delta);
				this.gui.update(gc, sbg, delta);
				// if the player presses the left mouse button on the map
				if (i.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
					if(my <= 512){
						Tile hoverTile = this.getLevel().getHoverTile();
						// attempt to get the tower that is being clicked on and display its info in the game GUI
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
				// if the game is paused, only keep the game GUI functioning
				this.gui.update(gc, sbg, delta);
				break;
			case WIN:
				// set boolean value to win
				this.gameOverUI.setWin(true);
				break;
			case LOSE:
				// set boolean value to lose
				this.gameOverUI.setWin(false);
				break;
		}
		// if the player's health goes below zero, they lose
		if (PlayerData.health <= 0) {
			this.currentState = GameState.LOSE;
		}
		// if the player's health is above zero and there are no more waves and all enemies are dead, they win
		else if (this.waveManager.isFinished() && this.enemyManager.isFinished())
			this.currentState = GameState.WIN;
		
	}

	@Override
	// returns the id number of this state
	public int getID() {
		return this.stateId;
	}

	// returns the level object of this game state
	public Level getLevel() {
		return level;
	}

	// set the level object of this game state
	public void setLevel(Level level) {
		this.level = level;
	}
	
	// returns the TowerManager object of this game state
	public TowerManager getTowerManager() {
		return this.towerManager;
	}
	
	// returns the WaveManager object of this game state
	public WaveManager getWaveManager() {
		return this.waveManager;
	}
	
	// passes the keyPressed method of KeyListener to the game GUI
	public void keyPressed(int key, char c){
		this.gui.keyPressed(key, c);
	}	
	
	// passes the mouseClicked method of MouseListener to the game GUI
	// passing the values depends on the current state of this game state
	public void mouseClicked(int button, int x, int y, int clickCount) {
		switch (this.currentState) {
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
	
}
