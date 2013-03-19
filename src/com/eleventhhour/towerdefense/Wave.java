package com.eleventhhour.towerdefense;

import java.util.ArrayList;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

public class Wave {
	
	public ArrayList<Integer> enemyType;
	public static final int TIMEBETWEENENEMIES = 1 * 1000; //multiply by 1000 for milliseconds
	public int nextEnemyIn = 0;
	
	public Wave(ArrayList<Integer> enemyType) {
		this.enemyType = (ArrayList<Integer>) enemyType.clone();
	}

	public void update(GameContainer gc, StateBasedGame sbg, EnemyManager enemyManager, int delta) {
		if (this.nextEnemyIn <= 0) {
			enemyManager.spawnEnemy(this.enemyType.remove(0));
			this.nextEnemyIn = Wave.TIMEBETWEENENEMIES;
		}
		else {
			this.nextEnemyIn -= delta;
		}
	}

	public boolean isOver() {
		return this.enemyType.isEmpty();
	}

	public String toString() {
		return "Wave [enemyType=" + enemyType + "]";
	}
	
}
