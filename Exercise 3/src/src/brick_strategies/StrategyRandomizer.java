package src.brick_strategies;

import danogl.GameManager;
import danogl.collisions.GameObjectCollection;
import danogl.gui.Sound;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import src.utils.TrackedGameObject;

import java.util.Random;

public class StrategyRandomizer {

    private final Random random;

    private final GameObjectCollection gameObjects;
    private final GameManager gameManager;
    private final UserInputListener inputListener;
    private final WindowController windowController;

    private final TrackedGameObject ball;

    private final Renderable extraPaddleRenderable;
    private final Vector2 extraPaddleSize;
    private final Vector2 windowDimensions;
    private final int extraPaddlePadding;
    private final Counter extraPaddleLives;

    private final Renderable puckRenderable;
    private final Sound puckSound;

    private final Renderable heartRenderable;
    private final Vector2 heartSize;
    private final Counter lives;


    public StrategyRandomizer(GameObjectCollection gameObjects,
                              GameManager gameManager, UserInputListener userInputListener,
                              WindowController windowController,
                              TrackedGameObject ball,
                              Renderable extraPaddleRenderable, Vector2 extraPaddleSize,
                              Vector2 windowDimensions, int extraPaddlePadding, Counter extraPaddleLives,
                              Renderable puckRenderable, Sound puckSound,
                              Renderable heartRenderable, Vector2 heartSize, Counter lives) {
        this.random = new Random();

        this.gameObjects = gameObjects;
        this.gameManager = gameManager;
        this.inputListener = userInputListener;
        this.windowController = windowController;

        this.ball = ball;

        this.extraPaddleRenderable = extraPaddleRenderable;
        this.extraPaddleSize = extraPaddleSize;
        this.windowDimensions = windowDimensions;
        this.extraPaddlePadding = extraPaddlePadding;
        this.extraPaddleLives = extraPaddleLives;

        this.puckRenderable = puckRenderable;
        this.puckSound = puckSound;

        this.heartRenderable = heartRenderable;
        this.heartSize = heartSize;
        this.lives = lives;
    }

    /**
     * Randomly selects a strategy out of all strategies
     *
     * @return   A new instance of the randomly chosen strategy
     */
    public CollisionStrategy getRandomStrategy() {
        int index = this.random.nextInt(6);

        if(index == 0) {
            return defaultCollisionStrategy();
        }

        return getRandomSpecialStrategy();
    }

    /**
     * Randomly selects a strategy out of the special ones (no CollisionStrategy)
     *
     * @return   A new instance of the randomly chosen strategy
     */
    public CollisionStrategy getRandomSpecialStrategy() {
        int index = this.random.nextInt(5);

        switch (index) {
            case 0: return cameraChangeStrategy();
            case 1: return doublePowerupStrategy();
            case 2: return extraLifeStrategy();
            case 3: return extraPaddleStrategy();
            case 4: return extraPucksStrategy();
        }

        return null;
    }

    /**
     * @return Creates a default Collision Strategy
     */
    private CollisionStrategy defaultCollisionStrategy() {
        return new CollisionStrategy(this.gameObjects);
    }

    /**
     * @return Creates a camera change Collision Strategy
     */
    private CollisionStrategy cameraChangeStrategy() {
        return new CameraChangeStrategy(this.gameObjects,
                this.gameManager,
                this.ball,
                this.windowController);
    }

    /**
     * @return Creates an extra life Collision Strategy
     */
    private CollisionStrategy extraLifeStrategy() {
        return new ExtraLifeStrategy(this.gameObjects,
                this.heartRenderable,
                this.heartSize,
                this.lives);
    }

    /**
     * @return Creates an extra paddle Collision Strategy
     */
    private CollisionStrategy extraPaddleStrategy() {
        return new ExtraPaddleStrategy(this.gameObjects,
                this.extraPaddleSize,
                this.extraPaddleRenderable,
                this.inputListener,
                this.windowDimensions,
                this.extraPaddlePadding,
                this.extraPaddleLives);
    }

    /**
     * @return Creates an extra pucks Collision Strategy
     */
    private CollisionStrategy extraPucksStrategy() {
        return new ExtraPucksStrategy(this.gameObjects,
                this.puckRenderable,
                this.puckSound);
    }

    /**
     * @return Creates a double powerup Collision Strategy
     */
    private CollisionStrategy doublePowerupStrategy() {
        return new DoublePowerupStrategy(this.gameObjects, this);
    }
}
