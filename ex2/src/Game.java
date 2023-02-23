/**
 * This class holds the tic tac toe game logic
 */

public class Game {

    private final static int DEFAULT_WIN_STREAK = 3;

    private final Player playerX, playerO;
    private final Renderer renderer;

    private final int winStreak;
    private final Board board;

    private Player currentPlayer;

    public Game(Player playerX, Player playerO, Renderer renderer) {
        this.playerX = playerX;
        this.playerO = playerO;
        this.renderer = renderer;

        this.winStreak = DEFAULT_WIN_STREAK;
        this.board = new Board();

        this.currentPlayer = playerX; // Assuming playerX always starts
    }

    public Game(Player playerX, Player playerO, int size, int winStreak, Renderer renderer) {
        this.playerX = playerX;
        this.playerO = playerO;
        this.renderer = renderer;

        this.winStreak = (winStreak < 1 || winStreak > size) ? size : winStreak;
        this.board = new Board(size);

        this.currentPlayer = playerX; // Assuming playerX always starts
    }


    // ===== Getters ===== \\

    public final int getWinStreak() {
        return this.winStreak;
    }


    // ===== Functions ===== \\

    /**
     * Runs a single game from start to finish.
     *
     * @return   Winner of the game, BLANK if the game was tied.
     */
    public Mark run() {
        Mark winner;

        // Asking current player to do a move and then swapping the players.
        // The game loops run for as long as we don't have a winner or the board is not full.
        while((winner = this.getWinner()) == null && !isBoardFull()) {
            this.currentPlayer.playTurn(this.board, (this.currentPlayer == this.playerX ? Mark.X : Mark.O));

            this.currentPlayer = (this.currentPlayer == this.playerX ? this.playerO : this.playerX);

            this.renderer.renderBoard(this.board);
        }

        return (isBoardFull() ? Mark.BLANK : winner);
    }

    /**
     * Checks if the game has ended with a winner.
     * Loops over every cell in the board and checks if there's a
     * solution starting at that cell
     *
     * @return   The winning player or null if no winner found
     */
    private Mark getWinner() {
        for(int row = 0; row < this.board.getSize(); row++) {
            for(int col = 0; col < this.board.getSize(); col++) {
                Mark mark = this.board.getMark(row, col);

                // Since we're looping over all cells in the board, we can
                // check only these for each cell and get the correct result:
                //   { right, down, diag right, diag left }
                if(mark != Mark.BLANK && (this.checkRowStreak(row, col, mark) ||
                        this.checkColStreak(row, col, mark) ||
                        this.checkDiagRight(row, col, mark) ||
                        this.checkDiagLeft(row, col, mark))) {
                    return mark;
                }
            }
        }

        return null;
    }

    /**
     * @return   Whether the cell (row, col) starts a solution to the right
     */
    private boolean checkRowStreak(int row, int col, Mark mark) {
        for(int i = 1; i < this.winStreak; i++) {
            // if we're outside the board/don't have k in a row
            if(col+i >= this.board.getSize() || this.board.getMark(row, col+i) != mark) {
                return false;
            }
        }

        return true;
    }

    /**
     * @return   Whether the cell (row, col) starts a solution to the bottom
     */
    private boolean checkColStreak(int row, int col, Mark mark) {
        for(int i = 1; i < this.winStreak; i++) {
            // if we're outside the board/don't have k in a col
            if(row+i >= this.board.getSize() || this.board.getMark(row+1, col) != mark) {
                return false;
            }
        }

        return true;
    }

    /**
     * @return   Whether the cell (row, col) starts a solution to the right diagonal
     */
    private boolean checkDiagRight(int row, int col, Mark mark) {
        for(int i = 1; i < this.winStreak; i++) {
            // if we're outside the board/don't have k in a col
            if(row+i >= this.board.getSize() || col+i >= this.board.getSize() ||
                    this.board.getMark(row+i, col+i) != mark) {

                return false;
            }
        }

        return true;
    }

    /**
     * @return   Whether the cell (row, col) starts a solution to the left diagonal
     */
    private boolean checkDiagLeft(int row, int col, Mark mark) {
        for(int i = 1; i < this.winStreak; i++) {
            // if we're outside the board/don't have k in a col
            if(row+i < 0 || col-i < 0 ||
                    this.board.getMark(row+i, col-i) != mark) {

                return false;
            }
        }

        return true;
    }

    /**
     * Checks if the board is full
     *
     * @return   True if no space left, false otherwise.
     */
    private boolean isBoardFull() {
        for(int row = 0; row < this.board.getSize(); row++) {
            for(int col = 0; col < this.board.getSize(); col++) {
                if(this.board.getMark(row, col) == Mark.BLANK) {
                    return false;
                }
            }
        }

        return true;
    }
}
