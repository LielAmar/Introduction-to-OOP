package src.gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

import java.awt.event.KeyEvent;

public class FinitePaddle extends Paddle {

    private final Counter paddleLifeCounter;

    public FinitePaddle(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                       UserInputListener inputListener, Vector2 windowDimensions, int minDistFromEdge,
                       Counter paddleLifeCounter) {
        super(topLeftCorner, dimensions, renderable, inputListener, windowDimensions, minDistFromEdge);

        this.paddleLifeCounter = paddleLifeCounter;
    }

    // TODO: make the same with HEART class in the future (only collide with paddle [but not extra paddle])
    @Override
    public boolean shouldCollideWith(GameObject other) {
        return super.shouldCollideWith(other) && other instanceof Ball;
    }

    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);

        this.paddleLifeCounter.decrement();
    }
}
