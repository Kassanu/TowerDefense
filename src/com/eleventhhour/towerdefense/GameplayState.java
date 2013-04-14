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
	private int playerHealth = 5;
	private int playerMoney = 0;
	private int playerScore = 0;
	
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
			this.level.loadMap("res/levels/test.tmx");
			this.waveManager = new WaveManager(1);
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
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		//update the camera first so all the following calculations will be correct
		this.camera.update(gc, sbg, this, delta);
		//System.out.println(this.camera.getWorldPosition());
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
					this.towerManager.addTower(this.level, hoverTile);
				else if (!((BuildableTile)hoverTile).isBuildable())
					this.towerManager.removeTower(hoverTile);
		} 
		
		/**
		 * Update game systems.
		 */
		this.level.update(gc, sbg, delta);		
		//this.waveManager.update(gc, sbg, this.enemyManager, delta);
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
	
	public void mouseWheelMoved(int change) {
		if (change > 0)
			TowerDefense.SCALE += 1;
		if (change < 0 && TowerDefense.SCALE > 1)
			TowerDefense.SCALE -= 1;
	}
	
	public void mouseDragged(int oldx, int oldy, int newx, int newy) {
		float dx = (newx - oldx) *(-1);
		float dy = (newy - oldy) * (-1);
		this.camera.moveCamera(new Vector2f(dx, dy));
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
