package image;

import java.awt.*;
import java.io.IOException;

/**
 * Facade for the image module and an interface representing an image.
 * @author Dan Nirel
 */
public interface Image {

    Color getPixel(int col, int row);
    int getWidth();
    int getHeight();

    /**
     * Open an image from file. Each dimensions of the returned image is guaranteed
     * to be a power of 2, but the dimensions may be different.
     * @param filename a path to an image file on disk
     * @return an object implementing Image if the operation was successful,
     * null otherwise
     */
    static Image fromFile(String filename) {
        try {
            return new FileImage(filename);
        } catch(IOException ioe) {
            return null;
        }
    }

    /**
     * Splits the current image (pixel grid) into sub images of size ${subImageSize}x${subImageSize}.
     * It goes over every row and every column, builds the sub image and adds it at the right spot
     * in the subImages array.
     *
     * @param subImageSize   Size of a single sub image
     * @return               A 2D array of sub images
     */
    default Image[][] splitToSubImages(int subImageSize) {
        int subImagesPerColumn = this.getWidth() / subImageSize;
        int subImagesPerRow = this.getHeight() / subImageSize;

        // TODO might need to change this to a Color[][] instead.
        Image[][] subImages = new SubImage[subImagesPerRow][subImagesPerColumn];

        Color[][] subImagePixels;

        for(int row = 0; row < subImages.length; row++) {
            for(int col = 0; col < subImages[row].length; col++) {
                subImagePixels = new Color[subImageSize][subImageSize];

                for(int i = 0; i < subImageSize; i++) {
                    for(int j = 0; j < subImageSize; j++) {
                        int currentPixelRow = (row * subImageSize) + i;
                        int currentPixelCol = (col * subImageSize) + j;

                        subImagePixels[i][j] = this.getPixel(currentPixelCol, currentPixelRow);
                    }
                }

                subImages[row][col] = new SubImage(subImagePixels);
            }
        }

        return subImages;
    }

    /**
     * Allows iterating the pixels' colors by order (first row, second row and so on).
     *
     * @return an Iterable<Color> that can be traversed with a foreach loop
     */
    default Iterable<Color> pixels() {
        return new ImageIterableProperty<>(
                this, this::getPixel);
    }

    /**
     * Allows iterating the sub images by order (first row, second row and so on).
     *
     * @return an Iterable<Image> that can be traversed with a foreach loop
     */
    default Iterable<Image> subImages(int subImageSize) {
        return new TwoDArrayIterator<>(this.splitToSubImages(subImageSize));
    }
}
