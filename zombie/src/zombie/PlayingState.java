package zombie;

import java.util.PriorityQueue;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.tiled.TiledMap;

public class PlayingState extends BasicGameState {
	
	boolean overlay = false;
	
	private TiledMap map;
	Survivor survivor;
	
	int[][] dist;
	
	boolean WATERED = false;
	boolean FOODED = false;
	
	Item water;
	Item food;
	Item rope;
	
	int lastTX;
	int lastTY;
	
	public class CoordSet implements Comparable<CoordSet> {
		int x;
		int y;
		int dist;
		
		CoordSet(int xx, int yy, int d) {
			x = xx;
			y = yy;
			dist = d;
		}

		@Override
		public int compareTo(CoordSet o) {
			return Integer.compare(this.dist, o.dist);
		}
	}

	// tile 0/0 = 60/60, +32 for each 1 tile direction
	
	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		map = new TiledMap("zombie/resource/protomap.tmx");

		water = new Item(60, 92, 0);
		food = new Item(380, 380, 1);
		rope = new Item(60, 124, 2);
		
		survivor = new Survivor(60, 60, 0.0f, 0.0f); 
		survivor.setTileTarget(((int)survivor.getX() - 60) / 32, ((int)survivor.getY() - 60) / 32);

		//tile 0 center = 60x60y = 44 border + 16 (1/2 tile width)
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		map.render(44, 44); //600 size - 44 border = 512x512 tile map
		water.render(g);
		food.render(g);
		survivor.render(g);
		if(WATERED && FOODED) {
			rope.render(g);
		}
		if(overlay) {
			for(int x = 0; x <= 15; x++) {
				for(int y = 0; y <= 15; y++) {
					if(dist[x][y] != Integer.MAX_VALUE) {
						g.setColor(Color.black);
						g.drawString("" + dist[x][y], x*32 + 54, y*32 + 50);
					}
				}
			}
		}
	}
	
	public void dijkstra(TiledMap graph, int srcX, int srcY) {
		dist = new int[map.getWidth()][map.getHeight()];
		PriorityQueue<CoordSet> Q = new PriorityQueue<CoordSet>();
		
		for(int x = 0; x <= map.getWidth()-1; x++) {
			for(int y = 0; y <= map.getHeight()-1; y++) {
				//for each tile on graph:
				
				CoordSet coord = new CoordSet(x, y, 0);
				if(!((x == srcX) && (y == srcY))) {
					dist[x][y] = Integer.MAX_VALUE; // default infinity
					coord.dist = Integer.MAX_VALUE;
				} else {
					dist[x][y] = 0; // source = 0
					Q.add(coord);
				}
			}
		}
				
		while (!Q.isEmpty()) {
			CoordSet u = Q.poll();
			
			if(u.y + 1 <= map.getHeight()-1) {
				if((map.getTileId(u.x, u.y+1, 0) == 1)  && (dist[u.x][u.y + 1] > dist[u.x][u.y])) {
					dist[u.x][u.y+1] = dist[u.x][u.y]+1;
					Q.add(new CoordSet(u.x, u.y+1, dist[u.x][u.y]+1));
				}
			}

			if(u.x + 1 <= map.getWidth()-1) {
				if((map.getTileId(u.x+1, u.y, 0) == 1)  && (dist[u.x+1][u.y] > dist[u.x][u.y])) {
					dist[u.x+1][u.y] = dist[u.x][u.y]+1;
					Q.add(new CoordSet(u.x+1, u.y, dist[u.x][u.y]+1));
				}
			}
			
			if(u.y - 1 >= 0) {
				if((map.getTileId(u.x, u.y-1, 0) == 1)  && (dist[u.x][u.y-1] > dist[u.x][u.y])) {
					dist[u.x][u.y-1] = dist[u.x][u.y]+1;
					Q.add(new CoordSet(u.x, u.y-1, dist[u.x][u.y]+1));
				}
			}

			if(u.x - 1 >= 0) {
				if((map.getTileId(u.x-1, u.y, 0) == 1) && (dist[u.x-1][u.y] > dist[u.x][u.y])) {
					dist[u.x-1][u.y] = dist[u.x][u.y]+1;
					Q.add(new CoordSet(u.x-1, u.y, dist[u.x][u.y]+1));
				}
			}
			

		}
		
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		Input input = container.getInput();
		
		int currTX = ((int)survivor.getX() - 60) / 32;
		int currTY = ((int)survivor.getY() - 60) / 32;
		if((currTX != lastTX) || (currTY != lastTY)) {
			lastTX = currTX;
			lastTY = currTY;
			this.dijkstra(map, currTX, currTY);
		}
		
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
		
		if(input.isKeyDown(Input.KEY_Q)) {
			overlay = true;
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
		
		if(survivor.collides(water) != null) {
			water.acquire(Item.WATER);
			game.enterState(ZombieGame.PLAYINGSTATE);
			WATERED = true;
		}
		
		if(survivor.collides(food) != null) {
			food.acquire(Item.FOOD);
			FOODED = true;
		}
		
		if(survivor.collides(rope) != null) {
			if(WATERED && FOODED) {
				game.enterState(ZombieGame.GAMEOVERSTATE);
			}
		}
		
		survivor.update(delta);
		
	}

	@Override
	public int getID() {
		return ZombieGame.PLAYINGSTATE;
	}

}
