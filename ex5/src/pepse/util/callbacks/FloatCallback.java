package pepse.util.callbacks;

/**
 * A callback for getting and returning float values.
 * Used to get the height at a given x index.
 */
@FunctionalInterface
public interface FloatCallback {
    float run(float x);
}
