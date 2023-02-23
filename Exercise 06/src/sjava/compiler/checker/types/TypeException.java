package oop.ex6.checker.types;

import oop.ex6.checker.CheckerException;

/**
 * An exception class representing a type exception
 */
public class TypeException extends CheckerException {

    public TypeException(String message) {
        super(message);
    }

    public TypeException(String message, Exception exception) {
        super(message, exception);
    }
}
