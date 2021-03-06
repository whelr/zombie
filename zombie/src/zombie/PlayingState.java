package zombie;

import java.util.HashMap;
import java.util.Map;
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
	boolean godded = false;
	
	private TiledMap map;
	Humanoid survivor;
	Humanoid zombie;
	int level;
	
	int[][] dist;
	
	boolean WATERED = false;
	boolean FOODED = false;
	
	Item water;
	Item food;
	Item door;
	
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
	public int getTCenter(int i) {
		int center = 60 + (32 * i);
		return center;
	}
	
	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		
		ZombieGame zg = (ZombieGame)game;
		level = zg.level;
		zg.dead = false;
		WATERED = false;
		FOODED = false;
		
		
		if(zg.level == 1) {
			map = new TiledMap("zombie/resource/level1.tmx");
			water = new Item(getTCenter(7), getTCenter(1), 0); //7/1
			food = new Item(getTCenter(6), getTCenter(11), 1); //6/11
			zombie = new Humanoid(getTCenter(15), getTCenter(15), 1); //bottom right
		}
		if(zg.level == 2) {
			map = new TiledMap("zombie/resource/level2.tmx");
			water = new Item(getTCenter(3), getTCenter(14), 0); //3/14
			food = new Item(getTCenter(14), getTCenter(4), 1); //14/4
			zombie = new Humanoid(getTCenter(15), getTCenter(15), 1); //bottom right
		}
		if(zg.level == 3) {
			map = new TiledMap("zombie/resource/level3.tmx");
			water = new Item(getTCenter(6), getTCenter(12), 0); //6/12
			food = new Item(getTCenter(11), getTCenter(4), 1); //11/4
			zombie = new Humanoid(getTCenter(15), getTCenter(15), 1); //bottom right
		}
		if(zg.level == 4) {
			map = new TiledMap("zombie/resource/level4.tmx");
			water = new Item(getTCenter(9), getTCenter(5), 0); //9/5
			food = new Item(getTCenter(4), getTCenter(14), 1); //4/14
			zombie = new Humanoid(getTCenter(13), getTCenter(15), 1); //bottom right
		}
		if(zg.level == 5) {
			map = new TiledMap("zombie/resource/level5.tmx");
			water = new Item(getTCenter(12), getTCenter(6), 0); //12/6
			food = new Item(getTCenter(1), getTCenter(15), 1); //1/15
			zombie = new Humanoid(getTCenter(15), getTCenter(15), 1); //bottom right
		}
		
		dist = new int[map.getWidth()][map.getHeight()];
		for(int x = 0; x <= map.getWidth()-1; x++) {
			for(int y = 0; y <= map.getHeight()-1; y++) {
				dist[x][y] = Integer.MAX_VALUE;
			}
		}
		
		survivor = new Humanoid(getTCenter(0), getTCenter(0), 0); //top left	
		door = new Item(getTCenter(14), getTCenter(14), 2);
		lastTX = ((int)survivor.getX() - 60) / 32;
		lastTY = ((int)survivor.getX() - 60) / 32;

	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		map.render(44, 44); //600 size - 44 border = 512x512 tile map
		water.render(g);
		food.render(g);
		survivor.render(g);
		zombie.render(g);
		
		if(WATERED && FOODED) {
			door.render(g);
		}
				
		if(godded) {
			g.setColor(Color.white);

			g.drawString("GODEMODE ENBABLED!", 200, 25);
		}
		
		g.setColor(Color.white);
		g.drawString("Level: " + level, 475, 25);
		
		if(overlay) {
			for(int x = 0; x <= 15; x++) {
				for(int y = 0; y <= 15; y++) {
					if(dist[x][y] != Integer.MAX_VALUE) {
						g.drawString("" + dist[x][y], x*32 + 54, y*32 + 50);
					} else {
						g.drawString("X", x*32 + 54, y*32 + 50);
					}
				}
			}
		}
	}
	
	public void dijkstra(TiledMap graph, int srcX, int srcY, int zombTX, int zombTY) {
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
			if((u.x == zombTX) && (u.y == zombTY)) {
				break;
			}
			//System.out.println("polled");
			int distance = dist[u.x][u.y];
			
			if(u.x + 1 <= map.getWidth()-1) {
				if((map.getTileId(u.x+1, u.y, 0) == 1)  && (dist[u.x+1][u.y] > distance)) {
					dist[u.x+1][u.y] = dist[u.x][u.y]+1;
					Q.add(new CoordSet(u.x+1, u.y, dist[u.x][u.y]+1));
				}
			}
			
			if(u.y + 1 <= map.getHeight()-1) {
				if((map.getTileId(u.x, u.y+1, 0) == 1)  && (dist[u.x][u.y + 1] > distance)) {
					dist[u.x][u.y+1] = dist[u.x][u.y]+1;
					Q.add(new CoordSet(u.x, u.y+1, dist[u.x][u.y]+1));
				}
			}
			
			if(u.y - 1 >= 0) {
				if((map.getTileId(u.x, u.y-1, 0) == 1)  && (dist[u.x][u.y-1] > distance)) {
					dist[u.x][u.y-1] = dist[u.x][u.y]+1;
					Q.add(new CoordSet(u.x, u.y-1, dist[u.x][u.y]+1));
				}
			}

			if(u.x - 1 >= 0) {
				if((map.getTileId(u.x-1, u.y, 0) == 1) && (dist[u.x-1][u.y] > distance)) {
					dist[u.x-1][u.y] = dist[u.x][u.y]+1;
					Q.add(new CoordSet(u.x-1, u.y, dist[u.x][u.y]+1));
				}
			}

		}
		
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		Input input = container.getInput();
		ZombieGame zg = (ZombieGame)game;
		
		int zombTX = ((int)zombie.getX() - 60) / 32;
		int zombTY = ((int)zombie.getY() - 60) / 32;
		
		int currTX = ((int)survivor.getX() - 60) / 32;
		int currTY = ((int)survivor.getY() - 60) / 32;
		
		if(input.isKeyPressed(Input.KEY_W)) {
			survivor.setDesiredDirection(Humanoid.UP);
		}
		
		if(input.isKeyPressed(Input.KEY_A)) {
			survivor.setDesiredDirection(Humanoid.LEFT);
		}
		
		if(input.isKeyPressed(Input.KEY_S)) {
			survivor.setDesiredDirection(Humanoid.DOWN);
		}
		
		if(input.isKeyPressed(Input.KEY_D)) {
			survivor.setDesiredDirection(Humanoid.RIGHT);
		}
		
		if(input.isKeyPressed(Input.KEY_UP)) {
			survivor.setDesiredDirection(Humanoid.UP);
		}
		
		if(input.isKeyPressed(Input.KEY_LEFT)) {
			survivor.setDesiredDirection(Humanoid.LEFT);
		}
		
		if(input.isKeyPressed(Input.KEY_DOWN)) {
			survivor.setDesiredDirection(Humanoid.DOWN);
		}
		
		if(input.isKeyPressed(Input.KEY_RIGHT)) {
			survivor.setDesiredDirection(Humanoid.RIGHT);
		}
		
		if(input.isKeyPressed(Input.KEY_Q)) {
			if (overlay) {
				overlay = false;
			} else {
				overlay = true;
			}
		}
		
		if(input.isKeyPressed(Input.KEY_G)) {
			if (godded) {
				godded = false;
			} else {
				godded = true;
			}
		}
		
		if(survivor.getDirection() != survivor.getDesiredDirection()) {
			if(survivor.getDirection() == Humanoid.STILL) {
				if(survivor.getDesiredDirection() == Humanoid.UP) {
					if(currTY - 1 >= 0) {
						if (map.getTileId(currTX, currTY - 1, 0) == 1 ) {
							survivor.setTileTarget(currTX, currTY - 1);
							this.dijkstra(map, survivor.tileTargetX, survivor.tileTargetY, zombTX, zombTY);
							survivor.setDirection(Humanoid.UP);
						}
					}
				}
				if(survivor.getDesiredDirection() == Humanoid.DOWN) {
					if(currTY + 1 <= map.getHeight()-1) {
						if (map.getTileId(currTX, currTY + 1, 0) == 1 ) {
							survivor.setTileTarget(currTX, currTY + 1);	
							this.dijkstra(map, survivor.tileTargetX, survivor.tileTargetY, zombTX, zombTY);
							survivor.setDirection(Humanoid.DOWN);
						}
					}
				}
				if(survivor.getDesiredDirection() == Humanoid.LEFT) {
					if(currTX - 1 >= 0) {
						if (map.getTileId(currTX - 1, currTY, 0) == 1 ) {
							survivor.setTileTarget(currTX - 1, currTY);
							this.dijkstra(map, survivor.tileTargetX, survivor.tileTargetY, zombTX, zombTY);

							survivor.setDirection(Humanoid.LEFT);
						}
					}
				}
				if(survivor.getDesiredDirection() == Humanoid.RIGHT) {
					if(currTX + 1 <= map.getWidth()-1) {
						if (map.getTileId(currTX + 1, currTY, 0) == 1 ) {
							survivor.setTileTarget(currTX + 1, currTY);
							this.dijkstra(map, survivor.tileTargetX, survivor.tileTargetY, zombTX, zombTY);
							survivor.setDirection(Humanoid.RIGHT);
						}
					}
				}
			}
		} else {
			if((currTX != lastTX) || (currTY != lastTY)) {
				this.dijkstra(map, survivor.tileTargetX, survivor.tileTargetY, zombTX, zombTY);
				lastTX = currTX;
				lastTY = currTY;
			}
		}
		
		if(zombie.getDirection() == Humanoid.STILL) {
			Map<String, Integer> choices = new HashMap<String, Integer>();
			if(zombTX - 1 >= 0) {
				if (map.getTileId(zombTX - 1, zombTY, 0) == 1 ) {
					choices.put("Left", dist[zombTX-1][zombTY]);
				}
			}
			if(zombTX + 1 <= map.getWidth()-1) {
				if (map.getTileId(zombTX + 1, zombTY, 0) == 1 ) {
					choices.put("Right", dist[zombTX+1][zombTY]);
				}
			}
			if(zombTY - 1 >= 0) {
				if (map.getTileId(zombTX, zombTY - 1, 0) == 1 ) {
					choices.put("Up", dist[zombTX][zombTY-1]);
				}
			}
			if(zombTY + 1 <= map.getHeight()-1) {
				if (map.getTileId(zombTX, zombTY + 1, 0) == 1 ) {
					choices.put("Down", dist[zombTX][zombTY+1]);
				}
			}
			int distance = dist[zombTX][zombTY];
			int NextX = zombTX;
			int NextY = zombTY;
			int NextDir = Humanoid.STILL;
			
			if(choices.containsKey("Right")) {
				if(choices.get("Right") <= distance) {
					NextX++;
					NextDir = Humanoid.RIGHT;
				}
			}
			if(choices.containsKey("Down")) {
				if(choices.get("Down") <= distance) {
					NextY++;
					NextDir = Humanoid.DOWN;
				}
			}
			if(choices.containsKey("Left")) {
				if(choices.get("Left") <= distance) {
					NextX--;
					NextDir = Humanoid.LEFT;
				}
			}
			if(choices.containsKey("Up")) {
				if(choices.get("Up") <= distance) {
					NextY--;
					NextDir = Humanoid.UP;
				}
			}
			
			zombie.setTileTarget(NextX, NextY);
			zombie.setDirection(NextDir);			
			
		}
		
		if(survivor.collides(water) != null) {
			water.acquire(Item.WATER);
			WATERED = true;
		}
		
		if(survivor.collides(food) != null) {
			food.acquire(Item.FOOD);
			FOODED = true;
		}
		
		if(survivor.collides(door) != null) {
			if(WATERED && FOODED) {
				if(zg.level < 6) {
					zg.level++;
					zg.getCurrentState().init(container, game);	
				}
				if(zg.level == 6) {
					zg.enterState(ZombieGame.GAMEOVERSTATE);
					return;
				}
			}
		}
		
		if(survivor.collides(zombie) != null) {
			if(!godded) {
				zg.dead = true;
				zg.enterState(ZombieGame.GAMEOVERSTATE);
				return;	
			}
		}
		
		survivor.update(delta);
		zombie.update(delta);
		
		if(input.isKeyPressed(Input.KEY_1)) {
			input.clearKeyPressedRecord();
			zg.level = 1;
			this.init(container, game);
		}
		
		if(input.isKeyPressed(Input.KEY_2)) {
			input.clearKeyPressedRecord();
			zg.level = 2;
			this.init(container, game);
		}
		
		if(input.isKeyPressed(Input.KEY_3)) {
			input.clearKeyPressedRecord();
			zg.level = 3;
			this.init(container, game);
		}
		
		if(input.isKeyPressed(Input.KEY_4)) {
			input.clearKeyPressedRecord();
			zg.level = 4;
			this.init(container, game);
		}
		
		if(input.isKeyPressed(Input.KEY_5)) {
			zg.level = 5;
			this.init(container, game);
		}
		
		
	}

	@Override
	public int getID() {
		return ZombieGame.PLAYINGSTATE;
	}

}
