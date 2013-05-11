package com.eleventhhour.towerdefense;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.Random;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

public abstract class Tower {
	
	/**
	 * Default settings array for towers
	 * 
	 * These are loading in from the towerPref.txt when a map is selected.
	 * 
	 * First index in the array is for a specific type of tower. they are as follows:
	 * 
	 * 0 - MGtower
	 * 1 - Rtower
	 * 2 - SlowTower
	 * 
	 * Second index is for the level of the tower
	 * 
	 * Third index is an array that each index corresponds to an towers stats, they are as follows:
	 * 0 = range
	 * 1 = damage
	 * 2 = firerate
	 * 3 = cost
	 * 4 = spriteGroup (where in the sprite sheet their sprite is.  Each tower has 8 rows of sprites so their position on the sprite group is always ((8*spriteGroup) + (aniType + attacking?4:0)) * tileSize)
	 * 5 = aniTotalDuration
	 */
	protected static int DEFAULTVALUES[][][];
	
	public static Image spriteSheet = null;
	
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
	public int towerLevel = 0;
	public int towerType = 0;
	protected int animationFrame; //current frame being animated
	protected int aniType; //The type of animation up,left,down,right
	protected boolean attacking = false; //whether or not they are attacking, adds 4 to aniType to get attacking animation for that rotation
	protected int aniTotalDuration; //Total duration of animationFrame
	protected int aniCurrentDuration; //current duration of this animationFrame
	protected int spriteGroup; //group on the sprite sheet this enemy gets it's sprite from
	protected Enemy attackingTarget = null;
	
	public Tower(long lASTID, Tile pos, Level level){
		this.ID = lASTID;
		this.position = pos;
		this.level = level;
		this.worldPosition = this.level.getTileWorldPosition(this.position);
		this.centerPosition = new Vector2f();
		this.calcCenterPosition();
		if (Tower.spriteSheet == null) {
			try {
				Tower.spriteSheet = new Image("res" + File.separator +"towersheet.png");
			} catch (SlickException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
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
		target.getAttacked(this.damage);
	}
	
	public void render(GameContainer gc, Graphics g, Vector2f offset){
		
		float x = (this.worldPosition.x + offset.x) * TowerDefense.SCALE;
		float y = (this.worldPosition.y + offset.y) * TowerDefense.SCALE;
		float srcx = this.animationFrame * TowerDefense.TILESIZE;
		float srcy = ((8 * this.spriteGroup) + (this.aniType + (this.attacking?4:0))) * TowerDefense.TILESIZE;
		
		g.drawImage(Tower.spriteSheet,x ,y,x + TowerDefense.TILESIZE, y + TowerDefense.TILESIZE,  srcx, srcy, srcx + TowerDefense.TILESIZE, srcy + TowerDefense.TILESIZE);
		
		//render attackable tiles
		Vector2f attackablePos = null;
		for (Tile attackableTile : this.attackable) {
			attackablePos = this.level.getTileWorldPosition(attackableTile); 
			g.drawRect((attackablePos.x + offset.x) * TowerDefense.SCALE, (attackablePos.y + offset.y) * TowerDefense.SCALE, (TowerDefense.TILESIZE * TowerDefense.SCALE), (TowerDefense.TILESIZE * TowerDefense.SCALE));
		}
		if (this.attackingTarget != null) {
			Vector2f dist = (this.attackingTarget.getCenterPosition().copy()).sub(this.centerPosition);
			g.drawLine(this.centerPosition.x,this.centerPosition.y , this.centerPosition.x+dist.x, this.centerPosition.y+dist.y);
		}
		
	}
	
	public long getId() {
		return this.ID;
	}

	public void update(GameContainer gc, StateBasedGame sbg, GameplayState gs, int delta) {
		//sprite stuff
		this.aniCurrentDuration += delta;
				
		if (this.aniCurrentDuration >= this.aniTotalDuration) {
			this.animationFrame = ((this.animationFrame+1) % 4);
			this.aniCurrentDuration = 0;
		}
		
		if (this.attackingTarget != null) {
			Vector2f dist = (this.attackingTarget.getCenterPosition().copy()).sub(this.centerPosition);
			double radAngle = Math.atan2(dist.x, dist.y);
			double degAngle = radAngle * (180/Math.PI);
			
			if (degAngle >= 0) {
				//pos looking right
				if ((degAngle > 90)) {
					//looking up
					this.aniType = 3;
				}
				else {
					//looking down
					this.aniType = 0;
				}
			}
			else {
				//nega looking left
				if ((degAngle < -90)) {
					//looking up
					this.aniType = 2;
				}
				else {
					//looking down
					this.aniType = 1;
				}
			}
		}
		
	}
	
	/**
	 * setDefaults -
	 * 
	 * Sets the DEFAULTVALUES array from the supplied array list.
	 * 
	 * @param prefList
	 */
	public static void setDefaults(ArrayList<ArrayList<int[]>> prefList) {
		Tower.DEFAULTVALUES = new int[prefList.size()][2][6];
		
		for (int i = 0; i < Tower.DEFAULTVALUES.length; i++) {
			for (int j = 0; j < Tower.DEFAULTVALUES[0].length; j++)
				Tower.DEFAULTVALUES[i][j] = prefList.get(i).get(j);
		}
		
	}
	
	/**
	 * upgradeTower
	 * 
	 * Call this method to upgrade the tower.
	 * 
	 * For now it will just set the level to one because as of right now
	 * there is only one upgraded version of each tower
	 * 
	 */
	public void upgradeTower() {
		if (this.towerLevel != 1 && (PlayerData.money >= Tower.DEFAULTVALUES[this.towerType][1][3])) {
			this.towerLevel = 1;
			this.range = Tower.DEFAULTVALUES[this.towerType][this.towerLevel][0];
			this.damage = Tower.DEFAULTVALUES[this.towerType][this.towerLevel][1];
			this.firerate = Tower.DEFAULTVALUES[this.towerType][this.towerLevel][2];
			this.cost = Tower.DEFAULTVALUES[this.towerType][this.towerLevel][3];
			this.spriteGroup = Tower.DEFAULTVALUES[this.towerType][this.towerLevel][4];
			this.aniTotalDuration = Tower.DEFAULTVALUES[this.towerType][this.towerLevel][5];
		}
	}
	
}
