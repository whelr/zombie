package zombie;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.tiled.TiledMap;

public class PlayingState extends BasicGameState {
	
	private TiledMap map;
	
	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		map = new TiledMap("zombie/resource/protomap.tmx");
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		map.render(44, 44); //600 size - 44 border = 512x512 tile map
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
	
	}

	@Override
	public int getID() {
		// TODO Auto-generated method stub
		return ZombieGame.PLAYINGSTATE;
	}

}
