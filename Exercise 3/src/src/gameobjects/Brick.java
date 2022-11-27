package src.gameobjects;

import src.brick_strategies.CollisionStrategy;
import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

/**
 * A class that represents the brick game object and handles its logic
 */
public class Brick extends GameObject {

    private final CollisionStrategy[] strategies;
    private final Counter counter;

    public Brick(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                 CollisionStrategy strategy, Counter counter) {
        this(topLeftCorner, dimensions, renderable, new CollisionStrategy[] { strategy }, counter);
    }

    public Brick(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                 CollisionStrategy[] strategies, Counter counter) {
        super(topLeftCorner, dimensions, renderable);

        this.strategies = strategies;
        this.counter = counter;
    }

    /**
     * Handles brick collision with other objects
     *
     * @param collider    Collider object
     * @param collision   Collision information
     */
    @Override
    public void onCollisionEnter(GameObject collider, Collision collision) {
        super.onCollisionEnter(collider, collision);

        for(CollisionStrategy strategy : this.strategies) {
            strategy.onCollision(this, collider, this.counter);
        }
    }
}