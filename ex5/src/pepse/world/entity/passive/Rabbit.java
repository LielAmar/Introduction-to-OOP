package pepse.world.entity.passive;

import danogl.gui.ImageReader;
import danogl.gui.rendering.AnimationRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import pepse.util.Constants;
import pepse.world.entity.ai.AIEntity;

/**
 * A class that represents the passive entity: rabbit
 */
public class Rabbit extends AIEntity implements IPassive {

    private static final Vector2 RABBIT_SIZE = new Vector2(40, 60);

    private static final String TAG = "animal";

    private Renderable idleAnimation;
    private Renderable runAnimation;

    public Rabbit(Vector2 bottomLeftCorner, ImageReader imageReader) {
        super(bottomLeftCorner.add(new Vector2(0, - RABBIT_SIZE.y() * 2)), RABBIT_SIZE, imageReader);

        this.setTag(TAG);
    }


    /**
     * Handles the rabbit animation
     */
    @Override
    protected void updateAnimations() {
        if (super.isMoving && this.getVelocity().x() != 0) {
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
    }


    /**
     * Initializes the idle animation
     *
     * @param imageReader Image reader
     */
    private void initIdleAnimation(ImageReader imageReader) {
        Renderable[] idleAnimationRenderable = {
                imageReader.readImage("assets/rabbit/idle1.png", true),
                imageReader.readImage("assets/rabbit/idle2.png", true),
                imageReader.readImage("assets/rabbit/idle3.png", true),
                imageReader.readImage("assets/rabbit/idle4.png", true)
        };

        this.idleAnimation = new AnimationRenderable(idleAnimationRenderable, 0.2);
    }

    /**
     * Initializes the run animation
     *
     * @param imageReader Image reader
     */
    private void initRunAnimation(ImageReader imageReader) {
        Renderable[] runAnimationRenderable = {
                imageReader.readImage("assets/rabbit/run1.png", true),
                imageReader.readImage("assets/rabbit/run2.png", true),
                imageReader.readImage("assets/rabbit/run3.png", true),
                imageReader.readImage("assets/rabbit/run4.png", true),
                imageReader.readImage("assets/rabbit/run5.png", true),
                imageReader.readImage("assets/rabbit/run6.png", true)
        };

        this.runAnimation = new AnimationRenderable(runAnimationRenderable, 0.2);
    }


    /**
     * Amount to heal once the entity dies
     *
     * @return Amount to heal
     */
    @Override
    public double healValue() {
        return Constants.RABBIT_HEAL_VALUE;
    }
}
