package com.eleventhhour.towerdefense;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class TowerDefense extends StateBasedGame {
	
	public final static int MAINMENUSTATE = 1;
	public final static int GAMEPLAYSTATE = 2;
	
    public TowerDefense() {
        super("Tower Defense");
    }
 
 
    public static void main(String[] args) throws SlickException {
		try {
			AppGameContainer app = new AppGameContainer(new TowerDefense());
			app.setDisplayMode(800, 600, false);
			app.setTargetFrameRate(60);
			app.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}
    }


	@Override
	public void initStatesList(GameContainer gc) throws SlickException {
		GameplayState state = new GameplayState(GAMEPLAYSTATE);
		addState(state);
	}

}
