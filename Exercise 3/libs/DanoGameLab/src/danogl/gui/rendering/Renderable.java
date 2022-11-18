package danogl.gui.rendering;

import danogl.util.Vector2;

import java.awt.*;

/**
 * Represents a visual representation of a game object
 * @author Dan Nirel
 */
public interface Renderable {
    /**
     * This should be called every frame if the Renderable is dynamic.
     * Implementing this method is optional.
     * @param deltaTime Time since last frame
     */
    default void update(double deltaTime) {}

    /**
     * Renders the object.
     * @param g The graphics object.
     * @param topLeftCorner Top-left corner of where to render the renderable.
     * @param dimensions Dimensions of the renderable (in pixels).
     */
    default void render(Graphics2D g, Vector2 topLeftCorner, Vector2 dimensions) {
        render(g, topLeftCorner, dimensions, 0, false, false, 1);
    }
    
    /**
     * Renders the object. Should be called at the render phase.
     * @param g The graphics object.
     * @param topLeftCorner Top-left corner of where to render the renderable.
     * @param dimensions Dimensions of the renderable (in pixels).
     * @param degreesCounterClockwise Angle of the image. The image is rotated around its
     *                             center.
     * @param isFlippedHorizontally True to mirror the renderable (left-right).
     * @param isFlippedVertically True to mirror the renderable (up-down).
     * @param opaqueness 1 for opaque, 0 for invisible, values in between
     *                   for degrees of transparency.
     */
    void render(Graphics2D g,
                Vector2 topLeftCorner,
                Vector2 dimensions,
                double degreesCounterClockwise,
                boolean isFlippedHorizontally, boolean isFlippedVertically,
                double opaqueness);
}
