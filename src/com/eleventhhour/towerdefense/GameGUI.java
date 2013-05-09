package com.eleventhhour.towerdefense;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.StateBasedGame;

public class GameGUI{

	public int width,height; // dimensions of GUI
	public int GUIx, GUIy; //top-left corner of GUI
	public Tower towerselect;
	private Rectangle RTbutton, MGbutton, STbutton, sellButton, upgradeButton, build, playeri, toweri, ppButton;
	private Image rocket, RTBimage, mgun, MGBimage, slow, STBimage, cancel, sell, upgrade, play, pause, PPimage;
	private GameplayState gameState;
	public boolean buildR, buildM, buildS, paused;
	
	public GameGUI(GameplayState gps){
		this.gameState = gps;
		this.width = 640;
		this.height = 128;
		this.GUIx = 0;
		this.GUIy = 512;
		this.buildR = false;
		this.buildM = false;
		this.buildS = false;
		this.paused = false;
		this.towerselect = null;
		try {
			this.rocket = new Image("res/rocketb.png");
			this.mgun = new Image("res/machineb.png");
			this.slow = new Image("res/slowb.png");
			this.cancel = new Image("res/cancel.png");
			this.sell = new Image("res/sellb.png");
			this.upgrade = new Image("res/upgradeb.png");
			this.play = new Image("res/playb.png");
			this.pause = new Image("res/pause.png");
		} catch (SlickException e) {
			e.printStackTrace();
		}
		this.RTBimage = this.rocket;
		this.MGBimage = this.mgun;
		this.STBimage = this.slow;
		this.PPimage = this.pause;
		this.build = new Rectangle(this.GUIx, this.GUIy, (this.width / 3), this.height);
		this.toweri = new Rectangle(this.build.getMaxX(), this.GUIy, (this.width / 3), this.height);
		this.playeri = new Rectangle(this.toweri.getMaxX(), this.GUIy, (this.width / 3), this.height);
		this.RTbutton = new Rectangle((this.build.getX() + (this.build.getWidth() / 4) - (this.rocket.getWidth() / 2)), (this.build.getY() + (this.build.getHeight() / 2) - (this.rocket.getHeight() / 2)), this.rocket.getWidth(), this.rocket.getHeight());
		this.MGbutton = new Rectangle((this.build.getX() + (this.build.getWidth() / 2) - (this.mgun.getWidth() / 2)), (this.build.getY() + (this.build.getHeight() / 2) - (this.mgun.getHeight() / 2)), this.mgun.getWidth(), this.mgun.getHeight());
		this.STbutton = new Rectangle((this.build.getMaxX() - (this.build.getWidth() / 4) - (this.slow.getWidth() / 2)), (this.build.getY() + (this.build.getHeight() / 2) - (this.slow.getHeight() / 2)), this.slow.getWidth(), this.slow.getHeight());
		this.sellButton = new Rectangle((this.toweri.getMaxX() - this.sell.getWidth() - 10), (this.toweri.getY() + (this.toweri.getHeight() / 3) - (this.sell.getHeight() / 2)), this.sell.getWidth(), this.sell.getHeight());
		this.upgradeButton = new Rectangle((this.toweri.getMaxX() - this.upgrade.getWidth() - 10), (this.toweri.getMaxY() - (this.toweri.getHeight() / 3) - (this.upgrade.getHeight() / 2)), this.upgrade.getWidth(), this.upgrade.getHeight());
		this.ppButton = new Rectangle((this.playeri.getMaxX() - this.pause.getWidth() - 10), (this.playeri.getY() + 20), this.pause.getWidth(), this.pause.getHeight());
	}
	
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		g.setColor(Color.red);
		g.drawString("Build", (this.build.getX() + 2), (this.build.getY() + 1));
		g.drawString("Tower Info", (this.toweri.getX() + 2), (this.toweri.getY() + 1));
		g.drawString("Player Info", (this.playeri.getX() + 2), (this.playeri.getY() + 1));
		g.drawString("h: " + PlayerData.health, (this.playeri.getX() + 10), (this.playeri.getY() + (this.playeri.getHeight() / 4)));
		g.setColor(Color.green);
		g.drawString("m: " + PlayerData.money, (this.playeri.getX() + 10), (this.playeri.getY() + (this.playeri.getHeight() / 2)));
		g.setColor(Color.blue);
		g.drawString("s: " + PlayerData.score, (this.playeri.getX() + 10), (this.playeri.getMaxY() - (this.playeri.getHeight() / 4)));
		g.draw(build);
		g.draw(toweri);
		g.draw(playeri);
		this.RTBimage.draw(this.RTbutton.getX(), this.RTbutton.getY());
		this.MGBimage.draw(this.MGbutton.getX(), this.MGbutton.getY());
		this.STBimage.draw(this.STbutton.getX(), this.STbutton.getY());
		this.PPimage.draw(this.ppButton.getX(), this.ppButton.getY());
		g.setColor(Color.white);
		if(this.towerselect != null){
			g.drawString("range:  " + towerselect.range, (this.toweri.getX() + 10), (this.toweri.getY() + (this.toweri.getHeight() / 4)));
			g.drawString("damage: " + towerselect.damage, (this.toweri.getX() + 10), (this.toweri.getY() + (this.toweri.getHeight() / 2)));
			g.drawString("f-rate: " + towerselect.firerate, (this.toweri.getX() + 10), (this.toweri.getMaxY() - (this.toweri.getHeight() / 4)));
			this.sell.draw(this.sellButton.getX(), this.sellButton.getY());
			if (this.towerselect.towerLevel != 1)
				this.upgrade.draw(this.upgradeButton.getX(), this.upgradeButton.getY());
		}
		else{
			g.drawString("range:  ", (this.toweri.getX() + 10), (this.toweri.getY() + (this.toweri.getHeight() / 4)));
			g.drawString("damage: ", (this.toweri.getX() + 10), (this.toweri.getY() + (this.toweri.getHeight() / 2)));
			g.drawString("f-rate: ", (this.toweri.getX() + 10), (this.toweri.getMaxY() - (this.toweri.getHeight() / 4)));
		}
	}

	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		
	}
	
	public void mouseClicked(int button, int x, int y, int clickCount){
		if(this.RTbutton.contains(x, y)){
			if(buildR){
				this.RTBimage = this.rocket;
				this.buildR = false;
				this.gameState.currentState = GameplayState.GameState.NORMAL;
			}
			else{
				this.RTBimage = this.cancel;
				this.STBimage = this.slow;
				this.MGBimage = this.mgun;
				this.buildS = false;
				this.buildM = false;
				this.buildR = true;
				this.gameState.currentState = GameplayState.GameState.PLACE;
			}
		}
		else if(this.MGbutton.contains(x, y)){
			if(buildM){
				this.MGBimage = this.mgun;
				this.buildM = false;
				this.gameState.currentState = GameplayState.GameState.NORMAL;
			}
			else{
				this.MGBimage = this.cancel;
				this.STBimage = this.slow;
				this.RTBimage = this.rocket;
				this.buildS = false;
				this.buildR = false;
				this.buildM = true;
				this.gameState.currentState = GameplayState.GameState.PLACE;
			}
		}
		else if(this.STbutton.contains(x, y)){
			if(buildS){
				this.STBimage = this.slow;
				this.buildS = false;
				this.gameState.currentState = GameplayState.GameState.NORMAL;
			}
			else{
				this.STBimage = this.cancel;
				this.MGBimage = this.mgun;
				this.RTBimage = this.rocket;
				this.buildS = true;
				this.buildR = false;
				this.buildM = false;
				this.gameState.currentState = GameplayState.GameState.PLACE;
			}
		}
		else if(this.ppButton.contains(x, y)){
			if(!paused){
				this.PPimage = this.play;
				this.paused = true;
				this.gameState.currentState = GameplayState.GameState.PAUSED;
			}
			else{
				this.PPimage = this.pause;
				this.paused = false;
				this.gameState.currentState = GameplayState.GameState.NORMAL;
			}
		}
		
		if(this.towerselect != null){
			if(this.sellButton.contains(x, y)){
				PlayerData.increaseMoney((int)(.5 * this.towerselect.cost));
				this.gameState.getTowerManager().removeTower(this.towerselect.position);
				this.towerselect = null;
			}
			else if(this.upgradeButton.contains(x, y)){
				this.towerselect.upgradeTower();
			}
		}
	}
	
	public void keyPressed(int key, char c){
		if(key == Input.KEY_1){
			if(buildR){
				this.RTBimage = this.rocket;
				this.buildR = false;
				this.gameState.currentState = GameplayState.GameState.NORMAL;
			}
			else{
				this.RTBimage = this.cancel;
				this.STBimage = this.slow;
				this.MGBimage = this.mgun;
				this.buildS = false;
				this.buildM = false;
				this.buildR = true;
				this.gameState.currentState = GameplayState.GameState.PLACE;
			}
		}
		else if(key == Input.KEY_2){
			if(buildM){
				this.MGBimage = this.mgun;
				this.buildM = false;
				this.gameState.currentState = GameplayState.GameState.NORMAL;
			}
			else{
				this.MGBimage = this.cancel;
				this.STBimage = this.slow;
				this.RTBimage = this.rocket;
				this.buildS = false;
				this.buildR = false;
				this.buildM = true;
				this.gameState.currentState = GameplayState.GameState.PLACE;
			}
		}
		else if(key == Input.KEY_3){
			if(buildS){
				this.STBimage = this.slow;
				this.buildS = false;
				this.gameState.currentState = GameplayState.GameState.NORMAL;
			}
			else{
				this.STBimage = this.cancel;
				this.MGBimage = this.mgun;
				this.RTBimage = this.rocket;
				this.buildS = true;
				this.buildR = false;
				this.buildM = false;
				this.gameState.currentState = GameplayState.GameState.PLACE;
			}
		}
	}
	
	public void resetButtons(){
		this.STBimage = this.slow;
		this.MGBimage = this.mgun;
		this.RTBimage = this.rocket;
		this.buildS = false;
		this.buildR = false;
		this.buildM = false;
	}
}
