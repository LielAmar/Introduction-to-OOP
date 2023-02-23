package pepse.world.daynight;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.OvalRenderable;
import danogl.util.Vector2;
import pepse.util.Constants;

import java.awt.*;

/**
 * A class that handles the creation of sun objects
 */
public class Sun {

    private static final String TAG = "SUN";

    /**
     * Creates a sun object
     *
     * @param gameObjects      List of game objects to add the sun object to
     * @param layer            Layer of the sun object
     * @param windowDimensions Window dimensions for the sun object size
     * @param cycleLength      The length of a full day cycle
     * @return Created sun game object
     */
    public static GameObject create(GameObjectCollection gameObjects, int layer,
                                    Vector2 windowDimensions, float cycleLength) {
        float screenMidX = (windowDimensions.x() / 2);
        float screenMidY = (windowDimensions.y() / 2);

        float sunX = screenMidX - (Constants.SUN_RADIUS / 2);
        float sunY = screenMidY - (Constants.SUN_RADIUS / 2);

        OvalRenderable renderer = new OvalRenderable(Color.YELLOW);

        GameObject sun = new GameObject(new Vector2(sunX, sunY),
                Vector2.ONES.mult(Constants.SUN_RADIUS), renderer);

        sun.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        sun.setTag(TAG);

        gameObjects.addGameObject(sun, layer);

        new Transition<>(
                sun,
                (degree) -> sun.setCenter(new Vector2(
                        (float) Math.cos(-degree) * Constants.SUN_ORBIT_RADIUS *
                                Constants.SUN_ORBIT_ELLIPSE_A + screenMidX,
                        (float) Math.sin(-degree) * Constants.SUN_ORBIT_RADIUS *
                                Constants.SUN_ORBIT_ELLIPSE_B + screenMidY
                )),
                Constants.MIDDAY_SUN_POSITION,
                Constants.MIDNIGHT_SUN_POSITION,
                Transition.LINEAR_INTERPOLATOR_FLOAT,
                cycleLength,
                Transition.TransitionType.TRANSITION_LOOP,
                null);

        return sun;
    }
}
