package danogl.gui;

import danogl.gui.mouse.MouseButton;
import danogl.util.Vector2;

/**
 * An interface for reading user input in the current frame
 */
public interface UserInputListener {
    /**
     * Returns whether a key is currently down. This method will return true
     * for a given key as long as it is pressed (i.e. from the frame in which
     * the key was held down until the frame in which the key was released).
     * @param keyFromKeyEvent a key code from the class KeyEvent (KeyEvent.VK_...)
     * @return is the specified key held down in the current frame
     */
    boolean isKeyPressed(int keyFromKeyEvent);

    /**
     * Returns whether a given key was just released. This method returns true
     * for a key only in the frame in which it was released.
     * @param keyFromKeyEvent a key code from the class KeyEvent (KeyEvent.VK_...)
     * @return was the specified key released in this frame
     */
    boolean wasKeyReleasedThisFrame(int keyFromKeyEvent);

    /**
     * Returns whether a given mouse button was just clicked (pressed and released shortly afterwards).
     * This method returns true for a button only in the frame in which it was clicked.
     * @param button the queried mouse button
     * @return was the specified button released in this frame
     */
    boolean wasMouseButtonClickedThisFrame(MouseButton button);

    /**
     * Returns whether a mouse button is currently down/depressed/pressed.
     * @param button the queried mouse button
     * @return is the specified button down in this frame
     */
    boolean isMouseButtonPressed(MouseButton button);

    /**
     * Returns the mouse's coordinates in the standard danogl coordinate system,
     * i.e. in pixels, where the top-left pixel is (0,0). This is relative to the screen,
     * i.e., window. If a camera is used and you need the mouse's position in world coordinates,
     * use the camera's transformation method screenToWorldCoords.
     */
    Vector2 getMouseScreenPos();

    /**
     * Returns the number of clicks the mousewheel was rotated this frame.
     * @return negative values for scrolling up, positive for scrolling down.
     */
    double mouseWheelClicksThisFrame();
}
