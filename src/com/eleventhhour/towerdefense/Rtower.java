package com.eleventhhour.towerdefense;

import java.util.Arrays;

public class Rtower extends Tower {
	
	public Rtower(int id, Tile pos, Level level){
		super(id, pos, level);
		this.cost = 30;
		this.range = 3;
		this.damage = 20;
		this.firerate = 3000;
		this.color = "FF6600";
		this.setAttackable(this.level.getAttackableTiles(this.position.getPosition(), this.range));
		System.out.println(Arrays.toString(this.attackable));
	}
}
