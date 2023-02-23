package src.brick_strategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.Sound;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import src.gameobjects.Heart;
import src.gameobjects.Puck;

/**
 * This class handles the strategy that adds an extra life
 */
public class ExtraLifeStrategy extends CollisionStrategy {

    private static final Vector2 INITIAL_HEART_VELOCITY = new Vector2(0, 100);

    private final Renderable renderable;
    private final Vector2 objectSize;
    private final Counter lifeCounter;

    public ExtraLifeStrategy(GameObjectCollection gameObjects, Renderable renderable,
                             Vector2 objectSize, Counter lifeCounter) {
        super(gameObjects);

        this.renderable = renderable;
        this.objectSize = objectSize;
        this.lifeCounter = lifeCounter;
    }

    /**
     * Handles the collision with a brick that has a extra life strategy
     *
     * @param collidedObject   Brick that collided
     * @param colliderObject   Ball
     * @param bricksCounter    Counter of bricks left
     */
    @Override
    public void onCollision(GameObject collidedObject, GameObject colliderObject, Counter bricksCounter) {
        super.onCollision(collidedObject, colliderObject, bricksCounter);

        GameObject heart = new Heart(collidedObject.getTopLeftCorner(),
                this.objectSize,
                this.renderable,
                INITIAL_HEART_VELOCITY,
                lifeCounter);

        heart.setCenter(collidedObject.getCenter());

        super.gameObjects.addGameObject(heart);
    }
}
