package com.eleventhhour.towerdefense;

public class Rtower extends Tower {
	
	public Rtower(Tile pos){
		super(pos);
		this.cost = 30;
		this.range = 3;
		this.damage = 20;
		this.firerate = 3000;
		this.color = "FF6600";
	}
}
