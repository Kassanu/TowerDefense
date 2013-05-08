package com.eleventhhour.towerdefense;

import java.io.File;

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
	private Rectangle RTbutton, MGbutton, STbutton, cancelButton, sellButton, upgradeButton, GUI, build, playeri, toweri;
	private Image rocket, RTBimage, mgun, MGBimage, slow, STBimage, cancel, sell, upgrade;
	private GameplayState gameState;
	private boolean buildR, buildM, buildS;
	
	public GameGUI(GameplayState gps){
		this.gameState = gps;
		this.width = 640;
		this.height = 128;
		this.GUIx = 0;
		this.GUIy = 512;
		this.GUI = new Rectangle(GUIx, GUIy, width, height);
		this.buildR = false;
		this.buildM = false;
		this.buildS = false;
		this.towerselect = null;
		try {
			this.rocket = new Image("res" + File.separator +"rocketb.png");
			this.mgun = new Image("res" + File.separator +"machineb.png");
			this.slow = new Image("res" + File.separator +"slowb.png");
			this.cancel = new Image("res" + File.separator +"cancel.png");
		} catch (SlickException e) {
			e.printStackTrace();
		}
		this.RTBimage = this.rocket;
		this.MGBimage = this.mgun;
		this.STBimage = this.slow;
		this.build = new Rectangle(this.GUIx, this.GUIy, (this.width / 3), this.height);
		this.toweri = new Rectangle(this.build.getMaxX(), this.GUIy, (this.width / 3), this.height);
		this.playeri = new Rectangle(this.toweri.getMaxX(), this.GUIy, (this.width / 3), this.height);
		this.RTbutton = new Rectangle((this.build.getX() + (this.build.getWidth() / 4) - (this.rocket.getWidth() / 2)), (this.build.getY() + (this.build.getHeight() / 2) - (this.rocket.getHeight() / 2)), this.rocket.getWidth(), this.rocket.getHeight());
		this.MGbutton = new Rectangle((this.build.getX() + (this.build.getWidth() / 2) - (this.mgun.getWidth() / 2)), (this.build.getY() + (this.build.getHeight() / 2) - (this.mgun.getHeight() / 2)), this.mgun.getWidth(), this.mgun.getHeight());
		this.STbutton = new Rectangle((this.build.getMaxX() - (this.build.getWidth() / 4) - (this.slow.getWidth() / 2)), (this.build.getY() + (this.build.getHeight() / 2) - (this.slow.getHeight() / 2)), this.slow.getWidth(), this.slow.getHeight());
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
		
		g.setColor(Color.white);
		if(this.towerselect != null){
			g.drawString("range:  " + towerselect.range, (this.toweri.getX() + 10), (this.toweri.getY() + (this.toweri.getHeight() / 4)));
			g.drawString("damage: " + towerselect.damage, (this.toweri.getX() + 10), (this.toweri.getY() + (this.toweri.getHeight() / 2)));
			g.drawString("f-rate: " + towerselect.firerate, (this.toweri.getX() + 10), (this.toweri.getMaxY() - (this.toweri.getHeight() / 4)));
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
	}
	
	public void resetButtons(){
		this.STBimage = this.rocket;
		this.MGBimage = this.mgun;
		this.RTBimage = this.rocket;
		this.buildS = false;
		this.buildR = false;
		this.buildM = false;
	}
	
	public int getTowerType() {
		if (this.buildM)
			return 0;
		else if (this.buildR)
			return 1;
		
		return 2;
	}
	
}
