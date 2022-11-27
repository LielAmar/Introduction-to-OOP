package src.brick_strategies;

import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.WindowController;
import danogl.gui.rendering.Camera;
import danogl.util.Counter;
import danogl.util.Vector2;
import src.gameobjects.Ball;
import src.utils.TrackedGameObject;

/**
 * This class handles the strategy that changes the camera to focus
 * on the ball
 */
public class CameraChangeStrategy extends CollisionStrategy {

    private final GameManager gameManager;
    private final TrackedGameObject targetObject;
    private final WindowController windowController;

    public CameraChangeStrategy(GameObjectCollection gameObjects, GameManager gameManager,
                                TrackedGameObject targetObject, WindowController windowController) {
        super(gameObjects);

        this.gameManager = gameManager;
        this.targetObject = targetObject;
        this.windowController = windowController;
    }

    /**
     * Handles the collision with a brick that has a camera change strategy
     *
     * @param collidedObject   Brick that collided
     * @param colliderObject   Ball
     * @param bricksCounter    Counter of bricks left
     */
    @Override
    public void onCollision(GameObject collidedObject, GameObject colliderObject, Counter bricksCounter) {
        super.onCollision(collidedObject, colliderObject, bricksCounter);

        // Only balls should activate the camera strategy
        if(!(colliderObject instanceof Ball)) {
            return;
        }

        // If we already have an active camera we don't want to do anything
        if(this.gameManager.getCamera() != null) {
            return;
        }

        // Setting a new camera to follow the target object and resetting the hit counter
        this.gameManager.setCamera(new Camera(
                        this.targetObject,
                        Vector2.ZERO,
                        this.windowController.getWindowDimensions().mult(1.2f),
                        this.windowController.getWindowDimensions()));

        this.targetObject.setTrackerValue(0);
    }
}
