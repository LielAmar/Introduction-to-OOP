package oop.ex6.checker.tables.variables;

import oop.ex6.checker.tables.ITable;
import oop.ex6.checker.tables.TableException;
import oop.ex6.checker.types.Variable;
import oop.ex6.utils.Messages;

import java.util.*;

/**
 * A class used to store all variables used within a sjava program
 */
public class VariableTable implements ITable<Variable> {

    private final Map<String, Variable> globalVariables;
    private final LinkedList<Map<String, Variable>> scopeVariables;

    public VariableTable() {
        this.globalVariables = new HashMap<>();
        this.scopeVariables = new LinkedList<>();
    }


    /**
     * Opens a new scope for local variables
     */
    public void openScope() {
        Map<String, Variable> scope = new HashMap<>();

        this.scopeVariables.add(scope);
    }

    /**
     * Closes the latest scope
     */
    public void closeScope() {
        if (this.scopeVariables.size() == 0) {
            return;
        }

        this.scopeVariables.removeLast();
    }


    /**
     * Returns a collection of all global variables saved until current point
     *
     * @return   Global variables collection
     */
    public Collection<Variable> getGlobalVariable() {
        return globalVariables.values();
    }


    /**
     * Tries to add a variable object to the current scope's table
     *
     * @param name              Name of the variable
     * @param variable          Variable to add
     * @throws TableException   Throws an exception if it couldn't add variable to the table
     */
    @Override
    public void add(String name, Variable variable) throws TableException {
        if (variable.isFinal() && !variable.isInitialized()) {
            throw new TableException(String.format(Messages.ILLEGAL_VARIABLE, name));
        }

        boolean succeeded = (this.scopeVariables.size() == 0) ?
                this.addGlobalVariable(name, variable) :
                this.addLocalVariable(name, variable);

        if(!succeeded) {
            throw new TableException(String.format(Messages.VARIABLE_ALREADY_EXISTS, name));
        }
    }

    /**
     * Tries to add a given variable into the global variables table
     *
     * @param name       Name of the variable to add table
     * @param variable   Variable object to add to the table
     * @return           Whether the insertion was successful
     */
    private boolean addGlobalVariable(String name, Variable variable) {
        if (this.globalVariables.containsKey(name)) {
            return false;
        }

        this.globalVariables.put(name, variable);
        return true;
    }

    /**
     * Tries to add a given variable into the latest local variables table
     *
     * @param name       Name of the variable to add table
     * @param variable   Variable object to add to the table
     * @return           Whether the insertion was successful
     */
    private boolean addLocalVariable(String name, Variable variable) {
        if (this.scopeVariables.getLast().containsKey(name)) {
            return false;
        }

        this.scopeVariables.getLast().put(name, variable);
        return true;
    }


    /**
     * Returns the variable matching to ${name} in the table
     *
     * @param name   Name of the variable to return
     * @return       Found variable or null otherwise
     */
    @Override
    public Variable get(String name) throws TableException {
        // Looks up for the variable in all inner scopes, and returns first match
        if (this.scopeVariables.size() > 0) {
            Iterator<Map<String, Variable>> scopeIterator = this.scopeVariables.descendingIterator();

            Map<String, Variable> currentScope;

            while (scopeIterator.hasNext()) {
                currentScope = scopeIterator.next();

                if (currentScope.containsKey(name)) {
                    return currentScope.get(name);
                }
            }
        }

        if (this.globalVariables.containsKey(name)) {
            return this.globalVariables.get(name);
        }

        throw new TableException(String.format(Messages.VARIABLE_NOT_FOUND, name));
    }
}