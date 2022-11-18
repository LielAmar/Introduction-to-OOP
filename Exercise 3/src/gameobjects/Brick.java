package gameobjects;

import brick_strategies.CollisionStrategy;
import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

public class Brick extends GameObject {

    private final CollisionStrategy strategy;
    private final Counter counter;

    public Brick(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                 CollisionStrategy strategy, Counter counter) {
        super(topLeftCorner, dimensions, renderable);

        this.strategy = strategy;
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

        this.strategy.onCollision(this, collider, this.counter);

        System.out.println("[DEUBG] bricks left: " + this.counter.value());
    }
}
