package com.eleventhhour.towerdefense;

import java.util.Arrays;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

public class MGtower extends Tower {
	
	public MGtower(long lASTID, Tile pos, Level level){
		super(lASTID, pos, level);
		this.cost = 15;
		this.range = 1;
		this.damage = 1;
		this.firerate = 200;
		this.color = "660099";
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
