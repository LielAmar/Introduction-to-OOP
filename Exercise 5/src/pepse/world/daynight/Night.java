package pepse.world.daynight;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.util.Constants;

import java.awt.*;

/**
 * A class that handles the creation of night objects
 */
public class Night {

    private static final String TAG = "night";

    /**
     * Creates a night object
     *
     * @param gameObjects      List of game objects to add the night object to
     * @param layer            Layer of the night object
     * @param windowDimensions Window dimensions for the night object size
     * @param cycleLength      The length of a full night cycle
     * @return Created night game object
     */
    public static GameObject create(GameObjectCollection gameObjects, int layer,
                                    Vector2 windowDimensions, float cycleLength) {
        RectangleRenderable nightRender = new RectangleRenderable(Color.BLACK);

        GameObject night = new GameObject(Vector2.ZERO, windowDimensions, nightRender);

        night.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        night.setTag(TAG);

        gameObjects.addGameObject(night, layer);

        new Transition<>(
                night,
                night.renderer()::setOpaqueness,
                Constants.MIDDAY_NIGHT_OPACITY,
                Constants.MIDNIGHT_NIGHT_OPACITY,
                Transition.CUBIC_INTERPOLATOR_FLOAT,
                cycleLength / 2,
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH,
                null);

        return night;
    }
}
