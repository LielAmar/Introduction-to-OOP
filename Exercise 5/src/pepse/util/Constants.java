package pepse.util;

import danogl.collisions.Layer;

import java.awt.event.KeyEvent;

/**
 * A class that stores all game constants
 */
public class Constants {

    // Common constants
    public static final int GAME_SEED = 30;

    // Day/night constants
    public static final float DAY_CYCLE_LENGTH = 30.0f;

    public static final float MIDDAY_SUN_POSITION = (float) Math.PI / 2;
    public static final float MIDNIGHT_SUN_POSITION = (float) (2.5 * Math.PI);

    public static final float SUN_RADIUS = 100;
    public static final float SUN_ORBIT_RADIUS = 125;
    public static final float SUN_ORBIT_ELLIPSE_A = 5;
    public static final float SUN_ORBIT_ELLIPSE_B = 3;

    public static final float MIDNIGHT_NIGHT_OPACITY = 0.5f;
    public static final float MIDDAY_NIGHT_OPACITY = 0f;

    // Block constants
    public static final int BLOCK_SIZE = 30;

    public static final int LEAFS_GRID_SIZE = 5;

    public static final float LEAF_FADE_LENGTH = 5;
    public static final float LEAF_X_VELOCITY = 30;
    public static final float LEAF_Y_VELOCITY = 100;

    public static final float LEAF_ANGLE_TRANSITION_STARTING_VALUE = -2 * (float) Math.PI;
    public static final float LEAF_ANGLE_TRANSITION_ENDING_VALUE = 2 * (float) Math.PI;
    public static final float LEAF_SIZE_TRANSITION_STARTING_VALUE = 1.0f;
    public static final float LEAF_SIZE_TRANSITION_ENDING_VALUE = 0.95f;
    public static final float LEAF_TRANSITIONS_CYCLE_LENGTH = 0.4f;

    public static final int TREE_SPAWN_RATE = 20;
    public static final int LEAF_SPAWN_RATE = 2;


    // Entity & Avatar constants
    public static final int AVATAR_ATTACK_KEY = KeyEvent.VK_A;
    public static final int AVATAR_FLIGHT_SPEED = 150;

    public static final float ENTITY_MASS = 1.0f;
    public static final int AVATAR_SPEED = 300;
    public static final int ENTITY_GRAVITY = 300;
    public static final int ENTITY_SPEED = 150;

    public static final double ENTITY_ENERGY_FACTOR = 0.5;
    public static final double ENTITY_MAX_ENERGY = 100;
    public static final double ENTITY_MIN_ENERGY = 0;

    public static final double ENTITY_HEALTH_FACTOR = 0.5;
    public static final double ENTITY_MAX_HEALTH = 100;
    public static final double ENTITY_MIN_HEALTH = 0;

    public static final int ENTITY_SPAWN_RATE = 25;

    public static final int HOSTILE_ENTITY_ATTACK_DELAY = 500;
    public static final int AI_ENTITY_MOVE_DELAY = 250;

    public static final double RABBIT_HEAL_VALUE = 30.0;

    public static final double PIRATE_DAMAGE_VALUE = 30.0;

    // UI constants
    public static final int BAR_HEIGHT = 20;

    /**
     * An enum used to keep track of object layers
     */
    public enum OBJECT_LAYER {
        ENTITY(Layer.DEFAULT),
        NIGHT_SKY(Layer.FOREGROUND),
        SUN(Layer.BACKGROUND + 1),
        SUN_HALO(Layer.BACKGROUND + 2),
        COLLIDABLE_TERRAIN(Layer.STATIC_OBJECTS),
        TERRAIN(Layer.STATIC_OBJECTS - 1),
        LOG(Layer.FOREGROUND),
        LEAF(Layer.FOREGROUND + 1);


        private final int layer;

        OBJECT_LAYER(int layer) {
            this.layer = layer;
        }

        public int getLayer() {
            return this.layer;
        }
    }
}
