package danogl.gui.rendering;

import danogl.util.Vector2;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * A simple renderable - a still image.
 * @author Dan Nirel
 */
public class ImageRenderable implements Renderable {
    private Image image;
    private ConfigureGraphics configureGraphics = new ConfigureGraphics();

    /**
     * Constructor. Typically, an instance of this class will be created via
     * {@link danogl.gui.ImageReader}.
     */
    public ImageRenderable(Image image) { this.image = image; }

    @Override
    public void render(Graphics2D g, Vector2 topLeftCorner, Vector2 dimensions,
                       double degreesCounterClockwise,
                       boolean isFlippedHorizontally, boolean isFlippedVertically,
                       double opaqueness) {
        if(image == null || opaqueness <= 0)
            return;

        int topLeftCornerX = (int)topLeftCorner.x();
        int topLeftCornerY = (int)topLeftCorner.y();
        int dimX = (int)dimensions.x();
        int dimY = (int)dimensions.y();
        if(isFlippedHorizontally) {
            dimX *= -1;
            topLeftCornerX -= dimX;
            degreesCounterClockwise *= -1;
        }
        if(isFlippedVertically) {
            dimY *= -1;
            topLeftCornerY -= dimY;
            degreesCounterClockwise *= -1;
        }

        configureGraphics.init(g, topLeftCorner, dimensions, degreesCounterClockwise, opaqueness);
        configureGraphics.setRotation();
        configureGraphics.setOpaqueness();

        g.drawImage(image,
                topLeftCornerX, topLeftCornerY,
                dimX, dimY, null);

        configureGraphics.rollbackChanges();
    }

    /**
     * Returns the image
     */
    public Image getImage() {
        return image;
    }

    /**
     * The image's original width, in pixels (unrelated to how large it's rendered in the game)
     */
    public int width() { return image.getWidth(null); }
    /**
     * The image's original height, in pixels (unrelated to how large it's rendered in the game)
     */
    public int height() { return image.getHeight(null); }

    /**
     * The image's original ratio (width/height). Unrelated to it's dimensions in the game)
     */
    public float ratioAsWidthDivHeight() {
        return (float)image.getWidth(null)
                /image.getHeight(null);
    }
}
