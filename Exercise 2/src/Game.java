public class Game {

    private final static int DEFAULT_WIN_STREAK = 3;

    private final Player playerX, playerO;
    private final Renderer renderer;

    private final int winStreak;
    private final Board board;

    private Player currentPlayer;

    public Game(Player playerX, Player playerO, Renderer renderer) {
        this(playerX, playerO, Board.DEFAULT_BOARD_SIZE, DEFAULT_WIN_STREAK, renderer);
    }

    public Game(Player playerX, Player playerO, int size, int winStreak, Renderer renderer) {
        this.playerX = playerX;
        this.playerO = playerO;
        this.renderer = renderer;

        this.winStreak = (winStreak < 1 || winStreak > size) ? size : winStreak;
        this.board = new Board(size);

        this.currentPlayer = playerX; // Assuming playerX always starts
    }

    // TODO: might wanna refactor the above constructors
    private void initGame(Player playerX, Player playerO, int winStreak, Renderer renderer) {}


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
        while((winner = this.getWinner()) == null || !isBoardFull()) {
            this.currentPlayer.playTurn(this.board, (this.currentPlayer == this.playerX ? Mark.X : Mark.O));

            this.currentPlayer = (this.currentPlayer == this.playerX ? this.playerO : this.playerX);
        }

        return (isBoardFull() ? Mark.BLANK : winner);
    }

    /**
     * Checks if the game has ended with a winner.
     *
     * @return   The winning player or null if no winner found
     */
    private Mark getWinner() {

        return null;
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
