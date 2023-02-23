package oop.ex6.checker.tables;

/**
 * An interface used to handle tables tracking different data in the sjava code
 *
 * @param <T>
 */
public interface ITable<T> {

    /**
     * Tries to add an object to the table
     *
     * @param name              Name of the object, used as key
     * @param object            Object to add
     * @throws TableException   Throws an exception if it couldn't add the object to the table
     */
    void add(String name, T object) throws TableException;

    /**
     * Returns the object matching to ${name} in the table
     *
     * @param name   Name of the object to return
     * @return       Found object or null otherwise
     */
    T get(String name) throws TableException;
}
