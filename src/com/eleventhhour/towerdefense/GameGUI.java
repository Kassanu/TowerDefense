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
	public Tower towerselect; // tower object that is currently selected, null if no tower is selected
	// rectangles for detecting mouse clicks on the buttons
	private Rectangle RTbutton, MGbutton, STbutton, sellButton, upgradeButton, build, playeri, toweri, ppButton, quitButton;
	// images used by the gui
	private Image rocket, RTBimage, mgun, MGBimage, slow, STBimage, cancel, sell, upgrade, play, pause, PPimage, quit;
	private GameplayState gameState; // reference to the gameState that uses this gui
	private StateBasedGame game;
	// booleans for telling the game state which tower the player is trying to build and if the user has clicked the pause button
	public boolean buildR, buildM, buildS, paused;
	
	// initializes the values of the variables in this class
	public GameGUI(GameplayState gps, StateBasedGame sbg){
		this.game = sbg;
		this.gameState = gps;
		this.width = 640;
		this.height = 127;
		this.GUIx = 0;
		this.GUIy = 513;
		this.buildR = false;
		this.buildM = false;
		this.buildS = false;
		this.paused = false;
		this.towerselect = null;
		try {
			// try loading the images used by this class
			this.rocket = new Image("res/rocketb.png");
			this.mgun = new Image("res/machineb.png");
			this.slow = new Image("res/slowb.png");
			this.cancel = new Image("res/cancel.png");
			this.sell = new Image("res/sellb.png");
			this.upgrade = new Image("res/upgradeb.png");
			this.play = new Image("res/playb.png");
			this.pause = new Image("res/pause.png");
			this.quit = new Image("res/quit.png");
		} catch (SlickException e) {
			e.printStackTrace();
		}
		// these images are place holders for which image is actually shown (tower's button or cancel button)
		this.RTBimage = this.rocket;
		this.MGBimage = this.mgun;
		this.STBimage = this.slow;
		this.PPimage = this.pause;
		// initializes the placement and dimensions of the different parts of this GUI
		this.build = new Rectangle(this.GUIx, this.GUIy, (this.width / 5), this.height); // part that contains the build tower buttons
		this.toweri = new Rectangle(this.build.getMaxX(), this.GUIy, (this.width * 2 / 5), this.height); // part that contains the information of the tower currently selected
		this.playeri = new Rectangle(this.toweri.getMaxX(), this.GUIy, (this.width * 2 / 5), this.height); // part that contains the player/game information
		// rectangles that represent the clickable areas of the different buttons
		this.RTbutton = new Rectangle((this.build.getMaxX() - this.rocket.getWidth() - 10), (this.build.getY() + 8), this.rocket.getWidth(), this.rocket.getHeight());
		this.MGbutton = new Rectangle((this.build.getMaxX() - this.mgun.getWidth() - 10), (this.RTbutton.getMaxY() + 8), this.mgun.getWidth(), this.mgun.getHeight());
		this.STbutton = new Rectangle((this.build.getMaxX() - this.slow.getWidth() - 10), (this.MGbutton.getMaxY() + 8), this.slow.getWidth(), this.slow.getHeight());
		this.sellButton = new Rectangle((this.toweri.getMaxX() - (this.toweri.getWidth() / 4) - this.sell.getWidth()), (this.toweri.getY() + (this.toweri.getHeight() / 3) - (this.sell.getHeight() / 2)), this.sell.getWidth(), this.sell.getHeight());
		this.upgradeButton = new Rectangle((this.toweri.getMaxX() - (this.toweri.getWidth() / 4) - this.upgrade.getWidth()), (this.toweri.getMaxY() - (this.toweri.getHeight() / 3) - (this.upgrade.getHeight() / 2)), this.upgrade.getWidth(), this.upgrade.getHeight());
		this.ppButton = new Rectangle((this.playeri.getMaxX() - this.pause.getWidth() - 10), (this.playeri.getY() + 20), this.pause.getWidth(), this.pause.getHeight());
		this.quitButton = new Rectangle((this.playeri.getMaxX() - this.quit.getWidth() - 10), (this.ppButton.getMaxY() + 20), this.quit.getWidth(), this.quit.getHeight());
	}
	
	// renders the different components of the game
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		g.setColor(Color.red);
		g.drawString("Build", (this.build.getX() + 2), (this.build.getY() + 1));
		g.drawString("Tower Info", (this.toweri.getX() + 2), (this.toweri.getY() + 1));
		g.drawString("Player Info", (this.playeri.getX() + 2), (this.playeri.getY() + 1));
		g.drawString("h: " + PlayerData.health, (this.playeri.getX() + 10), (this.playeri.getY() + (this.playeri.getHeight() / 4)));
		g.drawString("t: " + (this.gameState.getWaveManager().nextWaveIn / 1000), this.playeri.getCenterX(), (this.playeri.getY() + (this.playeri.getHeight() / 4)));
		g.setColor(Color.green);
		g.drawString("m: " + PlayerData.money, (this.playeri.getX() + 10), (this.playeri.getY() + (this.playeri.getHeight() / 2)));
		g.setColor(Color.blue);
		g.drawString("w: " + (this.gameState.getWaveManager().currentWave + 1) + "/" + this.gameState.getWaveManager().waves.length, this.playeri.getCenterX(), (this.playeri.getY() + (this.playeri.getHeight() / 2)));
		g.drawString("s: " + PlayerData.score, (this.playeri.getX() + 10), (this.playeri.getMaxY() - (this.playeri.getHeight() / 4)));
		g.draw(build);
		g.draw(toweri);
		g.draw(playeri);
		this.RTBimage.draw(this.RTbutton.getX(), this.RTbutton.getY());
		this.MGBimage.draw(this.MGbutton.getX(), this.MGbutton.getY());
		this.STBimage.draw(this.STbutton.getX(), this.STbutton.getY());
		this.PPimage.draw(this.ppButton.getX(), this.ppButton.getY());
		this.quit.draw(this.quitButton.getX(), this.quitButton.getY());
		g.setColor(Color.white);
		// write the information of the current tower selected and draw the upgrade and sell buttons 
		if(this.towerselect != null){
			g.drawString("level: " + (towerselect.towerLevel + 1), this.toweri.getX() + 10, (this.toweri.getY() + (this.toweri.getHeight() / 5)));
			g.drawString("range:  " + towerselect.range, (this.toweri.getX() + 10), (this.toweri.getY() + (this.toweri.getHeight() * 2 / 5)));
			g.drawString("damage: " + towerselect.damage, (this.toweri.getX() + 10), (this.toweri.getY() + (this.toweri.getHeight() * 3 / 5)));
			g.drawString("f-rate: " + towerselect.firerate, (this.toweri.getX() + 10), (this.toweri.getMaxY() - (this.toweri.getHeight() / 5)));
			// draw the button and the amount of money the user will receive
			this.sell.draw(this.sellButton.getX(), this.sellButton.getY());
			g.drawString("$" + (this.towerselect.cost / 2), this.sellButton.getMaxX() + 4, (this.sellButton.getY() + (this.sellButton.getHeight() / 4)));
			// show the upgrade button only if the tower is upgradable
			if (this.towerselect.towerLevel != 1){
				// draw the button and the amount of money the upgrade costs
				this.upgrade.draw(this.upgradeButton.getX(), this.upgradeButton.getY());
				g.drawString("$" + (Tower.DEFAULTVALUES[this.towerselect.towerType][this.towerselect.towerLevel+1][3]), this.upgradeButton.getMaxX() + 4, (this.upgradeButton.getY() + (this.upgradeButton.getHeight() / 4)));
			}
			// draw a rectangle to show which tower's info is being shown
			g.setColor(Color.blue);
			g.drawRect(this.towerselect.worldPosition.getX(), this.towerselect.worldPosition.getY(), TowerDefense.TILESIZE - 1, TowerDefense.TILESIZE - 1);
		}
		// if a tower is not selected, just write the names of the information fields
		else{
			g.drawString("level: ", this.toweri.getX() + 10, (this.toweri.getY() + (this.toweri.getHeight() / 5)));
			g.drawString("range:  ", (this.toweri.getX() + 10), (this.toweri.getY() + (this.toweri.getHeight() * 2 / 5)));
			g.drawString("damage: ", (this.toweri.getX() + 10), (this.toweri.getY() + (this.toweri.getHeight() * 3 / 5)));
			g.drawString("f-rate: ", (this.toweri.getX() + 10), (this.toweri.getMaxY() - (this.toweri.getHeight() / 5)));
		}
	}

	// no updating needed since all things are triggered by keyboard or mouse clicks
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		
	}
	
	// handles mouse clicks
	public void mouseClicked(int button, int x, int y, int clickCount){
		// if the player clicks on the rocket tower build button
		if(this.RTbutton.contains(x, y)){
			// if they are currently trying to build a rocket tower
			if(buildR){
				this.RTBimage = this.rocket; // turn the button's image back to the rocket tower image
				this.buildR = false; // the player is no long trying to build a rocket tower
				this.gameState.currentState = GameplayState.GameState.NORMAL; // go back to the normal state of the game state
			}
			// if they want to build a rocket tower
			else{
				this.RTBimage = this.cancel; // change the button's image to the cancel image
				this.STBimage = this.slow; // return the slow tower's button image to the slow tower
				this.MGBimage = this.mgun; // return the machine gun tower's button image to the machine gun tower
				this.buildS = false; // not trying to build a slow tower
				this.buildM = false; // not trying to build a maching gun tower
				this.buildR = true; // trying to build a rocket tower
				this.gameState.currentState = GameplayState.GameState.PLACE; // set the game state to place mode
			}
		}
		// the same goes for the other buttons on this GUI
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
		else if(this.quitButton.contains(x, y)){
			this.game.enterState(TowerDefense.LEVELSELECTION);
		}
		// if the player has selected a tower
		if(this.towerselect != null){
			// and they have clicked on the sell button
			if(this.sellButton.contains(x, y)){
				PlayerData.increaseMoney(this.towerselect.cost / 2); // return half the cost of the tower to the player
				this.gameState.getTowerManager().removeTower(this.towerselect.position); // remove the tower from the tower manager
				this.towerselect = null; // no tower currently selected
			}
			// if they have clicked on the upgrade button
			else if(this.upgradeButton.contains(x, y)){
				// attempt to upgrade the tower
				this.towerselect.upgradeTower();
			}
		}
	}
	
	// handles keyboard button presses
	public void keyPressed(int key, char c){
		// same code as the mouseclicked method, except that this is triggered by keyboard buttons
		// KEY_1 toggles building rocket tower
		// KEY_2 toggles building maching gun tower
		// KEY_3 toggles building slow tower
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
	
	// resets the button images to their original images and sets the build booleans to false
	public void resetButtons(){
		this.STBimage = this.slow;
		this.MGBimage = this.mgun;
		this.RTBimage = this.rocket;
		this.buildS = false;
		this.buildR = false;
		this.buildM = false;
	}
}
