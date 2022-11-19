import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Exercise2_Tests {

    private static double percentagedTournament(int rounds, int size, int streak, Player[] players) {
        int wins1 = 0;

        for(int i = 0; i < rounds; i++) {
            if(new Game(players[0], players[1], size, streak, new VoidRenderer()).run() == Mark.X)
                wins1++;
        }

        return (double) wins1/ rounds;
    }

    @Test
    public static void test_genius_clever(int rounds) {
        Player[] players = {
                new PlayerFactory().buildPlayer("genius"),
                new PlayerFactory().buildPlayer("clever")
        };

        for(int i = 3; i < 10; i++) {
            for(int j = 3; j < 10; j++) {
                Assertions.assertTrue(percentagedTournament(rounds, i, j, players) >= 0.55);
            }
        }
    }

    @Test
    public static void test_genius_whatever(int rounds) {
        Player[] players = {
                new PlayerFactory().buildPlayer("genius"),
                new PlayerFactory().buildPlayer("whatever")
        };

        for(int i = 3; i < 10; i++) {
            for(int j = 3; j < 10; j++) {
                Assertions.assertTrue(percentagedTournament(rounds, i, j, players) >= 0.55);
            }
        }
    }

    @Test
    public static void test_clever_whatever(int rounds) {
        Player[] players = {
                new PlayerFactory().buildPlayer("clever"),
                new PlayerFactory().buildPlayer("whatever")
        };

        for(int i = 3; i < 10; i++) {
            for(int j = 3; j < 10; j++) {
                Assertions.assertTrue(percentagedTournament(rounds, i, j, players) >= 0.55);
            }
        }
    }

    @Test
    public static void main(String[] args) {
        test_clever_whatever(100000);
        test_genius_clever(100000);
        test_genius_whatever(100000);

        test_clever_whatever(50000);
        test_genius_clever(50000);
        test_genius_whatever(50000);

        test_clever_whatever(10000);
        test_genius_clever(10000);
        test_genius_whatever(10000);

        test_clever_whatever(5000);
        test_genius_clever(5000);
        test_genius_whatever(5000);

        test_clever_whatever(100);
        test_genius_clever(100);
        test_genius_whatever(100);
    }
}
