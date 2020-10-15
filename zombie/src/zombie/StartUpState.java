package zombie;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.Input;

public class StartUpState extends BasicGameState {

	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		g.drawString("Speak 'Space' to Enter", 225, 300);
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		Input input = container.getInput();
		ZombieGame zg = (ZombieGame)game;
		
		if(input.isKeyDown(Input.KEY_SPACE))
			zg.enterState(ZombieGame.PLAYINGSTATE);
	}

	@Override
	public int getID() {
		return ZombieGame.STARTUPSTATE;
	}

}
