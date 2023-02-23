package pepse.world;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.util.Vector2;
import pepse.util.Constants;
import pepse.util.Pair;
import pepse.world.entity.IDamagable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

/**
 * A class that handles the infinite world creation
 */
public class InfiniteWorldManager {

    private final Map<Pair<Integer>, List<GameObject>> gameObjectsPerChunk;

    private final GameObjectCollection gameObjects;
    private final IWorldGenerator[] generators;

    private final int chunkWidth;
    private final int maxDistanceBeforeUpdated;

    private int minX, maxX;

    public InfiniteWorldManager(GameObjectCollection gameObjects, Vector2 windowDimensions,
                                IWorldGenerator... generators) {
        // Checks the optimal chunks per window variable (starting from 5 down to 1)
        int CHUNKS_PER_WINDOW = 1;

        for (int i = 5; i > 1; i--) {
            if (windowDimensions.x() % i == 0) {
                CHUNKS_PER_WINDOW = i;
                break;
            }
        }

        this.gameObjectsPerChunk = new HashMap<>();

        this.gameObjects = gameObjects;
        this.generators = generators;

        this.chunkWidth = (int) (windowDimensions.x() / CHUNKS_PER_WINDOW);
        this.maxDistanceBeforeUpdated = (int) (windowDimensions.x() - this.chunkWidth);

        this.minX = -this.chunkWidth;
        this.maxX = (int) (windowDimensions.x() + this.chunkWidth);

        this.initWorld();
    }

    /**
     * Initializes the world by creating all initial chunks
     */
    private void initWorld() {
        for (int x = this.minX; x < this.maxX; x += this.chunkWidth) {
            Pair<Integer> pair = new Pair<>(x, (x + this.chunkWidth));

            List<GameObject> createdObjects = new ArrayList<>();

            for (IWorldGenerator generator : this.generators) {
                createdObjects.addAll(generator.createInRangeAndReturn(x, x + this.chunkWidth));
            }

            this.gameObjectsPerChunk.put(pair, createdObjects);
        }
    }

    /**
     * Checks if the currentX (screen middle) is too far right or left.
     * If it is, we want to delete the furthest chunk and create a new one closer to us
     *
     * @param currentX X location of the screen
     */
    public void checkWorld(int currentX) throws ChunkException {
        float distFromMinX = Math.abs(currentX - this.minX);
        float distFromMaxX = Math.abs(currentX - this.maxX);

        Pair<Integer> toDelete, toCreate;

        if (distFromMinX > this.maxDistanceBeforeUpdated) {
            toDelete = new Pair<>(minX, minX + this.chunkWidth);
            toCreate = new Pair<>(maxX, maxX + this.chunkWidth);

            this.minX += this.chunkWidth;
            this.maxX += this.chunkWidth;
        } else if (distFromMaxX > this.maxDistanceBeforeUpdated) {
            toCreate = new Pair<>(minX - this.chunkWidth, minX);
            toDelete = new Pair<>(maxX - this.chunkWidth, maxX);

            this.minX -= this.chunkWidth;
            this.maxX -= this.chunkWidth;
        } else {
            return;
        }

        this.updatedChunks(toDelete, toCreate);
    }

    /**
     * Updates the game chunks by removing and creating chunks
     *
     * @param toDelete Chunk to delete
     * @param toCreate Chunk to create
     */
    private void updatedChunks(Pair<Integer> toDelete, Pair<Integer> toCreate) throws ChunkException {
        if (!this.gameObjectsPerChunk.containsKey(toDelete)) {
            throw new ChunkException("An error occurred while trying to delete chunk: ("
                    + toDelete.getA() + ", " + toDelete.getB() + ").\nChunk not found!");
        }

        if(this.gameObjectsPerChunk.containsKey(toCreate)) {
            throw new ChunkException("An error occurred while trying to create chunk: ("
                    + toCreate.getA() + ", " + toCreate.getB() + ").\nChunk exists!");
        }

        // Trying to remove all game objects in a chunk
        this.gameObjectsPerChunk.get(toDelete).stream().filter(Predicate.not(this::removeGameObject))
                .forEach(failed -> System.err.println(
                        "[DEBUG] couldn't delete object of type " + failed.getTag()));

        // Flushing all removed game objects
        this.gameObjects.update(0);

        this.gameObjectsPerChunk.remove(toDelete);

        List<GameObject> createdObjects = new ArrayList<>();

        for (IWorldGenerator generator : this.generators) {
            createdObjects.addAll(generator.createInRangeAndReturn(toCreate.getA(), toCreate.getB()));
        }

        this.gameObjectsPerChunk.put(toCreate, createdObjects);
    }

    /**
     * Removes a single game object - tries to remove it from all used layers
     *
     * @param gameObject Game object to remove
     * @return Whether the removal was successful
     */
    private boolean removeGameObject(GameObject gameObject) {
        if(gameObject instanceof IDamagable) {
            if(((IDamagable) gameObject).isDead()) {
                return true;
            }
        }

        for (Constants.OBJECT_LAYER objectLayer : Constants.OBJECT_LAYER.values()) {
            if (this.gameObjects.removeGameObject(gameObject, objectLayer.getLayer())) {
                return true;
            }
        }

        return false;
    }


    /**
     * A static class for ChunkException
     */
    public static class ChunkException extends Exception {
        public ChunkException(String message) {
            super(message);
        }
    }
}
