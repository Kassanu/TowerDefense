package com.eleventhhour.towerdefense;

public abstract class TObjectPool<T> extends ObjectPool {

	public TObjectPool() {
	}

	public TObjectPool(int size) {
		super(size);
	}

	@SuppressWarnings("unchecked")
	public T allocate() {
		return (T) super.allocate();
	}

}
