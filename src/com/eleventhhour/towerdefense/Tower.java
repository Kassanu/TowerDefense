package com.eleventhhour.towerdefense;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;

public abstract class Tower {
	public Tile position;
	public Tile[] attackable = null;
	public int cost = 0;
	public int range = 0;
	public int damage = 0;
	public int firerate = 0;
	public int cooldown = 0;
	public String color = "FFFFFF";
	
	public Tower(Tile pos){
		this.position = pos;
	}
	
	public void setattackable(Tile[] attack){
		this.attackable = attack;
	}
	
	public void init(){
		
	}
	
	public void update(GameContainer gc, StateBasedGame sbg, int delta){
		
	}
	
	public void render(GameContainer gc, Graphics g, int x, int y){
		
	}
}
