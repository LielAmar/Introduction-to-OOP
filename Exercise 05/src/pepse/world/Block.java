package pepse.world;

import danogl.GameObject;
import danogl.components.GameObjectPhysics;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.util.Constants;

/**
 * A class that represents a single block game object
 */
public class Block extends GameObject {

    public static final int SIZE = Constants.BLOCK_SIZE;

    private static final String TAG = "block";

    public Block(Vector2 topLeftCorner, Renderable renderable) {
        super(topLeftCorner, Vector2.ONES.mult(SIZE), renderable);

        this.physics().preventIntersectionsFromDirection(Vector2.ZERO);
        this.physics().setMass(GameObjectPhysics.IMMOVABLE_MASS);

        this.setTag(TAG);
    }
}
