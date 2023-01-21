package oop.ex6.checker.types;

import oop.ex6.utils.Messages;
import oop.ex6.utils.RegexConstants;

import java.util.regex.Pattern;

/**
 * An enum representing all allowed line types in sjava
 */
public enum LineType {

    EMPTY_LINE(RegexConstants.EMPTY_LINE),
    COMMENT_LINE(RegexConstants.COMMENT_LINE),

    INT_VAR_DECLARATION(RegexConstants.INT_VARIABLE),
    DOUBLE_VAR_DECLARATION(RegexConstants.DOUBLE_VARIABLE),
    CHAR_VAR_DECLARATION(RegexConstants.CHAR_VARIABLE),
    BOOLEAN_VAR_DECLARATION(RegexConstants.BOOLEAN_VARIABLE),
    STRING_VAR_DECLARATION(RegexConstants.STRING_VARIABLE),

    ASSIGNMENT(RegexConstants.ASSIGNMENT),

    IF(RegexConstants.IF_STATEMENT),
    WHILE(RegexConstants.WHILE_STATEMENT),

    METHOD_DECLARATION(RegexConstants.METHOD_DECLARATION),
    METHOD_CALL(RegexConstants.METHOD_CALL),

    RETURN(RegexConstants.RETURN),
    BLOCK_CLOSE(RegexConstants.BLOCK_CLOSER);


    private final Pattern pattern;

    LineType(String regex) {
        this.pattern = Pattern.compile(regex);
    }

    /**
     * Returns the pattern that matches the LineType object
     *
     * @return LineType's regex pattern
     */
    public Pattern getPattern() {
        return this.pattern;
    }


    /**
     * A static method to return the matching LineType object from a given line.
     *
     * @param line Line to parse the LineType from
     * @return Matching LineType entry
     */
    public static LineType fromLine(String line) throws TypeException {
        for (LineType lineType : LineType.values()) {
            if (lineType.pattern.matcher(line).matches()) {
                return lineType;
            }
        }

        throw new TypeException(String.format(Messages.ILLEGAL_LINE_TYPE, line));
    }


    /**
     * Returns whether a line type is a variable declaration line
     *
     * @param lineType Line type to check
     * @return Whether the given line is a variable declaration
     */
    public static boolean isVariableDeclarationLine(LineType lineType) {
        return lineType == LineType.INT_VAR_DECLARATION ||
                lineType == LineType.DOUBLE_VAR_DECLARATION ||
                lineType == LineType.CHAR_VAR_DECLARATION ||
                lineType == LineType.BOOLEAN_VAR_DECLARATION ||
                lineType == LineType.STRING_VAR_DECLARATION;
    }

    /**
     * Returns whether a line type is a variable assignment line
     *
     * @param lineType Line type to check
     * @return Whether the given line is a variable assignment
     */
    public static boolean isVariableAssignmentLine(LineType lineType) {
        return lineType == LineType.ASSIGNMENT;
    }

    /**
     * Returns whether a line type is a subroutine declaration line
     *
     * @param lineType Line type to check
     * @return Whether the given line is a subroutine declaration
     */
    public static boolean isSubroutineDeclarationLine(LineType lineType) {
        return lineType == LineType.METHOD_DECLARATION;
    }

    /**
     * Returns whether a line type is a subroutine call line
     *
     * @param lineType Line type to check
     * @return Whether the given line is a subroutine call
     */
    public static boolean isSubroutineCallLine(LineType lineType) {
        return lineType == LineType.METHOD_CALL;
    }

    /**
     * Returns whether a line type is a conditional line
     *
     * @param lineType Line type to check
     * @return Whether the given line is a conditional line
     */
    public static boolean isConditionalLine(LineType lineType) {
        return lineType == LineType.IF || lineType == LineType.WHILE;
    }

    /**
     * Returns whether a line type is a block opener line
     *
     * @param lineType Line type to check
     * @return Whether the given line is a block opener
     */
    public static boolean isBlockOpenerLine(LineType lineType) {
        return lineType == LineType.IF ||
                lineType == LineType.WHILE ||
                lineType == LineType.METHOD_DECLARATION;
    }

    /**
     * Returns whether a line type is a block closer line
     *
     * @param lineType Line type to check
     * @return Whether the given line is a block closer
     */
    public static boolean isBlockCloserLine(LineType lineType) {
        return lineType == LineType.BLOCK_CLOSE;
    }

    /**
     * Returns whether a line type is an ignored line
     *
     * @param lineType Line type to check
     * @return Whether the given line is an ignored line
     */
    public static boolean isIgnoredLine(LineType lineType) {
        return lineType == LineType.EMPTY_LINE ||
                lineType == LineType.COMMENT_LINE;
    }
}
