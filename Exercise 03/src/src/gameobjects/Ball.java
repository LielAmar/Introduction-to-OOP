package src.gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.Sound;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import src.utils.TrackedGameObject;

/**
 * A class that represents the ball game object and handles its logic
 */
public class Ball extends TrackedGameObject {

    private final Sound collisionSound;

    public Ball(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable, Sound collisionSound) {
        super(topLeftCorner, dimensions, renderable);

        this.collisionSound = collisionSound;
    }

    /**
     * Handles ball collision with other objects
     *
     * @param collider    Collider object
     * @param collision   Collision information
     */
    @Override
    public void onCollisionEnter(GameObject collider, Collision collision) {
        super.onCollisionEnter(collider, collision); // Unnecessary call to super method... >.<

        this.collisionSound.play();
        super.setTrackerValue(super.getTrackerValue() + 1);

        Vector2 direction = this.getVelocity().flipped(collision.getNormal());
        super.setVelocity(direction);
    }
}
