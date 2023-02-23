package danogl.util;

/**
 * An immutable class representing a 2D vector.
 * May represent a position, velocity, acceleration etc.
 * None of the methods
 * alter the initial x or y coordinates of the created vector;
 * some methods return a variation of this vector.
 * CAUTION: though a reference of this type can not alter the underlying object,
 * it does not mean the object may not change via another reference (contrary to most
 * immutable classes), as this class can be inherited
 * (an object of the subclass may be mutable).
 * @author Dan Nirel
 */
public class Vector2 {
    /** The vector (0, 0) */
    public static final Vector2 ZERO    = new Vector2( 0, 0);
    /** The vector (0, -1) */
    public static final Vector2 UP      = new Vector2( 0,-1);
    /** The vector (-1, 0) */
    public static final Vector2 LEFT    = new Vector2(-1, 0);
    /** The vector (1, 0) */
    public static final Vector2 RIGHT   = new Vector2( 1, 0);
    /** The vector (0, 1) */
    public static final Vector2 DOWN    = new Vector2( 0, 1);
    /** The vector (1, 1) */
    public static final Vector2 ONES    = new Vector2( 1, 1);

    private static final float EPSILON_FOR_COMPARING_VECTORS = 0.1f;

    protected float x,y;

    /**
     * Constructs a new vector based on the given coordinates.
     */
    public Vector2(float x, float y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Construct a copy of the given vector. Might be desirable if the other vector is
     * actually mutable.
     */
    public Vector2(Vector2 v) { this(v.x, v.y); }

    /** Returns the vector's x coordinate. */
    public float x() {
        return x;
    }
    /** Returns the vector's y coordinate. */
    public float y() {
        return y;
    }
    /** Returns a new vector that is the sum of this and the specified vector. */
    public Vector2 add(Vector2 other) {
        if(other.equals(Vector2.ZERO))
            return this;
        return createVec(x+other.x, y+other.y);
    }
    /** Returns a new vector that is the given vector subtracted from this one. */
    public Vector2 subtract(Vector2 other) {
        if(other.equals(Vector2.ZERO))
            return this;
        return createVec(x-other.x, y-other.y);
    }
    /** Returns a new vector whose coordinates are equal to this one's,
     * multiplied by the given factor. */
    public Vector2 mult(float factor) {
        if(factor == 1)
            return this;
        return createVec(factor*x, factor*y);
    }
    /** Returns a new vector with an equal y coordinate, and an x coordinate
     * which is the product of this vector's x and the given factor. */
    public Vector2 multX(float factor) {
        if(factor == 1)
            return this;
        return createVec(factor*x, y);
    }
    /** Returns a new vector with an equal x coordinate, and a y coordinate
     * which is the product of this vector's y and the given factor. */
    public Vector2 multY(float factor) {
        if(factor == 1)
            return this;
        return createVec(x, factor*y);
    }
    /** Returns the dot product of this vector and a given vector. */
    public float dot(Vector2 other) { return x*other.x + y*other.y; }
    /** Returns whether this vector's coordinates are both exactly zero. */
    public boolean isZero() { return x == 0 && y == 0; }
    /** Returns the vector's magnitude. */
    public float magnitude() { return (float)Math.sqrt(sqrMagnitude()); }
    /** Returns the square of the vector's magnitude. */
    public float sqrMagnitude() { return x*x+y*y; }
    /** Returns a vector with an equal direction but whose size is 1. */
    public Vector2 normalized() {
        if(isZero())
            return ZERO;
        return mult(1f/magnitude());
    }
    /** Returns the component of this vector that is perpendicular to the given normal. */
    public Vector2 zeroedAlongNormal(Vector2 normal) {
        float normalMag = normal.magnitude();
        float proj = dot(normal)/normalMag;
        float x = x()-normal.x()/normalMag*proj;
        float y = y()-normal.y()/normalMag*proj;
        return createVec(x, y);
    }
    /** Returns a new vector. The component of the new vector perpendicular to the given
     * normal is equal to this vector's. The component of the new vector parallel
     * to the normal is the negation of this vector's. May be used to simulate
     * semi-elastic collisions if flipping a velocity vector relative to
     * the collision normal.
     */
    public Vector2 flipped(Vector2 normal) {
        float normalMag = normal.magnitude();
        float proj = dot(normal)/normalMag;
        Vector2 newV = this;
        if(proj < 0) {
            //if projection is negative, vector opposes normal.
            //in this case, inverse it along the projection
            newV = createVec(x()-normal.x()*2f*proj/normalMag,
                    y()-normal.y()*2f*proj/normalMag);
        }
        return newV;
    }
    /** Returns a new vector with an equal magnitude, but whose direction is either
     * down, up, right, or left - depending on which is the closest.
     */
    public Vector2 rotatedToNearestAxis() {
        if(Math.abs(x) > Math.abs(y))
            return createVec(Math.signum(x)*magnitude(), 0);
        return createVec(0, Math.signum(y)*magnitude());
    }

    /** Returns a new vector which is the rotation of this one by the given
     * angle.
     * @param degreesCounterclockwise The angle by which to rotate
     *                                counter-clockwise, in degrees
     */
    public Vector2 rotated(float degreesCounterclockwise) {
        double cos = Math.cos(Math.toRadians(-degreesCounterclockwise));
        double sin = Math.sin(Math.toRadians(-degreesCounterclockwise));
        return createVec((float)(cos*x-sin*y), (float)(sin*x+cos*y));
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Vector2))
            return false;
        Vector2 other = (Vector2) obj;
        return x==other.x && y == other.y;
    }

    /**
     * Returns true iff the vector is approximately equal to the parameter (up to a small epsilon)
     */
    public boolean approximatelyEquals(Vector2 other) {
        float dx = x-other.x;
        float dy = y-other.y;
        return dx*dx+dy*dy < EPSILON_FOR_COMPARING_VECTORS;
    }

    @Override
    public String toString() {
        return String.format("(%.2f, %.2f)", x, y);
    }

    @Override
    public int hashCode() {
        return Float.hashCode(x + 31*y);
    }

    /**
     * This method should be called if the user requires a copy of the coordinates that would
     * not be altered outside their control. If this vector is already immutable, this
     * incurs no overhead and simply returns the object itself. Calling the method
     * is still necessary for cases where the underlying vector is an instance of
     * a mutable subclass.
     */
    public Vector2 getImmutableCopy() {
        return this;
    }

    protected Vector2 createVec(float x, float y) {
        return new Vector2(x, y);
    }

    /**
     * Convenience method, simply another way of creating a new Vector
     * (equivalent to using the constructor).
     */
    public static Vector2 of(float x, float y) {
        return new Vector2(x, y);
    }
}
