package src.brick_strategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.Sound;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import src.gameobjects.Puck;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * This class handles the strategy that applies 2 (or more) powerups
 */
public class DoublePowerupStrategy extends CollisionStrategy {

    private static final int MAX_STRATEGIES = 3;

    private final List<CollisionStrategy> strategies;

    /**
     * We want to choose 3 strategies randomly.
     * To do so, we first select 2 - firstStrategy and secondStrategy.
     * If they're not a DoublePowerupStrategy we add them to the list of strategies,
     * otherwise, well randomly choose 2 more strategies and insert them to the list as long
     * as we didn't reach 3 strategies.
     *
     * @param gameObjects          Collection of game objects
     * @param strategyRandomizer   Strategy Randomizer to help randomly choose strategies
     */
    public DoublePowerupStrategy(GameObjectCollection gameObjects, StrategyRandomizer strategyRandomizer) {
        super(gameObjects);

        this.strategies = new LinkedList<>();

        CollisionStrategy firstStrategy = strategyRandomizer.getRandomStrategy();
        CollisionStrategy secondStrategy = strategyRandomizer.getRandomSpecialStrategy();

        // Firstly adding
        if(!(firstStrategy instanceof DoublePowerupStrategy)) {
            this.strategies.add(firstStrategy);
        }

        if(!(secondStrategy instanceof DoublePowerupStrategy)) {
            this.strategies.add(secondStrategy);
        }

        if(firstStrategy instanceof DoublePowerupStrategy) {
            CollisionStrategy option1 = strategyRandomizer.getRandomSpecialStrategy();
            CollisionStrategy option2 = strategyRandomizer.getRandomSpecialStrategy();

            while (option1 instanceof DoublePowerupStrategy) {
                option1 = strategyRandomizer.getRandomSpecialStrategy();
            }

            while (option2 instanceof DoublePowerupStrategy) {
                option2 = strategyRandomizer.getRandomSpecialStrategy();
            }

            if(this.strategies.size() < MAX_STRATEGIES) {
                this.strategies.add(option1);
            }

            if(this.strategies.size() < MAX_STRATEGIES) {
                this.strategies.add(option2);
            }
        }

        if(secondStrategy instanceof DoublePowerupStrategy) {
            CollisionStrategy option1 = strategyRandomizer.getRandomSpecialStrategy();
            CollisionStrategy option2 = strategyRandomizer.getRandomSpecialStrategy();

            while (option1 instanceof DoublePowerupStrategy) {
                option1 = strategyRandomizer.getRandomSpecialStrategy();
            }

            while (option2 instanceof DoublePowerupStrategy) {
                option2 = strategyRandomizer.getRandomSpecialStrategy();
            }

            if(this.strategies.size() < MAX_STRATEGIES) {
                this.strategies.add(option1);
            }

            if(this.strategies.size() < MAX_STRATEGIES) {
                this.strategies.add(option2);
            }
        }
    }

    /**
     * Handles the collision with a brick that has a double powerup strategy
     *
     * @param collidedObject   Brick that collided
     * @param colliderObject   Ball
     * @param bricksCounter    Counter of bricks left
     */
    @Override
    public void onCollision(GameObject collidedObject, GameObject colliderObject, Counter bricksCounter) {
        super.onCollision(collidedObject, colliderObject, bricksCounter);

        // System.out.println("[DEBUG] hit a double powerup strat with: " + this.strategies.size() + " strategies");
        
        for(CollisionStrategy strategy : this.strategies) {
            strategy.onCollision(collidedObject, colliderObject, bricksCounter);
        }
    }
}
