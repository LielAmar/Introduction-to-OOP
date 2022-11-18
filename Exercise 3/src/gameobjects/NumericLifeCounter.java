package gameobjects;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Counter;
import danogl.util.Vector2;

import java.awt.*;

public class NumericLifeCounter extends GameObject {

    private final Counter livesCounter;

    private final TextRenderable textObject;

    public NumericLifeCounter(Counter livesCounter, Vector2 topLeftCorner, Vector2 dimensions,
                              GameObjectCollection gameObjectCollection) {
        super(topLeftCorner, Vector2.ZERO, null);

        this.livesCounter = livesCounter;

        this.textObject = new TextRenderable(this.livesCounter.value() + "");
        this.textObject.setColor(Color.green);

        gameObjectCollection.addGameObject(new GameObject(topLeftCorner, dimensions, this.textObject),
                Layer.BACKGROUND);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        this.textObject.setString(this.livesCounter.value() + "");

        if(this.livesCounter.value() == 3) {
            this.textObject.setColor(Color.green);
        } else if(this.livesCounter.value() == 2) {
            this.textObject.setColor(Color.yellow);
        } else if(this.livesCounter.value() == 1) {
            this.textObject.setColor(Color.red);
        }
    }
}
