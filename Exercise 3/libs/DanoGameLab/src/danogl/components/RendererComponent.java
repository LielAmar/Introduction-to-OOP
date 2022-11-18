package danogl.components;

import danogl.GameObject;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.*;

/**
 * Encapsulates the rendering properties and abilities of a GameObject
 * @author Dan Nirel
 */
public class RendererComponent implements Component {
    private final GameObject gameObject;

    private Renderable renderable;
    private float renderableDegreesCounterclockwise;
    private boolean isFlippedHorizontally = false;
    private boolean isFlippedVertically = false;
    private float opaqueness = 1;

    /**
     * Construct the component for the given GameObject. Sets its Renderable to be the
     * supplied argument.
     */
    public RendererComponent(GameObject gameObject, Renderable renderable) {
        this.gameObject = gameObject;
        this.renderable = renderable;
    }

    @Override
    public void update(float deltaTime) {
        if(renderable != null)
            renderable.update(deltaTime);
    }

    /**
     * Render the GameObject
      * @param g the graphics object used for rendering
     * @param topLeftCorner the GameObject's position
     * @param dimensions the GameObject's dimensions
     */
    public void render(Graphics2D g, Vector2 topLeftCorner, Vector2 dimensions) {
        if(renderable == null)
            return;
        renderable.render(g, topLeftCorner, dimensions,
                renderableDegreesCounterclockwise,
                isFlippedHorizontally, isFlippedVertically, opaqueness);
    }

    /**
     * @return The current Renderable.
     */
    public Renderable getRenderable() { return renderable; }

    /**
     * A setter for the image.
     * @param renderable the new image. Can be null.
     */
    public void setRenderable(Renderable renderable) {
        this.renderable = renderable;
    }

    /**
     * The angle at which the renderable is rendered, relative to its center,
     * in degrees, counterclockwise. Note that this angle does not affect
     * collisions, only rendering.
     */
    public float getRenderableAngle() {
        return renderableDegreesCounterclockwise;
    }

    /**
     * Set the angle at which the renderable is rendered, relative to its center,
     * in degrees, counterclockwise. Note that this angle does not affect
     * collisions, only rendering; it is therefore preferable to use a non-zero angle
     * only for objects whose width and height are similar or for
     * non-colliding objects.
     */
    public void setRenderableAngle(float renderableDegreesCounterclockwise) {
        this.renderableDegreesCounterclockwise = renderableDegreesCounterclockwise;
    }

    /**
     * Is the image flipped horizontally
     */
    public boolean isFlippedHorizontally() {
        return isFlippedHorizontally;
    }

    /**
     * Set whether the image should be flipped horizontally
     */
    public void setIsFlippedHorizontally(boolean isFlippedHorizontally) {
        if(this.isFlippedHorizontally == isFlippedHorizontally)
            return;
        this.isFlippedHorizontally = isFlippedHorizontally;
    }

    /**
     * Is the image flipped vertically
     */
    public boolean isFlippedVertically() {
        return isFlippedVertically;
    }

    /**
     * Set whether the image should be flipped vertically
     */
    public void setIsFlippedVertically(boolean isFlippedVertically) {
        if(this.isFlippedVertically == isFlippedVertically)
            return;
        this.isFlippedVertically = isFlippedVertically;
    }

    /**
     * The opaqueness of this object. See {@link #setOpaqueness(float)}.
     * @return
     */
    public float getOpaqueness() {
        return opaqueness;
    }

    /**
     * Sets the object's opaquness. 1 for opaque, 0 for invisible, values in between
     * for degrees of transparency.
     */
    public void setOpaqueness(float opaqueness) {
        opaqueness = Math.min(Math.max(opaqueness, 0), 1);
        this.opaqueness = opaqueness;
    }

    /**
     * Gradually increase the GameObject's opaqueness over the supplied
     * length of times, in seconds. After this time the opaqueness will be 1.
     */
    public void fadeIn(float fadeInTime) { fadeIn(fadeInTime, null); }

    /**
     * Gradually decrease the GameObject's opaqueness over the supplied
     * length of times, in seconds. After this time the opaqueness will be 0.
     */
    public void fadeOut(float fadeOutTime) { fadeOut(fadeOutTime, null); }

    /**
     * Gradually increase the GameObject's opaqueness over the supplied
     * length of times, in seconds. After this time the opaqueness will be 1.
     * @param afterFadeIn a function to execute when the fadeIn is over
     */
    public void fadeIn(float fadeInTime, Runnable afterFadeIn) {
        new Transition<>(gameObject, this::setOpaqueness, getOpaqueness(), 1f,
                Transition.LINEAR_INTERPOLATOR_FLOAT, fadeInTime,
                Transition.TransitionType.TRANSITION_ONCE,
                afterFadeIn);
    }

    /**
     * Gradually decrease the GameObject's opaqueness over the supplied
     * length of times, in seconds. After this time the opaqueness will be 0.
     * @param afterFadeOut a function to execute when the fadeOut is over
     */
    public void fadeOut(float fadeOutTime, Runnable afterFadeOut) {
        new Transition<>(gameObject, this::setOpaqueness, getOpaqueness(), 0f,
                Transition.LINEAR_INTERPOLATOR_FLOAT, fadeOutTime,
                Transition.TransitionType.TRANSITION_ONCE,
                afterFadeOut);
    }
}
