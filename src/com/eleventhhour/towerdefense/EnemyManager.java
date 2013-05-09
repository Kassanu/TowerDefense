package com.eleventhhour.towerdefense;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Map.Entry;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

import com.eleventhhour.towerdefense.Collision.CollisionShape;

public class EnemyManager {
	
	public static long LASTID = 1;
	
	private EnemyPool enemyPool;
	public Level level;
	public HashMap<Long, Enemy> enemies;
	public ArrayList<Long> toBeRemoved;
	
	public EnemyManager(Level level) {
		this.level = level;
		this.loadEnemyPref();
		this.enemyPool = new EnemyPool();
		this.enemies = new HashMap<Long, Enemy>();
		this.toBeRemoved = new ArrayList<Long>();
	}
	
	public EnemyManager(int poolSize) {
		this.loadEnemyPref();
		this.enemyPool = new EnemyPool(poolSize);
		this.enemies = new HashMap<Long, Enemy>();
		this.toBeRemoved = new ArrayList<Long>();
	}
	
	/**
	 * loadEnemyPref -
	 * 
	 * loads enemy preferences from a text file.  This allows us to tweak the enemies without having to repack the game.
	 * 
	 */
	private void loadEnemyPref() {
		Scanner scanner = null;
		try {
			scanner = new Scanner(new FileInputStream("res" + File.separator + "enemyPref.txt"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	
		String currentLine;
		String[] currentLineArray;
		ArrayList<double[]> prefList = new ArrayList<double[]>();
		double[] enemyPrefs = new double[6];
		while (scanner.hasNext()) {
			currentLine = scanner.nextLine();
			//if the line starts with a # it's a comment so ignore it.
			if (!(currentLine.substring(0, 1)).equals("#")) {
				currentLineArray = currentLine.split(",");
				for (int i = 0; i < currentLineArray.length; i++) {
					enemyPrefs[i] = Double.parseDouble(currentLineArray[i]);
				}
				prefList.add(Arrays.copyOf(enemyPrefs, 6));
			}
		}
		
		Enemy.setDefaults(prefList);
	}
	
	public void spawnEnemy(int enemyType) {
		Vector2f enemyStart = this.level.getTileWorldPosition(this.level.startpoint.x, this.level.startpoint.y);
		Enemy enemy = this.enemyPool.allocate();
		enemy.init(LASTID, enemyType, enemyStart, this.level.getTileGridPosition(enemyStart), TowerDefense.TILESIZE, TowerDefense.TILESIZE, 2, this.level.getWaypoint(0));
		this.addEnemy(enemy);
	}

	public void addEnemy(Enemy enemy) {
		this.enemies.put(LASTID, enemy);
		LASTID++;
	}
	
	private void removeEnemy(Long enemyId) {
		Enemy enemy = this.enemies.get(enemyId);
		this.enemyPool.release(enemy);
		this.enemies.remove(enemyId);
	}
	
	public void update(GameContainer gc, StateBasedGame sbg, GameplayState gs, int delta) {
		for (Entry<Long, Enemy> enemy : this.enemies.entrySet()) {
	        enemy.getValue().update(gc, sbg, gs, delta);
	        if (enemy.getValue().isDead()) {
	        	this.toBeRemoved.add(enemy.getKey());
	        }
	        else
	        	level.addEnemyToTile(enemy.getValue());
		}
		if (!this.toBeRemoved.isEmpty()) {
			for (Long enemyId : this.toBeRemoved) {
				this.removeEnemy(enemyId);
			}
			this.toBeRemoved.clear();
		}
	}

	public void render(GameContainer gc, StateBasedGame sbg, Graphics g, Vector2f offset) {
		for (Entry<Long, Enemy> enemy : this.enemies.entrySet()) {
			if (((GameplayState) sbg.getCurrentState()).getCamera().isInView(enemy.getValue()))
				enemy.getValue().render(gc, g, offset);
			else {
				System.out.println("Not Rendering");
			}
		}
	}

	public boolean isFinished() {
		return this.enemies.size() == 0;
	}

}
