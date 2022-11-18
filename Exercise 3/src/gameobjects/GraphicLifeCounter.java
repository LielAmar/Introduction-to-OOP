package gameobjects;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

/**
 * A class that represents the graphical life counter game object and handles its logic
 */
public class GraphicLifeCounter extends GameObject {

    private static final int WIDGET_PADDING = 3;

    private final Vector2 widgetTopLeftCorner;
    private final Vector2 widgetDimensions;
    private final Renderable widgetRenderable;

    private final GameObjectCollection gameObjects;
    private final Counter livesCounter;
    private int numOfLives;

    private final GameObject[] hearts;

    public GraphicLifeCounter(Vector2 widgetTopLeftCorner, Vector2 widgetDimensions, Counter livesCounter,
                       Renderable widgetRenderable, GameObjectCollection gameObjectsCollection,
                       int numOfLives) {
        super(widgetTopLeftCorner, Vector2.ZERO, null);

        this.widgetTopLeftCorner = widgetTopLeftCorner;
        this.widgetDimensions = widgetDimensions;
        this.widgetRenderable = widgetRenderable;

        this.gameObjects = gameObjectsCollection;
        this.livesCounter = livesCounter;
        this.numOfLives = numOfLives;

        this.hearts = new GameObject[this.numOfLives];

        this.initiateHearts();
    }

    /**
     * Initiates the ${numOfLives} heart objects into an array
     */
    private void initiateHearts() {
        for(int i = 0; i < this.numOfLives; i++) {
            Vector2 heartPosition = new Vector2(
                    this.widgetTopLeftCorner.x() + i*(this.widgetDimensions.x() + WIDGET_PADDING),
                    this.widgetTopLeftCorner.y());

            this.hearts[i] = new GameObject(heartPosition, this.widgetDimensions,
                    this.widgetRenderable);

            this.gameObjects.addGameObject(this.hearts[i], Layer.BACKGROUND);
        }
    }

    /**
     * Handles the update of the graphical life counter.
     * If the number of lives on screen (${numOfLives}) is not equal to livesCounter,
     * we want to re-render the amount of lives to match it, and reduce ${numOfLives}
     *
     * @param deltaTime   game update delta time
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        // If we've somehow got here, we have a problem and we just return
        if(numOfLives < this.livesCounter.value()) {
            return;
        }

        // Removing all unnecessary hearts
        while(numOfLives != this.livesCounter.value()) {
            this.gameObjects.removeGameObject(this.hearts[numOfLives-1]);
            numOfLives--;
        }
    }
}
