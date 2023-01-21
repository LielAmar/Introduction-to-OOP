package oop.ex6.utils;

import java.util.regex.Pattern;

/**
 * A class that's used to store all constant regexes in the code
 */
public class RegexConstants {

    public static final String EMPTY_LINE = "\\s*";
    public static final String COMMENT_LINE = "//.*";

    public static final String RETURN = "\\s*" + Constants.RETURN_KEYWORD + "\\s*;\\s*";
    public static final String BLOCK_CLOSER = "\\s*}\\s*";

    public static final String PARAMETER_TYPES = "(" +
            Constants.INT_KEYWORD + "|" +
            Constants.DOUBLE_KEYWORD + "|" +
            Constants.CHAR_KEYWORD + "|" +
            Constants.BOOLEAN_KEYWORD + "|" +
            Constants.STRING_KEYWORD +
            ")";
    public static final String RETURN_TYPES = "("
            + Constants.VOID_KEYWORD +
            ")";

    public static final String IDENTIFIER = "(([a-zA-Z]|_\\w)\\w*)";
    public static final String METHOD_IDENTIFIER = "(([a-zA-Z])\\w*)";

    public static final String OPTIONAL_FINAL = "(" + Constants.FINAL_KEYWORD + "\\s+){0,1}";

    public static final String INT_VALUE = "[+-]{0,1}\\d+";
    public static final String DOUBLE_VALUE = "[+-]{0,1}(\\d+([\\.]\\d*){0,1}|\\d*[\\.]\\d+)";
    public static final String CHAR_VALUE = "'.'";
    public static final String STRING_VALUE = "\".*\"";
    public static final String BOOLEAN_VALUE = "(" +
            Constants.TRUE_KEYWORD + "|" +
            Constants.FALSE_KEYWORD + "|" +
            INT_VALUE + "|" +
            DOUBLE_VALUE +
            ")";

    public static final String ANY_VALUE = "(" +
            IDENTIFIER + "|" +
            INT_VALUE + "|" +
            DOUBLE_VALUE + "|" +
            CHAR_VALUE + "|" +
            BOOLEAN_VALUE + "|" +
            STRING_VALUE +
            ")";

    // Declares a regex that defines a variable. It has a type and a regex defining its allowed value,
    // as well as optionally start with the word final
    // () = optional
    // [] = required
    private static final String VARIABLE =
            "\\s*" +                                       // Ignoring spaces at the beginning
                    OPTIONAL_FINAL + "%s\\s+" +                       // (final) [type]
                    IDENTIFIER +                                   // [name]
                    "(" + "\\s*,\\s*" + "(" + IDENTIFIER + ")" +   // [, another name
                    "|" +                                          // or
                    "\\s*=\\s*" + "(%s|" + IDENTIFIER + ")" + ")*" // = value]
                    + "(\\s*);\\s*";                               // ;

    public static final String INT_VARIABLE = String.format(VARIABLE,
            Constants.INT_KEYWORD, INT_VALUE);
    public static final String DOUBLE_VARIABLE = String.format(VARIABLE,
            Constants.DOUBLE_KEYWORD, DOUBLE_VALUE);
    public static final String CHAR_VARIABLE = String.format(VARIABLE,
            Constants.CHAR_KEYWORD, CHAR_VALUE);
    public static final String BOOLEAN_VARIABLE = String.format(VARIABLE,
            Constants.BOOLEAN_KEYWORD, BOOLEAN_VALUE);
    public static final String STRING_VARIABLE = String.format(VARIABLE,
            Constants.STRING_KEYWORD, STRING_VALUE);

    // Declares a regex that defines a variable assignment. It has an identifier which we update the
    // value of, as well as the value we set
    // () = optional
    // [] = required
    public static final String ASSIGNMENT =
            "\\s*" +                   // Ignoring spaces at the beginning
                    IDENTIFIER +               // [name]
                    "\\s*=\\s*" +              // =
                    ANY_VALUE +                // [value]
                    "(\\s*,\\s*" +             // Optionally, assign more variables
                    IDENTIFIER +
                    "\\s*=\\s*" +
                    ANY_VALUE +
                    "\\s*)*" +
                    "\\s*;\\s*";               // ;


    // Declares a regex that defines a block opener statement like if or while
    public static final String CONDITION = "(" + IDENTIFIER + "|" + BOOLEAN_VALUE + ")" +
            "(" + "\\s*(\\|\\||&&)\\s*" + "(" + IDENTIFIER + "|" + BOOLEAN_VALUE + ")" + ")*";

    private static final String CONDITIONAL_BLOCK = "\\s*%s\\s*\\(\\s*" + CONDITION + "\\s*\\)\\s*\\{\\s*";

    public static final String IF_STATEMENT = String.format(CONDITIONAL_BLOCK,
            Constants.IF_KEYWORD);
    public static final String WHILE_STATEMENT = String.format(CONDITIONAL_BLOCK,
            Constants.WHILE_KEYWORD);


    // Declares a regex that represents a method call, passing parameters of any type
    public static final String PASSED_PARAM_LIST = "\\(\\s*(()|" + ANY_VALUE +
            "(" + "\\s*,\\s*" + "(" + ANY_VALUE + "))*)\\s*\\)";

    public static final String METHOD_CALL =
            "\\s*" + METHOD_IDENTIFIER + "\\s*" + PASSED_PARAM_LIST + "\\s*;\\s*";


    // Declares a regex that represents a method declaration, receiving parameters of any type
    // () (type variable, type variable)
    public static final String RECEIVED_PARAM_LIST =
            "\\(" +
                    "\\s*(()|" + OPTIONAL_FINAL + PARAMETER_TYPES + "\\s+" + IDENTIFIER +
                    "(" + "\\s*,\\s*" + "(" + OPTIONAL_FINAL + PARAMETER_TYPES + "\\s+" + IDENTIFIER +
                    "))*)\\s*" +
                    "\\)";

    public static final String METHOD_DECLARATION = "\\s*" + RETURN_TYPES + "\\s*" + METHOD_IDENTIFIER +
            "\\s*" + RECEIVED_PARAM_LIST + "\\s*\\{\\s*";


    public static final Pattern INT_VALUE_PATTERN = Pattern.compile(RegexConstants.INT_VALUE);
    public static final Pattern DOUBLE_VALUE_PATTERN = Pattern.compile(RegexConstants.DOUBLE_VALUE);
    public static final Pattern CHAR_VALUE_PATTERN = Pattern.compile(RegexConstants.CHAR_VALUE);
    public static final Pattern STRING_VALUE_PATTERN = Pattern.compile(RegexConstants.STRING_VALUE);
    public static final Pattern BOOLEAN_VALUE_PATTERN = Pattern.compile(RegexConstants.BOOLEAN_VALUE);
    public static final Pattern IDENTIFIER_VALUE_PATTERN = Pattern.compile(RegexConstants.IDENTIFIER);

}
