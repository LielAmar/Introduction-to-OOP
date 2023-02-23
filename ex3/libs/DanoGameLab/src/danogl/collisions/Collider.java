package danogl.collisions;

import danogl.util.Vector2;

/**
 * Represents a general shape which specific shapes of GameObjects implement.
 * All Computations at the collider level are in object-coordinates (not world coordinates).
 * This should be used by the collisions engine; normal users should be more interested
 * in GameObject's API.
 * @author Dan Nirel
 */
public interface Collider {
    /**
     * A string representing the specific collider type.
     */
    String colliderType();

    /**
     * Returns whether the given point is included in the collider.
     * Note that this method takes object-coordinates, not world! To query
     * regarding a point in world-coordinates, see {@link danogl.GameObject#containsPoint(Vector2)}.
     */
    boolean containsPoint(Vector2 point);
}
