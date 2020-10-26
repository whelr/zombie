package zombie;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.Input;

public class StartUpState extends BasicGameState {

	Humanoid survivor;
	Humanoid zombie;
	Item food;
	Item water;
	Item DOOR;
	
	boolean starting = false;
	
	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		zombie = new Humanoid(50, 150, 1); //top left	
		survivor = new Humanoid(150, 150, 0); //bottom right
		water = new Item(200, 150, 0); //7/1
		food = new Item(250, 150, 1); //6/11
		DOOR = new Item(500, 150, 2); //6/1/
	}
	
	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		g.drawString("Welcome to Dijkstra's Zombie Experiment!", 25, 200);
		g.drawString("You are an experimental purple block humanoid,", 25, 220);
		g.drawString("Your goal is to not be consumed by the green zombie blockman.", 25, 240);
		g.drawString("Proceed through the level collecting food and water.", 25, 260);
		g.drawString("Once aquired, a door will take you to the next level.", 25, 280);
		g.drawString("5 Levels Await...", 25, 300);
		g.drawString("Speak 'Space' to Enter", 190, 350);
		
		g.drawString("Controls:", 25, 400);
		g.drawString("WASD - Movement.", 35, 420);
		g.drawString("1, 2, 3, 4, 5 to level skip.", 35, 440);
		g.drawString("Q to enabled Dijkstra's overlay.", 35, 460);

		survivor.render(g);
		zombie.render(g);
		food.render(g);
		water.render(g);
		DOOR.render(g);
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		Input input = container.getInput();
		ZombieGame zg = (ZombieGame)game;
		
		if(starting) {
			survivor.setX(survivor.getX() + delta * 0.06f);	
			zombie.setX(zombie.getX() + delta * 0.07f);	
		}
		
		if(input.isKeyDown(Input.KEY_SPACE))
			starting = true;
		
		if(survivor.collides(water) != null)
			water.acquire(Item.WATER);
		
		if(survivor.collides(food) != null)
			food.acquire(Item.FOOD);
		
		if(survivor.collides(DOOR) != null) {
			zg.getState(ZombieGame.PLAYINGSTATE).init(container, game);
			zg.enterState(ZombieGame.PLAYINGSTATE);
		}
		
	}

	
	@Override
	public int getID() {
		return ZombieGame.STARTUPSTATE;
	}

}
