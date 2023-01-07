package pepse.world.ui;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;

import pepse.util.callbacks.UIGameObjectCallback;

import java.awt.*;

/**
 * A class that handles a visual UI game object
 */
public class VisualGameObject extends UIGameObject {

    private static final String TAG = "ui_visual";

    public VisualGameObject(GameObjectCollection gameObjects, Vector2 topLeftCornet, Vector2 dimensions,
                            Color mainColor) {
        super(gameObjects, topLeftCornet, dimensions, mainColor);
    }

    /**
     * Creates a textual game object and returns it
     *
     * @param callback A callback used to update the object's renderable
     * @return Created textual ui object
     */
    public GameObject create(UIGameObjectCallback callback) {
        RectangleRenderable renderable = new RectangleRenderable(super.mainColor);

        GameObject gameObject = new GameObject(super.topLeftCorner, super.dimensions,
                renderable);

        gameObject.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        gameObject.setTag(TAG);

        gameObject.addComponent(deltaTime -> callback.update(gameObject));

        // Adding the back game object first
        super.gameObjects.addGameObject(gameObject, Layer.UI);

        return gameObject;
    }
}
