package danogl.util;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * A mutable 2D vector, extending Vector2.
 * All methods are self explanatory, with most corresponding to
 * a similar method of the immutable superclass.
 * For example, where Vector2.add returns a new Vector2 of the result,
 * selfAdd adds the given vector to itself.
 * This class is a compromise between design and efficiency necessary
 * in the Java port of the engine, since in Java objects can only be created
 * on the heap. By not creating new objects, this class helps reduce the
 * memory consumption and overhead of the engine significantly.
 * @author Dan Nirel
 */
public class MutableVector2 extends Vector2 {
    private static interface FloatToVecFunction {
        Vector2 apply(float f);
    }

    private boolean mutateSelf = false;

    public MutableVector2() { super(0, 0); }
    public MutableVector2(float x, float y) { super(x, y); }
    public MutableVector2(Vector2 v) { super(v); }

    public void setX(float x) { this.x = x; }
    public void setY(float y) { this.y = y; }
    public void setXY(float x, float y) { setX(x); setY(y); }
    public void setXY(Vector2 v) { setX(v.x); setY(v.y); }

    public MutableVector2 selfMult(float factor) { return mutableOperator(this::mult, factor); }
    public MutableVector2 selfMultX(float factor) { return mutableOperator(this::multX, factor); }
    public MutableVector2 selfMultY(float factor) { return mutableOperator(this::multY, factor); }
    public MutableVector2 selfAdd(Vector2 v) { return mutableOperator(this::add, v); }
    public MutableVector2 selfSubtract(Vector2 v) { return mutableOperator(this::subtract, v); }

    public MutableVector2 selfZeroAlongNormal(Vector2 normal)
            { return mutableOperator(this::zeroedAlongNormal, normal); }
    public MutableVector2 selfFlip(Vector2 normal)
            { return mutableOperator(this::flipped, normal);}
    public MutableVector2 selfRotateToNearestAxis()
            { return mutableOperator(this::rotatedToNearestAxis); }
    public MutableVector2 selfRotate(float degreesCounterclockwise)
            { return mutableOperator(this::rotated, degreesCounterclockwise); }

    public MutableVector2 selfNormalize() {
        if(isZero())
            setXY(ZERO);
        else
            selfMult(1f/magnitude());
        return this;
    }

    @Override
    public Vector2 getImmutableCopy() { return new Vector2(this); }

    @Override
    protected Vector2 createVec(float x, float y) {
        if(!mutateSelf)
            return new Vector2(x, y);
        setX(x);
        setY(y);
        return this;
    }

    private MutableVector2 mutableOperator(Function<Vector2, Vector2> method, Vector2 param){
        mutateSelf = true;
        method.apply(param);
        mutateSelf = false;
        return this;
    }

    private MutableVector2 mutableOperator(FloatToVecFunction method, float param) {
        mutateSelf = true;
        method.apply(param);
        mutateSelf = false;
        return this;
    }

    private MutableVector2 mutableOperator(Supplier<Vector2> method) {
        mutateSelf = true;
        method.get();
        mutateSelf = false;
        return this;
    }
}
