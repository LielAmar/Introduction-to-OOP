public class Tournament {

    private static final String INVALID_ARGUMENTS = "Please use: Tournament [round count] [size] [win_streak] " +
            "[render target: console/none] [player: human/whatever/clever/genius]x2";
    private static final String INVALID_PLAYERS = "Choose a player and start again\n" +
            "The players: [human, clever, whatever, genius]";
    private static final String INVALID_RENDERER = "Choose a renderer and start again\n" +
            "The renderers: [console, none]";
    private static final String RESULTS_MESSAGE = "######### Results #########\n" +
            "Player 1, %s won: %d rounds\n" +
            "Player 2, %s won: %d rounds\n" +
            "Ties: %d\n";

    private final int rounds;

    private final Renderer renderer;
    private final Player[] players;

    public Tournament(int rounds, Renderer renderer, Player[] players) {
        this.rounds = rounds;

        this.renderer = renderer;
        this.players = players;
    }


    // ===== Functions ===== \\

    /**
     * Runs a tic-tac-toe tournament of multiple game
     *
     * @param size          Size of the board for each game
     * @param winStreak     Needed streak to win
     * @param playerNames   Names of the players to use
     */
    public void playTournament(int size, int winStreak, String[] playerNames) {
        int player0Wins = 0;
        int player1Wins = 0;

        for(int round = 0; round < this.rounds; round++) {
            boolean reverse = round%2 != 0;

            Mark winner = this.runSingleGame(this.players[0], this.players[1], size, winStreak, reverse);

            // Adds 1 to whoever won the game and 0 to the other player
            player0Wins += (winner == Mark.X && !reverse || winner == Mark.O && reverse) ? 1 : 0;
            player1Wins += (winner == Mark.O && !reverse || winner == Mark.X && reverse) ? 1 : 0;
        }

        System.out.printf(RESULTS_MESSAGE,
                playerNames[0], player0Wins,
                playerNames[1], player1Wins,
                this.rounds - player0Wins - player1Wins);
    }

    /**
     * Runs a single game in the tournament and returns the winner's mark
     *
     * @param player1     First player in the tournament
     * @param player2     Second player in the tournament
     * @param size        Size of the board for the game
     * @param winStreak   Needed streak to win
     * @param reverse     Whether to reverse the order of the players
     * @return            Winner's mark
     */
    private Mark runSingleGame(Player player1, Player player2, int size, int winStreak, boolean reverse) {
        Game game = new Game(
                (reverse ? player2 : player1),
                (reverse ? player1 : player2),
                size, winStreak, renderer);

        return game.run();
    }


    // ===== Main ===== \\

    public static void main(String[] args) {
        if(args.length != 6) {
            System.err.println(INVALID_ARGUMENTS);
            return;
        }

        // TODO: might need to validate 1 <= winStreak <= size

        int rounds = Integer.parseInt(args[0]);
        int size = Integer.parseInt(args[1]);
        int winStreak = Integer.parseInt(args[2]);

        PlayerFactory playerFactory = new PlayerFactory();
        Player[] players = { playerFactory.buildPlayer(args[4]), playerFactory.buildPlayer(args[5]) };

        Renderer renderer = new RendererFactory().buildRenderer(args[3], size);

        if(players[0] == null || players[1] == null) {
            System.err.println(INVALID_PLAYERS);
            return;
        }

        if(renderer == null) {
            System.err.println(INVALID_RENDERER);
            return;
        }

        Tournament tournament = new Tournament(rounds, renderer, players);
        tournament.playTournament(size, winStreak, new String[] { args[4], args[5] });
    }
}

