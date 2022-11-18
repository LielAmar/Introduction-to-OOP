package danogl.components;

import danogl.util.MutableVector2;
import danogl.util.Vector2;

/**
 * Encapsulates some properties of a GameObject that are related
 * to its transform from object-space to world-space
 * @author Dan Nirel
 */
public class Transform implements Component {
    private MutableVector2 topLeftCorner;
    private MutableVector2 dimensions;
    private MutableVector2 velocity = new MutableVector2(Vector2.ZERO);
    private MutableVector2 velocityToApply = new MutableVector2(Vector2.ZERO);
    private MutableVector2 acceleration = new MutableVector2(Vector2.ZERO);
    private boolean isAccelerationEnabled = true;

    private static MutableVector2 freeComputationVector = new MutableVector2();

    /**
     * Construct a new Transform. Normally called by the GameObject's constructor.
     */
    public Transform(Vector2 topLeftCorner, Vector2 dimensions) {
        this.topLeftCorner = new MutableVector2(topLeftCorner);
        this.dimensions = new MutableVector2(dimensions);
    }

    /**
     * Returns the GameObject's top-left corner
     */
    public Vector2 getTopLeftCorner() {
        return topLeftCorner;
    }

    /**
     * Set the position of the object's top-left corner
     */
    public void setTopLeftCorner(Vector2 topLeftCorner) {
        this.topLeftCorner.setXY(topLeftCorner);
    }

    /**
     * Set the position of the object's top-left corner
     */
    public void setTopLeftCorner(float x, float y) {
        this.topLeftCorner.setXY(x, y);
    }

    /**
     * Returns the GameObject's dimensions
     */
    public Vector2 getDimensions() {
        return dimensions;
    }

    /**
     * Set the GameObject's dimensions
     */
    public void setDimensions(Vector2 dimensions) { setDimensions(dimensions.x(), dimensions.y()); }

    /**
     * Set the GameObject's dimensions. After the change, the object will maintain
     * its center.
     */
    public void setDimensions(float x, float y) {
        if(x < 0 || y < 0)
            throw new IllegalArgumentException(
                    "Dimensions must be non-negative. "+
                            "To flip image, see setIsflippedHorizontally()/setIsflippedVertically()");
        var oldCenter = getCenter();
        this.dimensions.setXY(x, y);
        setCenter(oldCenter);
    }

    /**
     * Returns the object's velocity
     */
    public Vector2 getVelocity() {
        return velocity;
    }

    /**
     * Set the object's velocity
     */
    public void setVelocity(Vector2 velocity) {
        this.velocity.setXY(velocity);
    }

    /**
     * Set the x-coordinate of the left side of the object
     */
    public void setTopLeftCornerX(float x) {
        topLeftCorner.setX(x);
    }

    /**
     * Set the y-coordinate of the top side of the object
     */
    public void setTopLeftCornerY(float y) {
        topLeftCorner.setY(y);
    }

    /**
     * Set the object's width. After the change, the object will maintain
     * its center.
     */
    public void setDimensionsX(float x) { setDimensions(x, dimensions.y()); }

    /**
     * Set the object's height. After the change, the object will maintain
     * its center.
     */
    public void setDimensionsY(float y) {
        setDimensions(dimensions.x(), y);
    }

    /**
     * Set the object's velocity
     */
    public void setVelocity(float x, float y) {
        this.velocity.setXY(x, y);
    }

    /**
     * Set the object's x-velocity
     */
    public void setVelocityX(float x) {
        velocity.setX(x);
    }

    /**
     * Set the object's y-velocity
     */
    public void setVelocityY(float y) {
        velocity.setY(y);
    }

    /**
     * Returns the object's center
     */
    public Vector2 getCenter() {
        return new MutableVector2(getDimensions())
                .selfMult(0.5f).selfAdd(getTopLeftCorner());
    }

    /**
     * Reposition the object by its center
     */
    public void setCenter(Vector2 center) {
        freeComputationVector.setXY(dimensions);
        freeComputationVector.selfMult(-0.5f).selfAdd(center);
        topLeftCorner.setXY(freeComputationVector);
    }

    /**
     * Reposition the object by its center
     */
    public void setCenter(float x, float y) {
        setCenterX(x);
        setCenterY(y);
    }

    /**
     * Reposition the center's x-position
     */
    public void setCenterX(float x) {
        topLeftCorner.setX(x-0.5f*dimensions.x());
    }

    /**
     * Reposition the center's y-position
     */
    public void setCenterY(float y) {
        topLeftCorner.setY(y-0.5f*dimensions.y());
    }

    /**
     * Returns the object's acceleration
     */
    public Vector2 getAcceleration() {
        return acceleration;
    }

    /**
     * Sets the object's acceleration
     */
    public void setAcceleration(Vector2 acceleration) {
        this.acceleration.setXY(acceleration);
    }

    /**
     * Sets the object's acceleration
     */    public void setAcceleration(float x, float y) {
        this.acceleration.setXY(x, y);
    }

    /**
     * Sets the object's x-acceleration
     */
    public void setAccelerationX(float x) {
        this.acceleration.setX(x);
    }

    /**
     * Sets the object's y-acceleration
     */
    public void setAccelerationY(float y) {
        this.acceleration.setY(y);
    }

    @Override
    public void update(float deltaTime) {
        if(!acceleration.equals(Vector2.ZERO) && isAccelerationEnabled) {
            velocity.setXY(
                    velocity.x() + acceleration.x() * deltaTime,
                    velocity.y() + acceleration.y() * deltaTime);
            //using "midpoint"/"improved Euler" integration
            velocityToApply.selfAdd(getVelocity()).selfMult(0.5f);
        }
        if(!velocityToApply.isZero()) {
            velocityToApply.selfMult(deltaTime);
            topLeftCorner.selfAdd(velocityToApply);
        }
        velocityToApply.setXY(velocity);
    }

    /**
     * Is the acceleration applied in this frame. The answer will be false
     * if accelerating will cause the object to intersect with an object
     * it is set to not intersect with.
     */
    public boolean isAccelerationEnabled() { return isAccelerationEnabled; }

    /**
     * Set whether the acceleration should be applied in this frame.
     * Used to prevent objects that are set to not intersect from intersecting.
     */
    public void setAccelerationEnabled(boolean accelerationEnabled) {
        isAccelerationEnabled = accelerationEnabled;
    }
}
