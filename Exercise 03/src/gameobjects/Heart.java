package gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

/**
 * A class that represents a heart object in the bricker game.
 * When it collides with a paddle (not finitePaddle), it increments the lifeCounter by 1.
 */
public class Heart extends GameObject {

    private final Counter lifeCounter;

    private boolean beenUsed;

    public Heart(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable) {
        this(topLeftCorner, dimensions, renderable, Vector2.ZERO, null);
    }

    public Heart(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                 Vector2 velocity, Counter lifeCounter) {
        super(topLeftCorner, dimensions, renderable);

        super.setVelocity(velocity);

        this.lifeCounter = lifeCounter;

        this.beenUsed = false;
    }

    /**
     * Specifies whether the heart object should collide with the given object
     *
     * @param other   The other GameObject
     * @return        Whether the 2 objects should collide
     */
    @Override
    public boolean shouldCollideWith(GameObject other) {
        return super.shouldCollideWith(other) && other instanceof Paddle
                && !(other instanceof FinitePaddle);
    }

    /**
     * Handles the collision event of the heart with a different object
     *
     * @param other       The GameObject with which a collision occurred
     * @param collision   Information regarding this collision
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);

        if(this.lifeCounter == null) {
            return;
        }

        this.lifeCounter.increment();
        this.beenUsed = true;
    }

    /**
     * Specifies whether this heart object has been used to increment health.
     * With this field, we can specify that this heart object is no longer needed.
     *
     * @return   Whether the heart object has been used
     */
    public boolean hasBeenUsed() {
        return this.beenUsed;
    }
}
