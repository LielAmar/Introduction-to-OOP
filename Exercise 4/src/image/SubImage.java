package image;

import java.awt.*;
import java.util.Arrays;
import java.util.Objects;

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


    /**
     * Overriding the equals function to tell when two sub images are equal
     *
     * @param obj   Object to compare with
     * @return      Whether the given object is the same as our object
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof SubImage)) {
            return false;
        }

        // typecast o to Complex so that we can compare data members
        SubImage subImage = (SubImage) obj;

        if(this.getWidth() != subImage.getWidth() || this.getHeight() != subImage.getHeight()) {
            return false;
        }

        for(int i = 0; i < this.getWidth(); i++) {
            for(int j = 0; j < this.getHeight(); j++) {
                if(!this.getPixel(j, i).equals(subImage.getPixel(j, i))) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Overriding the hash function to calculate the hash code of a sub-image
     *
     * @return   The sub image hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.getHeight(), this.getWidth(), Arrays.deepHashCode(this.pixels));
    }
}
