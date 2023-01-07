package pepse.world.entity;

import pepse.util.properties.NumericProperty;

/**
 * An interface representing damagable entities
 */
public interface IDamagable {

    /**
     * Damages the entity by the given factor
     *
     * @param factor   damage to deal
     */
    void damage(double factor);

    /**
     * Heals the entity by the given factor
     *
     * @param factor   amount to heal
     */
    void heal(double factor);

    /**
     * Kills the entity
     */
    void kill();

    /**
     * Returns whether the entity is dead
     *
     * @return   Whether entity is dead
     */
    boolean isDead();

    /**
     * Returns the entity's health property
     *
     * @return   Entity's health property
     */
    NumericProperty getHealthProperty();
}
