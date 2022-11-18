package danogl.collisions;

import danogl.GameObject;
import danogl.collisions.AABB.AABBPairCollisionHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * package-private. Manages all PairCollisionHandlers (implementing PairCollisionHandler).
 * Given a pair of GameObjects, this manager is responsible for finding the correct
 * CollisionHandler for their colliders. It also allows registering such handlers.
 * Users might be interested in {@link GameObjectCollection#registerPairCollisionHandler(PairCollisionHandler)}
 * if they wish to support new kinds of colliders, but are discouraged from trying that approach.
 * @author Dan Nirel
 */
class PairCollisionManager {
    private List<PairCollisionHandler> handlers = new ArrayList<>();

    public PairCollisionManager() {
        registerSinglePairCollisionHandler(
                new AABBPairCollisionHandler());
    }

    public void registerSinglePairCollisionHandler(PairCollisionHandler handler) {
        if(!handlers.contains(handler))
            handlers.add(handler);
    }

    public PairCollisionData getCollisionData(GameObject go1, GameObject go2) {
        for(var handler : handlers) {
            var collisionData = handler.computeCollisionData(go1, go2);
            if(collisionData != null)
                return collisionData;
        }
        return null;
    }
}
