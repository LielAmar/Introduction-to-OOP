import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * <B>Tests for the WhateverPlayer Class,</B>
 * featured in Exercise 2 of the new "Introduction to OOP" course,
 * HUJI, Winter 2021-2022 Semester.
 *
 * @author Erel Debel.
 */
class WhateverPlayerTest {
	/**
	 * expected number of marks in every cell.
	 */
	public static final int EXPECTED = 1000;

	/**
	 * distribution wiggle room.
	 */
	public static final int EPSILON = 100;
	Board board = new Board();
	private final int[][] boardMatrix = new int[board.getSize()][board.getSize()];
	private int emptyCells = board.getSize() * board.getSize();
	private final int numberOfIterations = emptyCells * EXPECTED;

	private final WhateverPlayer whateverPlayer = new WhateverPlayer();

	/**
	 * Checks that after (SIZE^2)*1000 markings, each cell was marked between 900 and 1100 times.
	 * <p>
	 * NOTICE:
	 * This test relies on probability, so you might fail it once in a few tries even if your code is correct.
	 */
	@Test
	void checkMarkingDistribution() {
		for (int i = 0; i < numberOfIterations; ++i) {
			Board board = new Board();
			whateverPlayer.playTurn(board, Mark.X);
			addMarkToCount(board);
		}
		assertEquals(0, emptyCells, "Whatever didn't mark all cells so it is highly likely that he can't.");
		for (int i = 0; i < board.getSize(); ++i) {
			for (int j = 0; j < board.getSize(); ++j) {
				assert (boardMatrix[i][j] >= EXPECTED - EPSILON);
				assert (boardMatrix[i][j] <= EXPECTED + EPSILON);
			}
		}
	}

	private void addMarkToCount(Board board) {
		for (int i = 0; i < board.getSize(); ++i) {
			for (int j = 0; j < board.getSize(); ++j) {
				if (board.getMark(i, j) != Mark.X) {
					continue;
				}
				if (boardMatrix[i][j] == 0) {
					--emptyCells;
				}
				++boardMatrix[i][j];
				return;
			}
		}
	}
}