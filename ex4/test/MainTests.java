import ascii_art.img_to_char.BrightnessImgCharMatcher;
import image.Image;

import java.util.Arrays;

public class MainTests {

    public static void main(String[] args) {
        Image img = Image.fromFile("./board.jpeg");
        BrightnessImgCharMatcher charMatcher = new BrightnessImgCharMatcher(img, "Ariel");
        var chars = charMatcher.chooseChars(2, new Character[] { 'm', 'o' });
        System.out.println(Arrays.deepToString(chars));
    }
}
