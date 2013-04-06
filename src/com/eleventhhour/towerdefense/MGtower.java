package com.eleventhhour.towerdefense;

import java.util.Arrays;

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
}
