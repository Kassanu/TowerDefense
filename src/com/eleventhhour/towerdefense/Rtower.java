package com.eleventhhour.towerdefense;

import java.util.Arrays;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

public class Rtower extends Tower {
	
	public Rtower(long lASTID, Tile pos, Level level){
		super(lASTID, pos, level);
		this.towerLevel = 0;
		this.range = Tower.DEFAULTVALUES[1][this.towerLevel][0];
		this.damage = Tower.DEFAULTVALUES[1][this.towerLevel][1];
		this.firerate = Tower.DEFAULTVALUES[1][this.towerLevel][2];
		this.cost = Tower.DEFAULTVALUES[1][this.towerLevel][3];
		this.spriteGroup = Tower.DEFAULTVALUES[1][this.towerLevel][4];
		this.aniTotalDuration = Tower.DEFAULTVALUES[1][this.towerLevel][5];
		this.animationFrame = 0;
		this.aniType = 0;
		this.aniCurrentDuration = 0;
		this.towerType = 1;
		this.setAttackable(this.level.getAttackableTiles(this.position.getPosition(), this.range));
	}
	
	@Override
	public void update(GameContainer gc, StateBasedGame sbg, GameplayState gs, int delta){
		if (this.cooldown <= 0) {
			this.attackingTarget = this.selectEnemyToAttack();
			if (this.attackingTarget != null) {
				this.attacking = true;
				this.attack(this.attackingTarget);
				Tile tile = this.attackable[this.attackingTileNum];
				gs.getTowerManager().spawnBullet(this.centerPosition.copy(), this.attackingTarget.getCenterPosition(), tile, (this.damage / 4));
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
