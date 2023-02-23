package pepse.util.properties;

public interface IProperty<T> {

    /**
     * Returns the value of the property
     *
     * @return   Property value
     */
    T getValue();

    /**
     * Sets the value of the property
     *
     * @param value   Value to set
     */
    void setValue(T value);
}
