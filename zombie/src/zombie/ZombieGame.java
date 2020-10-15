package zombie;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class ZombieGame extends StateBasedGame {

	public static final int STARTUPSTATE = 0;
	public static final int PLAYINGSTATE = 1;
	public static final int GAMEOVERSTATE = 2;
	
	public final int ScreenWidth;
	public final int ScreenHeight;
	
	public ZombieGame(String title, int width, int height) {
		super(title);
		ScreenHeight = height;
		ScreenWidth = width;
	}

	@Override
	public void initStatesList(GameContainer container) throws SlickException {
		addState(new StartUpState());
		addState(new GameOverState());
		addState(new PlayingState());
	}
	
	public static void main(String[] args) {
		AppGameContainer app;
		try {
			app = new AppGameContainer(new ZombieGame("Zombies!", 800, 600));
			app.setDisplayMode(800, 600, false);
			app.setVSync(true);
			app.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

}