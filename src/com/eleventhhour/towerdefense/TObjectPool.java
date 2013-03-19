package com.eleventhhour.towerdefense;

public abstract class TObjectPool<T> extends ObjectPool {

	  public TObjectPool() {
	  }

	  public TObjectPool(int paramInt) {
	    super(paramInt);
	  }

	  public T allocate() {
	    return (T) super.allocate();
	  }

}
