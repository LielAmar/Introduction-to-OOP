package pepse.world.daynight;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.OvalRenderable;

import java.awt.*;

/**
 * A class that handles the creation of sun halo objects
 */
public class SunHalo {

    private final static float HALO_DIMENSIONS_MULTIPLIER = 1.5f;

    private static final String TAG = "sunHalo";

    /**
     * Creates a sun halo object
     *
     * @param gameObjects List of game objects to add the sun halo object to
     * @param layer       Layer of the sun halo object
     * @param sun         An object to follow
     * @param color       The color of the halo
     * @return Created sun halo game object
     */
    public static GameObject create(GameObjectCollection gameObjects, int layer,
                                    GameObject sun, Color color) {
        OvalRenderable sunHaloRender = new OvalRenderable(color);

        GameObject sunHalo = new GameObject(sun.getTopLeftCorner(),
                sun.getDimensions().mult(HALO_DIMENSIONS_MULTIPLIER), sunHaloRender);

        sunHalo.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        sunHalo.setTag(TAG);

        sunHalo.addComponent((deltaTime -> sunHalo.setCenter(sun.getCenter())));

        gameObjects.addGameObject(sunHalo, layer);

        return sunHalo;
    }
}
