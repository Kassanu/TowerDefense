package com.eleventhhour.towerdefense;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Map.Entry;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

public class TowerManager {
	
	public static long LASTID = 1; // id of the last tower added
	public static long LASTBULLETID = 1; // id of the last bullet added
	public HashMap<Long, Tower> towers; // towers on the map
	public HashMap<Long, Bullet> bullets; // bullets on the map
	public ArrayList<Long> toBeRemoved; // ids of the bullets that need to be removed from the map 
	private BulletPool bulletPool; // pool of bullets ready to be used by the manager
	
	public TowerManager() {
		this.loadTowerPref();
		this.towers = new HashMap<Long, Tower>();
		this.bulletPool = new BulletPool();
		this.bullets = new HashMap<Long, Bullet>();
		this.toBeRemoved = new ArrayList<Long>();
	}
	
	/**
	 * loadTowerPref -
	 * 
	 * loads tower preferences from a text file.  This allows us to tweak the towers without having to repack the game.
	 * 
	 */
	private void loadTowerPref() {
		Scanner scanner = null;
		try {
			scanner = new Scanner(new FileInputStream("res" + File.separator + "towerPref.txt"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	
		String currentLine;
		String[] lineTowerLevelArray;
		String[] lineTowerPrefArray;
		ArrayList<ArrayList<int[]>> prefList = new ArrayList<ArrayList<int[]>>();
		ArrayList<int[]> levelsList = new ArrayList<int[]>();
		int[] towerPref = new int[6];
		while (scanner.hasNext()) {
			currentLine = scanner.nextLine();
			//if the line starts with a # it's a comment so ignore it.
			if (!(currentLine.substring(0, 1)).equals("#")) {
				lineTowerLevelArray = currentLine.split(";");
				//now we have an array of all the upgrades
				//loop through each and split the line by the commas and add it to the array list
				for (int i = 0; i < lineTowerLevelArray.length; i++) {
					lineTowerPrefArray = lineTowerLevelArray[i].split(",");
					for (int j = 0; j < lineTowerPrefArray.length; j++) {
						towerPref[j] = Integer.parseInt(lineTowerPrefArray[j]);
					}
					levelsList.add(Arrays.copyOf(towerPref, towerPref.length));
				}
				prefList.add((ArrayList<int[]>) levelsList.clone());
				levelsList.clear();
			}
		}
				
		Tower.setDefaults(prefList);
	}
	
	/*
	 * addTower -
	 * 
	 * - adds a tower to the tower manager, depending on the type that the player is trying to build
	 * - only adds the tower if the player has enough money to do so, and then decrements the player's money by the tower's cost
	 * - returns true if the tower was built, false otherwise
	 */
	public boolean addTower(Level level, Tile tile, String type) {
		Tower t;
		if(type.equals("ST")){
			t = new SlowTower(LASTID, tile, level);
		}
		else if(type.equals("RT")){
			t = new Rtower(LASTID, tile, level);
		}
		else{
			t = new MGtower(LASTID, tile, level);
		}
		
		if(PlayerData.money >= t.cost){
			PlayerData.decreaseMoney(t.cost);
			towers.put(LASTID, t);
			((BuildableTile)tile).addTower(t);
			LASTID++;
			return true;
		}
		else{
			return false;
		}
	}
	
	// removes a tower from the tower hashmap
	public void removeTower(Tile tile) {
		long removeId = ((BuildableTile) tile).getTower().getId();
		towers.remove(removeId);
		((BuildableTile) tile).removeTower();
	}
	
	// renders all the towers and bullets on the map
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g, Vector2f offset) throws SlickException {
		for (Entry<Long, Tower> entry : this.towers.entrySet()) {
			entry.getValue().render(gc, g, offset);
		}
		for (Entry<Long, Bullet> bullet : this.bullets.entrySet()) {
			if (((GameplayState) sbg.getCurrentState()).getCamera().isInView(bullet.getValue()))
				bullet.getValue().render(gc, g, offset);
		}
	}

	// updates all the towers and bullets on the map
	public void update(GameContainer gc, StateBasedGame sbg, GameplayState gs, int delta) throws SlickException {
		for (Entry<Long, Tower> entry : this.towers.entrySet()) {
			entry.getValue().update(gc, sbg, gs, delta);
		}
		
		for (Entry<Long, Bullet> bullet : this.bullets.entrySet()) {
			bullet.getValue().update(gc, sbg, gs, delta);
	        if (bullet.getValue().isDead()) {
	        	this.toBeRemoved.add(bullet.getKey());
	        }
		}
		if (!this.toBeRemoved.isEmpty()) {
			for (Long bulletId : this.toBeRemoved) {
				this.removeBullet(bulletId);
			}
			this.toBeRemoved.clear();
		}
	}

	// creates a bullet and adds it to the map
	public void spawnBullet(Vector2f worldPosition, Vector2f target, Tile tile, int damage) {
		Bullet bullet = this.bulletPool.allocate();
		bullet.init(LASTBULLETID, worldPosition, target, tile, damage);
		this.addBullet(bullet);
	}

	// adds a bullet to the bullet hash map and the map
	public void addBullet(Bullet bulllet) {
		this.bullets.put(LASTBULLETID, bulllet);
		LASTBULLETID++;
	}
	
	// removes a bullet fromt the map and the bullet hash map
	private void removeBullet(Long bulletId) {
		Bullet bulllet = this.bullets.get(bulletId);
		this.bulletPool.release(bulllet);
		this.bullets.remove(bulletId);
	}
	
}
