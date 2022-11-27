package src.brick_strategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.Sound;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import src.gameobjects.Puck;

/**
 * This class handles the strategy that adds extra pucks
 */
public class ExtraPucksStrategy extends CollisionStrategy {

    private final Renderable renderable;
    private final Sound sound;

    public ExtraPucksStrategy(GameObjectCollection gameObjects, Renderable renderable, Sound sound) {
        super(gameObjects);

        this.renderable = renderable;
        this.sound = sound;
    }

    /**
     * Handles the collision with a brick that has an extra pucks strategy
     *
     * @param collidedObject   Brick that collided
     * @param colliderObject   Ball
     * @param bricksCounter    Counter of bricks left
     */
    @Override
    public void onCollision(GameObject collidedObject, GameObject colliderObject, Counter bricksCounter) {
        super.onCollision(collidedObject, colliderObject, bricksCounter);

        // Calculating the size of a puck depending on the size of the
        float puckDiameter = collidedObject.getDimensions().x() / 3;

        GameObject puck = new Puck(
                collidedObject.getTopLeftCorner(),
                new Vector2(puckDiameter, puckDiameter),
                renderable,
                sound);

        puck.setCenter(new Vector2(
                collidedObject.getTopLeftCorner().x() + (1 * puckDiameter) + (puckDiameter / 2),
                collidedObject.getCenter().y()
        ));

        super.gameObjects.addGameObject(puck);
    }
}
