package pepse.world.entity;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.ImageReader;
import danogl.util.Vector2;

import pepse.util.Constants;
import pepse.world.Block;
import pepse.util.properties.NumericProperty;
import pepse.world.trees.Log;

import java.util.Random;

/**
 * A class that represents the abstraction implementation of an entity within the game
 */
public abstract class Entity extends GameObject implements IDamagable {

    protected final Random random;

    protected final NumericProperty healthProperty;

    public Entity(Vector2 topLeftCorner, Vector2 dimensions, ImageReader imageReader) {
        super(topLeftCorner, dimensions, null);

        this.random = new Random();

        this.healthProperty = new NumericProperty(
                Constants.ENTITY_HEALTH_FACTOR,
                Constants.ENTITY_MAX_HEALTH,
                Constants.ENTITY_MIN_HEALTH,
                Constants.ENTITY_MAX_HEALTH);

        this.physics().preventIntersectionsFromDirection(Vector2.ZERO);
        this.physics().setMass(Constants.ENTITY_MASS);
        this.transform().setAccelerationY(Constants.ENTITY_GRAVITY);

        this.initAnimations(imageReader);
    }


    /**
     * Updates the entity, specifically its animations, based on the implementation of #updateAnimations
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

        this.updateAnimations();
    }


    /**
     * Handles the entity collision enter. We want to reset its velocity, so it doesn't get out of bounds
     *
     * @param other     The GameObject with which a collision occurred.
     * @param collision Information regarding this collision.
     *                  A reasonable elastic behavior can be achieved with:
     *                  setVelocity(getVelocity().flipped(collision.getNormal()));
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);

        if (other instanceof Block && !(other instanceof Log)) {
            this.setVelocity(this.getVelocity().multY(0));
        }
    }


    /**
     * Damages the entity by the given factor
     *
     * @param factor   damage to deal
     */
    @Override
    public void damage(double factor) {
        this.healthProperty.decrease(factor);
    }

    /**
     * Heals the entity by the given factor
     *
     * @param factor   amount to heal
     */
    @Override
    public void heal(double factor) {
        this.healthProperty.increase(factor);
    }

    /**
     * Kills the entity
     */
    @Override
    public void kill() {
        this.healthProperty.setValue(this.healthProperty.getMinValue());
    }

    /**
     * Returns whether the entity is dead
     *
     * @return   Whether entity is dead
     */
    @Override
    public boolean isDead() {
        return this.healthProperty.getValue() == this.healthProperty.getMinValue();
    }

    /**
     * Returns the entity's health property
     *
     * @return   Entity's health property
     */
    @Override
    public NumericProperty getHealthProperty() {
        return this.healthProperty;
    }


    /**
     * Handles the entity animation
     */
    protected abstract void updateAnimations();

    /**
     * Initializes all animations
     *
     * @param imageReader Image reader
     */
    protected abstract void initAnimations(ImageReader imageReader);
}
