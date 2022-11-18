import brick_strategies.CollisionStrategy;
import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.components.CoordinateSpace;
import danogl.gui.*;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import gameobjects.Ball;
import gameobjects.Brick;
import gameobjects.Paddle;

public class BrickerGameManager extends GameManager {

    // TODO remove magic numbers

    public static final float BORDER_WIDTH = 20.0f;

    private static final int INITIAL_HEALTH = 3;

    private static final String BACKGROUND_IMAGE_PATH = "assets/DARK_BG2_small.jpeg";

    private static final String BALL_IMAGE_PATH = "assets/ball.png";
    private static final String BALL_SOUND_PATH = "assets/Bubble5_4.wav";
    private static final int BALL_INITIAL_SPEED = 450;
    private static final int BALL_DIAMETER = 20;

    private static final String PADDLE_IMAGE_PATH = "assets/paddle.png";

    private static final String BRICK_IMAGE_PATH = "assets/brick.png";
    private static final int BRICKS_TOP_PADDING = 40;
    private static final int BRICKS_PER_ROW = 7;
    private static final int BRICK_HEIGHT = 20;
    private static final int BRICK_PADDING = 5;

    private final Vector2 windowDimensions;
    private final Counter health;
    private Counter bricksLeft;
    private GameObject ball;

    public BrickerGameManager(String windowTitle, Vector2 windowDimensions) {
        super(windowTitle, windowDimensions);

        this.windowDimensions = windowDimensions;

        this.health = new Counter(INITIAL_HEALTH);
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

        // Creating scene
        this.createBackground(imageReader);
        this.createBorders();

        // Creating ball
        this.createBall(imageReader, soundReader);

        // Creating paddle
        this.createPaddle(imageReader, inputListener);

        // Creating bricks
        this.createBricks(imageReader, 56);
    }

    /**
     * Updates the game every 1 frame
     *
     * @param deltaTime   game update delta time
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        this.checkHealthStatus();
        this.checkGameEnd();
    }

    /**
     * Checks if the player has lost a heart due to the ball falling too much.
     * If they did lose a heart, we restart the ball's position and velocity
     */
    private void checkHealthStatus() {
        if((this.ball.getCenter().y() + (int)(BALL_DIAMETER/2)) > this.windowDimensions.y()) {
            this.health.decrement();
            this.restartBall();
            System.out.println("[DEBUG] Lost a heart. Hearts left: " + this.health.value());
        }
    }

    /**
     * Checks if the game has ended due to the player breaking all bricks, or dying more than
     * the allowed times
     *
     * @return   Whether the game has ended
     */
    private boolean checkGameEnd() {
        if(this.bricksLeft.value() == 0) {
            System.out.println("[DEBUG] You have won!");
            // TODO win scenario
            return true;
        }

        if(this.health.value() < 0) {
            System.out.println("[DEBUG] You have lost the game!");
            // TODO lose scenario
            return true;
        }

        return false;
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

        this.ball = new Ball(Vector2.ZERO, new Vector2(BALL_DIAMETER, BALL_DIAMETER), image,sound);
        this.restartBall();

        this.gameObjects().addGameObject(this.ball);
    }

    /**
     * Sets the state of the ball (center & velocity) to their initial values
     */
    private void restartBall() {
        this.ball.setCenter(this.windowDimensions.mult(0.5f));
        this.ball.setVelocity(new Vector2(0, BALL_INITIAL_SPEED));
    }

    /**
     * Creates the game paddle
     *
     * @param imageReader        Image reader
     * @param inputListener      Listener to user input
     */
    private void createPaddle(ImageReader imageReader, UserInputListener inputListener) {
        Renderable image = imageReader.readImage(PADDLE_IMAGE_PATH, false);

        GameObject paddle = new Paddle(Vector2.ZERO, new Vector2(100, 15),
                image, inputListener, this.windowDimensions, 20);
        paddle.setCenter(new Vector2(this.windowDimensions.x()/2, this.windowDimensions.y() - 20));

        this.gameObjects().addGameObject(paddle);
    }

    private void createBricks(ImageReader imageReader, int numberOfBreaks) {
        this.bricksLeft = new Counter(numberOfBreaks);

        Renderable image = imageReader.readImage(BRICK_IMAGE_PATH, false);

        // Calculating the length of a single brick with this formula:
        // (Width - left_border - right_border - padding_between_every_brick) / number_of_bricks
        int brickWidth = (int) (this.windowDimensions.x() - BORDER_WIDTH * 2 -
                (BRICK_PADDING*BRICKS_PER_ROW - 1)) / BRICKS_PER_ROW;

        Vector2 brickDimensions = new Vector2(brickWidth, BRICK_HEIGHT);

        for(int i = 0; i < numberOfBreaks; i++) {
            int row = i / BRICKS_PER_ROW;
            int column = i % BRICKS_PER_ROW;

            int currentBrickXPos = (int) (BORDER_WIDTH + (column * (brickWidth + BRICK_PADDING)));
            int currentBrickYPos = (int) (BRICKS_TOP_PADDING + (row * (BRICK_HEIGHT + BRICK_PADDING)));

            Vector2 brickPosition = new Vector2(currentBrickXPos, currentBrickYPos);
            GameObject brick = new Brick(brickPosition, brickDimensions, image,
                    new CollisionStrategy(this.gameObjects()), bricksLeft);

            this.gameObjects().addGameObject(brick);
        }
    }

    public static void main(String[] args) {
        new BrickerGameManager("Bricker Game", new Vector2(700, 500)).run();
    }
}
