package zombie;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import jig.Entity;
import jig.ResourceManager;

public class ZombieGame extends StateBasedGame {

	public static final int STARTUPSTATE = 0;
	public static final int PLAYINGSTATE = 1;
	public static final int GAMEOVERSTATE = 2;
	
	public static final String SURVIVORIMG_RSC = "zombie/resource/survivor.png";
	public static final String ZOMBIEIMG_RSC = "zombie/resource/zombie.png";
	public static final String WATERIMG_RSC = "zombie/resource/water.png";
	public static final String FOODIMG_RSC = "zombie/resource/food.png";
	public static final String ROPEIMG_RSC = "zombie/resource/rope.png";

	
	public final int ScreenWidth;
	public final int ScreenHeight;
	
	public int level = 1;
	
	public ZombieGame(String title, int width, int height) {
		super(title);
		ScreenHeight = height;
		ScreenWidth = width;
		Entity.setCoarseGrainedCollisionBoundary(Entity.AABB);

	}

	@Override
	public void initStatesList(GameContainer container) throws SlickException {
		addState(new StartUpState());
		addState(new GameOverState());
		addState(new PlayingState());
		
		ResourceManager.loadImage(SURVIVORIMG_RSC);
		ResourceManager.loadImage(ZOMBIEIMG_RSC);
		ResourceManager.loadImage(WATERIMG_RSC);
		ResourceManager.loadImage(FOODIMG_RSC);
		ResourceManager.loadImage(ROPEIMG_RSC);
	}
	
	public static void main(String[] args) {
		AppGameContainer app;
		try {
			app = new AppGameContainer(new ZombieGame("Zombies!", 800, 600));
			app.setDisplayMode(600, 600, false);
			app.setVSync(true);
			app.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

}
