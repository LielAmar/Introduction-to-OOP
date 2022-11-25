package src.brick_strategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.util.Counter;

/**
 * This class handles the strategy of ball-brick collision
 */
public class CollisionStrategy {

    protected final GameObjectCollection gameObjects;

    public CollisionStrategy(GameObjectCollection gameObjects) {
        this.gameObjects = gameObjects;
    }

    /**
     * Handles a collision between the ball and a specific brick.
     * It removes the brick from the list of game objects, and only if removal was successful,
     * it decrements bricksCounter
     *
     * @param collidedObject   Brick that collided
     * @param colliderObject   Ball
     * @param bricksCounter    Counter of bricks left
     */
    public void onCollision(GameObject collidedObject, GameObject colliderObject, Counter bricksCounter) {
        if(this.gameObjects.removeGameObject(collidedObject)) {
            bricksCounter.decrement();
            System.out.println("[DEBUG] decremented bricks counter to " + bricksCounter.value());
        }
    }
}
