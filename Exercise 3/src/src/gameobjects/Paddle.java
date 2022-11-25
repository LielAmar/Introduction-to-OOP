package src.gameobjects;

import danogl.GameObject;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.event.KeyEvent;

/**
 * A class that represents the paddle game object and handles its logic
 */
public class Paddle extends GameObject {

    public static final int MOVE_SPEED = 500;

    private final UserInputListener inputListener;
    private final Vector2 windowDimensions;
    private final int minDistFromEdge;

    public Paddle(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                  UserInputListener inputListener, Vector2 windowDimensions, int minDistFromEdge) {
        super(topLeftCorner, dimensions, renderable);

        this.inputListener = inputListener;
        this.windowDimensions = windowDimensions;
        this.minDistFromEdge = minDistFromEdge;
    }

    /**
     * Handles user input and updates the game every frame
     *
     * @param deltaTime   game update delta time
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        Vector2 direction = Vector2.ZERO;

        if(this.inputListener.isKeyPressed(KeyEvent.VK_LEFT)) {
            // We can only go left if we're less than ${minDistFromEdge} pixels away from the left edge
            if((this.getCenter().x() - super.getDimensions().x()/2) - this.minDistFromEdge > 0) {
                direction = direction.add(Vector2.LEFT);
            }
        }

        if(this.inputListener.isKeyPressed(KeyEvent.VK_RIGHT)) {
            // We can only go right if we're less than ${minDistFromEdge} pixels away from the right edge
            if((this.getCenter().x() + super.getDimensions().x()/2) +
                    this.minDistFromEdge < this.windowDimensions.x()) {
                direction = direction.add(Vector2.RIGHT);
            }
        }

        super.setVelocity(direction.mult(MOVE_SPEED));
    }
}
