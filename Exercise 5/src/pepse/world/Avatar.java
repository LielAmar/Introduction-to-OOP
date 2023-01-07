package pepse.world;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.gui.ImageReader;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.AnimationRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.util.Constants;
import pepse.world.entity.IDamagable;
import pepse.world.entity.Entity;
import pepse.world.entity.passive.IPassive;
import pepse.util.properties.NumericProperty;
import pepse.world.ui.VisualGameObject;

import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * This class represents an avatar - a playable character in the pepse game
 * An avatar can be damaged.
 */
public class Avatar extends Entity {

    private static final Vector2 AVATAR_SIZE = new Vector2(50, 60);

    private static final String TAG = "avatar";

    private final UserInputListener inputListener;
    private final NumericProperty energy;

    private boolean isFlying;
    private boolean isAttacking;

    private Renderable attackAnimation;
    private Renderable idleAnimation;
    private Renderable jumpAnimation;
    private Renderable runAnimation;
    private Renderable swimAnimation;

    public Avatar(Vector2 bottomLeftCorner, Vector2 dimensions,
                  ImageReader imageReader, UserInputListener inputListener,
                  NumericProperty energy) {
        super(bottomLeftCorner.add(new Vector2(0, - AVATAR_SIZE.y() * 2)), dimensions, imageReader);

        this.setTag(TAG);

        this.inputListener = inputListener;
        this.energy = energy;

        this.isFlying = false;
        this.isAttacking = false;

        this.renderer().setRenderable(this.idleAnimation);
    }


    /**
     * Handles the tick-update of the avatar to handle movement, animations and energy
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

        this.handleMovementInput();
        this.handleMiscInput();
        this.updateAnimations();
        this.handleEnergy();
    }


    /**
     * Handles the avatar collision stay. We want to kill damageable entities for as long as we
     * stay in collision with them.
     *
     * @param other     The collision partner.
     * @param collision Information regarding this collision.
     */
    @Override
    public void onCollisionStay(GameObject other, Collision collision) {
        super.onCollisionStay(other, collision);

        if (other instanceof IDamagable && this.isAttacking) {
            ((IDamagable) other).kill();

            // If the entity killed was healeable, heal the player with 30hp
            this.heal((other instanceof IPassive ? super.getHealthProperty().getFactor() * 60 : 0));
        }
    }


    /**
     * Handles the movement input of the avatar
     */
    private void handleMovementInput() {
        Vector2 direction = new Vector2(0, this.getVelocity().y());

        if (inputListener.isKeyPressed(KeyEvent.VK_LEFT)) {
            direction = direction.add(Vector2.LEFT.mult(Constants.AVATAR_SPEED));
        }

        if (inputListener.isKeyPressed(KeyEvent.VK_RIGHT)) {
            direction = direction.add(Vector2.RIGHT.mult(Constants.AVATAR_SPEED));
        }

        this.isFlying = false;

        if (inputListener.isKeyPressed(KeyEvent.VK_SPACE)) {
            if (this.energy.getValue() > 0 && inputListener.isKeyPressed(KeyEvent.VK_SHIFT)) {
                this.isFlying = true;
                direction = new Vector2(direction.x(), -1 * (float) Constants.AVATAR_FLIGHT_SPEED);
            } else {
                if (this.getVelocity().y() == 0) {
                    direction = direction.add(new Vector2(0, -1 * (float) Constants.ENTITY_GRAVITY));
                }
            }
        }

        this.setVelocity(direction);
    }

    /**
     * Handles the misc input of the avatar
     * For example: attacking
     */
    private void handleMiscInput() {
        this.isAttacking = this.inputListener.isKeyPressed(Constants.AVATAR_ATTACK_KEY)
                && !this.isFlying;
    }

    /**
     * Handles the energy updates
     */
    private void handleEnergy() {
        if (this.isFlying) {
            this.energy.decrease();
        } else {
            if (this.getVelocity().y() == 0) {
                this.energy.increase();
            }
        }
    }


    /**
     * Handles the avatar animation
     */
    @Override
    protected void updateAnimations() {
        if (this.getVelocity().x() != 0) {
            this.renderer().setRenderable(this.runAnimation);
            this.renderer().setIsFlippedHorizontally(this.getVelocity().x() < 0);
        }

        if (this.getVelocity().y() != 0) {
            this.renderer().setRenderable(this.jumpAnimation);
            this.renderer().setIsFlippedHorizontally(this.getVelocity().x() < 0);
        }

        if (this.getVelocity().x() == 0 && this.getVelocity().y() == 0) {
            this.renderer().setRenderable(this.idleAnimation);
        }

        if (this.isFlying) {
            this.renderer().setRenderable(this.swimAnimation);
        }

        if (this.isAttacking) {
            this.renderer().setRenderable(this.attackAnimation);
        }
    }

    /**
     * Initializes all animations
     *
     * @param imageReader Image reader
     */
    @Override
    protected void initAnimations(ImageReader imageReader) {
        this.initAttackAnimation(imageReader);
        this.initIdleAnimation(imageReader);
        this.initJumpAnimation(imageReader);
        this.initRunAnimation(imageReader);
        this.initSwimAnimation(imageReader);
    }


    /**
     * Initializes the attack animation
     *
     * @param imageReader Image reader
     */
    private void initAttackAnimation(ImageReader imageReader) {
        Renderable[] attackAnimationRenderable = {
                imageReader.readImage("assets/ninja/attack_0.png", true),
                imageReader.readImage("assets/ninja/attack_1.png", true),
                imageReader.readImage("assets/ninja/attack_2.png", true)
        };

        this.attackAnimation = new AnimationRenderable(attackAnimationRenderable, 0.1);
    }

    /**
     * Initializes the idle animation
     *
     * @param imageReader Image reader
     */
    private void initIdleAnimation(ImageReader imageReader) {
        Renderable[] idleAnimationRenderable = {
                imageReader.readImage("assets/ninja/idle_0.png", true),
                imageReader.readImage("assets/ninja/idle_1.png", true),
                imageReader.readImage("assets/ninja/idle_2.png", true),
                imageReader.readImage("assets/ninja/idle_3.png", true)
        };

        this.idleAnimation = new AnimationRenderable(idleAnimationRenderable, 0.2);
    }

    /**
     * Initializes the jump animation
     *
     * @param imageReader Image reader
     */
    private void initJumpAnimation(ImageReader imageReader) {
        Renderable[] jumpAnimationRenderable = {
                imageReader.readImage("assets/ninja/jump_0.png", true),
                imageReader.readImage("assets/ninja/jump_1.png", true),
                imageReader.readImage("assets/ninja/jump_2.png", true),
                imageReader.readImage("assets/ninja/jump_3.png", true)
        };

        this.jumpAnimation = new AnimationRenderable(jumpAnimationRenderable, 0.2);
    }

    /**
     * Initializes the run animation
     *
     * @param imageReader Image reader
     */
    private void initRunAnimation(ImageReader imageReader) {
        Renderable[] runAnimationRenderable = {
                imageReader.readImage("assets/ninja/run_0.png", true),
                imageReader.readImage("assets/ninja/run_1.png", true),
                imageReader.readImage("assets/ninja/run_2.png", true),
                imageReader.readImage("assets/ninja/run_3.png", true),
                imageReader.readImage("assets/ninja/run_4.png", true),
                imageReader.readImage("assets/ninja/run_5.png", true)
        };

        this.runAnimation = new AnimationRenderable(runAnimationRenderable, 0.2);
    }

    /**
     * Initializes the swim animation
     *
     * @param imageReader Image reader
     */
    private void initSwimAnimation(ImageReader imageReader) {
        Renderable[] swimAnimationRenderable = {
                imageReader.readImage("assets/ninja/swim_0.png", true),
                imageReader.readImage("assets/ninja/swim_1.png", true),
                imageReader.readImage("assets/ninja/swim_2.png", true),
                imageReader.readImage("assets/ninja/swim_3.png", true),
                imageReader.readImage("assets/ninja/swim_4.png", true),
                imageReader.readImage("assets/ninja/swim_5.png", true)
        };

        this.swimAnimation = new AnimationRenderable(swimAnimationRenderable, 0.2);
    }


    /**
     * Creates a basic avatar and all follow items
     *
     * @param gameObjects      Collection of game objects to add the avatar to
     * @param layer            Avatar's layer
     * @param bottomLeftCorner Bottom left corner of the avatar
     * @param inputListener    Input listener object
     * @param imageReader      Image reader object
     * @return Created avatar
     */
    public static Avatar create(GameObjectCollection gameObjects,
                                int layer, Vector2 bottomLeftCorner,
                                UserInputListener inputListener,
                                ImageReader imageReader) {
        // Creates the energy property for the avatar
        NumericProperty energy = new NumericProperty(
                Constants.ENTITY_ENERGY_FACTOR,
                Constants.ENTITY_MAX_ENERGY,
                Constants.ENTITY_MIN_ENERGY,
                Constants.ENTITY_MAX_ENERGY);

        // Creates the avatar and adds it to the game
        Avatar avatar = new Avatar(bottomLeftCorner, AVATAR_SIZE, imageReader, inputListener, energy);

        gameObjects.addGameObject(avatar, layer);

        // Creates the energy & health bars of the avatar
        Vector2 energyBarPosition = new Vector2(50, 50 + Constants.BAR_HEIGHT);
        Vector2 healthBarPosition = energyBarPosition.add(new Vector2(0, Constants.BAR_HEIGHT * 2));

        new VisualGameObject(gameObjects, energyBarPosition, Vector2.ZERO, Color.YELLOW)
                .create((gameObject) -> updateBar(gameObject, energyBarPosition, energy));

        new VisualGameObject(gameObjects, healthBarPosition, Vector2.ZERO, Color.RED)
                .create((gameObject) -> updateBar(gameObject, healthBarPosition,
                        avatar.getHealthProperty()));

        return avatar;
    }

    /**
     * Updates a progress bar attached to the given property
     *
     * @param gameObject Bar to update
     * @param position   Bar top left position
     * @param property   Property to control bar fullness
     */
    private static void updateBar(GameObject gameObject, Vector2 position, NumericProperty property) {
        gameObject.setDimensions(Vector2.ONES.multX(property.getValue().floatValue() * 2)
                .multY(Constants.BAR_HEIGHT));

        gameObject.setTopLeftCorner(position);
    }
}
