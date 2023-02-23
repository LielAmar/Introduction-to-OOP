package danogl.gui;

import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * Provides control and info regarding the game.
 * @author Dan Nirel
 */
public interface WindowController {
    /**Exit the game.*/
    void closeWindow();

    /**Reset the game, as if it was just opened.*/
    void resetGame();

    /**Open a message box. While the message box is open, the game is paused.*/
    void showMessageBox(String msg);

    /**Open a dialog window that shows the provided prompt and then asks
     * for yes or no. While the dialog is open, the game is paused.
     * @return true if the user chose yes, false if they chose no.
     */
    boolean openYesNoDialog(String msg);

    /**The current time-scale of the game. See {@link #setTimeScale(float)}.*/
    double getTimeScale();

    /**
     * Sets the game's time-scale. A value of 1 is normal speed,
     * a value in the range (0,1) is slow-motion. A value over 1 is fast.
     * A value of exactly 0 is not recommended - it might provide edge-cases
     * for some of your classes.
     * To pause the game, see {@link #setPauseButton(int)} method in this interface.
     * <p>A negative value is technically supported, but you'd better be sure
     * that your own GameObjects actually support a negative deltaTime parameter.</p>
     * <p>The deltaTime parameter supplied in the update methods
     * is effectively the actual time since the previous frame,
     * multiplied by whatever you put here.</p>
     */
    void setTimeScale(float value);

    /**The window's dimensions in pixels*/
    Vector2 getWindowDimensions();

    /**
     * The idle-time (sleeping-time) between frames will be adjusted to try and hit the
     * specified targetFramerate.
     * <p>A higher targetFramerate will make the game smoother, if this framerate is
     * indeed achievable given the resources your game consumes each frame.</p>
     * <p>A targetFramerate too high will cause the game to never sleep, and thus
     * the time between two frames will be exactly the time it took to update and
     * render the frame. This will result in a non-consistent framerate, and will
     * also hog more of the CPU's time. The deltaTime parameter of the update method
     * might be very inconsistent.</p>
     * <p>A low targetFramerate will assure that the game runs in a consistent pace
     * (which might or might not be important, depending on the application) and
     * consumes less resources, at the cost of a choppier experience.</p>*/
    void setTargetFramerate(int targetFramerate);

    /**
     * Sets a key to pause and unpause the game (prevent update calls).
     * By default, pausing is disabled (the game cannot be paused).
     * Call this method to allow pausing.
     * @param keyFromKeyEvent A constant from KeyEvent, or -1 to specify
     *                        that pausing should be disabled.
     */
    void setPauseButton(int keyFromKeyEvent);

    /**
     * Sets a key to exit the game.
     * By default, the escape key will exit the game.
     * @param keyFromKeyEvent A constant from KeyEvent, or -1 to specify
     *                        that no key should exit the game.
     */
    void setExitButton(int keyFromKeyEvent);

    /**
     * Sets a custom mouse cursor.
     * @param renderable the renderable to be rendered at the mouse position.
     *                   a value of null makes the cursor invisible.
     * @param dimensions cursor size, in pixels
     * @param offset offset of the renderable relative to the mouse position.
     *              For example, an offset of (0,0) means the top-left corner of the supplied
     *               renderable will be at the mouse position. An offset of
     *               dimensions.mult(-0.5f) will align the renderable's center
     *               with the mouse position.
     */
    void setMouseCursor(Renderable renderable, Vector2 dimensions, Vector2 offset);

    /**
     * Returns the current Renderable that serves as a mouse cursor
     */
    Renderable getMouseCursor();

    /**
     * Returns the current dimensions of the mouse cursor
     */
    Vector2 getMouseCursorDimensions();
}
