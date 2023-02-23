package gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.Sound;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.util.Random;

/**
 * A class that represents a puck - a ball with an initial velocity of a diagonal direction
 */
public class Puck extends Ball {

    private final static int PUCK_INITIAL_VELOCITY = 200;

    public Puck(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable, Sound collisionSound) {
        super(topLeftCorner, dimensions, renderable, collisionSound);

        // We want to create a diagonal velocity, meaning we want the x and y value of the velocity
        // to be the same, with possibly different signs.
        Random random = new Random();

        boolean xSign = random.nextBoolean();
        boolean ySign = true; /* random.nextBoolean();*/

        super.setVelocity(new Vector2(
                xSign ? PUCK_INITIAL_VELOCITY : (-1) * PUCK_INITIAL_VELOCITY,
                ySign ? PUCK_INITIAL_VELOCITY : (-1) * PUCK_INITIAL_VELOCITY
        ));
    }

    /**
     * Handles the collision event with a puck
     *
     * @param collider    Collider object
     * @param collision   Collision information
     */
    @Override
    public void onCollisionEnter(GameObject collider, Collision collision) {
        super.onCollisionEnter(collider, collision);
    }
}
