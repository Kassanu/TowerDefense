package com.eleventhhour.towerdefense;

import java.io.File;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.StateBasedGame;

public class gameOverGUI {

	public int width,height; // dimensions of GUI
	public int GUIx, GUIy; //top-left corner of GUI
	private Rectangle winImageR, loseImageR, scoreImageR, menuButtonR; // rectangles for button click detection
	private Image background, winImage, loseImage, scoreImage, menuButton; // images used by this gui
	private StateBasedGame sbg; // state based game that uses this gui
	private boolean win = false; // win/lose game
	
	public gameOverGUI(StateBasedGame sbg) {
		this.sbg = sbg;
		try {
			this.background = new Image("res" + File.separator +"menupopup.png");
			this.winImage = new Image("res" + File.separator +"levelcomplete.png");
			this.loseImage = new Image("res" + File.separator +"levelfailed.png");
			this.scoreImage = new Image("res" + File.separator +"score.png");
			this.menuButton = new Image("res" + File.separator +"menubutton.png");
		} catch (SlickException e) {
			e.printStackTrace();
		}
		this.width = this.background.getWidth();
		this.height = this.background.getHeight();
		this.GUIx = (TowerDefense.width / 2) - (this.width / 2);
		this.GUIy = ((TowerDefense.height - 128) / 2) - (this.height / 2);
		this.winImageR = new Rectangle((this.GUIx + (this.width / 2)) - (this.winImage.getWidth() / 2),(this.GUIy + 50), this.winImage.getWidth(), this.winImage.getHeight());
		this.loseImageR = new Rectangle((this.GUIx + (this.width / 2)) - (this.loseImage.getWidth() / 2),(this.GUIy + 50), this.loseImage.getWidth(), this.loseImage.getHeight());
		this.scoreImageR = new Rectangle((this.GUIx + 60),(this.GUIy + (this.height / 2)) - (this.scoreImage.getHeight() / 2), this.scoreImage.getWidth(), this.scoreImage.getHeight());
		this.menuButtonR = new Rectangle((this.GUIx + (this.width / 2)) - (this.menuButton.getWidth() / 2),((this.GUIy + this.height) - 50) - this.menuButton.getHeight(), this.menuButton.getWidth(), this.menuButton.getHeight());
	}
	
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		this.background.draw(this.GUIx, this.GUIy);
		if (this.win)
			this.winImage.draw(this.winImageR.getX(),this.winImageR.getY());
		else
			this.loseImage.draw(this.loseImageR.getX(),this.loseImageR.getY());
		
		this.scoreImage.draw(this.scoreImageR.getX(),this.scoreImageR.getY());
		g.setColor(Color.white);
		g.drawString(""+PlayerData.score, this.scoreImageR.getX() + (this.scoreImageR.getWidth() + 20),this.scoreImageR.getY());
		this.menuButton.draw(this.menuButtonR.getX(),this.menuButtonR.getY());

	}
	
	public void setWin(boolean win) {
		this.win = win;
	}

	public void mouseClicked(int button, int x, int y, int clickCount) {
		if(this.menuButtonR.contains(x, y)){
			this.sbg.enterState(TowerDefense.LEVELSELECTION);
			PlayerData.resetPlayer();
		}
	}
	
}
