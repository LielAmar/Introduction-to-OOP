package pepse.world;

import danogl.GameObject;

import java.util.List;

/**
 * An interface for classes that handle creation of terrain & elements within it
 */
public interface IWorldGenerator {

    /**
     * Creates the terrain/objects within the given chunk, and returns a list of all
     * game objects created during this phase.
     *
     * @param minX   Starting x of the chunk
     * @param maxX   Ending x of the chunk
     * @return       List of created game objects
     */
    List<GameObject> createInRangeAndReturn(int minX, int maxX);
}
