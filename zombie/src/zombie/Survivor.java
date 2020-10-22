package zombie;

import jig.Entity;
import jig.ResourceManager;

public class Survivor extends Entity {
	
	public static final int STILL = 0;
	public static final int UP = 1;
	public static final int LEFT = 2;
	public static final int RIGHT = 3;
	public static final int DOWN = 4;
	
	private int direction;
	private int desiredDirection;
	
	private int tileTargetX;
	private int tileTargetY;

	
	public Survivor(final float x, final float y, final float vx, final float vy) {
		super(x, y);
		addImageWithBoundingBox(ResourceManager.getImage(ZombieGame.SURVIVORIMG_RSC));
		direction = STILL;
		desiredDirection = STILL;
	}
	
	public int getDirection() {
		return direction;
	}
	
	public void setDirection(int dir) {
		direction = dir;
	}
	
	public int getDesiredDirection() {
		return desiredDirection;
	}
	
	public void setDesiredDirection(int ddir) {
		desiredDirection = ddir;
	}
	
	public void setTileTarget(int tx, int ty) {
		tileTargetX = tx;
		tileTargetY = ty;
	}

	
	
	public void update(final int delta) {
		if(direction == DOWN) {
			if(this.getY() + delta * 0.05f >= tileTargetY * 32 + 60) {
				this.setY(tileTargetY * 32 + 60);
				this.setDirection(STILL);
			} else {
				this.setY(this.getY() + delta * 0.05f);	
			}
		}
		if(direction == UP) {
			if(this.getY() + delta * -0.05f <= tileTargetY * 32 + 60) {
				this.setY(tileTargetY * 32 + 60);
				this.setDirection(STILL);
			} else {
				this.setY(this.getY() + delta * -0.05f);	
			}
		}
		if(direction == RIGHT) {
			if(this.getX() + delta * 0.05f >= tileTargetX * 32 + 60) {
				this.setX(tileTargetX * 32 + 60);
				this.setDirection(STILL);
			} else {
				this.setX(this.getX() + delta * 0.05f);	
			}
		}
		if(direction == LEFT) {
			if(this.getX() + delta * -0.05f <= tileTargetX * 32 + 60) {
				this.setX(tileTargetX * 32 + 60);
				this.setDirection(STILL);
			} else {
				this.setX(this.getX() + delta * -0.05f);	
			}
		}
	}
	
}
