package danogl.collisions;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.components.GameObjectPhysics;
import danogl.util.Vector2;

/**
 * Responsible for preventing two GameObjects from intersecting,
 * provided they're configured to not intersect.
 * @author Dan Nirel
 */
public class CollisionResolver {
    /**
     * Check if intersection between the objects should be prevented, and if so alter their
     * positions and velocities in accordance with their static status.
     * @param go1 GameObject 1
     * @param go2 GameObject 2
     * @param collision1 as go1 would receive it in onCollisionStay
     * @param collision2 as go2 would receive it in onCollisionStay
     */
    public void resolve(GameObject go1, Collision collision1,
                        GameObject go2, Collision collision2) {
        if(!shouldResolve(go1, go2, collision1))
            return;

        float mass1 = go1.physics().mass();
        float mass2 = go2.physics().mass();
        float go1Adjustment = 0, go2Adjustment = 0;
        if(mass1 == mass2)
            go1Adjustment = go2Adjustment = .5f;
        else if(mass1 == GameObjectPhysics.IMMOVABLE_MASS)
            go2Adjustment = 1f;
        else if(mass2 == GameObjectPhysics.IMMOVABLE_MASS)
            go1Adjustment = 1f;
        else {
            go1Adjustment = mass2/(mass1+mass2);
            go2Adjustment = mass1/(mass1+mass2);
        }

        resolveOneObject(go1, collision1, go1Adjustment);
        resolveOneObject(go2, collision2, go2Adjustment);
    }

    /**
     *
     * @param go
     * @param collision
     * @param goAdjustment between 0 and 1. 0 means not to adjust the object at all,
     *                     1 means this object does the entire adjustment.
     *                     A value in between does not solve the collision on its own
     *                     (requires the other object to move the rest of the way).
     */
    private static void resolveOneObject(GameObject go, Collision collision, float goAdjustment) {
        if(goAdjustment == 0)
            return;

        var relativeVelocity = collision.getRelativeVelocity();
        var normal = collision.getNormal();
        var penetrationArea = collision.getPenetrationArea();

        //then objects should be prevented from intersecting
        var yDir = normal.y() < 0?-1:1;
        var xDir = normal.x() < 0?-1:1;

        //if collision is mostly vertical
        if(penetrationArea.x() > penetrationArea.y()
                && relativeVelocity.y() * yDir <= 0) {
            //then set it to top of the other, but still keep it intersecting
            //so that onCollisionStay still gets called
            go.transform().setTopLeftCornerY(
                    go.transform().getTopLeftCorner().y()
                            +goAdjustment*(yDir*(collision.getPenetrationArea().y()-1)));
            if(relativeVelocity.y() * yDir < 0)
                go.transform().setVelocityY(
                        go.transform().getVelocity().y() - goAdjustment*relativeVelocity.y());
            go.transform().setAccelerationEnabled(
                    go.transform().isAccelerationEnabled() &&
                            go.transform().getAcceleration().y() * yDir >= 0);
        }
        //if collision is mostly horizontal
        else if(penetrationArea.y() > penetrationArea.x()
                && relativeVelocity.x() * xDir <= 0) {
            go.transform().setTopLeftCornerX(
                    go.transform().getTopLeftCorner().x()
                            +xDir*goAdjustment*(collision.getPenetrationArea().x()-1));
            if(relativeVelocity.x() * xDir < 0)
                go.transform().setVelocityX(
                        go.transform().getVelocity().x() - goAdjustment*relativeVelocity.x());
            go.transform().setAccelerationEnabled(
                    go.transform().isAccelerationEnabled() &&
                            go.transform().getAcceleration().x() * xDir >= 0);
        }
    }

    private static boolean shouldResolve(GameObject go1, GameObject go2, Collision collision) {
        //if static, no need to move.
        if(go1.getVelocity().equals(Vector2.ZERO) && go2.getVelocity().equals(Vector2.ZERO)
        && go1.transform().getAcceleration().equals(Vector2.ZERO)
                && go2.transform().getAcceleration().equals(Vector2.ZERO))
            return false;

        Vector2 dir1 = go1.physics().directionFromWhichIntersectionsArePrevented();
        Vector2 dir2 = go2.physics().directionFromWhichIntersectionsArePrevented();

        //resolve only if both objects want to
        if(dir1 == null || dir2 == null)
            return false;

        boolean dir1IsZero = dir1.equals(Vector2.ZERO);
        boolean dir2IsZero = dir2.equals(Vector2.ZERO);

        //if moving away from collision, do nothing
        Vector2 relativeVelocity = collision.getRelativeVelocity();
        if((!dir1IsZero && dir1.dot(relativeVelocity) < 0) ||
                (!dir2IsZero && dir2.dot(relativeVelocity) > 0)) {
            return false;
        }

        //if object is not coming from prevented dir
        var normal = collision.getNormal();
        if((!dir1IsZero && dir1.dot(normal) >= 0) ||
                (!dir2IsZero && dir2.dot(normal) <= 0)) {
            return false;
        }

        final float SMALL_PENETRATION = 10;

        //if penetration is small, ignore it
        var penetrationArea = collision.getPenetrationArea();
        if(penetrationArea.x() < SMALL_PENETRATION && penetrationArea.y() < SMALL_PENETRATION)
            return false;

        return true;
    }
}
