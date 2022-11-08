import java.util.Scanner;

public class HumanPlayer implements Player {

    private final static String ASK_FOR_COORDINATES = "Player %s, type coordinates: ";
    private final static String RE_ASK_FOR_COORDINATES = "Invalid coordinates, type again: ";

    private final Scanner scanner;

    public HumanPlayer() {
        scanner = new Scanner(System.in);
    }

    /**
     * Plays a single human turn.
     * We can assume input is an integer, can't assume it's within the range.
     *
     * @param board   Board to play in
     * @param mark    The current player's mark
     */
    @Override
    public void playTurn(Board board, Mark mark) {
        int boardSize = board.getSize();
        int range = boardSize - 1;

        System.out.printf(ASK_FOR_COORDINATES, mark);

        String line = this.scanner.nextLine();
        int row = Integer.parseInt(line.charAt(0) + "");
        int col = Integer.parseInt(line.charAt(1) + "");

        while(row < 0 || row > range || col < 0 || col > range || !board.putMark(mark, row, col)) {
            System.out.printf(RE_ASK_FOR_COORDINATES);

            line = this.scanner.nextLine();
            row = Integer.parseInt(line.charAt(0) + "");
            col = Integer.parseInt(line.charAt(1) + "");
        }
    }
}