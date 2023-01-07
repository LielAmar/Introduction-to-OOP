package pepse.world.trees;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.components.ScheduledTask;
import danogl.components.Transition;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.util.Constants;
import pepse.world.Block;

import java.awt.*;
import java.util.Random;

/**
 * A class that represents a single leaf game object
 */
public class Leaf extends Block {

    private static final Random random = new Random();
    private static final Color LEAF_COLOR = new Color(50, 150, 50);

    private static final String TAG = "leaf";

    private static final float LEAF_MASS = 1.0f;

    private final Vector2 initialPosition;

    private Transition<Float> fallingTransition;

    public Leaf(Vector2 topLeftCorner) {
        super(topLeftCorner, new RectangleRenderable(ColorSupplier.approximateColor(LEAF_COLOR)));

        // Setting the leaf mass, so it doesn't push "heavier" objects
        this.physics().setMass(LEAF_MASS);

        this.setTag(TAG);

        this.initialPosition = topLeftCorner;

        // Starting the leaf animation transition at a random time so leaves are out of sync
        float randomTime = (((float) random.nextInt(10) + 1) / 10);
        new ScheduledTask(this, randomTime, false, this::initLeafTransitions);

        // Starting the leaf falling animation after a random time
        randomTime = random.nextInt(3);
        new ScheduledTask(this, randomTime, false, this::startLeafAnimation);
    }


    /**
     * Resetting the leaf velocity upon collision with a different object.
     * Leaves should only collide with terrain (As seen in PepseGameManager)
     *
     * @param other     The GameObject with which a collision occurred.
     * @param collision Information regarding this collision.
     *                  A reasonable elastic behavior can be achieved with:
     *                  setVelocity(getVelocity().flipped(collision.getNormal()));
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);

        this.removeComponent(this.fallingTransition);
    }

    /**
     * If the y velocity is 0, meaning we're not falling down, we want to stop moving
     * in the x direction too.
     *
     * @param deltaTime The time elapsed, in seconds, since the last frame. Can
     *                  be used to determine a new position/velocity by multiplying
     *                  this delta with the velocity/acceleration respectively
     *                  and adding to the position/velocity:
     *                  velocity += deltaTime*acceleration
     *                  pos += deltaTime*velocity
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        if (this.transform().getVelocity().y() == 0) {
            this.transform().setVelocity(Vector2.ZERO);
        }
    }


    /**
     * Starts the leaf wind transition while on the tree.
     * These transitions are changing the angle and size of the leaf
     */
    private void initLeafTransitions() {
        new Transition<>(
                this,
                this.renderer()::setRenderableAngle,
                Constants.LEAF_ANGLE_TRANSITION_STARTING_VALUE,
                Constants.LEAF_ANGLE_TRANSITION_ENDING_VALUE,
                Transition.CUBIC_INTERPOLATOR_FLOAT,
                Constants.LEAF_TRANSITIONS_CYCLE_LENGTH,
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH,
                null);

        new Transition<>(
                this,
                (factor) -> this.setDimensions(new Vector2(Block.SIZE, Block.SIZE).mult(factor)),
                Constants.LEAF_SIZE_TRANSITION_STARTING_VALUE,
                Constants.LEAF_SIZE_TRANSITION_ENDING_VALUE,
                Transition.CUBIC_INTERPOLATOR_FLOAT,
                Constants.LEAF_TRANSITIONS_CYCLE_LENGTH,
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH,
                null);
    }


    /**
     * Starts the leaf animation.
     * It repositions the leaf and sets all the initial values
     * Then randomly selects a time (10-20) when the fall would start falling and fading
     * and calls #startFade
     */
    private void startLeafAnimation() {
        // Re-positioning the leaf
        this.setTopLeftCorner(this.initialPosition);
        this.renderer().fadeIn(Constants.LEAF_FADE_LENGTH);
        this.setVelocity(Vector2.ZERO);

        // Start the falling & fading animation after a random time
        int randomLifeTime = random.nextInt(11) + 10;
        new ScheduledTask(this, randomLifeTime, false, this::startFade);
    }

    /**
     * Starts the leaf falling animation.
     * It adds a wind transition, then randomly selects a time (2-10), when the leaf
     * would return to its origin position by calling #startLeafAnimation
     */
    private void startFade() {
        this.fallingTransition = new Transition<>(
                this,
                this.transform()::setVelocityX,
                -1 * Constants.LEAF_X_VELOCITY,
                Constants.LEAF_X_VELOCITY,
                Transition.CUBIC_INTERPOLATOR_FLOAT,
                1f,
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH,
                null);

        this.transform().setVelocityY(Constants.LEAF_Y_VELOCITY);

        int randomDeathTime = random.nextInt(9) + 2;

        this.renderer().fadeOut(Constants.LEAF_FADE_LENGTH, () ->
                new ScheduledTask(this,
                        randomDeathTime, false, this::startLeafAnimation));
    }
}
