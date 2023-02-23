package pepse.world.entity.hostile;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.ImageReader;
import danogl.gui.rendering.AnimationRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import pepse.util.Constants;
import pepse.world.entity.IDamagable;
import pepse.world.entity.ai.AIEntity;

/**
 * A class that represents the hostile entity: pirate
 */
public class Pirate extends AIEntity implements IHostile {

    public static final Vector2 PIRATE_SIZE = new Vector2(65, 80);

    private static final String TAG = "pirate";

    private final IDamagable target;

    private boolean isAttacking;

    private int attackDelayLeft;

    private Renderable idleAnimation;
    private Renderable runAnimation;
    private Renderable attackAnimation;

    public Pirate(Vector2 bottomLeftCorner, ImageReader imageReader, IDamagable target) {
        super(bottomLeftCorner.add(new Vector2(0, - PIRATE_SIZE.y() * 2)), PIRATE_SIZE, imageReader);

        this.setTag(TAG);

        this.target = target;

        this.isAttacking = false;

        this.attackDelayLeft = Constants.HOSTILE_ENTITY_ATTACK_DELAY;
    }


    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);

        this.attackDelayLeft = Math.min(this.attackDelayLeft, Constants.HOSTILE_ENTITY_ATTACK_DELAY);
    }

    /**
     * Handles the attack phase
     *
     * @param other     The former collision partner.
     * @param collision collision object
     */
    @Override
    public void onCollisionStay(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);

        // If the entity is still moving in a certain direction, and it didn't end its
        // movement, keep going
        if (this.attackDelayLeft > 0) {
            this.isAttacking = false;

            this.attackDelayLeft--;
            return;
        }

        if (other instanceof IDamagable && this.attackDelayLeft == 0) {
            this.isAttacking = true;

            this.attackDelayLeft = Constants.HOSTILE_ENTITY_ATTACK_DELAY;

            this.attack((IDamagable) other);
        }
    }

    /**
     * Handles the attack end phase
     *
     * @param other The former collision partner.
     */
    @Override
    public void onCollisionExit(GameObject other) {
        super.onCollisionExit(other);

        if (other.equals(this.target)) {
            this.isAttacking = false;
        }
    }


    /**
     * Handles the pirate animation
     */
    @Override
    protected void updateAnimations() {
        if (this.isAttacking) {
            this.renderer().setRenderable(this.attackAnimation);
        } else if (super.isMoving && this.getVelocity().x() != 0) {
            this.renderer().setRenderable(this.runAnimation);
            this.renderer().setIsFlippedHorizontally(this.getVelocity().x() < 0);
        } else {
            this.renderer().setRenderable(this.idleAnimation);
        }
    }

    /**
     * Initializes all animations
     *
     * @param imageReader Image reader
     */
    @Override
    public void initAnimations(ImageReader imageReader) {
        this.initIdleAnimation(imageReader);
        this.initRunAnimation(imageReader);
        this.initAttackAnimation(imageReader);
    }


    /**
     * Initializes the idle animation
     *
     * @param imageReader Image reader
     */
    private void initIdleAnimation(ImageReader imageReader) {
        Renderable[] idleAnimationRenderable = {
                imageReader.readImage("assets/pirate/idle_0.png", true),
                imageReader.readImage("assets/pirate/idle_1.png", true),
                imageReader.readImage("assets/pirate/idle_2.png", true),
                imageReader.readImage("assets/pirate/idle_3.png", true)
        };

        this.idleAnimation = new AnimationRenderable(idleAnimationRenderable, 0.2);
    }

    /**
     * Initializes the attack animation
     *
     * @param imageReader Image reader
     */
    private void initAttackAnimation(ImageReader imageReader) {
        Renderable[] attackAnimationRenderable = {
                imageReader.readImage("assets/pirate/attack_0.png", true),
                imageReader.readImage("assets/pirate/attack_1.png", true),
                imageReader.readImage("assets/pirate/attack_2.png", true),
                imageReader.readImage("assets/pirate/attack_3.png", true),
                imageReader.readImage("assets/pirate/attack_4.png", true)
        };

        this.attackAnimation = new AnimationRenderable(attackAnimationRenderable, 0.1);
    }

    /**
     * Initializes the run animation
     *
     * @param imageReader Image reader
     */
    private void initRunAnimation(ImageReader imageReader) {
        Renderable[] runAnimationRenderable = {
                imageReader.readImage("assets/pirate/run_0.png", true),
                imageReader.readImage("assets/pirate/run_1.png", true),
                imageReader.readImage("assets/pirate/run_2.png", true),
                imageReader.readImage("assets/pirate/run_3.png", true),
                imageReader.readImage("assets/pirate/run_4.png", true),
                imageReader.readImage("assets/pirate/run_5.png", true)
        };

        this.runAnimation = new AnimationRenderable(runAnimationRenderable, 0.2);
    }


    /**
     * Amount to damage once the entity attacks
     *
     * @return Amount to damage
     */
    @Override
    public double attackDamage() {
        return Constants.PIRATE_DAMAGE_VALUE;
    }
}
