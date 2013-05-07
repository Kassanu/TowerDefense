package com.eleventhhour.towerdefense;

import java.util.Arrays;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

public class Rtower extends Tower {
	
	public Rtower(long lASTID, Tile pos, Level level){
		super(lASTID, pos, level);
		this.range = (int) Tower.DEFAULTVALUES[1][0];
		this.damage = (int) Tower.DEFAULTVALUES[1][1];
		this.firerate = (int) Tower.DEFAULTVALUES[1][2];
		this.cost = (int) Tower.DEFAULTVALUES[1][3];
		this.spriteGroup = (int) Tower.DEFAULTVALUES[1][4];
		this.aniTotalDuration = (int) Tower.DEFAULTVALUES[1][5];
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
				Tile tile = this.attackable[this.attackingTileNum];
				tile.addEffect(new TileEffect(TileEffect.EffectType.DAMAGE, 200, 1000, tile));
				this.cooldown = this.firerate;
			}
		}
		else {
			this.cooldown -= delta;
		}
	}
	
}
