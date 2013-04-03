package com.eleventhhour.towerdefense;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

public class EnemyManager {
	
	public static long LASTID = 1;
	
	private EnemyPool enemyPool;
	public Level level;
	public HashMap<Long, Enemy> enemies;
	public ArrayList<Long> toBeRemoved;
	
	public EnemyManager(Level level) {
		this.level = level;
		this.enemyPool = new EnemyPool();
		this.enemies = new HashMap<Long, Enemy>();
		this.toBeRemoved = new ArrayList<Long>();
	}
	
	public EnemyManager(int poolSize) {
		this.enemyPool = new EnemyPool(poolSize);
		this.enemies = new HashMap<Long, Enemy>();
		this.toBeRemoved = new ArrayList<Long>();
	}
	
	public void spawnEnemy(int enemyType) {
		Vector2f enemyStart = this.level.getCenter(this.level.startpoint);
		Vector2f enemyWaypoint = this.level.getCenter(this.level.path[0]);
		Enemy enemy = this.enemyPool.allocate();
		System.out.println(enemy.toString());
		enemy.setProperties(LASTID, enemyType, enemyStart, enemyWaypoint);
		System.out.println(enemy.toString());
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

	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) {
		for (Entry<Long, Enemy> enemy : this.enemies.entrySet()) {
	        enemy.getValue().render(gc, g);
		}
	}

}
