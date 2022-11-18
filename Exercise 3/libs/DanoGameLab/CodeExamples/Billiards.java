import danogl.*;
import danogl.collisions.*;
import danogl.gui.*;
import danogl.gui.mouse.MouseButton;
import danogl.gui.rendering.*;
import danogl.util.Vector2;

import java.awt.*;
import java.util.function.Consumer;

/**
 * A very simplistic Billiards game.
 * Aim the white ball with the mouse cursor, and shoot with left-click.
 * Important note: the classes appear in the same file only for the sake of easy
 * importing to a different project. In a real project, it would be crucial
 * for development to separate the classes into different files.
 * @author Dan Nirel
 */
public class Billiards extends GameManager {
    /*************** PUBLIC CONSTANTS ****************/
    public static final float BALL_DIAMETER = 50;
    public static final String HOLE_TAG = "hole";
    public static final String WALL_TAG = "wall";

    /*************** PRIVATE CONSTANTS ****************/
    private static final Color BACKGROUND_COLOR = Color.GREEN;
    private static final int HOLE_DIAMETER = 100;
    private static final Color HOLE_COLOR = Color.BLACK;
    private static final Color WALL_COLOR = Color.ORANGE;
    private static final float WALL_WIDTH = 20;
    private static final String HITCOUNT_MESSAGE = "Hits: ";

    /*************** FIELDS ****************/
    private Vector2 windowDimensions;
    private int remainingBalls;
    private WhiteBall whiteBall;
    private WindowController windowController;
    private TextRenderable hitCounterTextRenderable = new TextRenderable(HITCOUNT_MESSAGE+"0");
    private GameObject hitCounter = new GameObject(
            Vector2.ZERO, Vector2.ONES.mult(16), hitCounterTextRenderable);

    /*************** PUBLIC METHODS ****************/
    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader, UserInputListener inputListener, WindowController windowController) {
        this.windowController = windowController;
        super.initializeGame(imageReader, soundReader, inputListener, windowController);
        windowDimensions = windowController.getWindowDimensions();

        // set background color
        gameObjects().addGameObject(
                new GameObject(
                        Vector2.ZERO,
                        windowController.getWindowDimensions(),
                        new RectangleRenderable(BACKGROUND_COLOR)
                ),
                Layer.BACKGROUND
        );

        // create holes
        float width = windowController.getWindowDimensions().x();
        float height = windowController.getWindowDimensions().y();
        createHoleAt(Vector2.ZERO);
        createHoleAt(Vector2.of(width/2,0));
        createHoleAt(Vector2.of(width,0));
        createHoleAt(Vector2.of(0,height));
        createHoleAt(Vector2.of(width/2,height));
        createHoleAt(Vector2.of(width,height));

        createWalls();
        createBalls();

        // add white ball
        whiteBall = new WhiteBall(
                Vector2.of(width/3, height/2), Color.WHITE, inputListener,
                go -> {
                    gameObjects().removeGameObject(go);
                    windowController.showMessageBox("You Lose");
                    windowController.closeWindow();
                });
        gameObjects().addGameObject(whiteBall);
        gameObjects().addGameObject(hitCounter, Layer.UI);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if(remainingBalls == 0) {
            windowController.showMessageBox("You win! "+whiteBall.hitCount()+" hits.");
            windowController.closeWindow();
        }
        hitCounterTextRenderable.setString(HITCOUNT_MESSAGE+whiteBall.hitCount());
    }

    /*************** PRIVATE METHODS ****************/
    private void createBalls() {
        float width = windowDimensions.x();
        float height = windowDimensions.y();
        createBallAt(Vector2.of(2*width/3, height/2));
        createBallAt(Vector2.of(2*width/3+BALL_DIAMETER, height/2-BALL_DIAMETER/2));
        createBallAt(Vector2.of(2*width/3+BALL_DIAMETER, height/2+BALL_DIAMETER/2));
        createBallAt(Vector2.of(2*width/3+2*BALL_DIAMETER, height/2));
        createBallAt(Vector2.of(2*width/3+2*BALL_DIAMETER, height/2-BALL_DIAMETER));
        createBallAt(Vector2.of(2*width/3+2*BALL_DIAMETER, height/2+BALL_DIAMETER));
        remainingBalls = 6;
    }

    private void createBallAt(Vector2 pos) {
        gameObjects().addGameObject(new Ball(pos, Color.RED,
                go ->{
                    gameObjects().removeGameObject(go);
                    remainingBalls--;
                }));
    }

    private void createWalls() {
        float width = windowDimensions.x();
        float height = windowDimensions.y();

        Renderable wallRenderable = new RectangleRenderable(WALL_COLOR);
        GameObject wall;
        wall = new GameObject(Vector2.ZERO, Vector2.of(width, WALL_WIDTH), wallRenderable);
        wall.setTag(WALL_TAG);
        gameObjects().addGameObject(wall, Layer.STATIC_OBJECTS);

        wall = new GameObject(Vector2.ZERO, Vector2.of(WALL_WIDTH, height), wallRenderable);
        wall.setTag(WALL_TAG);
        gameObjects().addGameObject(wall, Layer.STATIC_OBJECTS);

        wall = new GameObject(Vector2.of(0, height-WALL_WIDTH), Vector2.of(width, WALL_WIDTH), wallRenderable);
        wall.setTag(WALL_TAG);
        gameObjects().addGameObject(wall, Layer.STATIC_OBJECTS);

        wall = new GameObject(Vector2.of(width-WALL_WIDTH,0), Vector2.of(WALL_WIDTH, height), wallRenderable);
        wall.setTag(WALL_TAG);
        gameObjects().addGameObject(wall, Layer.STATIC_OBJECTS);
    }

    private void createHoleAt(Vector2 pos) {
        GameObject hole;
        Renderable holeRenderable = new OvalRenderable(HOLE_COLOR);
        hole = new GameObject(Vector2.ZERO, Vector2.ONES.mult(HOLE_DIAMETER), holeRenderable);
        hole.setCenter(pos);
        hole.setTag(HOLE_TAG);
        gameObjects().addGameObject(hole, Layer.STATIC_OBJECTS);
    }

    /*************** MAIN ****************/
    public static void main(String[] args) {
        new Billiards().run();
    }
}

/**
 * A ball! It has deceleration, and handles collisions with balls, walls, and holes.
 */
class Ball extends GameObject {
    private static final float DECELERATION = 50f;
    private Consumer<GameObject> removalCallback;

    public Ball(Vector2 initPos, Color color, Consumer<GameObject> removalCallback) {
        super(Vector2.ZERO, Vector2.ONES.mult(Billiards.BALL_DIAMETER), new OvalRenderable(color));
        this.removalCallback = removalCallback;
        setCenter(initPos);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        setVelocity(getVelocity().
                subtract(getVelocity()
                        .normalized()
                        .mult(DECELERATION *deltaTime)));
        if(getVelocity().approximatelyEquals(Vector2.ZERO))
            setVelocity(Vector2.ZERO);
    }

    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        if(other.getTag().equals(Billiards.WALL_TAG)) {
            setVelocity(getVelocity().flipped(collision.getNormal()));
            return;
        }
        if(other.getTag().equals(Billiards.HOLE_TAG)) {
            setVelocity(Vector2.ZERO);
            renderer().fadeOut(1, ()-> removalCallback.accept(this));
            return;
        }

        float velocityDelta = -collision.getRelativeVelocity().dot(collision.getNormal());
        setVelocity(getVelocity().add(collision.getNormal().mult(velocityDelta)));
    }
}

/**
 * The special white ball. it responds to mouse input and can be hit,
 * and also exposes the number of times it was hit.
 */
class WhiteBall extends Ball {
    private static final float HIT_VELOCITY = 400;
    private static final float ANGLE_CHANGE_RATE = 150;
    private static final GameObject ARROW =
            new GameObject(
                    Vector2.ZERO,
                    Vector2.of(10, 180),
                    new RectangleRenderable(Color.DARK_GRAY));

    private UserInputListener inputListener;
    private float angle = -90;
    private int hitCount = 0;

    public WhiteBall(Vector2 initPos, Color color, UserInputListener inputListener,
                     Consumer<GameObject> removalCallback) {
        super(initPos, color, removalCallback);
        this.inputListener = inputListener;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if(inputListener.isMouseButtonPressed(MouseButton.LEFT_BUTTON) && getVelocity().equals(Vector2.ZERO)) {
            setVelocity(Vector2.UP.mult(HIT_VELOCITY).rotated(angle));
            hitCount++;
        }
        Vector2 mouseDelta = inputListener.getMouseScreenPos().subtract(getCenter());
        angle = -90-(float)Math.toDegrees(Math.atan2(mouseDelta.y(), mouseDelta.x()));
    }

    @Override
    public void render(Graphics2D g) {
        super.render(g);
        if(!getVelocity().equals(Vector2.ZERO))
            return;
        ARROW.setCenter(
                getCenter()
                .add(Vector2.UP.mult(ARROW.getDimensions().y()/2+ Billiards.BALL_DIAMETER)
                .rotated(180+angle)));
        ARROW.renderer().setRenderableAngle(angle);
        ARROW.render(g);
    }

    public int hitCount() { return hitCount; }
}