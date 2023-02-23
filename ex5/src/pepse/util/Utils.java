package pepse.util;

public class Utils {

    /**
     * Rounds the given number to the closest multiplication of ${factor}
     *
     * @param number   Number to round
     * @param factor   Factor to round to
     * @return         Rounded value
     */
    public static int round(int number, int factor) {
        return (number >= 0 ? number - (number % factor) : number - (factor + (number % factor)));
    }
}
