package com.eleventhhour.towerdefense;

public class PlayerData {

	public static int health = 0, score = 0, money = 0, level = 0;
	private static PlayerData instance = null;

	protected PlayerData() { }
	
	public static PlayerData getInstance() {
		if(instance == null)
			instance = new PlayerData();
		return instance;
	}
	
	public static void resetPlayer() {
		PlayerData.health = 20;
		PlayerData.score = 0;
		PlayerData.money = 100;
		PlayerData.level = 0;
	}

	public static void decreaseHealth(int value) {
		PlayerData.health -= value;
	}
	
	public static void increaseMoney(int value) {
		PlayerData.money += value;
	}
	
	public static void decreaseMoney(int value) {
		PlayerData.money -= value;
	}
	
	public static void increaseScore(int value) {
		PlayerData.score += value;
	}
	
	public static void decreaseScore(int value) {
		PlayerData.score -= value;
	}

	public static void setLevel(int l) {
		PlayerData.level = l;
		
	}
	
}