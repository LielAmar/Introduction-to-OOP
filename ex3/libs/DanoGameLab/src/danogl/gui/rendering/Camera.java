package danogl.gui.rendering;

import danogl.GameObject;
import danogl.util.Vector2;

/**
 * Represents a view of the world. The view can translate and scale
 * (move around and zoom in and out). The camera/view may also follow an object.
 * @author Dan Nirel
 */
public class Camera extends GameObject {
    private GameObject objToFollow;
    private Vector2 deltaRelativeToObject;
    private Vector2 windowDimensions;

    /**
     * Construct a new Camera which covers a given rectangle in the world.
     * @param topLeftCorner world-coordinates of the top-left corner of
     *                      the rectangle the camera should cover
     * @param dimensions the dimensions (in world-coordinates) of the rectangle the
     *                   camera should cover
     * @param windowDimensions the dimensions, in pixels, of the window
     */
    public Camera(Vector2 topLeftCorner, Vector2 dimensions, Vector2 windowDimensions) {
        super(topLeftCorner, dimensions, null);
        this.windowDimensions = windowDimensions;
    }

    /**
     * Construct a new Camera which covers a given rectangle in the world.
     * @param objToFollow a GameObject the Camera should follow around.
     *                    If this parameter is null, the rectangle viewed will begin
     *                    in the world origin (Vector2.ZERO).
     * @param deltaRelativeToObject the desired delta between the camera's center and
     *                              objToFollow's center. A value of Vector2.ZERO
     *                              means objToFollow will always appear in the dead center.
     *                              A value of Vector2.UP.mult(50) means the object will
     *                              appear slightly below the view's center. This parameter
     *                              is meaningless if objToFollow is null.
     * @param dimensions the dimensions (in world-coordinates) of the rectangle the
     *                   camera should cover
     * @param windowDimensions the dimensions, in pixels, of the window
     */
    public Camera(GameObject objToFollow, Vector2 deltaRelativeToObject,
                  Vector2 dimensions, Vector2 windowDimensions) {
        super(Vector2.ZERO, dimensions, null);
        this.objToFollow = objToFollow;
        this.deltaRelativeToObject = deltaRelativeToObject;
        this.windowDimensions = windowDimensions;
    }

    /**
     * Make the camera follow a given object, or stop it from following the object
     * it currently follows.
     * @param objToFollow If this parameter is null, the view will freeze where it is. Otherwise,
     *                    follows this object around.
     * @param deltaRelativeToObject the desired delta between the camera's center and
     *                              objToFollow's center. A value of Vector2.ZERO
     *                              means objToFollow will always appear in the dead center.
     *                              A value of Vector2.UP.mult(50) means the object will
     *                              appear slightly below the view's center. This parameter
     *                              is meaningless if objToFollow is null.
    */
    public void setToFollow(GameObject objToFollow,
                            Vector2 deltaRelativeToObject) {
        this.objToFollow = objToFollow;
        this.deltaRelativeToObject = deltaRelativeToObject;
    }

    /**
     * Retrieve the followed object, or null if the Camera isn't following an object
     */
    public GameObject getObjectFollowed() {
        return objToFollow;
    }

    /**
     * Returns the delta between the camera's center and the followed object's
     * center. This value is meaningless if the camera isn't following an object.
     */
    public Vector2 getDeltaRelativeToObjectFollowed() {
        return deltaRelativeToObject;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if(objToFollow != null)
            setCenter(objToFollow.getCenter().add(deltaRelativeToObject));
    }

    /**
     * Returns the window's dimensions
     */
    public Vector2 windowDimensions() {
        return windowDimensions;
    }

    /**
     * Converts screen coordinates (in pixels, relative to the window's
     * top left corner), to world coordinates, according to this camera
     */
    public Vector2 screenToWorldCoords(Vector2 screenCoords) {
        freeCalculationsVector.setXY(screenCoords);
        freeCalculationsVector
                .selfMultX(getDimensions().x()/windowDimensions.x())
                .selfMultY(getDimensions().y()/windowDimensions.y())
                .selfAdd(getTopLeftCorner());
        return freeCalculationsVector;
    }

    /**
     * Converts world coordinates to screen coordinates (in pixels, relative
     * to the window's top left corner)
     */
    public Vector2 worldToScreenCoords(Vector2 worldCoords) {
        float dimFactorX = windowDimensions().x()/getDimensions().x();
        float dimFactorY = windowDimensions().y()/getDimensions().y();
        freeCalculationsVector.setXY(
                dimFactorX*(worldCoords.x()-getTopLeftCorner().x()),
                dimFactorY*(worldCoords.y()-getTopLeftCorner().y()));
        return freeCalculationsVector;
    }
}
