package pepse.util.properties;

/**
 * A class that represents a numeric property with current value, min, max and a change factor
 */
public class NumericProperty implements IProperty<Double> {

    private final double factor;

    private final double max;
    private final double min;

    private double current;

    public NumericProperty(double factor, double max, double min, double current) {
        this.factor = factor;

        this.max = max;
        this.min = min;

        this.current = current;
    }

    @Override
    public Double getValue() {
        return this.current;
    }

    @Override
    public void setValue(Double value) {
        this.current = value;
    }


    /**
     * Returns the property factor
     *
     * @return   Property factor
     */
    public double getFactor() {
        return this.factor;
    }

    /**
     * Returns the property max value
     *
     * @return   Property max value
     */
    public double getMaxValue() {
        return this.max;
    }

    /**
     * Returns the property min value
     *
     * @return   Property min value
     */
    public double getMinValue() {
        return this.min;
    }


    /**
     * Increases the property by its factor
     */
    public void increase() {
        this.increase(this.factor);
    }

    /**
     * Increases the property by the given factor
     *
     * @param factor   Factor to use for increment
     */
    public void increase(double factor) {
        this.setValue(Math.min(this.current + factor, this.max));
    }

    /**
     * Decreases the property by its factor
     */
    public void decrease() {
        this.decrease(this.factor);
    }

    /**
     * Decreases the property by the given factor
     *
     * @param factor   Factor to use for decrement
     */
    public void decrease(double factor) {
        this.setValue(Math.max(this.current - factor, this.min));
    }
}
