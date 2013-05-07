package com.eleventhhour.towerdefense;

import java.util.Arrays;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import com.eleventhhour.towerdefense.TileEffect.EffectType;

public class SlowTower extends Tower {

	public SlowTower(long lASTID, Tile pos, Level level){
		super(lASTID, pos, level);
		this.range = (int) Tower.DEFAULTVALUES[2][0];
		this.damage = (int) Tower.DEFAULTVALUES[2][1];
		this.firerate = (int) Tower.DEFAULTVALUES[2][2];
		this.cost = (int) Tower.DEFAULTVALUES[2][3];
		this.spriteGroup = (int) Tower.DEFAULTVALUES[2][4];
		this.aniTotalDuration = (int) Tower.DEFAULTVALUES[2][5];
		this.animationFrame = 0;
		this.aniType = 0;
		this.aniCurrentDuration = 0;
		this.setAttackable(this.level.getAttackableTiles(this.position.getPosition(), this.range));
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
