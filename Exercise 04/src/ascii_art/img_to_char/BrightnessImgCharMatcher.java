package ascii_art.img_to_char;

import image.Image;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * This class handles the matching of an image with its ascii image
 */
public class BrightnessImgCharMatcher {

    private static final double MAX_BRIGHTNESS = 1;
    private static final double MIN_BRIGHTNESS = 0;

    private static final int CHAR_RESOLUTION = 16;

    private static final char MIN_CHAR = 32;
    private static final char MAX_CHAR = 127;

    private static final int MAX_RGB_VALUE = 255;

    private static final double RED_PERCENTAGE = 0.2126;
    private static final double GREEN_PERCENTAGE = 0.7152;
    private static final double BLUE_PERCENTAGE = 0.0722;

    private final Image image;
    private final String font;

    private final Map<Character, Double> brightnessMap;

    // Last cached calculations
    private Pair<Integer, Pair<Image[][], Double[][]>> cachedSubImagesData;

    public BrightnessImgCharMatcher(Image img, String font) {
        this.image = img;
        this.font = font;

        this.brightnessMap = new HashMap<>();
        this.calculateBrightnessLevels();

        this.cachedSubImagesData = null;
    }

    /**
     * Loops over every character and calculates the brightness level of each.
     * It translates the character into a 16x16 image, and checks what percentage of
     * the pixels is white.
     * This function's time complexity is O(126 * (CHAR_RESOLUTION^2))
     */
    private void calculateBrightnessLevels() {
        for(int i = MIN_CHAR; i < MAX_CHAR; i++) {
            char character = (char) i;

            boolean[][] charImage = CharRenderer.getImg(character, CHAR_RESOLUTION, this.font);

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

            double brightnessValue = ((double) trueCounter / pixelCounter);

            this.brightnessMap.put(character, brightnessValue);
        }
    }


    /**
     * Translates the image passed in the constructor into an ascii image
     *
     * @param numCharsInRow   Number of ascii characters in a single row
     *                        Can assume it's lower than the image width
     * @param charSet         Set of allowed characters for the ascii image
     * @return                The ascii image represented in a 2D array
     */
    public char[][] chooseChars(int numCharsInRow, Character[] charSet) {
        // Creates a Map that stores for each character, it's corresponding brightness level
        Map<Character, Double> updatedBrightnessLevels = this.calculateUpdatedBrightnessLevels(charSet);

        // Sort all brightness levels
        List<Pair<Character, Double>> brightnessLevels = this.sortBrightnessLevels(updatedBrightnessLevels);

        // Used cached/re-caches data and returns a 2D array of sub-images and a 2D array of brightnesses
        Pair<Image[][], Double[][]> data = this.getSubImagesData(numCharsInRow);

        Image[][] subImages = data.getKey();
        Double[][] subImagesBrightnessLevels = data.getValue();

        // Created the 2D result array by looping over every sub-image and getting the closest character
        // to its brightness level
        char[][] result = new char[subImages.length][subImages[0].length];

        for (int row = 0; row < subImages.length; row++) {
            for (int column = 0; column < subImages[0].length; column++) {
                result[row][column] = this.getClosestValue(subImagesBrightnessLevels[row][column],
                        brightnessLevels).getKey();
            }
        }

        return result;
    }


    /**
     * Calculates the updated brightness level of each character, using the maximum and minimum ones.
     * This function's time complexity is O(126) at max
     *
     * @param characters   A set of allowed characters
     * @return             A map of allowed characters and their normalized brightness level
     */
    private Map<Character, Double> calculateUpdatedBrightnessLevels(Character[] characters) {
        double max_brightness = MIN_BRIGHTNESS, min_brightness = MAX_BRIGHTNESS;

        for(Character character : characters) {
            double charBrightness = this.brightnessMap.get(character);

            max_brightness = Math.max(max_brightness, charBrightness);
            min_brightness = Math.min(min_brightness, charBrightness);
        }

        Map<Character, Double> updatedBrightnessLevels = new HashMap<>();

        for(Character character : characters) {
            double newBrightness = (this.brightnessMap.get(character) - min_brightness) /
                    (max_brightness - min_brightness);

            updatedBrightnessLevels.put(character, newBrightness);
        }

        return updatedBrightnessLevels;
    }

    /**
     * Loops over all brightness levels, sort them and returns an array of sorted pairs of characters
     * and brightness levels.
     * This function's time complexity is O(n*log(n)) whereas n = length(brightnessMap)
     *
     * @param brightnessMap   Brightness map to sort
     * @return                Sorted list of brightness levels
     */
    private List<Pair<Character, Double>> sortBrightnessLevels(Map<Character, Double> brightnessMap) {
        List<Pair<Character, Double>> sortedBrightness = new ArrayList<>();

        brightnessMap.entrySet().stream().sorted(Map.Entry.comparingByValue())
                .forEach(entry -> sortedBrightness.add(new Pair<>(entry.getKey(), entry.getValue())));

        return sortedBrightness;
    }


    /**
     * A function that either returns cached data or calculates new data and caches it
     *
     * @param numCharsInRow   Number of characters in a single row in the ascii output
     *                        Used to access the right cached value
     * @return                Data matching the given numCharsInRow
     */
    private Pair<Image[][], Double[][]> getSubImagesData(int numCharsInRow) {
        // If we have the data cached, we simply return it
        if(this.cachedSubImagesData != null && this.cachedSubImagesData.getKey() == numCharsInRow) {
            return this.cachedSubImagesData.getValue();
        }

        // Else, split the image into sub images of size ${subImageSize}, calculate all
        // sub-images brightness levels and cache it
        int subImageSize = this.image.getWidth() / numCharsInRow;
        Image[][] subImages = this.image.splitToSubImages(subImageSize);

        Double[][] subImageBrightnesses = new Double[subImages.length][subImages[0].length];

        // Loops over all subImages and saves the closest character to its brightness
        for (int i = 0; i < subImages.length; i++) {
            for (int j = 0; j < subImages[0].length; j++) {
                subImageBrightnesses[i][j] = this.calculateSubImageBrightness(subImages[i][j]);
            }
        }

        Pair<Image[][], Double[][]> subImagesData = new Pair<>(subImages, subImageBrightnesses);
        this.cachedSubImagesData = new Pair<>(numCharsInRow, subImagesData);

        return subImagesData;
    }

    /**
     * Calculates the brightness of a sub image
     *
     * @param subImage     Sub image to calculate the brightness of
     * @return             Calculated brightness
     */
    private double calculateSubImageBrightness(Image subImage) {
        double greyPixelsSum = 0;
        int totalPixels = 0;

        for(int row = 0; row < subImage.getHeight(); row++) {
            for(int col = 0; col < subImage.getWidth(); col++) {
                Color pixel = subImage.getPixel(col, row);

                double greyPixel = pixel.getRed() * RED_PERCENTAGE +
                        pixel.getGreen() * GREEN_PERCENTAGE + pixel.getBlue() * BLUE_PERCENTAGE;

                greyPixelsSum += greyPixel;

                totalPixels++;
            }
        }

        return greyPixelsSum / (totalPixels * MAX_RGB_VALUE);
    }


    /**
     * Returns the Pair of character and brightness that has the closest brightness to desired.
     * Uses a binary search to find the closest item.
     * This function's time complexity is O(log(n)) whereas n = length(sortedBrightness)
     *
     * @param desired            Desired brightness level
     * @param sortedBrightness   Sorted brightness pairs
     * @return                   Closest pair
     */
    private Pair<Character, Double> getClosestValue(double desired,
                                                    List<Pair<Character, Double>> sortedBrightness) {
        /*
        Assuming the given list is indeed sorted, if the first item is bigger than the desired
        item, or the last item is smaller than the desired item, we can return them immediately.
        */

        if(desired <= sortedBrightness.get(0).getValue()) {
            return sortedBrightness.get(0);
        }

        if(desired >= sortedBrightness.get(sortedBrightness.size() - 1).getValue()) {
            return sortedBrightness.get(sortedBrightness.size() - 1);
        }

        // Returning the closest item between the middle, the left sided and the right sided items.
        int middleIndex = sortedBrightness.size() / 2;

        Pair<Character, Double> middle = sortedBrightness.get(middleIndex);

        if(desired == middle.getValue()) {
            return middle;
        }

        double middleDistance = middle.getValue() - desired;

        if(middleDistance > 0) {
            Pair<Character, Double> left = this.getClosestValue(desired,
                    sortedBrightness.subList(0, middleIndex));

            return this.getCloser(desired, middle, left);
        }

        Pair<Character, Double> right = this.getClosestValue(desired,
                sortedBrightness.subList(middleIndex + 1, sortedBrightness.size()));

        return this.getCloser(desired, middle, right);
    }

    /**
     * Returns the pair that's closer to the desired item
     *
     * @param desired   Desired target
     * @param first     First pair
     * @param second    Second pair
     * @return          Closer pair
     */
    private Pair<Character, Double> getCloser(double desired, Pair<Character, Double> first,
                                                 Pair<Character, Double> second) {
        double firstDistance = Math.abs(first.getValue() - desired);
        double secondDistance = Math.abs(second.getValue() - desired);

        return (firstDistance < secondDistance) ? first : second;
    }


    /**
     * A helper class that holds a pair of items
     *
     * @param <K>   Type of key
     * @param <V>   Type of value
     */
    private static class Pair<K, V> {

        private final K key;
        private final V value;

        public Pair(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }
    }
}
