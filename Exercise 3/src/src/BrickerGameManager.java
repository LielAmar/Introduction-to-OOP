package src;

import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.components.CoordinateSpace;
import danogl.gui.*;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import src.brick_strategies.*;
import src.gameobjects.*;
import src.utils.BoundedCounter;
import src.utils.TrackedGameObject;

import java.awt.event.KeyEvent;

/**
 * This class handles the main bricker game logic and creates all necessary objects
 */
public class BrickerGameManager extends GameManager {

    public static final float BORDER_WIDTH = 20.0f;

    private static final int INITIAL_LIVES = 3;
    private static final int LIVES_UPPER_BOUND = 4;
    private static final int INITIAL_NUMBER_OF_BRICKS = 56;

    private static final String BACKGROUND_IMAGE_PATH = "assets/DARK_BG2_small.jpeg";

    private static final String BALL_IMAGE_PATH = "assets/ball.png";
    private static final String BALL_SOUND_PATH = "assets/Bubble5_4.wav";
    private static final int BALL_INITIAL_SPEED = 450;
    private static final int BALL_DIAMETER = 20;

    private static final String PADDLE_IMAGE_PATH = "assets/paddle.png";
    private static final Vector2 PADDLE_SIZE = new Vector2(100, 20);
    private static final int PADDLE_PADDING = 20;

    private static final String BRICK_IMAGE_PATH = "assets/brick.png";
    private static final int BRICKS_TOP_PADDING = 40;
    private static final int BRICKS_PER_ROW = 7;
    private static final int BRICK_HEIGHT = 15;
    private static final int BRICK_PADDING = 2;

    private static final String HEART_IMAGE_PATH = "assets/heart.png";
    private static final int HEART_DIAMETER = 30;

    private static final String PUCK_IMAGE_PATH = "assets/mockBall.png";
    private static final String PUCK_SOUND_PATH = "assets/Bubble5_4.wav";

    private static final String EXTRA_PADDLE_IMAGE_PATH = "assets/botGood.png";

    private static final int MAX_HITS_PER_CAMERA_CHANGE = 4;

    private final Vector2 windowDimensions;
    private WindowController windowController;
    private UserInputListener inputListener;
    private Counter lives;
    private Counter extraPaddleLives;
    private Counter bricksLeft;
    private TrackedGameObject ball;

    private StrategyRandomizer strategyRandomizer;

    public BrickerGameManager(String windowTitle, Vector2 windowDimensions) {
        super(windowTitle, windowDimensions);

        this.windowDimensions = windowDimensions;
    }

    /**
     * Initializes the game with all relevant game objects
     *
     * @param imageReader        Image reader
     * @param soundReader        Sound reader
     * @param inputListener      Listener to user input
     * @param windowController   Window controller
     */
    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader,
                               UserInputListener inputListener, WindowController windowController) {
        // Initialization
        super.initializeGame(imageReader, soundReader, inputListener, windowController);

        this.windowController = windowController;
        this.inputListener = inputListener;
        this.lives = new BoundedCounter(INITIAL_LIVES, LIVES_UPPER_BOUND); // Upper bounded
        this.extraPaddleLives = new Counter(0);

        // Creating scene
        this.createBackground(imageReader);
        this.createLivesCounters(imageReader);

        // Creating borders
        this.createBorders();

        // Creating ball
        this.createBall(imageReader, soundReader);

        // Creating paddle
        this.createPaddle(imageReader);

        this.createStrategyRandomizer(imageReader, soundReader);

        // Creating bricks
        this.createBricks(imageReader);
    }

    /**
     * Updates the game every 1 frame
     *
     * @param deltaTime   game update delta time
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        this.checkPucks();
        this.checkExtraPaddle();
        this.checkHearts();
        this.checkCameraStatus();

        this.checkLivesStatus();
        this.checkGameEnd();

        if(this.inputListener.isKeyPressed(KeyEvent.VK_W)) {
            this.bricksLeft.reset();
            this.checkGameEnd();
        }
    }


    /**
     * Loops over all pucks and if a puck is outside the game's borders, it removes it from the
     * gameObjects list.
     */
    private void checkPucks() {
        for(GameObject gameObject : super.gameObjects()) {
            if(gameObject instanceof Puck) {
                if(this.isObjectOutsideBorders(gameObject)) {
                    System.out.println("[DEBUG] removing puck");
                    super.gameObjects().removeGameObject(gameObject);
                }
            }
        }
    }

    /**
     * If our extra paddle life is 0, we want to loop over all finite paddles and remove them.
     */
    private void checkExtraPaddle() {
        if(this.extraPaddleLives.value() == 0) {
            for(GameObject gameObject : super.gameObjects()) {
                if(gameObject instanceof FinitePaddle) {
                    super.gameObjects().removeGameObject(gameObject);
                    System.out.println("[DEBUG] removing extra paddle");
                }
            }
        }
    }

    /**
     * Loops over all hearts and if a heart has been used we remove it.
     */
    private void checkHearts() {
        for(GameObject gameObject : super.gameObjects()) {
            if(gameObject instanceof Heart) {
                if(((Heart)gameObject).hasBeenUsed()) {
                    super.gameObjects().removeGameObject(gameObject);
                    System.out.println("[DEBUG] removing heart");
                }
            }
        }
    }

    /**
     * Checks if the current camera needs to be removed - if we have a camera and the ball collided
     * more than MAX_HITS_PER_CAMERA_CHANGE times
     */
    private void checkCameraStatus() {
        if(super.getCamera() != null) {
            if(this.ball.getTrackerValue() >= MAX_HITS_PER_CAMERA_CHANGE) {
                super.setCamera(null);
            }
        }
    }

    /**
     * Checks if the player has lost a heart due to the ball falling too much.
     * If they did lose a heart, we restart the ball's position and velocity
     */
    private void checkLivesStatus() {
        if(this.isObjectOutsideBorders(this.ball)) {
            this.lives.decrement();
            this.restartBallPosition();
        }
    }

    /**
     * Checks if the game has ended due to the player breaking all bricks, or dying more than
     * the allowed times
     */
    private void checkGameEnd() {
        String promptMessage = "";
        if(this.bricksLeft.value() == 0) {
            promptMessage = "You Won! ";
        }

        if(this.lives.value() <= 0) {
            promptMessage = "You Lost! ";
        }

        if(promptMessage.isEmpty()) {
            return;
        }

        if(this.windowController.openYesNoDialog(promptMessage + "Play again?")) {
            this.windowController.resetGame();
        } else {
            this.windowController.closeWindow();
        }
    }

    /**
     * Checks whether the given game object is within the game borders
     *
     * @param gameObject   Game object to check
     * @return             Whether the given object is within the game borders
     */
    private boolean isObjectOutsideBorders(GameObject gameObject) {
        return (gameObject.getCenter().y() + (int) (gameObject.getDimensions().y() / 2))
                > this.windowDimensions.y();
    }


    /**
     * Creates the game background
     *
     * @param imageReader        Image reader
     */
    private void createBackground(ImageReader imageReader) {
        Renderable image = imageReader.readImage(BACKGROUND_IMAGE_PATH  , false);

        GameObject background = new GameObject(Vector2.ZERO, this.windowDimensions, image);
        background.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);

        this.gameObjects().addGameObject(background, Layer.BACKGROUND);
    }

    /**
     * Creates the lives counters
     */
    private void createLivesCounters(ImageReader imageReader) {
        Renderable image = imageReader.readImage(HEART_IMAGE_PATH, true);

        GameObject graphicalLifeCounter = new GraphicLifeCounter(
                new Vector2(BORDER_WIDTH, this.windowDimensions.y() - BORDER_WIDTH * 2 - HEART_DIAMETER),
                new Vector2(HEART_DIAMETER, HEART_DIAMETER),
                this.lives,
                image,
                this.gameObjects(),
                this.lives.value()
        );

        this.gameObjects().addGameObject(graphicalLifeCounter, Layer.BACKGROUND);

        GameObject numericLifeCounter = new NumericLifeCounter(
                this.lives,
                new Vector2(BORDER_WIDTH + HEART_DIAMETER * (LIVES_UPPER_BOUND + 1),
                        this.windowDimensions.y() - BORDER_WIDTH * 2 - HEART_DIAMETER),
                new Vector2(HEART_DIAMETER, HEART_DIAMETER),
                this.gameObjects()
        );

        this.gameObjects().addGameObject(numericLifeCounter, Layer.BACKGROUND);
    }

    /**
     * Creates the game borders
     */
    private void createBorders() {
        // Right Border
        this.gameObjects().addGameObject(
                new GameObject(Vector2.ZERO, new Vector2(BORDER_WIDTH, windowDimensions.y()), null)
        );

        // Left Border
        this.gameObjects().addGameObject(
                new GameObject(new Vector2(windowDimensions.x() - BORDER_WIDTH, 0),
                        new Vector2(BORDER_WIDTH, windowDimensions.y()), null)
        );

        // Top Border
        // noinspection SuspiciousNameCombination
        this.gameObjects().addGameObject(
                new GameObject(Vector2.ZERO, new Vector2(windowDimensions.x(), BORDER_WIDTH), null)
        );
    }

    /**
     * Creates the game ball
     *
     * @param imageReader        Image reader
     * @param soundReader        Sound reader
     */
    private void createBall(ImageReader imageReader, SoundReader soundReader) {
        Renderable image = imageReader.readImage(BALL_IMAGE_PATH, true);
        Sound sound = soundReader.readSound(BALL_SOUND_PATH);

        this.ball = new Ball(
                Vector2.ZERO,
                new Vector2(BALL_DIAMETER, BALL_DIAMETER),
                image,
                sound
        );

        this.restartBallPosition();

        this.gameObjects().addGameObject(this.ball);
    }

    /**
     * Sets the state of the ball (center & velocity) to their initial values
     */
    private void restartBallPosition() {
        this.ball.setCenter(this.windowDimensions.mult(0.5f));
        this.ball.setVelocity(new Vector2(0, BALL_INITIAL_SPEED));
    }

    /**
     * Creates the game paddle
     *
     * @param imageReader        Image reader
     */
    private void createPaddle(ImageReader imageReader) {
        Renderable image = imageReader.readImage(PADDLE_IMAGE_PATH, false);

        GameObject paddle = new Paddle(Vector2.ZERO, PADDLE_SIZE,
                image, this.inputListener, this.windowDimensions, PADDLE_PADDING);
        paddle.setCenter(new Vector2(this.windowDimensions.x()/2,
                this.windowDimensions.y() - PADDLE_PADDING));

        this.gameObjects().addGameObject(paddle);
    }

    /**
     * After creating all other objects but bricks, we create brick strategies that would be using
     * all other objects to have powerup functionality.
     */
    private void createStrategyRandomizer(ImageReader imageReader, SoundReader soundReader) {
        Renderable puckImage = imageReader.readImage(PUCK_IMAGE_PATH, true);
        Sound puckSound = soundReader.readSound(PUCK_SOUND_PATH);

        Renderable extraPaddle = imageReader.readImage(EXTRA_PADDLE_IMAGE_PATH, false);

        Renderable extraHeartImage = imageReader.readImage(HEART_IMAGE_PATH, true);

        Vector2 heartSize = new Vector2(HEART_DIAMETER, HEART_DIAMETER);

        this.strategyRandomizer = new StrategyRandomizer(
                super.gameObjects(),
                this,
                this.inputListener,
                this.windowController,

                this.ball,

                extraPaddle,
                PADDLE_SIZE,
                this.windowDimensions,
                PADDLE_PADDING,
                this.extraPaddleLives,

                puckImage,
                puckSound,

                extraHeartImage,
                heartSize,
                this.lives
        );
    }

    /**
     * Creates a grid of ${numberOfBricks} bricks
     *
     * @param imageReader Image renderer
     */
    private void createBricks(ImageReader imageReader) {
        this.bricksLeft = new Counter(BrickerGameManager.INITIAL_NUMBER_OF_BRICKS);

        Renderable image = imageReader.readImage(BRICK_IMAGE_PATH, false);

        // Calculating the length of a single brick with this formula:
        // (Width - left_border - right_border - padding_between_every_brick) / number_of_bricks
        int brickWidth = (int) (this.windowDimensions.x() - BORDER_WIDTH * 2 -
                (BRICK_PADDING*BRICKS_PER_ROW - 1)) / BRICKS_PER_ROW;

        Vector2 brickDimensions = new Vector2(brickWidth, BRICK_HEIGHT);

        for(int i = 0; i < BrickerGameManager.INITIAL_NUMBER_OF_BRICKS; i++) {
            int row = i / BRICKS_PER_ROW;
            int column = i % BRICKS_PER_ROW;

            int currentBrickXPos = (int) (BORDER_WIDTH + (column * (brickWidth + BRICK_PADDING)));
            int currentBrickYPos = (BRICKS_TOP_PADDING + (row * (BRICK_HEIGHT + BRICK_PADDING)));

            Vector2 brickPosition = new Vector2(currentBrickXPos, currentBrickYPos);

            GameObject brick;

            brick = new Brick(brickPosition, brickDimensions, image,
                    this.strategyRandomizer.getRandomStrategy(),
                        bricksLeft);

            this.gameObjects().addGameObject(brick);
        }
    }


    public static void main(String[] args) {
        new BrickerGameManager("Bricker Game", new Vector2(700, 500)).run();
    }
}
