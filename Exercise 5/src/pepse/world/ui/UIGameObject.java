package pepse.world.ui;


import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.util.Vector2;
import pepse.util.callbacks.UIGameObjectCallback;

import java.awt.*;

public abstract class UIGameObject {

    protected final GameObjectCollection gameObjects;
    protected final Vector2 topLeftCorner;
    protected final Vector2 dimensions;
    protected final Color mainColor;

    public UIGameObject(GameObjectCollection gameObjects, Vector2 topLeftCornet, Vector2 dimensions,
                            Color mainColor) {
        this.gameObjects = gameObjects;
        this.topLeftCorner = topLeftCornet;
        this.dimensions = dimensions;
        this.mainColor = mainColor;
    }

    abstract GameObject create(UIGameObjectCallback callback);
}
