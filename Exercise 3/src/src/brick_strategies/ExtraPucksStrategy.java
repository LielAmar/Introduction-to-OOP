package src.brick_strategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.Sound;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import src.gameobjects.Puck;

public class ExtraPucksStrategy extends CollisionStrategy {

    private final static int PUCKS_TO_ADD = 3;

    private final Renderable renderable;
    private final Sound sound;

    public ExtraPucksStrategy(GameObjectCollection gameObjects, Renderable renderable, Sound sound) {
        super(gameObjects);

        this.renderable = renderable;
        this.sound = sound;
    }

    @Override
    public void onCollision(GameObject collidedObject, GameObject colliderObject, Counter bricksCounter) {
        super.onCollision(collidedObject, colliderObject, bricksCounter);

        // Calculating the size of a puck depending on the size of the
        float puckDiameter = collidedObject.getDimensions().x() / 3;

        for(int i = 0; i < PUCKS_TO_ADD; i++) {
            GameObject puck = new Puck(
                    collidedObject.getTopLeftCorner(),
                    new Vector2(puckDiameter, puckDiameter),
                    renderable,
                    sound);

            puck.setCenter(new Vector2(
                    collidedObject.getTopLeftCorner().x() + (i * puckDiameter) + (puckDiameter / 2),
                    collidedObject.getCenter().y()
            ));

            super.gameObjects.addGameObject(puck);
        }
    }
}
