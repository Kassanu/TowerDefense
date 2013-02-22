package com.eleventhhour.towerdefense;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class TowerManager {
	
	public static int LASTID = 1;
	
	HashMap<Integer, Tower> towers;
	ArrayList<Integer> toBeRemoved;
	
	public TowerManager() {
		this.towers = new HashMap<Integer, Tower>();
	}
	
	public void addTower(Tile pos) {
		System.out.println("Creating tower at: " + pos.getPosition().toString());
		MGtower t = new MGtower(pos);
		towers.put(LASTID++, t);
		((BuildableTile)pos).addTower(t);
	}
	
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		for (Entry<Integer, Tower> entry : this.towers.entrySet()) {
	        entry.getValue().render(gc, g);
		}
	}

	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		for (Entry<Integer, Tower> entry : this.towers.entrySet()) {
	        //System.out.println("Name : " + entry.getKey() + " age " + entry.getValue());
		}
	}
	
}
