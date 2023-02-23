package oop.ex6.checker.tables;

import oop.ex6.checker.CheckerException;

/**
 * An exception class representing a table exception
 */
public class TableException extends CheckerException {

    public TableException(String message) {
        super(message);
    }

    public TableException(String message, Exception exception) {
        super(message, exception);
    }
}
