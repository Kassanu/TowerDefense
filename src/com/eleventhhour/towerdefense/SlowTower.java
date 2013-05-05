package com.eleventhhour.towerdefense;

import java.util.Arrays;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import com.eleventhhour.towerdefense.TileEffect.EffectType;

public class SlowTower extends Tower {

	public SlowTower(long lASTID, Tile pos, Level level){
		super(lASTID, pos, level);
		this.cost = 30;
		this.range = 1;
		this.damage = 20;
		this.firerate = 3000;
		this.color = "FF6600";
		this.setAttackable(this.level.getAttackableTiles(this.position.getPosition(), this.range));
		System.out.println(Arrays.toString(this.attackable));
	}
	
	@Override
	public void update(GameContainer gc, StateBasedGame sbg, GameplayState gs, int delta){
		if (this.cooldown <= 0) {
			for (Tile attackableTile : this.attackable) {
				attackableTile.addEffect(new TileEffect(EffectType.SPEED, 2, 2000, attackableTile));
			}
			this.cooldown = this.firerate;
		}
		else {
			this.cooldown -= delta;
		}
	}
	
}
