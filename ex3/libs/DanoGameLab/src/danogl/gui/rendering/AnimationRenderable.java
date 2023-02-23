package danogl.gui.rendering;

import danogl.gui.ImageReader;
import danogl.util.Vector2;

import java.awt.*;

/**
 * A dynamic, repeating animation.
 * @author Dan Nirel
 */
public class AnimationRenderable implements Renderable {
    private Renderable[] clips;
    private double timeBetweenClips;
    private int currImIndex = 0;
    private double timeSinceLastClipSwitch = 0;

    /**
     * Initialize the animation using Renderables
     * @param clips The animation's clips.
     * @param timeBetweenClips The time to wait before moving to the next clip.
     */
    public AnimationRenderable(Renderable[] clips, double timeBetweenClips) {
        this.clips = clips.clone();
        this.timeBetweenClips = timeBetweenClips;
    }

    /**
     * Initialize the animation using file-paths of images on disk
     * @param imagePaths Paths of images on disk.
     * @param imageReader To read the images with.
     * @param useTransparency Should the top-left pixel of every image be considered
     *                        a transparent color for that image.
     * @param timeBetweenClips The time to wait before moving to the next clip.
     */
    public AnimationRenderable(String[] imagePaths, ImageReader imageReader,
                               boolean useTransparency, double timeBetweenClips) {
        this.timeBetweenClips = timeBetweenClips;
        clips = new ImageRenderable[imagePaths.length];
        for (int i = 0; i < clips.length; i++) {
            clips[i] = imageReader.readImage(imagePaths[i], useTransparency);
        }
    }

    @Override
    public void update(double deltaTime) {
        timeSinceLastClipSwitch += deltaTime;
        if(timeSinceLastClipSwitch >= timeBetweenClips) {
            timeSinceLastClipSwitch = 0;
            currImIndex = (currImIndex+1)%clips.length;
        }
    }

    @Override
    public void render(Graphics2D g, Vector2 topLeftCorner, Vector2 dimensions,
                       double degreesCounterClockwise,
                       boolean isFlippedHorizontally, boolean isFlippedVertically,
                       double opaqueness) {
        clips[currImIndex].render(
                g, topLeftCorner, dimensions, degreesCounterClockwise,
                isFlippedHorizontally, isFlippedVertically, opaqueness);
    }

    /**Reset the animation (go back to the first frame).*/
    public void resetAnimation() {
        currImIndex = 0;
        timeSinceLastClipSwitch = 0;
    }
}
