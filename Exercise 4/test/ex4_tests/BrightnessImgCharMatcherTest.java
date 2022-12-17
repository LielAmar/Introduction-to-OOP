package ex4_tests;

import ascii_art.img_to_char.BrightnessImgCharMatcher;
import ascii_output.HtmlAsciiOutput;
import image.Image;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

/**
 * <B>Tests for the BrightnessCharMatcher Class,</B>
 * featured in Exercise 4 part 1 of the new "Introduction to OOP" course,
 * HUJI, Winter 2021-2022 Semester.
 *
 * @author Erel Debel.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BrightnessImgCharMatcherTest {

	/* The path of the test directory containing "ex4_tests", "output", "compare" and the images. */
	protected static final String TESTS_DIR = "test/";

	/* The font in use. this is the standard monospaced font in windows. */
	protected static final String FONT = "Courier New";

	private static final String OUTPUT_DIR = "output/";
	protected static final String COMPARE_DIR = "compare/";
	private static final String IMAGES_DIR = "images/";

	protected static final String JPEG = ".jpeg";
	protected static final String HTML = ".html";
	private static final String MARIO = "mario";
	private static final String BOARD = "board";
	private static final String LANDSCAPE = "landscape";

	/* The characters in use. Each should produce a different brightness value. */
	protected static final Character[] charSet = {'#', 'M', 'n', 'e', 'J', 'c', '_', 'W', '@', 'E', 'B', 'g',
			'A', 'G', 'w', 'y', ',', '.', '`', ' ', 'i', 'o', '+', '*', '"'};

	private static final String MESSAGE = "Line mismatch between your output (in the output directory) and " +
			"the reference (in the 'compare' directory) at the returned line index.";
	private static final String ERROR = "The files couldn't be opened. check that the TEST_DIR is the " +
			"correct relative path from the project directory or the correct absolute path to test";


	/**
	 * Test the chooseChars output for a small board image.
	 *
	 * @throws IOException If there was an error opening the files.
	 */
	@Test
	@Order(1)
	void chooseCharsSmallTest() throws IOException {
		checkResult(BOARD, 4);
		checkResult(BOARD, 16);
		checkResult(BOARD, 64);
	}

	/**
	 * Test the chooseChars output for a large landscape image.
	 *
	 * @throws IOException If there was an error opening the files.
	 */
	@Test
	@Order(3)
	void chooseCharsLargeTest() throws IOException {
		checkResult(LANDSCAPE, 512);
		checkResult(LANDSCAPE, 1024);
		checkResult(LANDSCAPE, 2048);
		checkResult(LANDSCAPE, 4096);
	}

	/* Call chooseChar and assert the comparison returned with no errors. */
	private void checkResult(String image, int numCharsInRow) throws IOException {
		assertEquals(0, CompareOutputWithPicture(image, numCharsInRow), MESSAGE);
		assertEquals(0, CompareOutputWithPicture(image, numCharsInRow), MESSAGE);
	}

	/* Runs chooseChar and checks that the output is identical to the compare file. */
	protected long CompareOutputWithPicture(String imageName, int numCharsInRow) throws IOException {
		Image img = Image.fromFile(TESTS_DIR + IMAGES_DIR + imageName + JPEG);
		BrightnessImgCharMatcher charMatcher = new BrightnessImgCharMatcher(img, FONT);
		HtmlAsciiOutput asciiOutput =
				new HtmlAsciiOutput(TESTS_DIR + OUTPUT_DIR + imageName + numCharsInRow + HTML, FONT);
		char[][] chars = charMatcher.chooseChars(numCharsInRow, charSet);
		asciiOutput.output(chars);
		return compareFilesByLine(
				Paths.get(TESTS_DIR + COMPARE_DIR + imageName + numCharsInRow + HTML),
				Paths.get(TESTS_DIR + OUTPUT_DIR + imageName + numCharsInRow + HTML)
		);
	}

	/* Compares 2 text files. if they are identical returns 0, else returns the index of the first different
	   line. */
	protected static long compareFilesByLine(Path comparePath, Path outputPath) throws IOException {
		try (
				BufferedReader compareReader = Files.newBufferedReader(comparePath);
				BufferedReader outputReader = Files.newBufferedReader(outputPath)
		) {
			long lineNumber = 1;
			String line1 = "", line2 = "";
			while ((line1 = outputReader.readLine()) != null) {
				line2 = compareReader.readLine();
				if (!line1.equals(line2))
					return lineNumber;
				lineNumber++;
			}
			if (compareReader.readLine() == null)
				return 0;
			return lineNumber;
		} catch (IOException e) {
			throw new IOException(ERROR, e);
		}
	}
}
