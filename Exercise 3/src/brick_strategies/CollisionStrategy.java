package brick_strategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.util.Counter;

public class CollisionStrategy {

    private final GameObjectCollection gameObjects;

    public CollisionStrategy(GameObjectCollection gameObjects) {
        this.gameObjects = gameObjects;
    }

    public void onCollision(GameObject collidedObject, GameObject colliderObject, Counter bricksCounter) {
        this.gameObjects.removeGameObject(collidedObject);

        bricksCounter.decrement();
    }
}
