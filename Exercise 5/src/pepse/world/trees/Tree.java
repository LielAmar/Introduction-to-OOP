package pepse.world.trees;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.util.Vector2;

import pepse.util.Constants;
import pepse.util.callbacks.FloatCallback;
import pepse.util.Utils;
import pepse.world.Block;
import pepse.world.IWorldGenerator;

import java.util.*;

/**
 * A class that handles the creation of trees within the game
 */
public class Tree implements IWorldGenerator {

    private final GameObjectCollection gameObjects;
    private final Vector2 windowDimensions;
    private final int logLayer;
    private final int leavesLayer;

    private final FloatCallback callback;

    private Random random;

    public Tree(GameObjectCollection gameObjects, int layer, Vector2 windowDimensions,
                FloatCallback callback) {
        this(gameObjects, layer, layer, windowDimensions, callback);
    }

    public Tree(GameObjectCollection gameObjects, int logLayer, int leavesLayer,
                Vector2 windowDimensions, FloatCallback callback) {
        this.gameObjects = gameObjects;
        this.logLayer = logLayer;
        this.leavesLayer = leavesLayer;

        this.windowDimensions = windowDimensions;
        this.callback = callback;
    }


    /**
     * Creates the trees terrain in a given range
     *
     * @param minX Range starting point
     * @param maxX Range ending point
     */
    public void createInRange(int minX, int maxX) {
        this.createInRangeAndReturn(minX, maxX);
    }

    /**
     * Creates the trees terrain in a given range and returns a list of created game objects
     *
     * @param minX Range starting point
     * @param maxX Range ending point
     * @return All game objects created within the given range
     */
    @Override
    public List<GameObject> createInRangeAndReturn(int minX, int maxX) {
        List<GameObject> createdObjects = new ArrayList<>();

        // Creating a random with a specific seed to be able to regenerate the same chunk
        this.random = new Random(Objects.hash(minX, maxX));

        minX = Utils.round(minX, Block.SIZE);
        maxX = Utils.round(maxX, Block.SIZE);

        for (int x = minX; x < maxX; x += Block.SIZE) {
            // We don't want to create a tree around the middle
            // of the screen to prevent issues with avatar
            if(Math.abs(x - (windowDimensions.x() / 2)) < 100) {
                continue;
            }

            if (this.random.nextInt(Constants.TREE_SPAWN_RATE) == 0) {
                this.createTree(createdObjects, x, this.random.nextInt(6) + 5);

                x += 30; // Increasing x by another 30 so there can't be 2 trees next to each other
            }
        }

        return createdObjects;
    }


    /**
     * Creates a single tree
     *
     * @param createdObjects List to add the created game objects to
     * @param xLocation      X location of the tree to create
     * @param treeHeight     Height of the tree to create
     */
    private void createTree(List<GameObject> createdObjects, int xLocation, int treeHeight) {
        float baseY = this.callback.run(xLocation);

        baseY = (float) Math.min(
                Math.floor(baseY / Block.SIZE) * Block.SIZE,
                windowDimensions.y() - Block.SIZE);

        this.createLog(createdObjects, xLocation, baseY, treeHeight);
        this.createLeaves(createdObjects, xLocation, baseY, treeHeight);
    }

    /**
     * Creates all the leaves of the current tree and adds them to the given list
     *
     * @param createdObjects List to add created leaves to
     * @param xLocation      X location of the tree to create the leaves of
     * @param height         Ground height at the tree to create the leaves of
     * @param treeHeight     Height of the tree to create
     */
    private void createLeaves(List<GameObject> createdObjects,
                              float xLocation, float height, int treeHeight) {
        int startX = (int) xLocation - (Block.SIZE * (Constants.LEAFS_GRID_SIZE / 2));
        int endX = (int) xLocation + Block.SIZE + (Block.SIZE * (Constants.LEAFS_GRID_SIZE / 2));

        int startY = (int) (height - treeHeight * Block.SIZE) -
                (Block.SIZE * (Constants.LEAFS_GRID_SIZE / 2));
        int endY = (int) (height - treeHeight * Block.SIZE) + Block.SIZE +
                (Block.SIZE * (Constants.LEAFS_GRID_SIZE / 2));

        for (int i = startX; i < endX; i += Block.SIZE) {
            for (int j = startY; j < endY; j += Block.SIZE) {
                if (this.random.nextInt(Constants.LEAF_SPAWN_RATE) == 0) {
                    Leaf leaf = new Leaf(new Vector2(i, j));

                    this.gameObjects.addGameObject(leaf, this.leavesLayer);

                    createdObjects.add(leaf);
                }
            }
        }
    }

    /**
     * Creates all the logs of the current tree and adds them to the given list
     *
     * @param createdObjects List to add created logs to
     * @param xLocation      X location of the tree to create the logs of
     * @param height         Ground height at the tree to create the logs of
     * @param treeHeight     Height of the tree to create
     */
    private void createLog(List<GameObject> createdObjects,
                           float xLocation, float height, int treeHeight) {
        for (int i = 0; i < treeHeight; i++) {
            Log log = new Log(new Vector2(xLocation, height - ((i + 1) * Block.SIZE)));

            this.gameObjects.addGameObject(log, this.logLayer);

            createdObjects.add(log);
        }
    }
}
