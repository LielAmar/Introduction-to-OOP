package danogl.collisions;

import danogl.util.MutableVector2;
import danogl.util.Vector2;

import java.util.function.BooleanSupplier;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

/**
 * Stores information regarding a given collision between two GameObjects
 * @author Dan Nirel
 */
public interface Collision {
    /**
     * The normal of the collision. Points towards the object receiving this object.
     */
    Vector2 getNormal();

    /**
     * An approximation of the point of collision.
     */
    Vector2 getCollisionPoint();

    /**
     * The area of penetration (width and height).
     */
    Vector2 getPenetrationArea();

    /**
     * The velocity of the object receiving this object, relative to the other object.
     */
    Vector2 getRelativeVelocity();

    /**
     * Notify the object a frame started. May help with caching decisions.
     */
    void onFrameStart();
}
