package com.eleventhhour.towerdefense;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

public class TowerManager {
	
	public static long LASTID = 1;
	public static long LASTBULLETID = 1;
	public HashMap<Long, Tower> towers;
	public HashMap<Long, Bullet> bullets;
	public ArrayList<Long> toBeRemoved;
	private BulletPool bulletPool;
	
	public TowerManager() {
		this.towers = new HashMap<Long, Tower>();
		this.bulletPool = new BulletPool();
		this.bullets = new HashMap<Long, Bullet>();
		this.toBeRemoved = new ArrayList<Long>();
	}
	
	public void addTower(Level level, Tile tile) {
		MGtower t = new MGtower(LASTID, tile, level);
		towers.put(LASTID, t);
		((BuildableTile)tile).addTower(t);
		LASTID++;
	}
	
	public void removeTower(Tile tile) {
		long removeId = ((BuildableTile) tile).getTower().getId();
		towers.remove(removeId);
		((BuildableTile) tile).removeTower();
	}
	
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g, Vector2f offset) throws SlickException {
		for (Entry<Long, Tower> entry : this.towers.entrySet()) {
			entry.getValue().render(gc, g, offset);
		}
		for (Entry<Long, Bullet> bullet : this.bullets.entrySet()) {
			if (((GameplayState) sbg.getCurrentState()).getCamera().isInView(bullet.getValue()))
				bullet.getValue().render(gc, g, offset);
		}
	}

	public void update(GameContainer gc, StateBasedGame sbg, GameplayState gs, int delta) throws SlickException {
		for (Entry<Long, Tower> entry : this.towers.entrySet()) {
			entry.getValue().update(gc, sbg, gs, delta);
		}
		
		for (Entry<Long, Bullet> bullet : this.bullets.entrySet()) {
			bullet.getValue().update(gc, sbg, gs, delta);
	        if (bullet.getValue().isDead()) {
	        	this.toBeRemoved.add(bullet.getKey());
	        }
		}
		if (!this.toBeRemoved.isEmpty()) {
			for (Long bulletId : this.toBeRemoved) {
				this.removeBullet(bulletId);
			}
			this.toBeRemoved.clear();
		}
	}

	public void spawnBullet(Vector2f worldPosition, Vector2f target) {
		Bullet bullet = this.bulletPool.allocate();
		bullet.init(LASTBULLETID, worldPosition, target);
		this.addBullet(bullet);
	}

	public void addBullet(Bullet bulllet) {
		this.bullets.put(LASTBULLETID, bulllet);
		LASTBULLETID++;
	}
	
	private void removeBullet(Long bulletId) {
		Bullet bulllet = this.bullets.get(bulletId);
		this.bulletPool.release(bulllet);
		this.bullets.remove(bulletId);
	}
	
}
