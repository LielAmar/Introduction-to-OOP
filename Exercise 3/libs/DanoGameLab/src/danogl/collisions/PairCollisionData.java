package danogl.collisions;

import danogl.GameObject;

/**
 * An abstract class that represents the computing and caching of data regarding the collision of
 * two objects. Concrete subclasses will correspond to a certain combination of
 * specific colliders.
 * @author Dan Nirel
 */
public abstract class PairCollisionData extends GameObjectPair {
    protected Collision collision1, collision2;

    /**
     * Constructor. Who knew.
     */
    public PairCollisionData(GameObject go1, GameObject go2) {
        setGameObject(0, go1);
        setGameObject(1, go2);
    }

    /**
     * Returns the collision object, 0 for the first object, 1 for second.
     */
    public Collision getCollision(int index) {
        if(index == 0)
            return collision1;
        if(index == 1)
            return collision2;
        throw new IndexOutOfBoundsException();
    }

    /**
     * Notify the object that a new frame has begun. This may help
     * with caching decisions.
     */
    public void onFrameStart() { }

    /**
     * Are the two objects currently intersecting
     */
    public abstract boolean areColliding();
}
