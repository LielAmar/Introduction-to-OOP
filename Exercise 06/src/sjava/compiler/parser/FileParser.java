package oop.ex6.parser;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A class that parses a line into an iterable list
 */
public class FileParser {

    private static final String FILE_NOT_FOUND_MESSAGE = "Couldn't find source file %s!";
    private static final String IO_ERROR_MESSAGE = "An error occurred while reading file %s!";

    private final List<String> lines;

    private int currentLineIndex;

    /**
     * Receives a file to read and parse
     *
     * @param sourceFile File to parse
     * @throws IOException Might throw an IOException of parse failed for any reason
     */
    public FileParser(String sourceFile) throws IOException {
        this.lines = new ArrayList<>();

        this.currentLineIndex = 0;

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(sourceFile))) {
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                // Splitting lines by \n and \r
                this.lines.addAll(Arrays.asList(line.split("[\n\r]")));
            }
        } catch (FileNotFoundException exception) {
            throw new FileNotFoundException(String.format(FILE_NOT_FOUND_MESSAGE, sourceFile));
        } catch (IOException exception) {
            throw new IOException(String.format(IO_ERROR_MESSAGE, sourceFile));
        }
    }


    /**
     * Strips all the lines in the file
     */
    public void stripLines() {
        for(int i = 0; i < lines.size(); i++) {
            this.lines.set(i, this.lines.get(i).strip());
        }
    }


    /**
     * Returns whether we have more lines to go throw in the file
     *
     * @return Whether there are more lines to read
     */
    public boolean hasMoreLines() {
        return (this.currentLineIndex < this.lines.size());
    }

    /**
     * Advances the file to the next line
     */
    public void advance() {
        this.currentLineIndex++;
    }

    /**
     * Resets the line pointer back to the first line
     */
    public void reset() {
        this.currentLineIndex = 0;
    }


    /**
     * Returns the current line in the file
     *
     * @return Current line
     */
    public String getCurrentLine() {
        if ((this.currentLineIndex >= this.lines.size()) || (this.currentLineIndex < 0)) {
            return null;
        }

        return this.lines.get(this.currentLineIndex);
    }

    /**
     * Returns the previous line in the file
     *
     * @return Previous line
     */
    public String getPreviousLine() {
        if (this.currentLineIndex <= 0) {
            return null;
        }

        return this.lines.get(this.currentLineIndex - 1);
    }
}
