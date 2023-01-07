package pepse.world.entity.passive;

/**
 * An interface representing passive entities that heal other entities upon killing
 */
public interface IPassive {

    /**
     * Amount to heal upon entity death
     *
     * @return   Heal amount
     */
    double healValue();
}
