package image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * A package-private class of the package image.
 * @author Dan Nirel
 */
class FileImage implements Image {
    private static final Color DEFAULT_COLOR = Color.WHITE;

    private final Color[][] pixelArray;

    public FileImage(String filename) throws IOException {
        java.awt.image.BufferedImage im = ImageIO.read(new File(filename));
        int origWidth = im.getWidth(), origHeight = im.getHeight();
        //im.getRGB(x, y)); getter for access to a specific RGB rates

        int newWidth = 0; //TODO: change
        int newHeight = 0; //TODO: change

        pixelArray = new Color[newHeight][newWidth];
        //add your code here
    }

    @Override
    public int getWidth() {
        //TODO: implement the function
        return 0;
    }

    @Override
    public int getHeight() {
        //TODO: implement the function
        return 0;
    }

    @Override
    public Color getPixel(int x, int y) {
        //TODO: implement the function
        return new Color(0, 0, 0);
    }

}
