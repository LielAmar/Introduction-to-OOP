package pepse.world;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;

import java.awt.*;

public class Sky {

    private static final Color BASIC_SKY_COLOR = Color.decode("#80C6E5");

    private static final String TAG = "sky";

    /**
     * Creates a sky object
     *
     * @param gameObjects      List of game objects to add the night object to
     * @param windowDimensions Window dimensions for the night object size
     * @param skyLayer         The Layer of a sky
     * @return Created sky game object
     */
    public static GameObject create(GameObjectCollection gameObjects,
                                    Vector2 windowDimensions, int skyLayer) {
        GameObject sky = new GameObject(Vector2.ZERO,
                windowDimensions, new RectangleRenderable(BASIC_SKY_COLOR));

        sky.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        sky.setTag(TAG);

        gameObjects.addGameObject(sky, skyLayer);

        return sky;
    }
}
