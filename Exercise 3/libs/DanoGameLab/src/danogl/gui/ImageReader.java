package danogl.gui;

import danogl.gui.rendering.ImageRenderable;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Used to read images from disk or from within a jar
 * @author Dan Nirel
 */
public class ImageReader {
    private static final int COLOR_DIS_EPSILON = 100;

    private WindowController windowController;
    private ResourceReader resReader = new ResourceReader();
    private boolean imageAlreadyContainsAlpha = false;

    /** Construct an ImageReader. Usually used by the engine itself */
    public ImageReader(WindowController windowController) {
        this.windowController = windowController;
    }

    /**
     * Read an image from disk or internally from the jar. The image may contain an alpha channel. If it doesn't,
     * background color may still be removed using the second parameter.
     * If the call fails, an error message will be displayed and the window will be closed.
     * @param path Relative or absolute path
     * @param isTopLeftPixelTransparency If true, it is assumed the top-left corner is part of
     *                                   the background. All pixels with this color will be invisible.
     * @return An ImageRenderable of the desired image.
     */
    public ImageRenderable readImage(String path, boolean isTopLeftPixelTransparency) {
        BufferedImage im = null;
        ResourceReader resReader = new ResourceReader();
        try(var stream = resReader.readResource(path)) {
            im = ImageIO.read(stream);
        }
        catch(IOException ioe) {
            windowController.showMessageBox(
                    String.format("Failed to open image '%s': %s", path, ioe.getMessage()));
            windowController.closeWindow();
        }
        Image finalImage = isTopLeftPixelTransparency ? addTransparency(im) : im;
        return new ImageRenderable(finalImage);
    }

    private Image addTransparency(BufferedImage im) {
        BufferedImage transparentImage =
                new BufferedImage(im.getWidth(),im.getHeight(),BufferedImage.TYPE_INT_ARGB);
        int transparentColor = im.getRGB(0,0);
        for(int y = 0 ; y < im.getHeight() ; y++) {
            for (int x = 0; x < im.getWidth(); x++) {
                int thisPixel = calcPixelAt(im, x, y, transparentColor);
                if(transparentColor == thisPixel)
                    thisPixel &= 0x00ffffff; //zero alpha
                transparentImage.setRGB(x, y, thisPixel);
            }
        }
        return transparentImage;
    }

    private int calcPixelAt(BufferedImage im, int x, int y, int transparentColor) {
        int pixel = im.getRGB(x, y);
        if((pixel >> 24) != 0)
            imageAlreadyContainsAlpha = true;
        if (imageAlreadyContainsAlpha ||
                !isAPixelAroundTransparentButWithNonZeroAlpha(im, x, y, transparentColor))
            return pixel;
        if (colorDistance(pixel, transparentColor) < COLOR_DIS_EPSILON)
            return transparentColor;
        //so this is an edge of the sprite, and was probably affected
        //by the transparency during down-sampling: the shape-color
        //and background (which we are trying to remove) were mixed in this pixel.
        //instead of applying a high-pass filter,
        //assume the current color was a weighted average of
        //transparency and original color with respective weights of 1/3 and 2/3.
        //now recover original color.
        int a = (pixel >> 24) & 0xff;
        double r = ((pixel >> 16) & 0xff) * 1.5 - ((transparentColor >> 16) & 0xff)/2;
        double g = ((pixel >> 8) & 0xff) * 1.5 - ((transparentColor >> 8) & 0xff)/2;
        double b = (pixel & 0xff) * 1.5 - (transparentColor & 0xff)/2;
        r = Math.min(Math.max(r, 0), 255);
        g = Math.min(Math.max(g, 0), 255);
        b = Math.min(Math.max(b, 0), 255);
        pixel = (a << 24) + ((int)r << 16) + ((int)g << 8) + (int)b;
        return pixel;
    }

    private boolean isAPixelAroundTransparentButWithNonZeroAlpha(BufferedImage im, int x, int y, int transparentColor) {
        int width = im.getWidth();
        int height = im.getHeight();
        for (int i = -1; i <= 1 ; i++) {
            for (int j = -1; j <= 1 ; j++) {
                if(x+i < 0 || y+j < 0 || x+i >= width || y+j >= height)
                    continue;
                int pixel = im.getRGB(x+i,y+j);
                if((pixel>>24)!=0 && pixel == transparentColor)
                    return true;
            }
        }
        return false;
    }

    private static double colorDistance(int c1, int c2){
        int rmean = ( ((c1>>16)&0xff) + ((c2>>16)&0xff) ) / 2;
        int rDif = ((c1>>16)&0xff) - ((c2>>16)&0xff);
        int gDif = ((c1>> 8)&0xff) - ((c2>> 8)&0xff);
        int bDif = ((c1>> 0)&0xff) - ((c2>> 0)&0xff);
        return Math.sqrt((((512+rmean)*rDif*rDif)>>8)
                + 4*gDif*gDif + (((767-rmean)*bDif*bDif)>>8));
    }
}
