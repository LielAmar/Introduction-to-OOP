package danogl.collisions.AABB;

import danogl.collisions.Collision;
import danogl.util.MutableVector2;
import danogl.util.Vector2;

/**
 * Encapsulates the collision info for a single GameObject whose
 * collider is AABB (axis-aligned bounding-box)
 * @author Dan Nirel
 */
class AABBCollision implements Collision {
    private AABBPairCollisionData pairCollisionData;
    private int myIndex;
    private MutableVector2 normal, collisionPoint,
            penetrationArea, relativeVelocity;
    private boolean alreadyComputedRelativeVelocityThisFrame = false;

    public AABBCollision(AABBPairCollisionData pairCollisionData, int myIndex) {
        this.pairCollisionData = pairCollisionData;
        this.myIndex = myIndex;
    }

    @Override
    public void onFrameStart() {
        alreadyComputedRelativeVelocityThisFrame = false;
    }

    @Override
    public Vector2 getNormal() {
        if(normal == null)
            normal = new MutableVector2();
        var x = pairCollisionData.getIntersectionRect();
        pairCollisionData.updateIntersectionRect();
        x = pairCollisionData.getIntersectionRect();
        var y = x.getCenterX();
        normal.setX((float)(pairCollisionData.getRectWorld(myIndex).getCenterX()
                -pairCollisionData.getIntersectionRect().getCenterX()));
        normal.setY((float)(pairCollisionData.getRectWorld(myIndex).getCenterY()
                -pairCollisionData.getIntersectionRect().getCenterY()));
        return normal.selfNormalize();
    }

    @Override
    public Vector2 getCollisionPoint() {
        if(collisionPoint == null)
            collisionPoint = new MutableVector2();
        pairCollisionData.updateIntersectionRect();
        collisionPoint.setXY(
                (float)pairCollisionData.getIntersectionRect().getCenterX(),
                (float)pairCollisionData.getIntersectionRect().getCenterY());
        return collisionPoint;
    }

    @Override
    public Vector2 getPenetrationArea() {
        if(penetrationArea == null)
            penetrationArea = new MutableVector2();
        pairCollisionData.updateIntersectionRect();
        penetrationArea.setXY(
                pairCollisionData.getIntersectionRect().width,
                pairCollisionData.getIntersectionRect().height);
        return penetrationArea;
    }

    @Override
    public Vector2 getRelativeVelocity() {
        if(alreadyComputedRelativeVelocityThisFrame)
            return relativeVelocity;
        if(relativeVelocity == null)
            relativeVelocity = new MutableVector2();

        alreadyComputedRelativeVelocityThisFrame = true;

        var otherAABBCollision = (AABBCollision) pairCollisionData.getCollision(1-myIndex);
        if(otherAABBCollision.alreadyComputedRelativeVelocityThisFrame) {
            //then just invert that of the other collision object
            relativeVelocity.setXY(otherAABBCollision.relativeVelocity);
            return relativeVelocity.selfMult(-1);
        }

        //then there's no inert velocity to rely on
        relativeVelocity.setXY(
                pairCollisionData.getGameObject(myIndex).getVelocity());
        return relativeVelocity.selfSubtract(
                pairCollisionData.getGameObject(1-myIndex).getVelocity());
    }
}
