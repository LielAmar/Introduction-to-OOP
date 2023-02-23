package pepse.world.entity.ai;

import danogl.gui.ImageReader;
import danogl.util.Vector2;
import pepse.util.Constants;
import pepse.world.entity.Entity;

/**
 * A class that represents an AI entity that can walk around
 */
public abstract class AIEntity extends Entity {

    protected boolean isMoving;

    protected int moveTicksLeft;

    public AIEntity(Vector2 topLeftCorner, Vector2 dimensions, ImageReader imageReader) {
        super(topLeftCorner, dimensions, imageReader);

        this.isMoving = false;

        this.moveTicksLeft = 0;
    }

    /**
     * Updates the entity location by (possibly) moving to one side
     *
     * @param deltaTime The time elapsed, in seconds, since the last frame. Can
     *                  be used to determine a new position/velocity by multiplying
     *                  this delta with the velocity/acceleration respectively
     *                  and adding to the position/velocity:
     *                  velocity += deltaTime*acceleration
     *                  pos += deltaTime*velocity
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        this.handleMovement();
    }


    /**
     * Moves in a certain direction
     */
    private void handleMovement() {
        // If the entity is still moving in a certain direction, and it didn't end its
        // movement, keep going
        if(this.moveTicksLeft > 0) {
            this.moveTicksLeft--;
            return;
        }

        // Should the entity move?
        if(!this.random.nextBoolean()) {
            this.setVelocity(Vector2.ZERO);
        } else {
            float direction = (this.random.nextBoolean() ? -1 : 1);

            Vector2 moveDirection = new Vector2(direction * Constants.ENTITY_SPEED, this.getVelocity().y());
            this.setVelocity(moveDirection);

            this.isMoving = true;
        }

        this.moveTicksLeft = Constants.AI_ENTITY_MOVE_DELAY;
    }
}
