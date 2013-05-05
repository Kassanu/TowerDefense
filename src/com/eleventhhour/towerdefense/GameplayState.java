package com.eleventhhour.towerdefense;

import java.io.FileNotFoundException;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.MouseListener;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import com.eleventhhour.towerdefense.Level.TileType;

public class GameplayState extends BasicGameState implements MouseListener {
	
	private int stateId;
	private Level level;
	private TowerManager towerManager;
	private WaveManager waveManager;
	private EnemyManager enemyManager;
	private Camera camera;
	public static enum GameState {NORMAL, PAUSED, WIN, LOSE, PLACE};
<<<<<<< HEAD
	public GameState currentState = GameState.PLACE;
=======
	public GameState currentState = GameState.NORMAL;
	public GameGUI gui;
>>>>>>> origin/David-dev
	
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
		try {
			this.camera = new Camera(0,new Vector2f(0,00), new Vector2f(0,0), 20 * TowerDefense.TILESIZE, 16 * TowerDefense.TILESIZE, 0, this);
			this.setLevel(new Level(this, gc.getWidth(), gc.getHeight(), new Vector2f(0,00)));
			this.towerManager = new TowerManager();
			this.enemyManager = new EnemyManager(this.getLevel());
			this.level.loadMap("res/levels/level"+PlayerData.level+"/map.tmx");
			this.waveManager = new WaveManager(PlayerData.level);
			this.gui = new GameGUI(this);
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
		this.camera.render(gc, g, offset);
		this.gui.render(gc, sbg, g);
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		
		Input i = gc.getInput();
		int mx = i.getMouseX();
		int my = i.getMouseY();
		this.getLevel().setHover(this.getLevel().getTileGridPosition(mx,my));
		
		switch (this.currentState) {
			case PLACE:
				if (i.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
					if(my <= 512){
						Tile hoverTile = this.getLevel().getHoverTile();
						if (hoverTile.getTileType() == TileType.BUILDABLE)
							if (((BuildableTile)hoverTile).isBuildable()){
								this.towerManager.addTower(this.level, hoverTile);
								this.currentState = GameState.NORMAL;
								this.gui.resetButtons();
							}
							else if (!((BuildableTile)hoverTile).isBuildable())
								this.towerManager.removeTower(hoverTile);
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
				break;
			case WIN:
				break;
			case LOSE:
				break;
		}
		//System.out.println(this.camera.getWorldPosition());
		if (PlayerData.health <= 0) {
			//trigger game over
			System.out.println("GAME OVER");
			System.exit(0);
		}
		
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
	
	public void mouseClicked(int button, int x, int y, int clickCount){
		this.gui.mouseClicked(button, x, y, clickCount);
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
