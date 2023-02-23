package oop.ex6.checker.types;

import oop.ex6.utils.Pair;

import java.util.List;

/**
 * A class that represents a single subroutine object
 */
public class Subroutine {

    private final ReturnType returnType;
    private final List<Pair<String, Variable>> parameters;

    public Subroutine(ReturnType returnType, List<Pair<String, Variable>> parameters) {
        this.returnType = returnType;
        this.parameters = parameters;
    }


    /**
     * @return The subroutine's return type
     */
    public ReturnType getReturnType() {
        return this.returnType;
    }

    /**
     * @return The subroutine's parameter list with their respected name
     */
    public List<Pair<String, Variable>> getParameters() {
        return this.parameters;
    }
}
