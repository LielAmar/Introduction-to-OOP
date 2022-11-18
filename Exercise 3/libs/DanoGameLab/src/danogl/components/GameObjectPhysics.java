package danogl.components;

import danogl.collisions.Collider;
import danogl.util.Vector2;

/**
 * Encapsulates the physics-related attributes of a GameObject
 * @author Dan Nirel
 */
public class GameObjectPhysics {
    /**
     * When a GameObject with this mass collides with another GameObject,
     * and they are both set to not only collide but also to resolve the collision
     * themselves (using {@link #preventIntersectionsFromDirection(Vector2)},
     * this object's velocity will not be affected by the collision.
     */
    public static final float IMMOVABLE_MASS = Float.MAX_VALUE;

    private Collider collider;
    private Vector2 dirToPrevent = null;
    private float mass = 1f;

    /**
     * Returns the object's collider
     */
    public Collider collider() {
        return collider;
    }

    /**
     * Sets the GameObject's collider
     */
    public void setCollider(Collider collider) {
        this.collider = collider;
    }

    /**
     * Returns the direction from which intersections are resolved by the engine.
     * (1,0) for example means that objects coming from the right will not intersect
     * with this object, provided the objects are set to collide in the first place,
     * and that the other object is also set to prevent intersections.
     */
    public Vector2 directionFromWhichIntersectionsArePrevented() {
        return dirToPrevent;
    }

    /**
     * Sets the direction from which objects are prevented from intersecting.
     * @see #directionFromWhichIntersectionsArePrevented()
     */
    public void preventIntersectionsFromDirection(Vector2 dirToPrevent) {
        this.dirToPrevent = dirToPrevent == null?null:dirToPrevent.getImmutableCopy();
    }

    /**
     * Returns the GameObject's mass.
     * The mass is relevant for collision resolution (see {@link #directionFromWhichIntersectionsArePrevented()}
     */
    public float mass() {
        return mass;
    }

    /**
     * Sets the GameObject's mass.
     * The mass is relevant for collision resolution (see {@link #directionFromWhichIntersectionsArePrevented()}
     */
    public void setMass(float mass) {
        this.mass = mass;
    }
}
