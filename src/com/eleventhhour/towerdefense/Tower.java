package com.eleventhhour.towerdefense;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

public abstract class Tower {
	
	public final long ID;
	
	public Tile position;
	public Vector2f worldPosition;
	public Vector2f centerPosition;
	public Tile[] attackable = null;
	protected Level level = null;
	public int cost = 0;
	public int range = 0;
	public int damage = 0;
	public int firerate = 0;
	public int cooldown = 0;
	public String color = "FFFFFF";
	
	public Tower(long lASTID, Tile pos, Level level){
		this.ID = lASTID;
		this.position = pos;
		this.level = level;
		this.worldPosition = this.level.getTileWorldPosition(this.position);
		this.centerPosition = new Vector2f();
		this.calcCenterPosition();
	}
	
	public void setAttackable(Tile[] attack){
		this.attackable = attack;
	}
	
	public void init(){
		
	}
	
	/**
	 * Calculates the center position
	 */
	public void calcCenterPosition() {
		this.centerPosition.x = (TowerDefense.TILESIZE / 2) + this.worldPosition.x;
		this.centerPosition.y = (TowerDefense.TILESIZE / 2) + this.worldPosition.y;
	}
	
	public void update(GameContainer gc, StateBasedGame sbg, GameplayState gs, int delta){
		if (this.cooldown <= 0) {
			Enemy enemy = this.selectEnemyToAttack();
			if (enemy != null) {
				gs.getTowerManager().spawnBullet(this.centerPosition.copy(), enemy.getCenterPosition());
				this.attack(enemy);
				this.cooldown = this.firerate;
			}
		}
		else {
			this.cooldown -= delta;
		}
	}
	
	public Enemy selectEnemyToAttack() {
		Enemy enemy = null;
		int i = this.attackable.length - 1;
		Random r = new Random();		
		while (enemy == null && i >= 0) {
			ArrayList<Enemy> enemiesOnTile = this.attackable[i].getEnemiesOnTile();
			if (!enemiesOnTile.isEmpty()) {
				enemy = enemiesOnTile.get(r.nextInt(enemiesOnTile.size()));
			}
			i--;
		}
		return enemy;
	}
	
	public void attack(Enemy target) {
		//System.out.println("Attacking: " + target);
		target.getAttacked(this.damage);
	}
	
	public void render(GameContainer gc, Graphics g, Vector2f offset){
		g.setColor(Color.blue);
		g.drawRect(((this.worldPosition.x - offset.x) * TowerDefense.SCALE) + (((TowerDefense.TILESIZE / 2) * TowerDefense.SCALE) - 5), ((this.worldPosition.y - offset.y ) * TowerDefense.SCALE) + (((TowerDefense.TILESIZE / 2) * TowerDefense.SCALE) - 5), 10, 10);
		//render attackable tiles
		Vector2f attackablePos = null;
		for (Tile attackableTile : this.attackable) {
			attackablePos = this.level.getTileWorldPosition(attackableTile); 
			g.drawRect((attackablePos.x - offset.x) * TowerDefense.SCALE, (attackablePos.y - offset.y) * TowerDefense.SCALE, (TowerDefense.TILESIZE * TowerDefense.SCALE), (TowerDefense.TILESIZE * TowerDefense.SCALE));
		}
	}
	
	public long getId() {
		return this.ID;
	}
}
