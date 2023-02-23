package danogl.gui.rendering;

import danogl.util.Vector2;
import java.awt.*;

/**
 * A rectangle Renderable. Note that only the color is supplied in the
 * constructor; the dimensions and position are supplied as usual in
 * every call to {@link #render}.
 * @author Dan Nirel
 */
public class RectangleRenderable extends ShapeRenderable {
    /**
     * Create a RectangleRenderable in a given color. The exact shape will be determined
     * by the GameObject with this Renderable.
     */
    public RectangleRenderable(Color color) {
        super(color);
    }

    @Override
    protected void callGraphicsMethod(Graphics2D g, Vector2 topLeftCorner, Vector2 dimensions) {
        g.fillRect(
                (int)topLeftCorner.x()/2*2, //fillRect misses one pixel if coordinate is odd
                (int)topLeftCorner.y()/2*2,
                (int)dimensions.x(),
                (int)dimensions.y());
    }
}
