package danogl.collisions;

import danogl.GameObject;
import danogl.collisions.PairCollisionData;

/**
 * Responsible for the collision detection (and not resolution!) of two GameObjects.
 * Implementing concrete classes will correspond to GameObjects with SPECIFIC COLLIDERS.
 * Multiple classes implementing this interface are supposed to cover all supported combinations
 * of colliders.
 * @author Dan Nirel
 */
public interface PairCollisionHandler {
    /**
     * Assuming this CollisionHandler supports the given pair of GameObjects,
     * the method computes the relevant data regarding their collision.
     */
    PairCollisionData computeCollisionData(GameObject go1, GameObject go2);
    /**
     * Is this CollisionHandler responsible for detecting these two GameObjects
     */
    boolean supportsPair(GameObject go1, GameObject go2);
}
