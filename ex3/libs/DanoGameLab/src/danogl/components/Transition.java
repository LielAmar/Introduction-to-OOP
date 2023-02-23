package danogl.components;

import danogl.GameObject;
import danogl.util.MutableVector2;
import danogl.util.Vector2;

import java.util.function.Consumer;

/**
 * Transition a value over a range
 * @param <T> The value's type
 * @author Dan Nirel
 */
public class Transition<T> implements Component {
    private Runnable onReachingFinalValue;
    private static MutableVector2 calcVec = new MutableVector2();

    /************* Inner Types **************/
    /**
     * Represents an interpolator, which receives an initial value, a final value,
     * and a t parameter between 0 and 1, and returns the in-between value at t
     * @param <T> The type of the initial and final values
     */
    @FunctionalInterface
    public static interface Interpolator<T> {
        /**
         * Interpolate between two values
         * @param val1 initial value
         * @param val2 final value
         * @param t a range parameter, where 0 represents initial value, 1 represents the final
         *          value, and in-between values are a mix of the two. The legal range of this
         *          variable is [0,1].
         * @return A value of type T, in the range between val1 and val2
         */
        T interpolate(T val1, T val2, float t);
    }

    /**
     * Type of transition
     */
    public enum TransitionType {
        /**Transition only once, from initial value to final. If this transition type is chosen,
        the Transform will be deleted once it's done. */
        TRANSITION_ONCE,
        /**Repetitively transition from initial value to final, and back to initial*/
        TRANSITION_LOOP,
        /**After transitioning to final value, go back in reverse to
         * the initial value, from there back to final value and so on*/
        TRANSITION_BACK_AND_FORTH
    }

    /************* Constants **************/
    /**
     * A built-in linear interpolator for floats. The transition will advance from the
     * initial value to the final in a straight line.
      */
    public static final Interpolator<Float> LINEAR_INTERPOLATOR_FLOAT = (n1,n2,t)->(1-t)*n1+t*n2;
    /**
     * A built-in linear interpolator for Vector2. Each axis will advance from the
     * initial value to the final in a straight line.
     */
    public static final Interpolator<Vector2> LINEAR_INTERPOLATOR_VECTOR =
            (v1, v2, t)-> {
                calcVec.setXY(LINEAR_INTERPOLATOR_FLOAT.interpolate(v1.x(), v2.x(), t),
                        LINEAR_INTERPOLATOR_FLOAT.interpolate(v1.y(), v2.y(), t));
                return calcVec;
            };
    /**
     * A built-in cubit interpolator for floats. Will start slowly from the initial value,
     * accelerate, then slow when approaching its goal.
     */
    public static final Interpolator<Float> CUBIC_INTERPOLATOR_FLOAT = (n1,n2,t)
            ->(2*n1-2*n2)*t*t*t + (3*n2-3*n1)*t*t + n1;
    /**
     * A built-in cubit interpolator for Vector2. Will start slowly from the initial value,
     * accelerate, then slow when approaching its goal.
     */
    public static final Interpolator<Vector2> CUBIC_INTERPOLATOR_VECTOR =
            (v1, v2, t)-> {
                calcVec.setXY(CUBIC_INTERPOLATOR_FLOAT.interpolate(v1.x(), v2.x(), t),
                        (CUBIC_INTERPOLATOR_FLOAT.interpolate(v1.y(), v2.y(), t)));
                return calcVec;
            };

    /************* Fields **************/
    private GameObject gameObjectToUpdateThrough;
    private final Consumer<T> setValueCallback;
    private final T initialValue;
    private final T finalValue;
    private final Interpolator<T> interpolator;
    private final float transitionTime;
    private final TransitionType transitionType;
    private float t = 0;
    private int tAdvancementFactor = 1;

    /************* Methods **************/

    /**
     * Create a new Transition. After creating the transition it will immediately
     * be applied. There might not be any need for the reference to the created object.
     * @param gameObjectToUpdateThrough The transition will leech onto this object to get
     *                                  its update method called. It doesn't really matter
     *                                  which object is supplied here, as long as the object
     *                                  itself lives for at least as long as the transition should.
     *                                  Therefore it makes sense to send the GameObject most
     *                                  associated with this transition.
     * @param setValueCallback During the transition, it will traverse over a range of values.
     *                         Each such value will be sent to this callback. Effectively,
     *                         this is the method that actually changes the required attribute.
     *                         In a normal setting, unless using a very specialized interpolator,
     *                         the transition is meaningless without this callback that applies
     *                         the value to something external.
     * @param initialValue The value to start from.
     * @param finalValue A final value (the other edge value).
     * @param interpolator A function to interpolate between the edge values. Most cases
     *                     will find the built-in interpolators (declared as constants of the class)
     *                     sufficient.
     * @param transitionTime The time for a single transition from edge to edge, in seconds.
     * @param transitionType Type of transition.
     * @param onReachingFinalValue A function to be called every time the transition reached the final value.
     * @see TransitionType
     * @see Interpolator
     */
    public Transition(
            GameObject gameObjectToUpdateThrough,
            Consumer<T> setValueCallback,
            T initialValue,
            T finalValue,
            Interpolator<T> interpolator,
            float transitionTime,
            TransitionType transitionType,
            Runnable onReachingFinalValue) {
        this.onReachingFinalValue = onReachingFinalValue;
        if(gameObjectToUpdateThrough == null || setValueCallback == null
            || initialValue == null || finalValue == null
            || interpolator == null || transitionType == null) {
            throw new NullPointerException("None of Transition's parameters may be null except onReachingFinalValue");
        }
        this.gameObjectToUpdateThrough = gameObjectToUpdateThrough;
        this.setValueCallback = setValueCallback;
        this.initialValue = initialValue;
        this.finalValue = finalValue;
        this.interpolator = interpolator;
        this.transitionTime = transitionTime;
        this.transitionType = transitionType;

        gameObjectToUpdateThrough.addComponent(this);
    }

    @Override
    public void update(float deltaTime) {
        t += tAdvancementFactor * deltaTime/transitionTime;
        t = Math.max(Math.min(1, t), 0);
        setValueCallback.accept(interpolator.interpolate(initialValue, finalValue, t));
        if(t >= 1.0) {
            if(onReachingFinalValue != null)
                onReachingFinalValue.run();
            switch(transitionType) {
                case TRANSITION_BACK_AND_FORTH:
                    tAdvancementFactor *= -1;
                    break;
                case TRANSITION_LOOP:
                    t = 0;
                    break;
                case TRANSITION_ONCE:
                    gameObjectToUpdateThrough.removeComponent(this);
                    return;
            }
        }
        if(t <= 0) //then going back and forth
            tAdvancementFactor *= -1;
    }
}
