package com.eleventhhour.towerdefense;

import java.util.Arrays;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

public class MGtower extends Tower {
	
	public MGtower(long lASTID, Tile pos, Level level){
		super(lASTID, pos, level);
		this.towerLevel = 0;
		this.range = Tower.DEFAULTVALUES[0][this.towerLevel][0];
		this.damage = Tower.DEFAULTVALUES[0][this.towerLevel][1];
		this.firerate = Tower.DEFAULTVALUES[0][this.towerLevel][2];
		this.cost = Tower.DEFAULTVALUES[0][this.towerLevel][3];
		this.spriteGroup = Tower.DEFAULTVALUES[0][this.towerLevel][4];
		this.aniTotalDuration = Tower.DEFAULTVALUES[0][this.towerLevel][5];
		this.animationFrame = 0;
		this.aniType = 0;
		this.aniCurrentDuration = 0;
		this.towerType = 1;
		this.setAttackable(this.level.getAttackableTiles(this.position.getPosition(), this.range));
	}
	
	@Override
	public void update(GameContainer gc, StateBasedGame sbg, GameplayState gs, int delta){
		if (this.cooldown <= 0) {
			
			Enemy enemy = this.selectEnemyToAttack();
			if (enemy != null) {
				this.attacking = true;
				//gs.getTowerManager().spawnBullet(this.centerPosition.copy(), enemy.getCenterPosition());
				this.attack(enemy);
				this.cooldown = this.firerate;
			}
		}
		else {
			this.attacking = false;
			this.cooldown -= delta;
		}
		//sprite stuff is handled in super
		super.update(gc, sbg, gs, delta);
	}
	
}
