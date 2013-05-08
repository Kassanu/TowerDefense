package com.eleventhhour.towerdefense;

import java.util.Arrays;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import com.eleventhhour.towerdefense.TileEffect.EffectType;

public class SlowTower extends Tower {

	public SlowTower(long lASTID, Tile pos, Level level){
		super(lASTID, pos, level);
		this.towerLevel = 0;
		this.range = Tower.DEFAULTVALUES[2][this.towerLevel][0];
		this.damage = Tower.DEFAULTVALUES[2][this.towerLevel][1];
		this.firerate = Tower.DEFAULTVALUES[2][this.towerLevel][2];
		this.cost = Tower.DEFAULTVALUES[2][this.towerLevel][3];
		this.spriteGroup = Tower.DEFAULTVALUES[2][this.towerLevel][4];
		this.aniTotalDuration = Tower.DEFAULTVALUES[2][this.towerLevel][5];
		this.animationFrame = 0;
		this.aniType = 0;
		this.aniCurrentDuration = 0;
		this.setAttackable(this.level.getAttackableTiles(this.position.getPosition(), this.range));
	}
	
	@Override
	public void update(GameContainer gc, StateBasedGame sbg, GameplayState gs, int delta){
		if (this.cooldown <= 0) {
			this.attacking = true;
			for (Tile attackableTile : this.attackable) {
				attackableTile.addEffect(new TileEffect(EffectType.SPEED, 2, 2000, attackableTile));
			}
			this.cooldown = this.firerate;
		}
		else {
			this.attacking = false;
			this.cooldown -= delta;
		}
		//sprite stuff is handled in super
		super.update(gc, sbg, gs, delta);
	}
	
}
