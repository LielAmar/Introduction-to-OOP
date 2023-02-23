package pepse.world;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;

import pepse.util.ColorSupplier;
import pepse.util.NoiseGenerator;
import pepse.util.Utils;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * A class that handles the creation of terrain within the game
 */
public class Terrain implements IWorldGenerator {

    private static final Color BASE_GROUND_COLOR = new Color(212, 123, 74);

    private static final int NOISE_SMOOTHNESS = 35;
    private static final int TERRAIN_DEPTH = 20;
    private static final int COLLIDABLE_DEPTH = 2;

    private final GameObjectCollection gameObjects;
    private final Vector2 windowDimensions;

    private final int collidableGroundLayer;
    private final int groundLayer;

    private final float groundHeightAtX0;
    private final NoiseGenerator noiseGenerator;

    public Terrain(GameObjectCollection gameObjects, int groundLayer, Vector2 windowDimensions, int seed) {
        this(gameObjects, groundLayer, groundLayer, windowDimensions, seed);
    }

    public Terrain(GameObjectCollection gameObjects, int collidableGroundLayer,
                   int groundLayer, Vector2 windowDimensions, int seed) {
        this.gameObjects = gameObjects;
        this.windowDimensions = windowDimensions;

        this.collidableGroundLayer = collidableGroundLayer;
        this.groundLayer = groundLayer;

        this.noiseGenerator = new NoiseGenerator(seed);
        this.groundHeightAtX0 = ((float) 2 / 3) * windowDimensions.y();
    }


    /**
     * Returns the ground height at a certain point
     *
     * @param x Point to return its height
     * @return Height at X
     */
    public float groundHeightAt(float x) {
        return this.groundHeightAtX0 +
                Math.abs(this.noiseGenerator.noise(x / NOISE_SMOOTHNESS)) * this.groundHeightAtX0;
    }


    /**
     * Creates the ground terrain in a given range
     *
     * @param minX Range starting point
     * @param maxX Range ending point
     */
    public void createInRange(int minX, int maxX) {
        this.createInRangeAndReturn(minX, maxX);
    }

    /**
     * Creates the ground terrain in a given range and returns a list of created game objects
     *
     * @param minX Range starting point
     * @param maxX Range ending point
     * @return All game objects created within the given range
     */
    @Override
    public List<GameObject> createInRangeAndReturn(int minX, int maxX) {
        List<GameObject> createdObjects = new ArrayList<>();

        minX = Utils.round(minX, Block.SIZE);
        maxX = Utils.round(maxX, Block.SIZE);

        for (float x = minX; x < maxX; x += Block.SIZE) {
            float height = this.groundHeightAt(x);

            height = (float) Math.min(Math.floor(height / Block.SIZE) * Block.SIZE,
                    this.windowDimensions.y() - Block.SIZE);

            for (int i = 0; i < TERRAIN_DEPTH; i++) {
                createdObjects.add(this.createBlock(i, x, height + (i * Block.SIZE)));
            }
        }

        return createdObjects;
    }


    /**
     * Creates a single block at the given coordinates
     *
     * @param depth Depth of the block
     * @param x     Block X
     * @param y     Block Y
     * @return Created block
     */
    private Block createBlock(int depth, float x, float y) {
        RectangleRenderable renderableBlock = new RectangleRenderable(
                ColorSupplier.approximateColor(BASE_GROUND_COLOR));

        Block newBlock = new Block(new Vector2(x, y), renderableBlock);

        this.gameObjects.addGameObject(newBlock,
                depth < COLLIDABLE_DEPTH ? this.collidableGroundLayer : this.groundLayer);

        return newBlock;
    }
}
