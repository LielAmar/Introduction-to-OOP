package danogl.collisions.AABB;

import danogl.collisions.Collider;
import danogl.util.Vector2;

import java.awt.geom.Rectangle2D;

/**
 * An axis-aligned bounding-box collider (rectangle)
 * @author Dan Nirel
 */
public class AABBCollider implements Collider {
    public static final String AABB_COLLIDER_TYPE = "AABB";
    private Rectangle2D.Float rectangle;

    /**
     * Create a new AABB collider. the top-left corner coordinates
     * and dimensions are supplied, in object-coordinates (not world coordinates).
     */
    public AABBCollider(float objectSpaceX, float objectSpaceY,
                        float objectSpaceWidth, float objectSpaceHeight) {
        this.rectangle = new Rectangle2D.Float(
                objectSpaceX, objectSpaceY, objectSpaceWidth, objectSpaceHeight);
    }

    @Override
    public String colliderType() {
        return AABB_COLLIDER_TYPE;
    }

    @Override
    public boolean containsPoint(Vector2 point) {
        return rectangle.contains(point.x(), point.y());
    }

    /**
     * Returns the rectangle represented by this collider,
     * in object-coordinates (not world).
     */
    public Rectangle2D.Float getRectangle() {
        return rectangle;
    }
}
