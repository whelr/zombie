package zombie;

import jig.Entity;
import jig.ResourceManager;

public class Item extends Entity {

	public static final int WATER = 0;
	public static final int FOOD = 1;
	public static final int ROPE = 2;
	
	public Item(final float x, final float y, final int type) {
		super(x, y);
		if(type == WATER) {
			addImageWithBoundingBox(ResourceManager
					.getImage(ZombieGame.WATERIMG_RSC));
		}
		if(type == FOOD) {
			addImageWithBoundingBox(ResourceManager
					.getImage(ZombieGame.FOODIMG_RSC));
		}
		if(type == ROPE) {
			addImageWithBoundingBox(ResourceManager
					.getImage(ZombieGame.ROPEIMG_RSC));
		}
	}
	
	public void acquire(int type) {
		if(type == WATER) {
			removeImage(ResourceManager
					.getImage(ZombieGame.WATERIMG_RSC));
		}
		if(type == FOOD) {
			removeImage(ResourceManager
					.getImage(ZombieGame.FOODIMG_RSC));
		}
		if(type == ROPE) {
			removeImage(ResourceManager
					.getImage(ZombieGame.ROPEIMG_RSC));
		}
	}
	
}
