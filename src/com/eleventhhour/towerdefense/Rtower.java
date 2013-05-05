package com.eleventhhour.towerdefense;

import java.util.Arrays;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

public class Rtower extends Tower {
	
	public Rtower(long lASTID, Tile pos, Level level){
		super(lASTID, pos, level);
		this.cost = 30;
		this.range = 3;
		this.damage = 20;
		this.firerate = 3000;
		this.color = "FF6600";
		this.setAttackable(this.level.getAttackableTiles(this.position.getPosition(), this.range));
		System.out.println(Arrays.toString(this.attackable));
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
