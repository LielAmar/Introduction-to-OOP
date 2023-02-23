package danogl.collisions.AABB;

import danogl.GameObject;
import danogl.collisions.PairCollisionData;
import danogl.collisions.PairCollisionHandler;
import java.awt.geom.Rectangle2D;

/**
 * Responsible for computing and caching the intersection data of
 * two GameObjects whose colliders are AABBs (axis-aligned bounding-boxes).
 * Is not responsible for resolving the collision.
 * @author Dan Nirel
 */
public class AABBPairCollisionHandler implements PairCollisionHandler {
    private Rectangle2D.Float rect1World = new Rectangle2D.Float();
    private Rectangle2D.Float rect2World = new Rectangle2D.Float();

    @Override
    public PairCollisionData computeCollisionData(GameObject go1, GameObject go2) {
        if(!supportsPair(go1, go2))
            return null;

        if(calcIntersectionRect(go1, go2, rect1World, rect2World) == null)
            return null;

        return new AABBPairCollisionData(go1, go2);
    }

    @Override
    public boolean supportsPair(GameObject go1, GameObject go2) {
        return go1.physics().collider().colliderType().equals(AABBCollider.AABB_COLLIDER_TYPE) &&
                go2.physics().collider().colliderType().equals(AABBCollider.AABB_COLLIDER_TYPE);
    }

    /**
     * Calculates the intersection rect of two game objects.
     * @param go1 game object 1
     * @param go2 game object 1
     * @param rect1World a container for the world space rect of game
     *                   object 1. updated if supplied. can be null.
     * @param rect2World a container for the world space rect of game
     *                   object 2. updated if supplied. can be null.
     * @return the intersection rectangle. if null, no intersection occurs.
     */
    public static Rectangle2D.Float calcIntersectionRect(
            GameObject go1, GameObject go2,
            Rectangle2D.Float rect1World, Rectangle2D.Float rect2World) {

        if(rect1World == null) {
            rect1World = new Rectangle2D.Float();
            rect2World = new Rectangle2D.Float();
        }

        var rect1 = ((AABBCollider)go1.physics().collider()).getRectangle();
        var rect2 = ((AABBCollider)go2.physics().collider()).getRectangle();

        var go1Corner = go1.getTopLeftCorner();
        rect1World.setRect(
                go1Corner.x() + rect1.x*go1.getDimensions().x(),
                go1Corner.y() + rect1.y*go1.getDimensions().y(),
                rect1.width * go1.getDimensions().x(),
                rect1.height * go1.getDimensions().y());

        var go2Corner = go2.getTopLeftCorner();
        rect2World.setRect(
                go2Corner.x() + rect2.x*go2.getDimensions().x(),
                go2Corner.y() + rect2.y*go2.getDimensions().y(),
                rect2.width * go2.getDimensions().x(),
                rect2.height * go2.getDimensions().y());

        if(!rect1World.intersects(rect2World))
            return null;
        return (Rectangle2D.Float) rect1World.createIntersection(rect2World);
    }
}
