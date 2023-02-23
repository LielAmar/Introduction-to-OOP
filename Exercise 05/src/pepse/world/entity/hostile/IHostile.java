package pepse.world.entity.hostile;

import pepse.world.entity.IDamagable;

/**
 * An interface representing hostile entities that deal damage to other entities
 */
public interface IHostile {

    /**
     * Damage to deal upon attack
     *
     * @return   Damage amount
     */
    double attackDamage();

    /**
     * A default function that attacks a target entity
     *
     * @param target   Target to attack
     */
    default void attack(IDamagable target) {
        target.damage(this.attackDamage());
    }
}
