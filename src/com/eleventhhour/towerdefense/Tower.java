package com.eleventhhour.towerdefense;

import java.util.ArrayList;
import java.util.Random;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;

public abstract class Tower {
	
	public final long ID;
	
	public Tile position;
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
	}
	
	public void setAttackable(Tile[] attack){
		this.attackable = attack;
	}
	
	public void init(){
		
	}
	
	public void update(GameContainer gc, StateBasedGame sbg, int delta){
		if (this.cooldown <= 0) {
			Enemy enemy = this.selectEnemyToAttack();
			if (enemy != null) {
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
	
	public void render(GameContainer gc, Graphics g){
		g.drawRect((this.position.getPosition().x * this.position.tileSize * TowerDefense.SCALE) + ((16 * TowerDefense.SCALE) - 5), (this.position.getPosition().y * this.position.tileSize * TowerDefense.SCALE) + ((16 * TowerDefense.SCALE) - 5), 10, 10);
	}
	
	public long getId() {
		return this.ID;
	}
}
