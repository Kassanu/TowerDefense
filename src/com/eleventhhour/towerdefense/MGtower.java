package com.eleventhhour.towerdefense;

public class MGtower extends Tower {
	
	public MGtower(long lASTID, Tile pos){
		super(lASTID, pos);
		this.cost = 15;
		this.range = 2;
		this.damage = 1;
		this.firerate = 200;
		this.color = "660099";
	}
}
