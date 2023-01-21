package oop.ex6.checker;

/**
 * An exception class representing a checker exception
 */
public class CheckerException extends Exception {

    public CheckerException(String message) {
        super(message);
    }

    public CheckerException(String message, Exception exception) {
        super(message, exception);
    }
}
