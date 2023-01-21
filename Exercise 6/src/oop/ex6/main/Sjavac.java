package oop.ex6.main;

import oop.ex6.checker.Checker;
import oop.ex6.checker.CheckerException;
import oop.ex6.parser.FileParser;

import java.io.IOException;

public class Sjavac {

    private static final String WRONG_ARGUMENT_NUMBER = "Wrong number of arguments entered";

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println(Result.IO_ERROR);
            System.err.println(WRONG_ARGUMENT_NUMBER);
            return;
        }

        String sourceFileName = args[0];

        try {
            FileParser fileParser = new FileParser(sourceFileName);
            new Checker(fileParser).check();

            System.out.println(Result.LEGAL.getCode());
        } catch (CheckerException exception) {
            System.out.println(Result.ILLEGAL.getCode());
            System.err.println(exception.getMessage());
        } catch (IOException exception) {
            System.out.println(Result.IO_ERROR.getCode());
            System.err.println(exception.getMessage());
        }
    }
}
