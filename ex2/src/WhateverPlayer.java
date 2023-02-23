import java.util.Random;

/**
 * This class represents a normal player that knows how to play tic tac toe
 */

public class WhateverPlayer implements Player {

    private final Random random;

    public WhateverPlayer() {
        this.random = new Random();
    }

    /**
     * Plays a single Mr. Whatever turn.
     *
     * @param board   Board to play in
     * @param mark    The current player's mark
     */
    @Override
    public void playTurn(Board board, Mark mark) {
        int boardSize = board.getSize();

        int row = this.random.nextInt(boardSize);
        int col = this.random.nextInt(boardSize);

        while(!board.putMark(mark, row, col)) {
            row = this.random.nextInt(boardSize);
            col = this.random.nextInt(boardSize);
        }
    }
}