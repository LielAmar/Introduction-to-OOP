package image;

import java.awt.*;

/**
 * A package-private class of the package image.
 * Represents a sub image.
 */
public class SubImage implements Image {

    private final Color[][] pixels;

    public SubImage(Color[][] pixels) {
        this.pixels = pixels;
    }

    /**
     * Returns the width of the image
     *
     * @return   Image width
     */
    @Override
    public int getWidth() {
        return this.pixels[0].length;
    }

    /**
     * Returns the height of the image
     *
     * @return   Image height
     */
    @Override
    public int getHeight() {
        return this.pixels.length;
    }

    /**
     * Returns the color of a single pixel
     *
     * @param row   row coordinate of the pixel to return
     * @param col   col coordinate of the pixel to return
     * @return      Color of the desired pixel
     */
    @Override
    public Color getPixel(int col, int row) {
        return this.pixels[row][col];
    }
}
