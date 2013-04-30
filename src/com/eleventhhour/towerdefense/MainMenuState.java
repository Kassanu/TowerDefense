package com.eleventhhour.towerdefense;
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
	
	private Color bgcolor = Color.black;
	private Color buttoncolor = Color.green;
	private Color textcolor = Color.white;
	private Rectangle startButton;
	private String buttonText = new String("Play");
	private String gameTitle = new String("Modern Tower Defense");
	private String authors = new String("Created by David Levy and Matt Martucciello");
	private boolean pressed;
	private int stateId;
	
	public MainMenuState(int id){
		super();
		this.stateId = id;
	}

	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		this.pressed = false;
		gc.getGraphics().setBackground(bgcolor);
		int centerX = gc.getWidth() / 2;
		int centerY = gc.getHeight() / 2;
		int buttonW = 100;
		int buttonH = 60;
		this.startButton = new Rectangle((centerX - (buttonW / 2)), (centerY + 25), buttonW, buttonH);
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		//int centerX = gc.getWidth() / 2;
		//int centerY = gc.getHeight() / 2;
		
		//g.setColor(textcolor);
		g.drawString(this.gameTitle, 30, 30);
		g.drawString(this.authors, 30, 50);
		g.setColor(buttoncolor);
		g.fill(this.startButton);
		g.setColor(textcolor);
		g.drawString(this.buttonText, (this.startButton.getX() + 2), (this.startButton.getY() + 2));
		
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		if(this.pressed == true){
			sbg.enterState(TowerDefense.LEVELSELECTION);
		}
	}
	
	public void mouseClicked(int button, int x, int y, int clickCount){
		if(this.startButton.contains(x, y)){
			this.pressed = true;
		}
	}

	@Override
	public int getID() {
		return this.stateId;
	}

}
