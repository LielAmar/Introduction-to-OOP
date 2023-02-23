package oop.ex6.main;

/**
 * An enum used to represent a Result of the sjava checker.
 */
public enum Result {

    LEGAL(0),
    ILLEGAL(1),
    IO_ERROR(2);


    private final int code;

    Result(int code) {
        this.code = code;
    }

    /**
     * Returns the result's code
     *
     * @return Result's code
     */
    public int getCode() {
        return code;
    }
}
