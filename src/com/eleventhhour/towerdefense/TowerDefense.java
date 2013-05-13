package com.eleventhhour.towerdefense;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

/**
 * Tower Defense
 * blah
 * blah blah 
 * game description
 * 
 *	@author Dave Levy, Matt Martucciello
 *	@copyright Dave Levy, Matt Martucciello
 *	@credits Dave Levy, Matt Martucciello
 *	@liscense None
 *	@version Pre-Alpha 0.0.1
 */

public class TowerDefense extends StateBasedGame {
	
	// integer representation of the different states
	public final static int MAINMENUSTATE = 1;
	public final static int GAMEPLAYSTATE = 2;
	public final static int LEVELSELECTION = 3;
	// width and height of the display
	public final static int width = 640;
	public final static int height = 640;
	// size of each tile
	public final static int TILESIZE = 32;
	public static int SCALE = 1; 	//RENDER SCALE - this will just effect the rendering really all the math can be done without the scaling just the final render has to be
									// scaled so it will show on the screen correctly
	
    public TowerDefense() {
        super("Tower Defense");
    }
 
    // main method starts and runs the game
    public static void main(String[] args) throws SlickException {
    	System.out.println(System.getProperty("java.runtime.version"));
		try {
			PlayerData.getInstance();
			// create a new game container to start and run this game
			AppGameContainer app = new AppGameContainer(new TowerDefense());
			app.setDisplayMode(TowerDefense.width, TowerDefense.height, false);
			app.setTargetFrameRate(60);
			app.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}
    }


	@Override
	// initializes the different states in this state based game
	// this method is called before the main method
	public void initStatesList(GameContainer gc) throws SlickException {
		MainMenuState state1 = new MainMenuState(MAINMENUSTATE);
		GameplayState state2 = new GameplayState(GAMEPLAYSTATE);
		LevelSelectorState state3 = new LevelSelectorState(LEVELSELECTION);
		addState(state1);
		addState(state2);
		addState(state3);
	}

}
