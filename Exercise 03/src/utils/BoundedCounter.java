package utils;

import danogl.util.Counter;

/**
 * A utility class that enhances the default functionality of a Counter.
 * It adds an upper bound that we can't go past.
 */
public class BoundedCounter extends Counter {

    private final int upperBound;

    public BoundedCounter(int initValue, int upperBound) {
        super(initValue);

        this.upperBound = upperBound;
    }

    /**
     * Overrides the default increment behaviour to only increment if we didn't reach the upper bound.
     */
    @Override
    public void increment() {
        if(super.value() < this.upperBound) {
            super.increment();
        }
    }
}
