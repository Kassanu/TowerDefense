package com.eleventhhour.towerdefense;

import java.util.Arrays;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

public class MGtower extends Tower {
	
	public MGtower(long lASTID, Tile pos, Level level){
		super(lASTID, pos, level);
		this.range = (int) Tower.DEFAULTVALUES[0][0];
		this.damage = (int) Tower.DEFAULTVALUES[0][1];
		this.firerate = (int) Tower.DEFAULTVALUES[0][2];
		this.cost = (int) Tower.DEFAULTVALUES[0][3];
		this.spriteGroup = (int) Tower.DEFAULTVALUES[0][4];
		this.aniTotalDuration = (int) Tower.DEFAULTVALUES[0][5];
		this.animationFrame = 0;
		this.aniType = 0;
		this.aniCurrentDuration = 0;
		this.setAttackable(this.level.getAttackableTiles(this.position.getPosition(), this.range));
	}
	
	@Override
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
	
}
