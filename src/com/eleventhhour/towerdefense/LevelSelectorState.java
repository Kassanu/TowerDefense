package com.eleventhhour.towerdefense;


import java.io.File;
import java.util.Arrays;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.MouseListener;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class LevelSelectorState extends BasicGameState implements MouseListener {
	
	private int stateId;
	private Image arrow, play;
	private Image[] levelImages;
	private Color bgcolor = Color.black;
	private Color buttoncolor = Color.green;
	private Color textcolor = Color.white;
	private Rectangle nextButton, prevButton, playButton, levelRect;
	private String levelSelectTitle = new String("Level Selection");
	public String levelName;
	public int centerX, centerY, currentLevel = 0;
	private StateBasedGame sbg;
	private File[] levels;
	
	public LevelSelectorState(int id){
		super();
		this.stateId = id;
	}

	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		this.sbg = sbg;
		gc.getGraphics().setBackground(bgcolor);
		this.arrow = new Image("res" + File.separator +"arrow.png");
		this.play = new Image("res" + File.separator +"play.png");
		this.centerX = gc.getWidth() / 2;
		this.centerY = gc.getHeight() / 2;
		this.playButton = new Rectangle(this.centerX - (this.play.getWidth() / 2), gc.getWidth() - 130, this.play.getWidth(), this.play.getHeight());
		this.nextButton = new Rectangle(560 - this.arrow.getWidth(), this.centerY - (this.arrow.getHeight() / 2), this.arrow.getWidth(), this.arrow.getHeight());
		this.prevButton = new Rectangle(80, this.centerY - (this.arrow.getHeight() / 2), this.arrow.getWidth(), this.arrow.getHeight());
		this.levelRect = new Rectangle(this.centerX - (320 / 2), this.centerY - (240 / 2), 320, 240);
	}
	
	public void enter(GameContainer gc, StateBasedGame sbg) throws SlickException {
		File levelDirectory = new File("res" + File.separator +"levels");
		this.levels = levelDirectory.listFiles();
		this.levelImages = new Image[this.levels.length];
		for (int i = 0; i < this.levelImages.length; i++)
			this.levelImages[i] = new Image(this.levels[i] + File.separator + "level.png");
	}
	
	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		g.setColor(textcolor);
		g.drawString(this.levelSelectTitle, (this.centerX), 100);
		this.arrow.setRotation(0);
		this.arrow.draw(this.prevButton.getX(), this.prevButton.getY());
		this.arrow.setRotation(180);
		this.arrow.draw(this.nextButton.getX(), this.nextButton.getY());
		this.play.draw(this.playButton.getX(), this.playButton.getY());
		this.levelImages[this.currentLevel].draw(this.levelRect.getX(), this.levelRect.getY());
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {	}
	
	public void mouseClicked(int button, int x, int y, int clickCount){
		if(this.playButton.contains(x, y)){
			PlayerData.resetPlayer();
			PlayerData.setLevel(this.currentLevel + 1);
			this.sbg.enterState(TowerDefense.GAMEPLAYSTATE);
		}
		else if (this.prevButton.contains(x, y)){
			if (this.currentLevel == 0)
				this.currentLevel = this.levels.length - 1;
			else
				this.currentLevel--;
		}
		else if (this.nextButton.contains(x, y)){
			this.currentLevel = (this.currentLevel+1)%this.levels.length;
		}
	}
	
	@Override
	public int getID() {
		return this.stateId;
	}
	
}
