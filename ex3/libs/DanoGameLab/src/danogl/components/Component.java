package danogl.components;

/**
 * Represents a function that should be executed in a GameObject in each update
 * @author Dan Nirel
 */
@FunctionalInterface
public interface Component {
    /**
     * When the a GameObject is assigned this component, it calls this method
     * in every update
     * @param deltaTime the time, in seconds, since the previous update
     */
    void update(float deltaTime);
}
