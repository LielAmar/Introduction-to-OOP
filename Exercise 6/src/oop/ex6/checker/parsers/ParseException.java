package oop.ex6.checker.parsers;

import oop.ex6.checker.CheckerException;

/**
 * An exception class representing a parse exception
 */
public class ParseException extends CheckerException {

    public ParseException(String message) {
        super(message);
    }

    public ParseException(String message, Exception exception) {
        super(message, exception);
    }
}