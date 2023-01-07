package pepse.world.ui;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Vector2;
import pepse.util.callbacks.UIGameObjectCallback;

import java.awt.*;

/**
 * A class that handles a textual UI game object
 */
public class TextualGameObject extends UIGameObject {

    private static final String TAG = "ui_textual";

    public TextualGameObject(GameObjectCollection gameObjects, Vector2 topLeftCornet, Vector2 dimensions,
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
        TextRenderable textRenderable = new TextRenderable("");
        textRenderable.setColor(super.mainColor);

        GameObject textualGameObject = new GameObject(super.topLeftCorner, super.dimensions,
                textRenderable);

        textualGameObject.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        textualGameObject.setTag(TAG);

        textualGameObject.addComponent(deltaTime -> callback.update(textualGameObject));

        super.gameObjects.addGameObject(textualGameObject, Layer.UI);

        return textualGameObject;
    }
}
