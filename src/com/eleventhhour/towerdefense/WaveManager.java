package com.eleventhhour.towerdefense;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

public class WaveManager {
	
	public int currentWave = 0;
	public boolean waveOver = false;
	public Wave[] waves;
	public static final int TIMEBETWEENWAVES = 10 * 1000; //multiply by 1000 for milliseconds
	public int nextWaveIn = 0;
	
	public WaveManager(int level) {
		this.loadWaves(level);
	}
	/**
	 * loadWaves -
	 * 
	 * This method will load a text file that contains the information for a levels waves.  It will create new wave objects
	 * @param level - level to load waves for
	 */
	private void loadWaves(int level) {
		Scanner scanner = null;
		try {
			scanner = new Scanner(new FileInputStream("res/levels/level"+level+"/waves.txt"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	
		String currentLine;
		String[] currentLineArray;
		ArrayList<Wave> waveList = new ArrayList<Wave>();
		while (scanner.hasNext()) {
			currentLine = scanner.nextLine();
			
			currentLineArray = currentLine.split(",");
			ArrayList<Integer> enemyType = new ArrayList<Integer>();
			for (int i = 0; i < currentLineArray.length; i++) {
				int eType, eAmount;
				eType = Integer.parseInt(currentLineArray[i].substring(0, currentLineArray[i].indexOf("-")));
				eAmount = Integer.parseInt(currentLineArray[i].substring(currentLineArray[i].indexOf("-")+1));
				for (int j = 0; j < eAmount; j++) {
					enemyType.add(eType);
				}
			}
			waveList.add(new Wave(enemyType));
		}
		
		this.waves = waveList.toArray(new Wave[waveList.size()]);
	}
	
	public void update(GameContainer gc, StateBasedGame sbg, EnemyManager enemyManager, int delta){
		if (this.currentWave < this.waves.length) {
			if (!this.waveOver) {
				if (this.waves[this.currentWave].isOver()) {
					this.waveOver = true;
					this.nextWaveIn = WaveManager.TIMEBETWEENWAVES;
				}
				else {
					this.waves[this.currentWave].update(gc, sbg, enemyManager, delta);
				}
			}
			else {
				//wave is over start counting down until next wave
				this.nextWaveIn -= delta;
				if (this.nextWaveIn <= 0) {
					this.currentWave++;
					this.waveOver = false;
				}
			}
		}
	}

	public String toString() {
		return "WaveManager [waves=" + Arrays.toString(waves) + "]";
	}
	
	
}
