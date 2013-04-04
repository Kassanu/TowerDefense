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
	
	public final static int MAINMENUSTATE = 1;
	public final static int GAMEPLAYSTATE = 2;
	public final static int width = 320;
	public final static int height = 320;
	public final static int SCALE = 2;
	public final static int TILESIZE = 32;
	public final static int SCALEDTILESIZE = SCALE * TILESIZE;
	
    public TowerDefense() {
        super("Tower Defense");
    }
 
 
    public static void main(String[] args) throws SlickException {
		try {
			AppGameContainer app = new AppGameContainer(new TowerDefense());
			app.setDisplayMode(TowerDefense.width * TowerDefense.SCALE, TowerDefense.height * TowerDefense.SCALE, false);
			app.setTargetFrameRate(60);
			app.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}
    }


	@Override
	public void initStatesList(GameContainer gc) throws SlickException {
		MainMenuState state1 = new MainMenuState(MAINMENUSTATE);
		GameplayState state2 = new GameplayState(GAMEPLAYSTATE);
		addState(state1);
		addState(state2);
	}

}
