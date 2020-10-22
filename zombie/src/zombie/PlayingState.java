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
	// tile 0/0 = 60/60, +32 for each 1 tile direction
	
	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		map = new TiledMap("zombie/resource/protomap.tmx");
		
		for(int x = 0; x <= 15; x++) {
			for(int y = 0; y <= 15; y++) {
				int tile = map.getTileId(x, y, 0);
				System.out.println("(" + x + "," + y + ") = " + tile);
			}
		}
		survivor = new Survivor(60, 60, 0.0f, 0.0f); 
		survivor.setTileTarget(((int)survivor.getX() - 60) / 32, ((int)survivor.getY() - 60) / 32);

		//tile 0 center = 60x60y = 44 border + 16 (1/2 tile width)
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		map.render(44, 44); //600 size - 44 border = 512x512 tile map
		survivor.render(g);
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		Input input = container.getInput();
		
		int currTX = ((int)survivor.getX() - 60) / 32;
		int currTY = ((int)survivor.getY() - 60) / 32;
		
		if(input.isKeyDown(Input.KEY_W)) {
			survivor.setDesiredDirection(Survivor.UP);
		}
		
		if(input.isKeyDown(Input.KEY_A)) {
			survivor.setDesiredDirection(Survivor.LEFT);
		}
		
		if(input.isKeyDown(Input.KEY_S)) {
			survivor.setDesiredDirection(Survivor.DOWN);
		}
		
		if(input.isKeyDown(Input.KEY_D)) {
			survivor.setDesiredDirection(Survivor.RIGHT);
		}
		
		if(survivor.getDirection() != survivor.getDesiredDirection()) {
			if(survivor.getDirection() == Survivor.STILL) {
				if(survivor.getDesiredDirection() == Survivor.UP) {
					if(currTY - 1 >= 0) {
						if (map.getTileId(currTX, currTY - 1, 0) == 1 ) {
							survivor.setTileTarget(currTX, currTY - 1);
							survivor.setDirection(Survivor.UP);
						}
					}
				}
				if(survivor.getDesiredDirection() == Survivor.DOWN) {
					if(currTY + 1 <= 15) {
						if (map.getTileId(currTX, currTY + 1, 0) == 1 ) {
							survivor.setTileTarget(currTX, currTY + 1);	
							survivor.setDirection(Survivor.DOWN);
						}
					}
				}
				if(survivor.getDesiredDirection() == Survivor.LEFT) {
					if(currTX - 1 >= 0) {
						if (map.getTileId(currTX - 1, currTY, 0) == 1 ) {
							survivor.setTileTarget(currTX - 1, currTY);
							survivor.setDirection(Survivor.LEFT);
						}
					}
				}
				if(survivor.getDesiredDirection() == Survivor.RIGHT) {
					if(currTX + 1 <= 15) {
						if (map.getTileId(currTX + 1, currTY, 0) == 1 ) {
							survivor.setTileTarget(currTX + 1, currTY);
							survivor.setDirection(Survivor.RIGHT);
						}
					}
				}
			}
		}
		
		survivor.update(delta);
		
	}

	@Override
	public int getID() {
		return ZombieGame.PLAYINGSTATE;
	}

}
