package danogl.gui.rendering;

import danogl.util.MutableVector2;
import danogl.util.Vector2;

import java.awt.*;
import java.awt.geom.AffineTransform;

/**
 * A class in package visibility, for inner use
 * @author Dan Nirel
 */
class ConfigureGraphics {
    private Graphics2D g;
    private Vector2 topLeftCorner;
    private Vector2 dimensions;
    private double degreesCounterClockwise;
    private double opaqueness;
    AffineTransform originalTransform = null;

    private static MutableVector2 freeVector = new MutableVector2();

    public void init(Graphics2D g, Vector2 topLeftCorner, Vector2 dimensions,
                     double degreesCounterClockwise, double opaqueness) {
        this.g = g;
        this.topLeftCorner = topLeftCorner;
        this.dimensions = dimensions;
        this.degreesCounterClockwise = degreesCounterClockwise;
        this.opaqueness = opaqueness;
    }

    public void setRotation() {
        if(degreesCounterClockwise == 0)
            return;
        originalTransform = g.getTransform();
        freeVector.setXY(dimensions);
        freeVector.selfMult(0.5f).selfAdd(topLeftCorner);
        g.rotate(Math.toRadians(-degreesCounterClockwise),
                freeVector.x(), freeVector.y());
    }

    public void setOpaqueness() {
        if(opaqueness < 1)
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,(float) opaqueness));
        else if(g.getComposite() != AlphaComposite.SrcOver)
            g.setComposite(AlphaComposite.SrcOver);
    }

    public void rollbackChanges() {
        if(degreesCounterClockwise != 0)
            g.setTransform(originalTransform);
    }
}
