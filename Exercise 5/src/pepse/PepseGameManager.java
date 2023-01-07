package pepse;

import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.*;
import danogl.gui.rendering.Camera;
import danogl.util.Vector2;

import pepse.util.Constants;
import pepse.world.*;
import pepse.world.daynight.Night;
import pepse.world.daynight.Sun;
import pepse.world.daynight.SunHalo;
import pepse.world.entity.Entity;
import pepse.world.entity.EntityManager;
import pepse.world.trees.Tree;

import java.awt.*;

/**
 * The entry class for the Pepse game
 */
public class PepseGameManager extends GameManager {

    private static final String YOU_LOST_MESSAGE = "You Lost! Want to play again?";

    private Vector2 windowDimensions;
    private WindowController windowController;
    private UserInputListener inputListener;
    private ImageReader imageReader;

    private InfiniteWorldManager infiniteWorldManager;
    private Terrain terrainGenerator;
    private Tree treeGenerator;
    private EntityManager entityGenerator;

    private Entity avatar;

    @Override
    public void run() {
        super.run();
    }


    /**
     * Initializes the game
     *
     * @param imageReader      Contains a single method: readImage, which reads an image from disk.
     *                         See its documentation for help.
     * @param soundReader      Contains a single method: readSound, which reads a wav file from
     *                         disk. See its documentation for help.
     * @param inputListener    Contains a single method: isKeyPressed, which returns whether
     *                         a given key is currently pressed by the user or not. See its
     *                         documentation.
     * @param windowController Contains an array of helpful, self-explanatory methods
     *                         concerning the window.
     */
    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader,
                               UserInputListener inputListener, WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);

        this.windowDimensions = windowController.getWindowDimensions();
        this.windowController = windowController;
        this.inputListener = inputListener;
        this.imageReader = imageReader;

        this.createBackground();
        this.createTerrain();
        this.createEntities();
        this.createAvatar();

        this.initInfiniteWorldManager();
        this.initLayerCollisions();
    }


    /**
     * Updates the world if needed, so a feeling of infinite world is given
     * Also, checks if the avatar's health is 0. If so, it ends the game
     *
     * @param deltaTime The time, in seconds, that passed since the last invocation
     *                  of this method (i.e., since the last frame). This is useful
     *                  for either accumulating the total time that passed since some
     *                  event, or for physics integration (i.e., multiply this by
     *                  the acceleration to get an estimate of the added velocity or
     *                  by the velocity to get an estimate of the difference in position).
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        if (this.infiniteWorldManager != null) {
            try {
                this.infiniteWorldManager.checkWorld((int) this.avatar.getCenter().x());
            } catch(InfiniteWorldManager.ChunkException exception) {
                System.err.println("[DEBUG] " + exception.getMessage());
            }
        }

        if (this.avatar.isDead()) {
            this.restartGame();
        }
    }


    /**
     * Creates the background scene: sky, night and day
     */
    private void createBackground() {
        Sky.create(this.gameObjects(), this.windowDimensions, Layer.BACKGROUND);

        GameObject night = Night.create(this.gameObjects(),
                Constants.OBJECT_LAYER.NIGHT_SKY.getLayer(),
                this.windowDimensions, Constants.DAY_CYCLE_LENGTH);

        GameObject sun = Sun.create(this.gameObjects(),
                Constants.OBJECT_LAYER.SUN.getLayer(),
                this.windowDimensions, Constants.DAY_CYCLE_LENGTH);

        GameObject halo = SunHalo.create(this.gameObjects(),
                Constants.OBJECT_LAYER.SUN_HALO.getLayer(),
                sun, new Color(255, 255, 0, 20));
    }

    /**
     * Creates the terrain scene: ground and trees
     */
    private void createTerrain() {
        this.terrainGenerator = new Terrain(this.gameObjects(),
                Constants.OBJECT_LAYER.COLLIDABLE_TERRAIN.getLayer(),
                Constants.OBJECT_LAYER.TERRAIN.getLayer(),
                this.windowDimensions, Constants.GAME_SEED);

        this.treeGenerator = new Tree(this.gameObjects(),
                Constants.OBJECT_LAYER.LOG.getLayer(),
                Constants.OBJECT_LAYER.LEAF.getLayer(),
                this.windowDimensions, this.terrainGenerator::groundHeightAt);
    }

    /**
     * Creates the game's avatar and sets the camera to follow it
     */
    private void createAvatar() {
        float avatarX = this.windowDimensions.x() / 2;
        float avatarY = this.terrainGenerator.groundHeightAt(avatarX);

        this.avatar = Avatar.create(this.gameObjects(),
                Constants.OBJECT_LAYER.ENTITY.getLayer(),
                new Vector2(avatarX, avatarY),
                this.inputListener, this.imageReader);

        // Setting the camera to track the avatar
        this.setCamera(new Camera(this.avatar, Vector2.ZERO,
                this.windowController.getWindowDimensions(),
                this.windowController.getWindowDimensions()));
    }

    /**
     * Creates the entity manager to create entities across the world
     */
    private void createEntities() {
        this.entityGenerator = new EntityManager(this.gameObjects(),
                Constants.OBJECT_LAYER.ENTITY.getLayer(),
                this.avatar, this.imageReader, this.terrainGenerator::groundHeightAt);
    }


    /**
     * Initializes the infinite world manager
     */
    private void initInfiniteWorldManager() {
        this.infiniteWorldManager = new InfiniteWorldManager(this.gameObjects(), this.windowDimensions,
                this.terrainGenerator, this.treeGenerator, this.entityGenerator);
    }

    /**
     * Initializes collision between layers
     */
    private void initLayerCollisions() {
        // Making leaves collide with terrain
        this.gameObjects().layers().shouldLayersCollide(
                Constants.OBJECT_LAYER.LEAF.getLayer(),
                Constants.OBJECT_LAYER.COLLIDABLE_TERRAIN.getLayer(),
                true);

        // Making entities collide with terrain
        this.gameObjects().layers().shouldLayersCollide(
                Constants.OBJECT_LAYER.ENTITY.getLayer(),
                Constants.OBJECT_LAYER.COLLIDABLE_TERRAIN.getLayer(),
                true);

        // Making entities collide with logs
        this.gameObjects().layers().shouldLayersCollide(
                Constants.OBJECT_LAYER.ENTITY.getLayer(),
                Constants.OBJECT_LAYER.LOG.getLayer(),
                true);
    }


    /**
     * Asks the user whether they want to restart the game
     */
    private void restartGame() {
        if (this.windowController.openYesNoDialog(YOU_LOST_MESSAGE)) {
            this.windowController.resetGame();
        } else {
            this.windowController.closeWindow();
        }
    }


    public static void main(String[] args) {
        new PepseGameManager().run();
    }
}
