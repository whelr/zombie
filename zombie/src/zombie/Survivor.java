package zombie;

import jig.Entity;
import jig.ResourceManager;
import jig.Vector;

public class Survivor extends Entity {
	
	public static final int STILL = 0;
	public static final int UP = 1;
	public static final int LEFT = 2;
	public static final int RIGHT = 3;
	public static final int DOWN = 4;
	
	private int direction;
	private Vector velocity;

	
	public Survivor(final float x, final float y, final float vx, final float vy) {
		super(x, y);
		addImageWithBoundingBox(ResourceManager.getImage(ZombieGame.SURVIVORIMG_RSC));
		direction = STILL;
		velocity = new Vector(vx, vy);
	}
	
	public int getDirection() {
		return direction;
	}
	
	public void setDirection(int dir) {
		if(dir == STILL) {
			velocity = new Vector(0.0f, 0.0f);	
		}
		if(dir == UP) {
			velocity = new Vector(0.0f, -0.05f);	
		}
		if(dir == RIGHT) {
			velocity = new Vector(0.05f, 0.f);	
		}
		if(dir == LEFT) {
			velocity = new Vector(-0.05f, 0.f);	
		}
		if(dir == DOWN) {
			velocity = new Vector(0.0f, 0.05f);	
		}
		direction = dir;
	}
	
	public void setVelocity(final Vector v) {
		velocity = v;
	}
	
	public Vector getVelocity() {
		return velocity;
	}
	
	public void update(final int delta) {
		translate(velocity.scale(delta));
	}
	
}
