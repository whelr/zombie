package zombie;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.tiled.TiledMap;

public class PlayingState extends BasicGameState {
	
	private TiledMap map;
	Survivor survivor;
	
	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		map = new TiledMap("zombie/resource/protomap.tmx");
		survivor = new Survivor(60, 60, 0.0f, 0.0f);
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		map.render(44, 44); //600 size - 44 border = 512x512 tile map
		survivor.render(g);
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		Input input = container.getInput();
		
		if(input.isKeyDown(Input.KEY_W)) {
			survivor.setDirection(Survivor.UP);
		}
		
		if(input.isKeyDown(Input.KEY_A)) {
			survivor.setDirection(Survivor.LEFT);
		}
		
		if(input.isKeyDown(Input.KEY_S)) {
			survivor.setDirection(Survivor.DOWN);
		}
		
		if(input.isKeyDown(Input.KEY_D)) {
			survivor.setDirection(Survivor.RIGHT);
		}
		
		survivor.update(delta);
		
	}

	@Override
	public int getID() {
		// TODO Auto-generated method stub
		return ZombieGame.PLAYINGSTATE;
	}

}
