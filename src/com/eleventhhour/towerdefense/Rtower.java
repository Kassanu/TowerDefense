package com.eleventhhour.towerdefense;

public class Rtower extends Tower {
	
	public Rtower(int id, Tile pos){
		super(id, pos);
		this.cost = 30;
		this.range = 3;
		this.damage = 20;
		this.firerate = 3000;
		this.color = "FF6600";
	}
}
