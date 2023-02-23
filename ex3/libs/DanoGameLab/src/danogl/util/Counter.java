package danogl.util;

/**
 * A simple class implementing a counter. This can be used instead of a
 * primitive integer where you wish the integer to be passed by reference.
 * In other words, when one object alters
 * the value of the counter, the change is visible in another object with
 * the same object-reference.
 * @author Dan Nirel
 */
public class Counter {
    private int counter;

    /** Initialize a new counter with the given value */
    public Counter(int initValue) { counter = initValue; }
    /** Initialize a new counter with the value 0 */
    public Counter() { this(0); }
    /** Increment the value by 1 */
    public void increment() { counter++; }
    /** Decrement the value by 1 */
    public void decrement() { counter--; }
    /** Return the counter's current value */
    public int value() { return counter; }
    /** Reset the counter to zero */
    public void reset() { counter = 0; }
    /** Increase the counter by the supplied value (can be negative) */
    public void increaseBy(int val) { counter += val; }
}
