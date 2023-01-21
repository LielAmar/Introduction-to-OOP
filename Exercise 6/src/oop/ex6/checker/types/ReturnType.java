package oop.ex6.checker.types;

import oop.ex6.utils.Messages;

/**
 * An enum representing all allowed return types in sjava
 */
public enum ReturnType {

    VOID("void");


    private final String declarator;

    ReturnType(String declarator) {
        this.declarator = declarator;
    }

    /**
     * Returns the keyword used to reference the return type
     *
     * @return Declarator keyword
     */
    public String getDeclarator() {
        return this.declarator;
    }


    /**
     * A static method to return the matching ReturnType object from a keyword.
     *
     * @param keyword Keyword to parse the ReturnType from
     * @return Matching ReturnType entry
     */
    public static ReturnType fromKeyword(String keyword) throws TypeException {
        for (ReturnType returnType : ReturnType.values()) {
            if (returnType.getDeclarator().equals(keyword)) {
                return returnType;
            }
        }

        throw new TypeException(String.format(Messages.ILLEGAL_RETURN_TYPE, keyword));
    }
}
