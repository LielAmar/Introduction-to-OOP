package oop.ex6.utils;

public class Messages {

    public static final String SUBROUTINE_ALREADY_EXISTS =
            "A subroutine with the name %s already exists!";
    public static final String SUBROUTINE_NOT_FOUND =
            "Couldn't find a subroutine with the name %s!";


    public static final String VARIABLE_ALREADY_EXISTS =
            "A variable with the name %s already exists!";
    public static final String VARIABLE_NOT_FOUND =
            "Couldn't find a variable with the name %s!";
    public static final String ILLEGAL_VARIABLE =
            "%s is an illegal variable!";


    public static final String ILLEGAL_RETURN_TYPE =
            "%s is an illegal return type!";
    public static final String ILLEGAL_VARIABLE_TYPE =
            "%s is an illegal variable type!";
    public static final String ILLEGAL_LINE_TYPE =
            "%s is an illegal line type!";


    public static final String FINAL_VARIABLE_INITIALIZATION =
            "Tried to initialized a final variable";
    public static final String ILLEGAL_INITIALIZATION_TYPE =
            "Tried to initialize a variable with illegal type";
    public static final String INITIALIZATION_WITH_UNINITIALIZED_VARIABLE =
            "Tried to initialize a variable with an uninitialized variable";


    public static final String ILLEGAL_VARIABLE_DECLARATION_PARSE =
            "Couldn't parse variable declaration line: %s";
    public static final String ILLEGAL_VARIABLE_VALUE =
            "Variable %s was attempted assignment with illegal value";


    public static final String ILLEGAL_SUBROUTINE_RETURN_TYPE =
            "Line %s declares a subroutine with an illegal return type!";
    public static final String ILLEGAL_SUBROUTINE_PARAMETER =
            "Subroutine %s has illegal parameters!";
    public static final String ILLEGAL_SUBROUTINE_CLOSE =
            "A subroutine was closed without returning!";


    public static final String INVALID_AMOUNT_OF_ARGUMENTS =
            "A called subroutine didn't receive the correct number of arguments!";
    public static final String UNINITIALIZED_ARGUMENT_CALL =
            "An uninitialized argument was passed to a subroutine!";
    public static final String INVALID_ARGUMENT_TYPE =
            "An invalid argument type was passed to a subroutine!";


    public static final String ILLEGAL_CONDITION =
            "%s is an illegal condition!";
    public static final String UNINITIALIZED_VARIABLE_IN_CONDITION =
            "%s is an uninitialized variable!";


    public static final String SCOPE_NOT_CLOSED =
            "A scope was never closed!";
}
