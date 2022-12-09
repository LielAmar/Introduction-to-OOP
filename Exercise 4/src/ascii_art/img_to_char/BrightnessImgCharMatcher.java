package ascii_art.img_to_char;

import image.Image;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class BrightnessImgCharMatcher {

    private static final int CHAR_RESOLUTION = 16;

    private final Image image;
    private final String font;

    public BrightnessImgCharMatcher(Image img, String font) {
        this.image = img;
        this.font = font;
    }

    public char[][] chooseChars(int numCharsInRow, Character[] charSet) {
        Map<Character, Double> brightness = this.calculateBrightnessLevels(charSet);
        this.calculateUpdatedBrightnessLevels(brightness);
        this.sortBrightnessLevels();



        Image[][] subImages = this.image.splitToSubImages(numCharsInRow);

        char[][] result = new char[];
    }

    /**
     * Loops over every character in the given set and calculates the brightness level
     * of each. It translates the character into a 16x16 image, and checks what percentage of
     * the pixels is white
     *
     * @param charSet   Characters to calculate brightness of
     * @return          Brightness level of each character
     */
    private Map<Character, Double> calculateBrightnessLevels(Character[] charSet) {
        Map<Character, Double> brightness = new HashMap<>();

        for(Character c : charSet) {
            boolean[][] charImage = CharRenderer.getImg(c, CHAR_RESOLUTION, this.font);

            int trueCounter = 0;
            int pixelCounter = 0;

            for (boolean[] row : charImage) {
                for (boolean col : row) {
                    pixelCounter++;

                    if (col) {
                        trueCounter++;
                    }
                }
            }

            brightness.put(c, ((double) trueCounter / pixelCounter));
        }

        return brightness;
    }

    private void calculateUpdatedBrightnessLevels(Map<Character, Double> brightness) {
        double max_brightness = 0, min_brightness = 1;

        for(Double charBrightness : brightness.values()) {
            if(max_brightness < charBrightness) {
                max_brightness = charBrightness;
            }
            if(min_brightness > charBrightness) {
                min_brightness = charBrightness;
            }
        }

        for(Character character : brightness.keySet()) {
            double newBrightness = (brightness.get(character) - min_brightness) /
                    (max_brightness - min_brightness);

            brightness.put(character, newBrightness);
        }
    }

    private double calculateSubImageBrightness(Map<Character, Double> brightness, Image subImage) {
        double greyPixelsSum = 0;
        int totalPixels = 0;

        for(int row = 0; row < subImage.getHeight(); row++) {
            for(int col = 0; col < subImage.getWidth(); col++) {
                Color pixel = subImage.getPixel(col, row);

                double greyPixel = pixel.getRed() * 0.2126 +
                        pixel.getGreen() * 0.7152 + pixel.getBlue() * 0.0722;

                greyPixelsSum += greyPixel;

                totalPixels++;
            }
        }

        return greyPixelsSum / (totalPixels * 255);
    }


    private static class Pair<K, V> {

        private final K key;
        private final V value;

        public Pair(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public K getKey() {
            return this.key;
        }

        public V getValue() {
            return this.value;
        }
    }
}
