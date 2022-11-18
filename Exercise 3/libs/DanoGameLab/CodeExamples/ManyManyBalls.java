import danogl.GameManager;
import danogl.GameObject;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.gui.rendering.OvalRenderable;
import danogl.util.Vector2;

import java.awt.*;
import java.util.Random;

/**
 * Creates many many balls that move around slowly.
 * @author Dan Nirel
 */
public class ManyManyBalls extends GameManager {
    private static final int BALL_DIAMETER = 100;
    private static final float BALL_VELOCITY = 1f;
    private static final Color BALL_COLOR = Color.ORANGE;

    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader, UserInputListener inputListener, WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);

        Random rand = new Random();

        for(int y = 0 ; y < windowController.getWindowDimensions().y() ; y += BALL_DIAMETER) {
            for(int x = 0 ; x < windowController.getWindowDimensions().x() ; x += BALL_DIAMETER) {
                GameObject ball =
                        new GameObject(
                            Vector2.of(x,y),
                            Vector2.ONES.mult(BALL_DIAMETER),
                            new OvalRenderable(BALL_COLOR)
                        );
                ball.setVelocity(Vector2.UP.mult(BALL_VELOCITY).rotated(rand.nextInt(360)));
                gameObjects().addGameObject(ball);
            }
        }
    }

    public static void main(String[] args) {
        new ManyManyBalls().run();
    }
}
