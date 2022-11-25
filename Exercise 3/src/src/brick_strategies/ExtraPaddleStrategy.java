package src.brick_strategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import src.gameobjects.FinitePaddle;

public class ExtraPaddleStrategy extends CollisionStrategy {

    private static final int EXTRA_PADDLE_INITIAL_LIFE = 3;

    private final Vector2 paddleSize;
    private final Renderable renderable;
    private final UserInputListener userInputListener;
    private final Vector2 windowDimensions;
    private final int minDistFromEdge;
    private final Counter extraPaddleLife;

    public ExtraPaddleStrategy(GameObjectCollection gameObjects, Vector2 paddleSize, Renderable renderable,
                               UserInputListener userInputListener, Vector2 windowDimensions,
                               int minDistFromEdge, Counter extraPaddleLife) {
        super(gameObjects);

        this.paddleSize = paddleSize;
        this.renderable = renderable;
        this.userInputListener = userInputListener;
        this.windowDimensions = windowDimensions;
        this.minDistFromEdge = minDistFromEdge;
        this.extraPaddleLife = extraPaddleLife;
    }


    @Override
    public void onCollision(GameObject collidedObject, GameObject colliderObject, Counter bricksCounter) {
        super.onCollision(collidedObject, colliderObject, bricksCounter);

        // We only want to create a new Extra Paddle if extraPaddleLife is 0, meaning we removed the last one
        if(this.extraPaddleLife.value() == 0) {
            FinitePaddle finitePaddle = new FinitePaddle(Vector2.ZERO, this.paddleSize, this.renderable,
                    this.userInputListener, this.windowDimensions, this.minDistFromEdge,
                    this.extraPaddleLife);

            finitePaddle.setCenter(new Vector2(windowDimensions.x() / 2, windowDimensions.y() / 2));

            super.gameObjects.addGameObject(finitePaddle);

            this.extraPaddleLife.increaseBy(EXTRA_PADDLE_INITIAL_LIFE);
        }
    }
}
