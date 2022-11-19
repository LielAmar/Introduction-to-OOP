import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * <B>Tests for the Board Class,</B>
 * featured in Exercise 2 of the new "Introduction to OOP" course,
 * HUJI, Winter 2021-2022 Semester.
 *
 * @author Erel Debel.
 */
public class BoardTest {
	private Board board;
	private final static int GIVEN_BOARD_SIZE = 8;
	private final static int DEFAULT_BOARD_SIZE = 4;



	/**
	 * Checks that a board with given size is created with all marks Blank.
	 */
	@Test
	void checkInitializationWithGivenSize() {
		board = new Board(GIVEN_BOARD_SIZE);
		for (int row = 0; row < board.getSize(); row++) {
			for (int col = 0; col < board.getSize(); col++) {
				assertEquals(Mark.BLANK, board.getMark(row, col));
			}
		}
		assertEquals(board.getSize(), GIVEN_BOARD_SIZE);
	}

	/**
	 * Checks that a board is created with default size with all marks Blank.
	 */
	@Test
	void checkInitializationWithDefaultSize() {
		board = new Board();
		for (int row = 0; row < board.getSize(); row++) {
			for (int col = 0; col < board.getSize(); col++) {
				assertEquals(Mark.BLANK, board.getMark(row, col));
			}
		}
		assertEquals(board.getSize(), DEFAULT_BOARD_SIZE);
	}

	/**
	 * Checks the PutMark and GetMark work using the same indices.
	 */
	@Test
	void checkPutMarkAndGetMarkSynchronization() {
		board = new Board();
		Mark[] marks = {Mark.X, Mark.O};
		for (int row = 0; row < board.getSize(); row++) {
			for (int col = 0; col < board.getSize(); col++) {
				board.putMark(marks[(row + 3 * col) % 2], row, col);
			}
		}
		for (int row = 0; row < board.getSize(); row++) {
			for (int col = 0; col < board.getSize(); col++) {
				assertEquals(marks[(row + 3 * col) % 2], board.getMark(row, col));
			}
		}
	}

	/**
	 * Checks that you can't override a placed mark.
	 */
	@Test
	void checkMarksCanNotChange() {
		board = new Board();
		assertTrue(board.putMark(Mark.X, 1, 0));
		assertFalse(board.putMark(Mark.X, 1, 0));
		assertFalse(board.putMark(Mark.O, 1, 0));
		assertEquals(Mark.X, board.getMark(1, 0));
		assertTrue(board.putMark(Mark.O, 0, 1));
		assertFalse(board.putMark(Mark.O, 0, 1));
		assertFalse(board.putMark(Mark.X, 0, 1));
		assertEquals(Mark.O, board.getMark(0, 1));
	}

	/**
	 * Checks that putMark returns false for coordinates out of range and does not mark them.
	 */
	@Test
	void checkPutAndGetMarkOutOfRange() {
		checkPutMarkOutOfRangeWithMark(Mark.X);
		checkPutMarkOutOfRangeWithMark(Mark.O);
	}

	private void checkPutMarkOutOfRangeWithMark(Mark mark) {
		board = new Board();
		assertFalse(board.putMark(mark, -1, 0));
		assertFalse(board.putMark(mark, -2, 0));
		assertFalse(board.putMark(mark, 0, -1));
		assertFalse(board.putMark(mark, 0, -2));
		assertFalse(board.putMark(mark, -1, -1));
		assertFalse(board.putMark(mark, board.getSize(), 0));
		assertFalse(board.putMark(mark, board.getSize() + 1, 0));
		assertFalse(board.putMark(mark, 0, board.getSize()));
		assertFalse(board.putMark(mark, 0, board.getSize() + 1));
		assertFalse(board.putMark(mark, board.getSize(), board.getSize()));
		assertFalse(board.putMark(mark, -1, board.getSize()));
		assertFalse(board.putMark(mark, board.getSize(), -1));

		assertEquals(Mark.BLANK, board.getMark(-1, 0));
		assertEquals(Mark.BLANK, board.getMark(-2, 0));
		assertEquals(Mark.BLANK, board.getMark(0, -1));
		assertEquals(Mark.BLANK, board.getMark(0, -2));
		assertEquals(Mark.BLANK, board.getMark(-1, -1));
		assertEquals(Mark.BLANK, board.getMark(board.getSize(), 0));
		assertEquals(Mark.BLANK, board.getMark(board.getSize() + 1, 0));
		assertEquals(Mark.BLANK, board.getMark(0, board.getSize()));
		assertEquals(Mark.BLANK, board.getMark(0, board.getSize() + 1));
		assertEquals(Mark.BLANK, board.getMark(board.getSize(), board.getSize()));
		assertEquals(Mark.BLANK, board.getMark(-1, board.getSize()));
		assertEquals(Mark.BLANK, board.getMark(board.getSize(), -1));
	}

}