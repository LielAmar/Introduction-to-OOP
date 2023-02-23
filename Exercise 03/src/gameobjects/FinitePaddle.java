package gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

import java.awt.event.KeyEvent;

/**
 * A class that represents a paddle with a finite amount of uses
 */
public class FinitePaddle extends Paddle {

    private final Counter paddleLifeCounter;

    public FinitePaddle(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                       UserInputListener inputListener, Vector2 windowDimensions, int minDistFromEdge,
                       Counter paddleLifeCounter) {
        super(topLeftCorner, dimensions, renderable, inputListener, windowDimensions, minDistFromEdge);

        this.paddleLifeCounter = paddleLifeCounter;
    }

    /**
     * Specifies all objects that should collide with a finite paddle
     *
     * @param other   The other GameObject
     * @return        Whether a collision should occur
     */
    @Override
    public boolean shouldCollideWith(GameObject other) {
        return super.shouldCollideWith(other) && other instanceof Ball;
    }

    /**
     * Handles the event of a collision with the extra paddle
     *
     * @param other       The GameObject with which a collision occurred
     * @param collision   Information regarding this collision.
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);

        this.paddleLifeCounter.decrement();
    }
}
