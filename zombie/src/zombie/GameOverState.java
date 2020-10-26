package zombie;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class GameOverState extends BasicGameState {

	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		ZombieGame zg = (ZombieGame)game;
		if(zg.dead) {
			g.drawString("Dijkstra's Zombie Experiment was a success!", 50, 200);
			g.drawString("The zombie ate you...", 50, 240);
			g.drawString("Speak 'Space' to Main Menu", 190, 350);
		} else {
			g.drawString("Dijkstra's Zombie Experiment was a fail!", 50, 200);
			g.drawString("The zombie failed to eat you...", 50, 240);
		}
		g.drawString("Speak 'Space' to Main Menu", 190, 350);
		
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		
		Input input = container.getInput();
		ZombieGame zg = (ZombieGame)game;
		
		if(input.isKeyDown(Input.KEY_SPACE)) {
			System.out.println("space");
			input.clearKeyPressedRecord();
			zg.level = 1;
			zg.dead = false;
			zg.getState(ZombieGame.STARTUPSTATE).init(container, game);
			zg.enterState(ZombieGame.STARTUPSTATE);
			return;
		}
		
	}

	@Override
	public int getID() {
		return ZombieGame.GAMEOVERSTATE;
	}

}
