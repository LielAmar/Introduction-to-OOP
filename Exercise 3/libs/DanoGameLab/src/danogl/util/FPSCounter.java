package danogl.util;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.components.ScheduledTask;
import danogl.gui.rendering.TextRenderable;

import java.awt.*;

/**
 * A factory for a GameObject that displays the frames-per-second.
 * @author Dan Nirel
 */
public class FPSCounter {
    private static final float TIME_BETWEEN_FPS_REFRESHES = 0.5f;

    /**
     * Create a GameObject that displays the frames-per-second. Like any GameObject, this object needs
     * to be added to a GameObjectCollection or otherwise updated and rendered in order to work.
     * If added to GameObjectCollection, make sure as usual to add it AFTER any other GameObjects
     * that might occupy the same place on the screen (the last one added to a
     * GameObjectCollection is rendered on top).
     * @param topLeftCorner The upper-left corner of the counter.
     * @param height Height, in pixels, of the counter. This determines the font-size.
     * @return
     */
    public static GameObject create(Vector2 topLeftCorner, int height, Color color) {
        GameObject fpsCounter = new GameObject(topLeftCorner, Vector2.DOWN.mult(height), null);
        Counter framesCounter = new Counter();
        TextRenderable fpsStringRenderer = new TextRenderable("");
        fpsStringRenderer.setColor(color);
        new ScheduledTask(
                fpsCounter, TIME_BETWEEN_FPS_REFRESHES, true,
                ()->{
                    int fps = (int)(framesCounter.value() / TIME_BETWEEN_FPS_REFRESHES);
                    framesCounter.reset();
                    fpsStringRenderer.setString("FPS: "+fps);
                });
        fpsCounter.renderer().setRenderable(fpsStringRenderer);
        fpsCounter.addComponent(deltaTime -> framesCounter.increment());
        fpsCounter.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        return fpsCounter;
    }
}
