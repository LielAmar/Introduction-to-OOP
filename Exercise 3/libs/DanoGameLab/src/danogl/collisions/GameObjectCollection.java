package danogl.collisions;

import danogl.GameObject;
import java.util.*;
import java.util.List;

/**
 * A container for accumulating/removing instances of GameObject and
 * for handling their collisions.
 * The items in this collection can be iterated using a foreach loop
 * (e.g., for(GameObject go : gameObjectCollection) {...} ).
 * @author Dan Nirel
 */
public class GameObjectCollection implements Iterable<GameObject> {
    protected CollisionResolver resolver = new CollisionResolver();
    protected LayerManager layerManager = new LayerManager(this::handlePair);
    private PairCollisionManager pairCollisionManager = new PairCollisionManager();
    private Map<GameObjectPair, PairCollisionData> alreadyInCollision
            = new HashMap<>();
    private GameObjectPair pair = new GameObjectPair();

    /**
     * Iterator for all the objects in all layers, in drawing order.
     * @see #objectsInLayer(int)
     */
    @Override
    public Iterator<GameObject> iterator() {
        return layerManager.iterator();
    }

    /**
     * Returns an Iterable of the GameObjects in this collection, in reverse order
     * of the drawing order. Includes objects of all layers.
     * @see #objectsInLayer(int)
     */
    public Iterable<GameObject> reverseOrder() { return layerManager.reverseOrder(); }

    /**
     * Add the specified GameObject.
     * Important note: the object will only be added at the end of
     * the nearest invocation of handleCollisions. Until such time,
     * traversal of the objects in this container using the foreach loop will not
     * show this object.
     * Also note that objects are rendered in the order they are added. Thus,
     * the last object added will always be in the foreground.
     * @param obj The GameObject to add.
     * @param layerId The id of the layer to which obj should be added.
     */
    public void addGameObject(GameObject obj, int layerId) {
        layerManager.addGameObject(obj, layerId);
    }

    /**
     * Add the specified obj to the default layer.
     * @see #addGameObject(GameObject, int)
     */
    public void addGameObject(GameObject obj) {
        addGameObject(obj, Layer.DEFAULT);
    }

    /**
     * Remove the specified GameObject.
     * Important note: the object will only be removed at the end of
     * the nearest invocation of handleCollisions. Until such time,
     * traversal of the objects in this container using the foreach loop will still
     * show this object.
     * @param obj The GameObject to remove.
     * @param layerId id of the layer to remove object from
     * @return false if the object does not exist in the collection, or it exists
     * but is already queued for removal, true if removal was successful.
     */
    public boolean removeGameObject(GameObject obj, int layerId) {
        boolean wasAnObjRemoved = layerManager.removeGameObject(obj, layerId);
        if(wasAnObjRemoved) {
            List<GameObjectPair> keysToRemove = new ArrayList<>();
            for(var pair : alreadyInCollision.keySet()) {
                if(pair.getGameObject(0) == obj
                        || pair.getGameObject(1) == obj)
                    keysToRemove.add(pair);
            }
            keysToRemove.forEach(alreadyInCollision::remove);
        }
        return wasAnObjRemoved;
    }

    /**
     * Removes an object from the default layer.
     * @see #removeGameObject(GameObject, int)
     */
    public boolean removeGameObject(GameObject obj) {
        return removeGameObject(obj, Layer.DEFAULT);
    }

    /**
     * Register another implementation of PairCollisionHandler, which creates
     * the collision data of two objects implementing {@link Collider}.
     * Thus the engine can be extended to support collisions of custom shapes.
     */
    public void registerPairCollisionHandler(PairCollisionHandler pairCollisionHandler) {
        if(pairCollisionHandler != null)
            pairCollisionManager.registerSinglePairCollisionHandler(pairCollisionHandler);
    }

    /**
     * Updates the internal structures of the collection. Does not update the objects themselves.
     * @param deltaTime Time since last frame
     */
    public void update(float deltaTime) {
        layerManager.flushChanges();
    }

    /**
     * Looks for collisions between the accumulated GameObjects.
     * When two objects enter a new collision, their shouldCollideWith method is invoked.
     * If both objects return true, a collision occurs:
     * On the first frame of collision, their onCollisionEnter method is invoked.
     * For every frame they stay in collision, including the first frame,
     * their onCollisionStay method is invoked.
     * On the first frame after the collision ends, their onCollisionExit method is invoked.
     * The method additionally executes the pending actions of adding and removing
     * objects via the addGameObject/removeGameObject methods. This occurs after all
     * pair-handling already took place.
     * To alter the broadphase of the collision detection (which pairs should be
     * checked for collisions), override this method and use the protected method
     * handlePair.
     */
    public void handleCollisions() {
        layerManager.handleCollisions();
    }

    /**
     * Is the specified layer empty of objects
     */
    public boolean isLayerEmpty(int layerId) {
        return layerManager.isLayerEmpty(layerId);
    }

    /**
     * Iterate over the objects of the specified layer
     */
    public Iterable<GameObject> objectsInLayer(int layerId) {
        return layerManager.objectsInLayer(layerId);
    }

    /**
     * Layer management.
     */
    public LayerManager layers() { return layerManager; }

    /**
     * Calls any callbacks required on the objects (if any),
     * and resolves collisions if needed using {@link #resolver}.
     * @return whether the pair collided or not.
     */
    protected boolean handlePair(GameObject obj1, GameObject obj2) {
        pair.setGameObject(0, obj1);
        pair.setGameObject(1, obj2);
        if(!pair.go1().shouldCollideWith(pair.go2()) ||
                !pair.go2().shouldCollideWith(pair.go1()) ||
                pair.go1().getCoordinateSpace() != pair.go2().getCoordinateSpace()) {
            return false;
        }
        //do they already intersect?
        var cachedCollisionData = alreadyInCollision.getOrDefault(pair, null);
        if(cachedCollisionData != null) {
            //then this is not the first time
            cachedCollisionData.onFrameStart();
            if(!cachedCollisionData.areColliding()) {
                //then they did intersect but no longer
                pair.go1().onCollisionExit(pair.go2());
                pair.go2().onCollisionExit(pair.go1());
                alreadyInCollision.remove(pair);
                return false;
            }
            //then they still intersect
            pair.go1().onCollisionStay(pair.go2(), cachedCollisionData.getCollision(0));
            pair.go2().onCollisionStay(pair.go1(), cachedCollisionData.getCollision(1));
            if(resolver != null)
                resolver.resolve(pair.go1(), cachedCollisionData.getCollision(0),
                                 pair.go2(), cachedCollisionData.getCollision(1));
            return true;
        }
        //so far they weren't intersecting
        var collisionData = pairCollisionManager.getCollisionData(pair.go1(), pair.go2());
        if(null == collisionData)
            return false; //then no collision
        //then colliding for first time
        alreadyInCollision.put(collisionData, collisionData);
        pair.go1().onCollisionEnter(pair.go2(), collisionData.getCollision(0));
        pair.go1().onCollisionStay(pair.go2(), collisionData.getCollision(0));
        pair.go2().onCollisionEnter(pair.go1(), collisionData.getCollision(1));
        pair.go2().onCollisionStay(pair.go1(), collisionData.getCollision(1));
        return true;
    }
}
