import java.util.Map;
import java.util.logging.Logger;

/**
 * A supplied class for the Tic Tac Toe exercise in the OOP MOOC by the Hebrew University.
 * Renders a given part_2.Board to the console.
 *
 * @author Dan Nirel
 */
public class ConsoleRenderer implements Renderer {

    private static final Character FIRST_ROW = '0';
    private static final Character FIRST_COL = '0';
    private static final int ROWS_PER_MARK = 3;
    private static final int COLS_PER_MARK = 9;
    private static final char DEFAULT_CHAR = ' ';
    private static final char HORIZONTAL_LINE_CHAR = '-';
    private static final char VERTICAL_LINE_CHAR = '|';
    private static final int NUM_ROWS_BEFORE_BOARD = 4;
    private static final int NUM_COLS_BEFORE_BOARD = 6;

//                                            for the marks         for lines         for indices
//	private static final int BUFFER_ROWS = board.getSize() * ROWS_PER_MARK + (board.getSize()-1) + NUM_ROWS_BEFORE_BOARD;
//	private static final int BUFFER_COLS = board.getSize() * COLS_PER_MARK + (board.getSize()-1) + NUM_COLS_BEFORE_BOARD;

    private static final Map<Mark, String[]> MARKS_DRAWINGS = Map.of(

            Mark.X, new String[]{"  X   X  ",
                    "    X    ",
                    "  X   X  "},

            Mark.O, new String[]{"   OOO   ",
                    "  O   O  ",
                    "   OOO   "},

            Mark.BLANK, new String[]{"         ",
                    "         ",
                    "         "}
    );

    private final char[][] buffer; // = new char[BUFFER_ROWS][BUFFER_COLS];

    /**
     * Initializes the renderer.
     */
    public ConsoleRenderer(int size) {
        int bufferRows = size * ROWS_PER_MARK + (size - 1) + NUM_ROWS_BEFORE_BOARD;
        int bufferCols = size * COLS_PER_MARK + (size - 1) + NUM_COLS_BEFORE_BOARD;
        this.buffer = new char[bufferRows][bufferCols];

        if (size > 9 || size < 2) {
            String errorMsg = "part_2.Board size must be in the range [2, 9]";
            Logger.getGlobal().severe(errorMsg);
            throw new Error(errorMsg);
        }

        //fill buffer with spaces
        for (int i = 0; i < buffer.length; i++) {
            for (int j = 0; j < buffer[i].length; j++)
                buffer[i][j] = DEFAULT_CHAR;
        }

        //draw horizontal lines
        for (int i = NUM_ROWS_BEFORE_BOARD + ROWS_PER_MARK; i < bufferRows; i += ROWS_PER_MARK + 1) {
            for (int j = NUM_COLS_BEFORE_BOARD; j < bufferCols; j++)
                buffer[i][j] = HORIZONTAL_LINE_CHAR;
        }

        //draw vertical lines
        for (int i = NUM_COLS_BEFORE_BOARD + COLS_PER_MARK; i < bufferCols; i += COLS_PER_MARK + 1) {
            for (int j = NUM_ROWS_BEFORE_BOARD; j < bufferRows; j++)
                buffer[j][i] = VERTICAL_LINE_CHAR;
        }

        //draw indices
        char index = FIRST_COL; // start counting column indexes here
        for (int col = NUM_COLS_BEFORE_BOARD + COLS_PER_MARK / 2; col < bufferCols; col += COLS_PER_MARK + 1)
            buffer[NUM_ROWS_BEFORE_BOARD / 2][col] = index++;
        index = FIRST_ROW; // start counting row indexes here: 00
        for (int row = NUM_ROWS_BEFORE_BOARD + ROWS_PER_MARK / 2; row < bufferRows; row += ROWS_PER_MARK + 1) {
            buffer[row][NUM_COLS_BEFORE_BOARD / 2 - 1] = index++;
//			buffer[row][NUM_COLS_BEFORE_BOARD/2] = ' ';//'0';
        }
    }

    /**
     * Prints the supplied board to the console.
     *
     * @param board the board to print.
     */
    @Override
    public void renderBoard(Board board) {
        for (int i = 0; i < board.getSize(); i++) {
            for (int j = 0; j < board.getSize(); j++) {
                drawMarkInBuffer(NUM_ROWS_BEFORE_BOARD + i * (ROWS_PER_MARK + 1),
                        NUM_COLS_BEFORE_BOARD + j * (COLS_PER_MARK + 1), board.getMark(i, j));
            }
        }

        for (int i = 0; i < buffer.length; i++) {
            for (int j = 0; j < buffer[i].length; j++)
                System.out.print(buffer[i][j]);
            System.out.println();
        }

        System.out.println();
    }

    private void drawMarkInBuffer(int rowStart, int colStart, Mark mark) {
        String[] markLines = MARKS_DRAWINGS.get(mark);

        for (int i = 0; i < markLines.length; i++) {
            for (int j = 0; j < markLines[i].length(); j++) {
                buffer[rowStart + i][colStart + j] = markLines[i].charAt(j);
            }
        }
    }
}
