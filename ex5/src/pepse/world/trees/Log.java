package pepse.world.trees;

import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.world.Block;

import java.awt.*;

/**
 * A class that represents a single log game object
 */
public class Log extends Block {

    private static final Color LOG_COLOR = new Color(100, 50, 20);

    private static final String TAG = "log";

    public Log(Vector2 topLeftCorner) {
        super(topLeftCorner, new RectangleRenderable(ColorSupplier.approximateColor(LOG_COLOR)));

        this.setTag(TAG);
    }
}
