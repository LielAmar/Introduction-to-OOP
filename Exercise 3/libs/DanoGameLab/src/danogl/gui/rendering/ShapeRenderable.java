package danogl.gui.rendering;

import danogl.util.Vector2;

import java.awt.*;

/**
 * A package-visibility superclass for Renderables of simple shapes
 * @author Dan Nirel
 */
abstract class ShapeRenderable implements Renderable {
    ConfigureGraphics configureGraphics = new ConfigureGraphics();
    private Color color;

    public ShapeRenderable(Color color) {
        this.color = color;
    }

    @Override
    public void render(
            Graphics2D g,
            Vector2 topLeftCorner,
            Vector2 dimensions,
            double degreesCounterClockwise,
            boolean isFlippedHorizontally, boolean isFlippedVertically,
            double opaqueness) {

        if(isFlippedHorizontally)
            degreesCounterClockwise *= -1;
        if(isFlippedVertically)
            degreesCounterClockwise *= -1;

        configureGraphics.init(g, topLeftCorner, dimensions, degreesCounterClockwise, opaqueness);
        configureGraphics.setRotation();
        configureGraphics.setOpaqueness();
        g.setColor(color);
        callGraphicsMethod(g, topLeftCorner, dimensions);
        configureGraphics.rollbackChanges();
    }

    protected abstract void callGraphicsMethod(Graphics2D g,
                                          Vector2 topLeftCorner,
                                          Vector2 dimensions);
}
