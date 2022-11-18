package danogl.gui.mouse;

import danogl.util.MutableVector2;
import danogl.util.Vector2;

/**
 * Encapsulated information regarding a mouse action.
 * @author Dan Nirel
 */
public class MouseActionParams {
    private MouseAction mouseAction;
    private MutableVector2 mouseScreenPos;
    private MutableVector2 mouseWorldPos;
    private double scrollClicks;
    private MouseButton button;

    /**
     * An instance of this class is usually created internally by the engine.
     */
    public MouseActionParams(MouseAction mouseAction,
                             Vector2 mouseScreenPos,
                             MutableVector2 mouseWorldPos, double scrollClicks,
                             MouseButton button) {
        this.mouseAction = mouseAction;
        this.mouseScreenPos = new MutableVector2(mouseScreenPos);
        this.mouseWorldPos = new MutableVector2(mouseWorldPos);
        this.scrollClicks = scrollClicks;
        this.button = button;
    }

    /**
     * An empty constructor for use by extending classes
     */
    protected MouseActionParams() {
        mouseScreenPos = new MutableVector2();
        mouseWorldPos = new MutableVector2();
    }

    /**
     * Returns the action performed
     */
    public MouseAction getMouseAction() {
        return mouseAction;
    }

    /**
     * The current screen-coordinates of the cursor (in pixels)
     */
    public Vector2 getMouseScreenPos() {
        return mouseScreenPos;
    }

    /**
     * The current world-coordinates of the cursor
     */
    public Vector2 getMouseWorldPos() {
        return mouseWorldPos;
    }

    /**
     * The number of clicks scrolled since the last frame. Negative values for scrolling down,
     * positive for up.
     */
    public double getScrollClicks() {
        return scrollClicks;
    }

    /**
     * If the actions concnerns any of the buttons, this method retrieves it.
     * @return
     */
    public MouseButton getButton() {
        return button;
    }

    /**
     * Normally called internally by the engine
     */
    public void setMouseScreenPos(Vector2 mouseScreenPos) {
        this.mouseScreenPos.setXY(mouseScreenPos);
    }

    /**
     * Normally called internally by the engine
     */
    public void setMouseWorldPos(Vector2 mouseWorldPos) {
        this.mouseWorldPos.setXY(mouseWorldPos);
    }

    protected void setMouseAction(MouseAction mouseAction) {
        this.mouseAction = mouseAction;
    }

    protected void setScrollClicks(double scrollClicks) {
        this.scrollClicks = scrollClicks;
    }

    protected void setButton(MouseButton button) {
        this.button = button;
    }
}
