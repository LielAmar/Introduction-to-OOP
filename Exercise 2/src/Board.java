import java.util.Arrays;

/**
 * This class represents a board that can be used for various games.
 */

public class Board {

    private static final int DEFAULT_BOARD_SIZE = 4;

    private final int boardSize;
    private final Mark[][] board;

    public Board() {
        this(DEFAULT_BOARD_SIZE);
    }

    public Board(int size) {
        this.boardSize = size;
        this.board = new Mark[this.boardSize][this.boardSize];

        this.initBoard();
    }

    /**
     * Initializes the created board
     */
    private void initBoard() {
        for (Mark[] marks : this.board) {
            Arrays.fill(marks, Mark.BLANK);
        }
    }


    // ===== Getters ===== \\

    public final int getSize() {
        return this.boardSize;
    }

    public Mark[][] getBoard() {
        return this.board;
    }


    // ===== Functions ===== \\

    /**
     * Tries to place the given mark at index (row, col).
     *
     * @param mark   Mark to place
     * @param row    Index row
     * @param col    Index column
     * @return       Whether the placement was successful
     */
    public boolean putMark(Mark mark, int row, int col) {
        if(row < 0 || row >= this.getSize() || col < 0 || col >= this.getSize()) {
            return false;
        }

        if(this.getMark(row, col) != Mark.BLANK) {
            return false;
        }

        this.board[row][col] = mark;
        return true;
    }

    /**
     * Returns the mark at the index (row, col).
     *
     * @param row   Index row
     * @param col   Index column
     * @return      BLANK mark if illegal arguments, the mark at (row, col) otherwise
     */
    public Mark getMark(int row, int col) {
        if(row < 0 || row >= this.board.length || col < 0 || col >= this.board[row].length) {
            return Mark.BLANK;
        }

        return this.board[row][col];
    }
}
