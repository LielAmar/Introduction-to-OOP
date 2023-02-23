package pepse.world.entity;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.ImageReader;
import danogl.util.Vector2;

import pepse.util.Constants;
import pepse.util.callbacks.FloatCallback;
import pepse.world.IWorldGenerator;
import pepse.world.entity.hostile.Pirate;
import pepse.world.entity.passive.Rabbit;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * A class that handles the creation of new entities in the game
 */
public class EntityManager implements IWorldGenerator {

    private final Random random;

    private final GameObjectCollection gameObjects;
    private final int layer;

    private final Entity target;

    private final ImageReader imageReader;
    private final FloatCallback callback;

    public EntityManager(GameObjectCollection gameObjects, int layer, Entity target,
                         ImageReader imageReader, FloatCallback callback) {
        this.random = new Random();

        this.gameObjects = gameObjects;
        this.layer = layer;

        this.target = target;

        this.imageReader = imageReader;
        this.callback = callback;
    }


    /**
     * Creates entities in a given range and returns a list of created game objects
     *
     * @param minX Range starting point
     * @param maxX Range ending point
     * @return All game objects created within the given range
     */
    @Override
    public List<GameObject> createInRangeAndReturn(int minX, int maxX) {
        List<GameObject> createdGameObjects = new ArrayList<>();

        for (int xLocation = minX; xLocation < maxX; xLocation += 60) {
            if (this.random.nextInt(Constants.ENTITY_SPAWN_RATE) == 0) {
                float heightAtX = this.callback.run(xLocation);

                Vector2 initialPosition = new Vector2(xLocation, heightAtX);

                if (this.random.nextBoolean()) {
                    createdGameObjects.add(this.spawnPassive(initialPosition));
                } else {
                    createdGameObjects.add(this.spawnHostile(initialPosition));
                }
            }
        }

        for(GameObject gameObject : createdGameObjects) {
            this.gameObjects.addGameObject(gameObject, this.layer);
        }

        return createdGameObjects;
    }


    /**
     * Spawns a passive entity to the world
     *
     * @param initialPosition   Initial entity position
     * @return                  Created entity
     */
    private Entity spawnPassive(Vector2 initialPosition) {
        Entity rabbit = new Rabbit(initialPosition, this.imageReader);

        rabbit.addComponent((deltaTime) -> {
            if(rabbit.isDead()) {
                gameObjects.removeGameObject(rabbit, this.layer);
            }
        });

        return rabbit;
    }

    /**
     * Spawns a hostile entity to the world
     *
     * @param initialPosition   Initial entity position
     * @return                  Created entity
     */
    private Entity spawnHostile(Vector2 initialPosition) {
        Entity pirate = new Pirate(initialPosition, this.imageReader, this.target);

        pirate.addComponent((deltaTime) -> {
            if(pirate.isDead()) {
                gameObjects.removeGameObject(pirate, this.layer);
            }
        });

        return pirate;
    }
}
