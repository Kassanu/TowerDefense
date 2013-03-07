package com.eleventhhour.towerdefense;

public class MGtower extends Tower {
	
	public MGtower(int id, Tile pos){
		super(id, pos);
		this.cost = 15;
		this.range = 2;
		this.damage = 1;
		this.firerate = 200;
		this.color = "660099";
	}
}
