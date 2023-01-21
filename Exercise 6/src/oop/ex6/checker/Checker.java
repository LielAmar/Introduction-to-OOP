package oop.ex6.checker;

import oop.ex6.checker.parsers.LineParser;
import oop.ex6.checker.tables.subroutines.SubroutineTable;
import oop.ex6.checker.types.LineType;
import oop.ex6.checker.types.TypeException;
import oop.ex6.checker.types.Variable;
import oop.ex6.checker.tables.variables.VariableTable;
import oop.ex6.parser.FileParser;
import oop.ex6.utils.Messages;

/**
 * A class used to handle the whole process of checking whether given sjava code is valid
 */
public class Checker {

    private final VariableTable variables;
    private final SubroutineTable subroutines;

    private final FileParser fileParser;
    private final LineParser lineParser;

    public Checker(FileParser fileParser) throws CheckerException {
        this.variables = new VariableTable();
        this.subroutines = new SubroutineTable();

        this.fileParser = fileParser;
        this.lineParser = new LineParser(this.variables, this.subroutines);
    }


    /**
     * Checks if the given file is a legal, compilable, sjava file
     *
     * @throws CheckerException Throws an exception if the code is illegal
     */
    public void check() throws CheckerException {
        try {
            // Checks if all lines are legal
            this.fileParser.reset();
            this.legalCheck();

            // After the legal check, we can strip all lines
            this.fileParser.stripLines();

            // Checks global declarations
            this.fileParser.reset();
            this.globalCheck();

            // Thoroughly checks each line
            this.fileParser.reset();
            this.thoroughCheck();
        } catch (CheckerException exception) {
            throw new CheckerException(exception.getMessage(), exception);
        }
    }

    /**
     * Checks if all lines are of legal types
     *
     * @throws CheckerException Throws an exception if there was a problem with any line
     */
    private void legalCheck() throws CheckerException {
        while (this.fileParser.hasMoreLines()) {
            try {
                LineType.fromLine(this.fileParser.getCurrentLine());

                this.fileParser.advance();
            } catch (TypeException exception) {
                throw new CheckerException(exception.getMessage(), exception);
            }
        }
    }

    /**
     * Initializes the tables by looping over the entire code and parsing global variables and
     * subroutine declarations
     *
     * @throws CheckerException Throws an exception if there was a problem with any line
     */
    private void globalCheck() throws CheckerException {
        int scope = 0;

        // Loops over all lines
        while (this.fileParser.hasMoreLines()) {
            try {
                LineType lineType = LineType.fromLine(this.fileParser.getCurrentLine());

                // Updates the scope according to whether we have a line that is a block opener or closer
                if (LineType.isBlockOpenerLine(lineType)) {
                    this.variables.openScope();
                    scope++;
                } else if (LineType.isBlockCloserLine(lineType)) {
                    this.variables.closeScope();
                    scope--;
                }


                // Handles variable declaration lines in the global scope
                if (LineType.isVariableDeclarationLine(lineType) && scope == 0) {
                    this.lineParser.parseVariableDeclarationLine(this.fileParser.getCurrentLine());
                }

                // Handles variable assignment lines in the global scope
                else if (LineType.isVariableAssignmentLine(lineType) && scope == 0) {
                    this.lineParser.parseVariableAssignmentLine(this.fileParser.getCurrentLine());
                }

                // Handles subroutine declaration lines in the global scope
                else if (LineType.isSubroutineDeclarationLine(lineType) && scope == 1) {
                    this.lineParser.parseSubroutineDeclarationLine(this.fileParser.getCurrentLine());
                }

                // Advances to next line
                this.fileParser.advance();
            } catch (CheckerException exception) {
                throw new CheckerException(exception.getMessage(), exception);
            }
        }

        // Loops over all initialized global variables, and sets them to initialized-globally.
        this.variables.getGlobalVariable()
                .stream()
                .filter(Variable::isInitialized)
                .forEach(Variable::initializeGlobally);
    }


    /**
     * Checks if the file is legal by looping over the entire code and handling each
     * line type separately, through the line parser.
     *
     * @throws CheckerException Throws an exception if there was a problem with any line
     */
    public void thoroughCheck() throws CheckerException {
        int scope = 0;

        while (this.fileParser.hasMoreLines()) {
            try {
                LineType lineType = LineType.fromLine(this.fileParser.getCurrentLine());

                // Updates the scope according to whether we have a line that is a block opener or closer
                if (LineType.isBlockOpenerLine(lineType)) {
                    this.variables.openScope();
                    scope++;
                } else if (LineType.isBlockCloserLine(lineType)) {
                    this.variables.closeScope();
                    scope--;
                }

                this.parseCurrentLine(scope, lineType);
            } catch (CheckerException exception) {
                throw new CheckerException(exception.getMessage(), exception);
            }
        }

        // Checks if all scopes were closed
        if (scope != 0) {
            throw new CheckerException(Messages.SCOPE_NOT_CLOSED);
        }
    }

    /**
     * Parses a single line
     *
     * @param scope               Current scope
     * @param lineType            Type of the current line parsed
     *
     * @throws CheckerException   Throws an exception if it couldn't parse the line
     */
    private void parseCurrentLine(int scope, LineType lineType) throws CheckerException {
        // Handles variable declaration lines in local scopes
        if (LineType.isVariableDeclarationLine(lineType) && scope >= 1) {
            this.lineParser.parseVariableDeclarationLine(this.fileParser.getCurrentLine());
        }

        // Handles variable assignment lines in local scopes
        else if (LineType.isVariableAssignmentLine(lineType) && scope >= 1) {
            this.lineParser.parseVariableAssignmentLine(this.fileParser.getCurrentLine());
        }

        // Handles subroutine declaration lines in the global scope
        else if (LineType.isSubroutineDeclarationLine(lineType) && scope == 1) {
            this.lineParser.parseSubroutineStartLine(this.fileParser.getCurrentLine());
        }

        // Handles subroutine call lines in all scopes
        else if (LineType.isSubroutineCallLine(lineType) && scope >= 0) {
            this.lineParser.parseSubroutineCall(this.fileParser.getCurrentLine());
        }

        // Handles block close lines of a subroutine
        else if (LineType.isBlockCloserLine(lineType) && scope == 0) {
            this.lineParser.parseSubroutineCloseLine(this.fileParser.getPreviousLine());
        }

        // Handles if & while conditional lines in local scopes
        else if (LineType.isConditionalLine(lineType) && scope >= 1) {
            this.lineParser.parseConditionalLine(this.fileParser.getCurrentLine());
        }

        // Advances to next line
        this.fileParser.advance();
    }
}
