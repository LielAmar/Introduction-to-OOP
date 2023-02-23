package oop.ex6.checker.tables.subroutines;

import oop.ex6.checker.tables.ITable;
import oop.ex6.checker.tables.TableException;
import oop.ex6.checker.types.Subroutine;
import oop.ex6.utils.Messages;

import java.util.HashMap;
import java.util.Map;

/**
 * A class used to store all subroutines used within a sjava program
 */
public class SubroutineTable implements ITable<Subroutine> {

    private final Map<String, Subroutine> subroutines;

    public SubroutineTable() {
        this.subroutines = new HashMap<>();
    }


    /**
     * Tries to add a subroutine object to the table
     *
     * @param name              Name of the subroutine
     * @param subroutine        Subroutine to add
     * @throws TableException   Throws an exception if it couldn't add subroutine to the table
     */
    @Override
    public void add(String name, Subroutine subroutine) throws TableException {
        if (this.subroutines.containsKey(name)) {
            throw new TableException(String.format(Messages.SUBROUTINE_ALREADY_EXISTS, name));
        }

        this.subroutines.put(name, subroutine);
    }


    /**
     * Returns the subroutine matching to ${name} in the table
     *
     * @param name   Name of the subroutine to return
     * @return       Found subroutine or null otherwise
     */
    @Override
    public Subroutine get(String name) throws TableException {
        if (this.subroutines.containsKey(name)) {
            return this.subroutines.get(name);
        }

        throw new TableException(String.format(Messages.SUBROUTINE_NOT_FOUND, name));
    }
}