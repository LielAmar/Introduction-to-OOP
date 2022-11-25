package src.gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

public class Heart extends GameObject {

    private final Counter lifeCounter;

    public Heart(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable) {
        this(topLeftCorner, dimensions, renderable, Vector2.ZERO, null);
    }

    public Heart(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                 Vector2 velocity, Counter lifeCounter) {
        super(topLeftCorner, dimensions, renderable);

        super.setVelocity(velocity);

        this.lifeCounter = lifeCounter;
    }

    @Override
    public boolean shouldCollideWith(GameObject other) {
        return super.shouldCollideWith(other) && other instanceof Paddle
                && !(other instanceof FinitePaddle);
    }

    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);

        if(this.lifeCounter == null) {
            return;
        }

        // TODO: find a way to only increment if not reached max health
        this.lifeCounter.increment();
    }
}
