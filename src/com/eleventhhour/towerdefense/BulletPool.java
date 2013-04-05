package com.eleventhhour.towerdefense;

public class BulletPool extends TObjectPool<Bullet> {
	
	public BulletPool() {
		super();
	}

	public BulletPool(int size) {
		super(size);
	}

	protected void fill() {
	    for (int i = 0; i <= this.getSize() ; i++) {
	      this.thePool.add(new Bullet());
	    }
	}

	public void reset() {
		
	}

}
