package oop.ex6.checker.types;

import oop.ex6.utils.Messages;

/**
 * A class that represents a single variable object
 */
public class Variable {

    private final VariableType type;

    private boolean isFinal;
    private boolean isInitialized;
    private boolean globallyInitialized;

    public Variable(VariableType type, boolean isFinal, boolean isInitialized) {
        this.type = type;

        this.isFinal = isFinal;
        this.isInitialized = isInitialized;
        this.globallyInitialized = isInitialized;
    }


    /**
     * @return   The variable's type
     */
    public VariableType getType() {
        return this.type;
    }


    /**
     * @return   Whether the variable is final
     */
    public boolean isFinal() {
        return this.isFinal;
    }

    /**
     * @param isFinal   Sets the variable's final state
     */
    public void setFinal(boolean isFinal) {
        this.isFinal = isFinal;
    }


    /**
     * @return   Whether the variable is initialized
     */
    public boolean isInitialized() {
        return this.isInitialized;
    }

    /**
     * @return   Whether the variable was initialized in the global scope
     */
    public boolean isGloballyInitialized() {
        return this.globallyInitialized;
    }


    /**
     * Tries to initialize the variable with the given type.
     *
     * @param assigmentType    Type to use for the initialization
     * @throws TypeException   Throws an exception if initialization was illegal
     */
    public void initialize(VariableType assigmentType) throws TypeException {
        if (this.isFinal) {
            throw new TypeException(Messages.FINAL_VARIABLE_INITIALIZATION);
        }

        if (!this.getType().canAccept(assigmentType)) {
            throw new TypeException(Messages.ILLEGAL_INITIALIZATION_TYPE);
        }

        this.isInitialized = true;
    }

    /**
     * Tries to initialize the variable with the given variable.
     *
     * @param variable         Variable to use for the initialization
     * @throws TypeException   Throws an exception if initialization was illegal
     */
    public void initialize(Variable variable) throws TypeException {
        if(!variable.isInitialized) {
            throw new TypeException(Messages.INITIALIZATION_WITH_UNINITIALIZED_VARIABLE);
        }

        this.initialize(variable.getType());
    }

    /**
     * Sets the variable to be initialized globally, meaning it was initialized in
     * the global scope, and not within a specific subroutine
     */
    public void initializeGlobally() {
        this.globallyInitialized = true;
    }


    /**
     * Uninitializes the variable
     */
    public void uninitialize() {
        this.isInitialized = false;
    }
}
