package danogl;

import danogl.collisions.GameObjectCollection;
import danogl.components.CoordinateSpace;
import danogl.gui.mouse.MouseActionParams;
import danogl.gui.rendering.Camera;
import danogl.util.*;
import danogl.gui.*;
import java.awt.*;

/**
 * The principle class of the game.
 * To have an instance called and used, your main method (which can be anywhere)
 * should create a new GameGUIComponent instance and supply it with an instance of GameManager
 * (or of a deriving class).
 * The application is implemented by altering the behavior of the public instance
 * methods of this class, which are called automatically by the GameGUIComponent.
 * Note that the class contains a builtin instance of GameObjectCollection,
 * the methods of which are called in the respective places.
 * You can therefore add and remove GameObjects in a class extending
 * GameManager using this collection (via its protected accessor gameObjects()).
 * @author Dan Nirel
 */
public class GameManager {
    private static final String DEFAULT_WINDOW_TITLE = "DanoGameLab";
    private final String windowTitle;
    private final Vector2 windowDimensions;
    private GameObjectCollection gameObjects;
    private GameGUIComponent gameGUIComponent;
    private Camera camera;
    private NotifyOfMouseActions mouseActionNotifier;

    /**
     * Creates a new full-screen window with a default title.
     * The window's size will be the main screen's resolution.
     */
    public GameManager() {
        this(DEFAULT_WINDOW_TITLE, null);
    }

    /**
     * Creates a new full-screen window with the specified title.
     * The window's size will be the main screen's resolution.
     */
    public GameManager(String windowTitle) {
        this(windowTitle, null);
    }

    /**
     * Creates a new window with the specified title and of the specicied dimensions
     * @param windowTitle can be null to indicate the usage of the default window title
     * @param windowDimensions dimensions in pixels. can be null to indicate a
     *                         full-screen window whose size in pixels is the main screen's resolution
     */
    public GameManager(String windowTitle, Vector2 windowDimensions) {
        if(windowTitle == null)
            windowTitle = DEFAULT_WINDOW_TITLE;
        this.windowTitle = windowTitle;
        this.windowDimensions = windowDimensions;
    }

    /**
     * Begins execution of the GameManager: after calling this method,
     * initializeGame will run and then all other callbacks.
     */
    public void run() {
        if(windowDimensions == null) {
            gameGUIComponent = new GameGUIComponent(this, windowTitle);
        }
        else {
            gameGUIComponent = new GameGUIComponent(this, windowTitle,
                    (int) windowDimensions.x(),
                    (int) windowDimensions.y(), false);
        }
        gameGUIComponent.run();
    }

    /**
     * The method will be called once when a GameGUIComponent is created,
     * and again after every invocation of windowController.resetGame().
     * @param imageReader Contains a single method: readImage, which reads an image from disk.
     *                 See its documentation for help.
     * @param soundReader Contains a single method: readSound, which reads a wav file from
     *                    disk. See its documentation for help.
     * @param inputListener Contains a single method: isKeyPressed, which returns whether
     *                      a given key is currently pressed by the user or not. See its
     *                      documentation.
     * @param windowController Contains an array of helpful, self explanatory methods
     *                         concerning the window.
     * @see ImageReader
     * @see SoundReader
     * @see UserInputListener
     * @see WindowController
     */
    public void initializeGame(
            ImageReader imageReader,
            SoundReader soundReader, UserInputListener inputListener,
            WindowController windowController) {
        gameObjects = new GameObjectCollection();
        mouseActionNotifier = new NotifyOfMouseActions(this, inputListener);
        gameGUIComponent.addMouseListener(mouseActionNotifier);
        gameGUIComponent.addMouseWheelListener(mouseActionNotifier);
        setCamera(null);
    }

    /**
     * Called once per frame. Any logic is put here. Rendering, on the other hand,
     * should only be done within 'render'.
     * Note that the time that passes between subsequent calls to this method is not constant.
     * @param deltaTime The time, in seconds, that passed since the last invocation
     *                  of this method (i.e., since the last frame). This is useful
     *                  for either accumulating the total time that passed since some
     *                  event, or for physics integration (i.e., multiply this by
     *                  the acceleration to get an estimate of the added velocity or
     *                  by the velocity to get an estimate of the difference in position).
     */
    public void update(float deltaTime) {
        //update all objects and look for collisions.
        for(GameObject obj : gameObjects)
            obj.update(deltaTime);
        if(camera != null)
            camera.update(deltaTime);
        gameObjects.update(deltaTime);
        gameObjects.handleCollisions();
    }

    /**
     * Called automatically every frame. Only use this method for rendering;
     * any logical calculations should be made in update.
     * @param g A utility object for drawing shapes, text, and images.
     */
    public void render(Graphics2D g) {
        for (GameObject obj : gameObjects)
            obj.render(g, camera);
    }

    /**
     * Returns the current camera.
     * @see danogl.gui.rendering.Camera
     */
    public Camera camera() {
        return camera;
    }

    /**
     * Gets the current camera. Cameras are used to render objects not according to their
     * world-position, for example following an avatar. A value of null means
     * world-position is used as rendering-coordinates as well.
     */
    public Camera getCamera() {
        return camera;
    }

    /**
     * Sets the current camera. Cameras are used to render objects not according to their
     * world-position, for example following an avatar. To disable an existing camera, use null.
     * @param camera the new camera to use
     * @see danogl.gui.rendering.Camera
     */
    public void setCamera(Camera camera) {
        this.camera = camera;
        mouseActionNotifier.setScreenToWorldPos(camera == null?null:camera::screenToWorldCoords);
    }

    /**
     * Called upon mouse actions - buttons and scrolling
     * @param params info regarding the action
     */
    public void onMouseAction(MouseActionParams params) {
        var mouseScreenPos = params.getMouseScreenPos();
        var mouseWorldPos = params.getMouseWorldPos();

        for(var obj : gameObjects.reverseOrder()) {
            var mousePos =
                    obj.getCoordinateSpace() == CoordinateSpace.CAMERA_COORDINATES?
                    mouseScreenPos: mouseWorldPos;
            if(obj.containsPoint(mousePos)) {
                obj.onMouseAction(params);
            }
        }
    }

    /**
     * A container for accumulating/removing instances of GameObject.
     * All such instances within the same instance of GameObjectCollection
     * can also collide with each other using the GameObjectCollection.handleCollisions()
     * method.
     * @see GameObjectCollection
     */
    protected GameObjectCollection gameObjects() {
        return gameObjects;
    }

    /**
     * For advanced users who wish to alter the implementation of the game-object collection.
     */
    protected void setGameObjectsCollection(GameObjectCollection gameObjects) {
        this.gameObjects = gameObjects;
    }
}
