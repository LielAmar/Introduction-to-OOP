package src.utils;

import danogl.GameObject;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

public abstract class TrackedGameObject extends GameObject {

    protected final Counter tracker;

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     *                      the GameObject will not be rendered.
     */
    public TrackedGameObject(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable) {
        super(topLeftCorner, dimensions, renderable);

        this.tracker = new Counter(0);
    }


    /**
     * Returns the tracker value
     *
     * @return   Tracker value
     */
    public int getTrackerValue() {
        return this.tracker.value();
    }

    /**
     * Sets the tracker value to the given parameter
     *
     * @param trackerValue   New tracker value
     */
    public void setTrackerValue(int trackerValue) {
        this.tracker.reset();
        this.tracker.increaseBy(trackerValue);
    }
}
