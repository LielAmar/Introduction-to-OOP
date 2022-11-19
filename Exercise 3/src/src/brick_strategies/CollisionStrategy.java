package src.brick_strategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.util.Counter;

/**
 * This class handles the strategy of ball-brick collision
 */
public class CollisionStrategy {

    private final GameObjectCollection gameObjects;

    public CollisionStrategy(GameObjectCollection gameObjects) {
        this.gameObjects = gameObjects;
    }

    /**
     * Handles a collision between the ball and a specific brick.
     * It removes the brick from the list of game objects
     *
     * @param collidedObject   Brick that collided
     * @param colliderObject   Ball
     * @param bricksCounter    Counter of bricks left
     */
    public void onCollision(GameObject collidedObject, GameObject colliderObject, Counter bricksCounter) {
        this.gameObjects.removeGameObject(collidedObject);

        bricksCounter.decrement();
    }
}
