package com.eleventhhour.towerdefense;

public class EnemyPool extends TObjectPool<Enemy> {
	
	public EnemyPool() {
		super();
	}

	public EnemyPool(int size) {
		super(size);
	}

	protected void fill() {
	    for (int i = 0; i <= this.getSize() ; i++) {
	      this.thePool.add(new Enemy());
	    }
	}

	public void reset() {
		
	}

}
