package image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * A package-private class of the package image.
 */
class FileImage implements Image {

    private static final Color DEFAULT_COLOR = Color.WHITE;

    private final Color[][] pixels;

    public FileImage(String filename) throws IOException {
        java.awt.image.BufferedImage im = ImageIO.read(new File(filename));
        int origWidth = im.getWidth(), origHeight = im.getHeight();

        // Getting the closest power of 2 to ${num} can be done with:
        // 2^(ciel(log(num)/log(2)))
        int newHeight = (int) Math.pow(2, Math.ceil(Math.log(origHeight) / Math.log(2)));
        int newWidth = (int) Math.pow(2, Math.ceil(Math.log(origWidth) / Math.log(2)));

        int heightMargin = (newHeight - origHeight) / 2;
        int widthMargin = (newWidth - origWidth) / 2;

        this.pixels = new Color[newHeight][newWidth];

        for(int row = 0; row < newHeight; row++) {
            for(int col = 0; col < newWidth; col++) {
                // If the current pixel is within the added margin, we want to set it to default color.
                if(row < heightMargin || row >= (newHeight - heightMargin)
                        || col < widthMargin || col >= (newWidth - widthMargin)) {
                    this.pixels[row][col] = DEFAULT_COLOR;
                } else {
                    this.pixels[row][col] = new Color(
                            im.getRGB(col - widthMargin, row - heightMargin));
                }
            }
        }
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
