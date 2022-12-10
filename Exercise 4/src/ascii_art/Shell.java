package ascii_art;

import ascii_art.img_to_char.BrightnessImgCharMatcher;
import ascii_output.ConsoleAsciiOutput;
import ascii_output.HtmlAsciiOutput;
import image.Image;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

/**
 * This class is used to handle shell commands for rendering ascii images
 */
public class Shell {

    private static final String COMMAND_PREFIX = "<<< ";
    private static final String EXIT_STRING = "exit";
    private static final String CHARS_COMMAND = "chars";
    private static final String CONSOLE_COMMAND = "console";
    private static final String RENDER_COMMAND = "render";
    private static final String ADD_COMMAND = "add";
    private static final String REMOVE_COMMAND = "remove";
    private static final String RES_COMMAND = "res";

    private static final String SPACE_KEYWORD = "space";
    private static final String ALL_KEYWORD = "all";
    private static final String UP_KEYWORD = "up";
    private static final String DOWN_KEYWORD = "down";

    private static final String ADD_ERROR_MESSAGE = "Did not add due to incorrect format";
    private static final String REMOVE_ERROR_MESSAGE = "Did not remove due to incorrect format";
    private static final String WIDTH_CHANGE_MESSAGE = "Width set to %d";
    private static final String WIDTH_CHANGE_ERROR_MESSAGE = "Did not change due to exceeding boundaries";
    private static final String INCORRECT_COMMAND_MESSAGE = "Did not executed due to incorrect command";

    private static final int MIN_PIXELS_PER_CHAR = 2;
    private static final int INITIAL_CHARS_IN_ROW = 64;

    private static final String DEFAULT_HTML_FILE_NAME = "./out.html";
    private static final String DEFAULT_FONT = "Courier New";

    private final Scanner scanner;

    private final BrightnessImgCharMatcher charMatcher;
    private final Set<Character> allowedCharacters;

    private final ConsoleAsciiOutput consoleAsciiOutput;
    private final HtmlAsciiOutput htmlAsciiOutput;

    private final int minCharsInRow;
    private final int maxCharsInRow;

    private int charsInRow;
    private boolean usingConsole;

    public Shell(Image image) {
        this.scanner = new Scanner(System.in);

        this.charMatcher = new BrightnessImgCharMatcher(image, DEFAULT_FONT);
        this.allowedCharacters = new HashSet<>();

        this.consoleAsciiOutput = new ConsoleAsciiOutput();
        this.htmlAsciiOutput = new HtmlAsciiOutput(DEFAULT_HTML_FILE_NAME, DEFAULT_FONT);

        this.minCharsInRow = Math.max(1, image.getWidth() / image.getHeight());
        this.maxCharsInRow = image.getWidth() / MIN_PIXELS_PER_CHAR;

        this.charsInRow = Math.max(Math.min(INITIAL_CHARS_IN_ROW, this.maxCharsInRow), this.minCharsInRow);
        this.usingConsole = false;
    }

    /**
     * Runs the Shell program manager and listens to user commands for as long as
     * an exit command is not encountered
     */
    public void run() {
        String command;

        do {
            System.out.print(COMMAND_PREFIX);
            command = this.scanner.nextLine();

            if(command.equals(CHARS_COMMAND)) {
                this.run_chars();
                continue;
            }

            if(command.equals(CONSOLE_COMMAND)) {
                this.run_console();
                continue;
            }

            if(command.equals(RENDER_COMMAND)) {
                this.run_render();
                continue;
            }

            String[] splitCommand = command.split(" ");

            if(splitCommand.length != 1) {
                if (splitCommand[0].equals(ADD_COMMAND)) {
                    this.run_set_command(splitCommand[1], this.allowedCharacters::add, ADD_ERROR_MESSAGE);
                    continue;
                }

                if (splitCommand[0].equals(REMOVE_COMMAND)) {
                    this.run_set_command(splitCommand[1], this.allowedCharacters::remove,
                            REMOVE_ERROR_MESSAGE);
                    continue;
                }

                if (splitCommand[0].equals(RES_COMMAND)) {
                    if (this.run_res(splitCommand[1])) {
                        continue;
                    }
                }
            }

            System.out.println(INCORRECT_COMMAND_MESSAGE);
        } while(!command.equals(EXIT_STRING));
    }

    /**
     * Prints all characters that can be used within our program.
     */
    private void run_chars() {
        for(Character character : this.allowedCharacters) {
            System.out.print(character + " ");
        }

        System.out.println();
    }

    /**
     * Switches to console print mode
     */
    private void run_console() {
        this.usingConsole = true;
    }

    /**
     * Renders the image at the current resolution with current characters
     */
    private void run_render() {
        char[][] output = charMatcher.chooseChars(this.charsInRow,
                this.allowedCharacters.toArray(new Character[]{}));

        if(this.usingConsole) {
            this.consoleAsciiOutput.output(output);
        } else {
            this.htmlAsciiOutput.output(output);
        }
    }

    /**
     * Runs a set command on a character
     *
     * @param characters   Character(s) to run the command on
     * @param callback     Command callback to execute for every character
     * @param errorMessage Message to be sent when encountering an error
     */
    private void run_set_command(String characters, SetCommand callback, String errorMessage) {
        if(characters.length() == 1) {
            callback.run(characters.charAt(0));
        } else if(characters.equals(SPACE_KEYWORD)) {
            callback.run(' ');
        } else if(characters.equals(ALL_KEYWORD)) {
            for(int i = 32; i < 127; i++) {
                callback.run((char) i);
            }
        } else if(characters.length() == 3 && characters.charAt(1) == '-') {
            char startingChar = characters.charAt(0);
            char endingChar = characters.charAt(2);

            // If the range startingChar comes after endingChar, swap them
            if(startingChar > endingChar) {
                char tmp = startingChar;
                startingChar = endingChar;
                endingChar = tmp;
            }

            while(startingChar != (endingChar + 1)) {
                callback.run(startingChar);

                startingChar++;
            }
        } else {
            System.out.println(errorMessage);
        }
    }

    /**
     * Changes the resolution of the image
     *
     * @param command   Resolution command to execute
     * @return          Whether the command was valid
     */
    private boolean run_res(String command) {
        if(command.equals(UP_KEYWORD)) {
            if(this.charsInRow * 2 < this.maxCharsInRow) {
                this.charsInRow *= 2;
                System.out.printf((WIDTH_CHANGE_MESSAGE) + "%n", this.charsInRow);
            } else {
                System.out.println(WIDTH_CHANGE_ERROR_MESSAGE);
            }

            return true;
        } else if(command.equals(DOWN_KEYWORD)) {
            if(this.charsInRow / 2 > this.minCharsInRow) {
                this.charsInRow /= 2;
                System.out.printf((WIDTH_CHANGE_MESSAGE) + "%n", this.charsInRow);
            } else {
                System.out.println(WIDTH_CHANGE_ERROR_MESSAGE);
            }

            return true;
        }

        return false;
    }

    /**
     * An interface used for lambdas
     */
    private interface SetCommand {

        void run(char character);

    }
}
