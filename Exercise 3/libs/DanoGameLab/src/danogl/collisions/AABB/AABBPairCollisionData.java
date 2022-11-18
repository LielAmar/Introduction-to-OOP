package danogl.collisions.AABB;

import danogl.GameObject;
import danogl.collisions.PairCollisionData;
import java.awt.geom.Rectangle2D;

/**
 * package-private.
 * Responsible for computing and caching the collision data for a pair
 * of GameObjects whose colliders are AABBs.
 * @author Dan Nirel
 */
class AABBPairCollisionData extends PairCollisionData {
    private int lastCalcedHash;
    private Rectangle2D.Float rect1World, rect2World;
    private Rectangle2D.Float intersectionRect;
    private boolean alreadyComputedIntersectionRectThisFrame = false;

    public AABBPairCollisionData(GameObject go1,GameObject go2) {
        super(go1, go2);
        collision1 = new AABBCollision(this, 0);
        collision2 = new AABBCollision(this, 1);
    }

    @Override
    public void onFrameStart() {
        alreadyComputedIntersectionRectThisFrame = false;
        collision1.onFrameStart();
        collision2.onFrameStart();
    }

    public void updateIntersectionRect() {
        if(alreadyComputedIntersectionRectThisFrame || !hasAnythingChanged())
            return;

        if(rect1World == null) {
            rect1World = new Rectangle2D.Float();
            rect2World = new Rectangle2D.Float();
        }

        intersectionRect =
            AABBPairCollisionHandler.calcIntersectionRect(
                    go1(), go2(), rect1World, rect2World);

        alreadyComputedIntersectionRectThisFrame = true;
    }

    @Override
    public boolean areColliding() {
        updateIntersectionRect();
        return intersectionRect != null && !intersectionRect.isEmpty();
    }

    public Rectangle2D.Float getRectWorld(int index) {
        return index == 0 ? rect1World : rect2World;
    }

    public Rectangle2D.Float getIntersectionRect() {
        return intersectionRect;
    }

    private boolean hasAnythingChanged() {
        if(rect1World == null)
            return true; //then wasn't ever inited
        int newHash =
                go1().getTopLeftCorner().hashCode()
                        + 31*go2().getTopLeftCorner().hashCode()
                        + go1().getDimensions().hashCode()
                        + go2().getDimensions().hashCode()
                        + ((AABBCollider)go1().physics().collider()).getRectangle().hashCode()
                        + ((AABBCollider)go2().physics().collider()).getRectangle().hashCode();
        if(newHash == lastCalcedHash)
            return false;
        lastCalcedHash = newHash;
        return true;
    }
}
