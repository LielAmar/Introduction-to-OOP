import oop.ex6.checker.Checker;
import oop.ex6.checker.CheckerException;
import oop.ex6.main.Result;
import oop.ex6.parser.FileParser;

import java.io.File;
import java.io.IOException;

public class TestsRunner {

    public static void main(String[] args) throws java.lang.InterruptedException {
        File file = new File("./gal_test/test/tests");

        if(file.isDirectory())     {
            for(File test : file.listFiles()) {
                TestsRunner.runTest(test);
                Thread.sleep(100);
            }
        }
    }

    public static void runTest(File test) {
        try {
            FileParser fileParser = new FileParser(test.getPath());
            Checker checker = new Checker(fileParser);
            checker.check();

            System.out.println(test.getName().replaceAll("\\.sjava", "") + " 0");

//            System.out.println(test.getName() + " finished with: " + Result.LEGAL.getCode());
        } catch(CheckerException exception) {
            System.out.println(test.getName().replaceAll("\\.sjava", "") + " 1");
        } catch (IOException exception) {
            System.out.println(test.getName().replaceAll("\\.sjava", "") + " 2");
        }

    }
}
