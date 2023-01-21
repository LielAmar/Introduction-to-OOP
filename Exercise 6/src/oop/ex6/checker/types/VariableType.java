package oop.ex6.checker.types;

import oop.ex6.utils.Constants;
import oop.ex6.utils.Messages;
import oop.ex6.utils.RegexConstants;

/**
 * An enum representing all allowed variable types in sjava
 */
public enum VariableType {

    INT(Constants.INT_KEYWORD),
    DOUBLE(Constants.DOUBLE_KEYWORD, INT),
    STRING(Constants.STRING_KEYWORD),
    BOOLEAN(Constants.BOOLEAN_KEYWORD, INT, DOUBLE),
    CHAR(Constants.CHAR_KEYWORD),
    IDENTIFIER();


    private final String declarator;
    private final VariableType[] acceptedTypes;

    VariableType(String declarator, VariableType... acceptedTypes) {
        this.declarator = declarator;
        this.acceptedTypes = acceptedTypes;
    }

    VariableType() {
        this(null);
    }


    /**
     * Returns the keyword used to reference the variable type
     *
     * @return   Declarator keyword
     */
    public String getDeclarator() {
        return this.declarator;
    }

    /**
     * Returns whether a given type can be accepted for the current type
     *
     * @param variableType   Type to check acceptance
     * @return               Whether the given type can be accepted
     */
    public boolean canAccept(VariableType variableType) {
        if(variableType == this) {
            return true;
        }

        for(VariableType accepted : this.acceptedTypes) {
            if(accepted == variableType) {
                return true;
            }
        }

        return false;
    }


    /**
     * A static method to return the matching VariableType object from a keyword.
     *
     * @param keyword   Keyword to parse the VariableType from
     * @return          Matching VariableType entry
     */
    public static VariableType fromKeyword(String keyword) throws TypeException {
        for(VariableType variableType : VariableType.values()) {
            if(variableType.getDeclarator() != null &&
                    variableType.getDeclarator().equals(keyword)) {
                return variableType;
            }
        }

        throw new TypeException(String.format(Messages.ILLEGAL_VARIABLE_TYPE, keyword));
    }

    /**
     * A static method to return the matching VariableType object from an expression
     *
     * @param expression   Expression to parse the VariableType from
     * @return          Matching VariableType entry
     */
    public static VariableType fromExpression(String expression) throws TypeException {
        if (RegexConstants.INT_VALUE_PATTERN.matcher(expression).matches()) {
            return VariableType.INT;
        } else if (RegexConstants.DOUBLE_VALUE_PATTERN.matcher(expression).matches()) {
            return VariableType.DOUBLE;
        } else if (RegexConstants.CHAR_VALUE_PATTERN.matcher(expression).matches()) {
            return VariableType.CHAR;
        } else if (RegexConstants.STRING_VALUE_PATTERN.matcher(expression).matches()) {
            return VariableType.STRING;
        } else if (RegexConstants.BOOLEAN_VALUE_PATTERN.matcher(expression).matches()) {
            return VariableType.BOOLEAN;
        } else if (RegexConstants.IDENTIFIER_VALUE_PATTERN.matcher(expression).matches()) {
            return VariableType.IDENTIFIER;
        }

        throw new TypeException(String.format(Messages.ILLEGAL_VARIABLE_TYPE, expression));
    }
}
