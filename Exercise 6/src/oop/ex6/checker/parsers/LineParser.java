package oop.ex6.checker.parsers;

import oop.ex6.checker.tables.TableException;
import oop.ex6.checker.tables.subroutines.SubroutineTable;
import oop.ex6.checker.tables.variables.VariableTable;
import oop.ex6.checker.types.*;
import oop.ex6.utils.Constants;
import oop.ex6.utils.Messages;
import oop.ex6.utils.Pair;

import java.util.*;
import java.util.function.Predicate;

/**
 * A class that helps parsing lines within the sjava code
 */
public class LineParser {

    private final VariableTable variables;
    private final SubroutineTable subroutines;

    public LineParser(VariableTable variables, SubroutineTable subroutines) {
        this.variables = variables;
        this.subroutines = subroutines;
    }


    /**
     * Parses a variable declaration line and adds all declared variables into the variables table
     * - Assumes ${line} is indeed of type VariableDeclaration
     *
     * @param line Line to parse
     * @throws ParseException Throws an exception if the parsing process have failed
     */
    public void parseVariableDeclarationLine(String line) throws ParseException {
        // Splits the line by declarations, while removing commas
        // Example: "final  int  a = b, c = -5, e = 5"   ->  [final  int  a=b, c="1 2", e=5]
        String[] declarations = line
                .replaceAll("\\s*=\\s*", "=")
                .replaceAll("\\s*;\\s*", "")
                .split("(\\s*,\\s*)");

        // Saving variable data (final, type, etc.) which are stored in the first declaration,
        // and then fixing first declarations
        // Example: "final    int   a=b"   ->   [final, int] [a=b, ...]
        String[] variableData = declarations[0].split("\\s+");

        declarations[0] = declarations[0].replaceFirst(
                "\\s*(" + Constants.FINAL_KEYWORD + "\\s)?\\s*\\w+\\s*", "");

        boolean isFinal;
        VariableType variableType;

        try {
            isFinal = variableData[0].equals(Constants.FINAL_KEYWORD);
            variableType = VariableType.fromKeyword(isFinal ? variableData[1] : variableData[0]);
        } catch (TypeException exception) {
            throw new ParseException(
                    String.format(Messages.ILLEGAL_VARIABLE_DECLARATION_PARSE, line), exception);
        }

        // Loops over all declarations, creates variables and adds them to the table
        for (String declaration : declarations) {
            this.declareVariable(declaration, variableType, isFinal);
        }
    }

    /**
     * Parses a variable assignment line
     * - Assumes ${line} is indeed of type VariableAssignment
     *
     * @param line Line to parse
     * @throws ParseException Throws an exception if the parsing process have failed
     */
    public void parseVariableAssignmentLine(String line) throws ParseException {
        // Splits the line by declarations, while removing commas
        // Example: "a = b, c = -5, e = 5"   ->  [a=b, c="1 2", e=5]
        String[] assignments = line
                .replaceAll("\\s*=\\s*", "=")
                .replaceAll("\\s*;\\s*", "")
                .split("(\\s*,\\s*)");

        // Loops over every declaration and tries to initialize the variable
        for (String assignment : assignments) {
            this.assignVariable(assignment);
        }
    }

    /**
     * Declares a single variable from a declaration string:
     * Create a variable object and add it to the variables table
     *
     * @param declaration  Declaration string to create variable from
     * @param variableType Type of the variable to create
     * @param isFinal      Whether the created variable is final
     * @throws ParseException Throws an exception if it couldn't add variable to the table
     */
    private void declareVariable(String declaration, VariableType variableType,
                                 boolean isFinal) throws ParseException {

        String name = declaration.split("=")[0];
        boolean initialize = declaration.contains("=");

        // Tries to create a variable object, initialize it and add it to the table
        try {
            Variable variable = new Variable(variableType, false, initialize);

            // If the variable is initialized, make sure we have a legal initialization
            if (initialize) {
                String value = declaration.split("=")[1];
                VariableType valueType = VariableType.fromExpression(value);

                if (valueType == VariableType.IDENTIFIER) {
                    variable.initialize(this.variables.get(value));
                } else {
                    variable.initialize(valueType);
                }
            }

            variable.setFinal(isFinal);

            this.variables.add(name, variable);
        } catch (TypeException exception) {
            throw new ParseException(String.format(Messages.ILLEGAL_VARIABLE_VALUE, name), exception);
        } catch (TableException exception) {
            throw new ParseException(exception.getMessage(), exception);
        }
    }

    /**
     * Assigns a single variable from an assignment string
     *
     * @param assignment Assignment string to assign variable from
     * @throws ParseException Throws an exception if it couldn't initialize variable
     */
    private void assignVariable(String assignment) throws ParseException {
        try {
            String assigned = assignment.split("=")[0];
            String value = assignment.split("=")[1];

            Variable variable = this.variables.get(assigned);

            VariableType assignmentType = VariableType.fromExpression(value);

            if (assignmentType == VariableType.IDENTIFIER) {
                variable.initialize(this.variables.get(value));
            } else {
                variable.initialize(assignmentType);
            }
        } catch (TypeException | TableException exception) {
            throw new ParseException(exception.getMessage(), exception);
        }
    }


    /**
     * Parses a subroutine declaration line and adds it to the subroutines table
     * - Assumes ${line} is indeed of type SubroutineDeclaration
     *
     * @param line Line to parse
     * @throws ParseException Throws an exception if the parsing process have failed
     */
    public void parseSubroutineDeclarationLine(String line) throws ParseException {
        // Splits the line into all arguments, removing unnecessary spaces, commas and ';'
        // Example: "void foo(int a, int b) {"   ->   [void foo, int a, int b]
        String[] arguments = line
                .replaceAll("\\s+", " ")
                .split("(\\s*,\\s*|\\s*\\(\\s*|\\s*\\)\\s*\\{\\s*)");

        // Saving subroutine data (return type, name) which are stored in the first argument
        String subroutineName;
        ReturnType returnType;

        try {
            subroutineName = arguments[0].split(" ")[1];
            returnType = ReturnType.fromKeyword(arguments[0].split(" ")[0]);
        } catch (TypeException exception) {
            throw new ParseException(
                    String.format(Messages.ILLEGAL_SUBROUTINE_RETURN_TYPE, line), exception);
        }

        // Parses all parameters of the subroutine
        List<Pair<String, Variable>> parameters =
                this.parseParameterList(Arrays.copyOfRange(arguments, 1, arguments.length));

        // Adds subroutine to the subroutines table
        try {
            Subroutine subroutine = new Subroutine(returnType, parameters);

            this.subroutines.add(subroutineName, subroutine);
        } catch (TableException exception) {
            throw new ParseException(exception.getMessage(), exception);
        }
    }

    /**
     * Parses a subroutine start line
     * - Assumes ${line} is indeed of type SubroutineStart
     *
     * @param line Line to parse
     * @throws ParseException Throws an exception if the parsing process have failed
     */
    public void parseSubroutineStartLine(String line) throws ParseException {
        try {
            String subroutineName = line.split("\\(")[0].split("\\s+")[1];

            Subroutine subroutine = this.subroutines.get(subroutineName);

            for (Pair<String, Variable> parameter : subroutine.getParameters()) {
                Variable variable = new Variable(
                        parameter.getValue().getType(),
                        parameter.getValue().isFinal(),
                        parameter.getValue().isInitialized()
                );

                this.variables.add(parameter.getKey(), variable);
            }
        } catch (TableException exception) {
            throw new ParseException(exception.getMessage(), exception);
        }
    }

    /**
     * Parses a subroutine close line
     * - Assumes ${line} is indeed of type SubroutineClose
     *
     * @param previousLine Previous line
     * @throws ParseException Throws an exception if the parsing process have failed
     */
    public void parseSubroutineCloseLine(String previousLine) throws ParseException {
        try {
            LineType previousLineType = LineType.fromLine(previousLine);

            if (previousLineType != LineType.RETURN) {
                throw new ParseException(Messages.ILLEGAL_SUBROUTINE_CLOSE);
            }

            // Loops over all globally-uninitialized variables, and uninitializes them.
            this.variables.getGlobalVariable()
                    .stream()
                    .filter(Predicate.not(Variable::isGloballyInitialized))
                    .forEach(Variable::uninitialize);
        } catch (TypeException exception) {
            throw new ParseException(String.format(Messages.ILLEGAL_LINE_TYPE, previousLine), exception);
        }
    }

    /**
     * Parses a subroutine call line
     * - Assumes ${line} is indeed of type SubroutineCall
     *
     * @param line Line to parse
     * @throws ParseException Throws an exception if the parsing process have failed
     */
    public void parseSubroutineCall(String line) throws ParseException {
        // Splits the line by conditions, while removing commas, brackets and keywords
        // Example: "foo(1,a,b,'c'); "   ->  [foo, 1, a, b, 'c']
        String[] arguments = line.split("(\\s*\\(\\s*|\\s*,\\s*|\\s*\\)\\s*;\\s*)");

        String subroutineName = arguments[0];

        try {
            Subroutine subroutine = this.subroutines.get(subroutineName);

            this.checkCallArguments(subroutine,
                    Arrays.copyOfRange(arguments, 1, arguments.length));
        } catch (TableException exception) {
            throw new ParseException(exception.getMessage(), exception);
        }
    }

    /**
     * Parses an array of arguments into a list of parameters
     *
     * @param arguments Subroutine's arguments to parse
     * @return A list of parameters the subroutine accepts
     * @throws ParseException Throws an exception if it couldn't parse arguments
     */
    private List<Pair<String, Variable>> parseParameterList(String[] arguments) throws ParseException {
        List<Pair<String, Variable>> parameters = new ArrayList<>();

        for (String argument : arguments) {
            String[] variableData = argument.split(" ");

            boolean isFinal;
            VariableType variableType;
            String variableName;

            try {
                if (variableData[0].equals(Constants.FINAL_KEYWORD)) {
                    isFinal = true;
                    variableType = VariableType.fromKeyword(variableData[1]);
                    variableName = variableData[2];
                } else {
                    isFinal = false;
                    variableType = VariableType.fromKeyword(variableData[0]);
                    variableName = variableData[1];
                }
            } catch (TypeException exception) {
                throw new ParseException(
                        String.format(Messages.ILLEGAL_SUBROUTINE_PARAMETER, argument), exception);
            }

            parameters.add(new Pair<>(variableName, new Variable(variableType, isFinal, true)));
        }

        return parameters;
    }

    /**
     * Checks if all arguments passed to a subroutine are correct
     *
     * @param subroutine Subroutine to check with
     * @param arguments  Passed arguments
     * @throws ParseException Throws an exception if there was a problem with any argument
     */
    private void checkCallArguments(Subroutine subroutine, String[] arguments) throws ParseException {
        // If we don't have as many arguments as the subroutine demands
        if (arguments.length != subroutine.getParameters().size()) {
            throw new ParseException(Messages.INVALID_AMOUNT_OF_ARGUMENTS);
        }

        // Loops over every parameter and makes sure it matches the expected parameter in the subroutine
        for (int i = 0; i < arguments.length; i++) {
            try {
                Variable parameter = subroutine.getParameters().get(i).getValue();

                VariableType argumentType = VariableType.fromExpression(arguments[i]);

                if (argumentType == VariableType.IDENTIFIER) {
                    Variable argument = this.variables.get(arguments[i]);

                    if (!argument.isInitialized()) {
                        throw new ParseException(Messages.UNINITIALIZED_ARGUMENT_CALL);
                    }

                    argumentType = argument.getType();
                }

                if (!parameter.getType().canAccept(argumentType)) {
                    throw new ParseException(Messages.INVALID_ARGUMENT_TYPE);
                }
            } catch (TypeException exception) {
                throw new ParseException(
                        String.format(Messages.ILLEGAL_VARIABLE_TYPE, arguments[i]), exception);
            } catch (TableException exception) {
                throw new ParseException(exception.getMessage(), exception);
            }
        }
    }


    /**
     * Parses a conditional line
     * - Assumes ${line} is indeed of type ConditionalLine
     *
     * @param line Line to parse
     * @throws ParseException Throws an exception if the parsing process have failed
     */
    public void parseConditionalLine(String line) throws ParseException {
        // Splits the line by conditions, while removing commas, brackets and keywords
        // Example: "while(true && false && a || b) {"   ->  [true, false, a, b]
        String[] conditions = line
                .replaceAll("(\\s*if\\s*|\\s*while\\s*|\\s*\\(\\s*|\\s*\\)\\s*|\\s*\\{\\s*)",
                        "")
                .split("\\s*\\|\\|\\s*|\\s*&&\\s*");

        this.checkConditions(conditions);
    }

    /**
     * Checks all conditions within a conditional line
     *
     * @param conditions Conditions to check
     * @throws ParseException Throws an exception if any condition was invalid
     */
    private void checkConditions(String[] conditions) throws ParseException {
        for (String condition : conditions) {
            try {
                VariableType variableType = VariableType.fromExpression(condition);

                if (variableType == VariableType.IDENTIFIER) {
                    Variable variable = variables.get(condition);

                    if (!VariableType.BOOLEAN.canAccept(variable.getType())) {
                        throw new ParseException(
                                String.format(Messages.ILLEGAL_CONDITION, condition));
                    } else if (!variable.isInitialized()) {
                        throw new ParseException(
                                String.format(Messages.UNINITIALIZED_VARIABLE_IN_CONDITION, condition));
                    }
                } else if (!VariableType.BOOLEAN.canAccept(variableType)) {
                    throw new ParseException(
                            String.format(Messages.ILLEGAL_CONDITION, condition));
                }
            } catch (TypeException exception) {
                throw new ParseException(
                        String.format(Messages.ILLEGAL_VARIABLE_TYPE, condition), exception);
            } catch (TableException exception) {
                throw new ParseException(exception.getMessage(), exception);
            }
        }
    }
}
