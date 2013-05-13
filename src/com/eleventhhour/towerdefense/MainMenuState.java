package com.eleventhhour.towerdefense;
import java.io.File;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.MouseListener;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;


public class MainMenuState extends BasicGameState implements MouseListener {
	
	// different colors of the different parts of this game state
	private Color bgcolor = Color.black;
	private Color textcolor = Color.white;
	// strings for printing text
	private String gameTitle = new String("Modern Tower Defense");
	private String authors = new String("Created by David Levy and Matt Martucciello");
	private Rectangle playButton; // rectangle to detect a click on the play button
	private Image play; // image of the play button
	private boolean pressed; // boolean to determine when the play button is pressed
	private int stateId; // the id number of this state
	
	public MainMenuState(int id){
		super();
		this.stateId = id;
	}

	@Override
	// initializes the values of the variables in this game state
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		this.pressed = false;
		gc.getGraphics().setBackground(bgcolor);
		int centerX = gc.getWidth() / 2;
		int centerY = gc.getHeight() / 2;
		this.play = new Image("res" + File.separator +"play.png");
		this.playButton = new Rectangle((centerX - (this.play.getWidth() / 2)), (centerY - (this.play.getHeight() / 2)), this.play.getWidth(), this.play.getHeight());
	}

	@Override
	// renders the different components in this game state
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		g.setColor(textcolor);
		g.drawString(this.gameTitle, 30, 30);
		g.drawString(this.authors, 30, 50);
		this.play.draw(this.playButton.getX(), this.playButton.getY());
		
	}

	@Override
	// updates the components of this game state
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		if(this.pressed == true){
			sbg.enterState(TowerDefense.LEVELSELECTION);
		}
	}
	
	// method is triggered on a mouse click, part implemented from MouseListener 
	public void mouseClicked(int button, int x, int y, int clickCount){
		if(this.playButton.contains(x, y)){
			this.pressed = true;
		}
	}

	@Override
	// returns the id of this game state
	public int getID() {
		return this.stateId;
	}

}
