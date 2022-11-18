import danogl.GameManager;
import danogl.util.Vector2;

/**
 * Creates a simple window that does nothing.
 * @author Dan Nirel
 */
public class SimpleWindow extends GameManager {
    public SimpleWindow(String windowTitle, Vector2 windowDimensions) {
        super(windowTitle, windowDimensions);
    }

    public static void main(String[] args) {
        new SimpleWindow("Simple Window", Vector2.of(800, 600)).run();
    }
}
