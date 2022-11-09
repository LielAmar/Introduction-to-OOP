public class GeniusPlayer implements Player {

    @Override
    public void playTurn(Board board, Mark mark) {
        for(int j = 1; j < board.getSize(); j++) {
            for(int i = 0; i < board.getSize(); i++) {
                if(board.putMark(mark, i, j)) {
                    return;
                }
            }
        }

        for(int i = 0; i < board.getSize(); i++) {
            for(int j = 0; j < board.getSize(); j++) {
                if(board.putMark(mark, i, j)) {
                    return;
                }
            }
        }
    }
}