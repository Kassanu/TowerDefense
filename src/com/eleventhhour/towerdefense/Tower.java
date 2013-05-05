package com.eleventhhour.towerdefense;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.Random;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

public abstract class Tower {
	
	public static Image spriteSheet;
	
	public final long ID;
	
	public Tile position;
	public Vector2f worldPosition;
	public Vector2f centerPosition;
	public Tile[] attackable = null;
	public int attackingTileNum = 0;
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
		
		System.out.println(Arrays.toString(this.attackable));
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
	
	/**
	 * selectEnemyToAttack --
	 * 
	 * The tower will select an enemy from the last tile in it's attackable array(should be the tile farthest along the path)
	 * and will select the enemy with the lowest health to attack
	 * @return the enemy that will be attacked
	 */
	public Enemy selectEnemyToAttack() {
		Enemy enemy = null;
		this.attackingTileNum = this.attackable.length - 1;
		while (enemy == null && this.attackingTileNum >= 0) {
			ArrayList<Enemy> enemiesOnTile = this.attackable[this.attackingTileNum].getEnemiesOnTile();
			if (!enemiesOnTile.isEmpty()) {
				Collections.sort(enemiesOnTile, new EnemyHealthComparator()); //use custom comparator to sort enemies by health
				enemy = enemiesOnTile.get(0); //the first enemy in the list should have the lowest health
			}
			else 
				this.attackingTileNum--;
		}
		return enemy;
	}
	
	public void attack(Enemy target) {
		//System.out.println("Attacking: " + target);
		target.getAttacked(this.damage);
	}
	
	public void render(GameContainer gc, Graphics g, Vector2f offset){
		g.setColor(Color.blue);
		g.drawRect(((this.worldPosition.x + offset.x) * TowerDefense.SCALE) + (((TowerDefense.TILESIZE / 2) * TowerDefense.SCALE) - 5), ((this.worldPosition.y + offset.y ) * TowerDefense.SCALE) + (((TowerDefense.TILESIZE / 2) * TowerDefense.SCALE) - 5), 10, 10);
		//render attackable tiles
		Vector2f attackablePos = null;
		for (Tile attackableTile : this.attackable) {
			attackablePos = this.level.getTileWorldPosition(attackableTile); 
			g.drawRect((attackablePos.x + offset.x) * TowerDefense.SCALE, (attackablePos.y + offset.y) * TowerDefense.SCALE, (TowerDefense.TILESIZE * TowerDefense.SCALE), (TowerDefense.TILESIZE * TowerDefense.SCALE));
		}
	}
	
	public long getId() {
		return this.ID;
	}

	public void update(GameContainer gc, StateBasedGame sbg, GameplayState gs, int delta) {}
}
