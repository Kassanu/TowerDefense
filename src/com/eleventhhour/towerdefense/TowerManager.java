package com.eleventhhour.towerdefense;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class TowerManager {
	
	public static long LASTID = 1;
	
	public HashMap<Long, Tower> towers;
	public ArrayList<Integer> toBeRemoved;
	
	public TowerManager() {
		this.towers = new HashMap<Long, Tower>();
	}
	
	public void addTower(Tile tile) {
		System.out.println("Creating tower at: " + tile.getPosition().toString());
		MGtower t = new MGtower(LASTID, tile);
		towers.put(LASTID, t);
		((BuildableTile)tile).addTower(t);
		LASTID++;
	}
	
	public void removeTower(Tile tile) {
		System.out.println("Removing tower at: " + tile.getPosition().toString());
		long removeId = ((BuildableTile) tile).getTower().getId();
		towers.remove(removeId);
		((BuildableTile) tile).removeTower();
	}
	
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		for (Entry<Long, Tower> entry : this.towers.entrySet()) {
	        entry.getValue().render(gc, g);
		}
	}

	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		for (Entry<Long, Tower> entry : this.towers.entrySet()) {
	        System.out.println("Name : " + entry.getKey() + " age " + entry.getValue());
		}
	}
	
}
