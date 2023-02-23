package danogl;

import danogl.collisions.AABB.AABBCollider;
import danogl.collisions.Collider;
import danogl.components.*;
import danogl.collisions.Collision;
import danogl.components.Component;
import danogl.gui.mouse.MouseActionParams;
import danogl.gui.rendering.Camera;
import danogl.gui.rendering.Renderable;
import danogl.util.ModifiableList;
import danogl.util.MutableVector2;
import danogl.util.Vector2;

import java.awt.*;

/**
 * Represents an object in the game. For example, an object has dimensions, a certain
 * position, it has a velocity, an image that represents it, and it can potentially
 * collide with other GameObjects.
 * @author Dan Nirel
 */
public class GameObject {
    private RendererComponent renderer;
    private Transform transform;
    private GameObjectPhysics physics;
    private CoordinateSpace coordinateSpace = CoordinateSpace.WORLD_COORDINATES;
    private String tag = "";
    private ModifiableList<Component> components;

    /**
     * Use with care. this a general-purpose container vector for calculations where
     * it's undesirable to create a new vector. use only in the main thread.
     */
    protected MutableVector2 freeCalculationsVector = new MutableVector2();

    /**
     * Construct a new GameObject instance.
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions Width and height in window coordinates.
     * @param renderable The renderable representing the object. Can be null, in which case
     *              the GameObject will not be rendered.
     */
    public GameObject(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable) {
        this.transform = new Transform(topLeftCorner, dimensions);
        this.renderer = new RendererComponent(this, renderable);
        this.physics = new GameObjectPhysics();
        this.physics.setCollider(new AABBCollider(0, 0, 1, 1));
    }

    /**
     * Should be called once per frame.
     * @param deltaTime The time elapsed, in seconds, since the last frame. Can
     *                  be used to determine a new position/velocity by multiplying
     *                  this delta with the velocity/acceleration respectively
     *                  and adding to the position/velocity:
     *                  velocity += deltaTime*acceleration
     *                  pos += deltaTime*velocity
     */
    public void update(float deltaTime) {
        transform.update(deltaTime);
        transform.setAccelerationEnabled(true); //might be cancelled when resolving collisions
        renderer.update(deltaTime);
        if(components != null) {
            for (var component : components)
                component.update(deltaTime);
            components.flushChanges();
        }
    }

    /**
     * An overload of {@link #render(Graphics2D, Camera)} for the default camera.
     */
    public void render(Graphics2D g) {
        renderer.render(g, transform.getTopLeftCorner(), transform.getDimensions());
    }

    /**
     * Renders the game object if the Renderable is not null.
     * Should be called in the rendering phase (i.e., not within an update).
     * The method is automatically called if the object was added to an active
     * GameObjectCollection.
     * @param g The graphics objects passed to GameManager.render.
     * @param camera The active camera
     */
    public void render(Graphics2D g, Camera camera) {
        if(camera == null || coordinateSpace == CoordinateSpace.CAMERA_COORDINATES) {
            render(g);
            return;
        }
        if(coordinateSpace != CoordinateSpace.WORLD_COORDINATES)
            throw new UnsupportedOperationException(coordinateSpace + ": Unsupported coordinate space");
        //then coords are in world space, translate to screen space
        float dimFactorX = camera.windowDimensions().x()/camera.getDimensions().x();
        float dimFactorY = camera.windowDimensions().y()/camera.getDimensions().y();
        freeCalculationsVector.setXY(getDimensions().x()*dimFactorX,getDimensions().y()*dimFactorY);
        renderer.render(g, camera.worldToScreenCoords(getTopLeftCorner()), freeCalculationsVector);
    }

    /**
     * Should this object be allowed to collide the the specified other object.
     * If both this object returns true for the other, and the other returns true
     * for this one, the collisions may occur when they overlap, meaning that their
     * respective onCollisionEnter/onCollisionStay/onCollisionExit will be called.
     * Note that this assumes that both objects have been added to the same
     * GameObjectCollection, and that its handleCollisions() method is invoked.
     * @param other The other GameObject.
     * @return true if the objects should collide. This does not guarantee a collision
     * would actually collide if they overlap, since the other object has to confirm
     * this one as well.
     */
    public boolean shouldCollideWith(GameObject other) {
        return true;
    }

    /**
     * Called on the first frame of a collision.
     * @param other The GameObject with which a collision occurred.
     * @param collision Information regarding this collision.
     *                  A reasonable elastic behavior can be achieved with:
     *                  setVelocity(getVelocity().flipped(collision.getNormal()));
     */
    public void onCollisionEnter(GameObject other, Collision collision) { }

    /**
     * Called on every frame of a collision with a given object, including the first.
     * @param other The collision partner.
     * @param collision Information regarding this collision.
     */
    public void onCollisionStay(GameObject other, Collision collision) { }

    /**
     * Called once, AFTER a collision ended.
     * @param other The former collision partner.
     */
    public void onCollisionExit(GameObject other) { }

    /**
     * Get the center of this GameObject, in window coordinates.
     */
    public Vector2 getCenter() {
        return transform.getCenter();
    }

    /**
     * Set the new center position of the object, in window coordinates.
     */
    public void setCenter(Vector2 center) {
        transform.setCenter(center);
    }

    /**
     * Get the top-left corner of this GameObject, in window coordinates.
     */
    public Vector2 getTopLeftCorner() {
        return transform.getTopLeftCorner();
    }

    /**
     * Set the top-left corner of this object. Moves the entire object so that
     * its top-left corner is as specified.
     */
    public void setTopLeftCorner(Vector2 topLeftCorner) {
        transform.setTopLeftCorner(topLeftCorner);
    }

    /**
     * Returns the current velocity.
     */
    public Vector2 getVelocity() {
        return transform.getVelocity();
    }

    /**
     * Set the object's velocity.
     */
    public void setVelocity(Vector2 velocity) {
        transform.setVelocity(velocity);
    }

    /**
     * Get the object's width and height.
     */
    public Vector2 getDimensions() {
        return transform.getDimensions();
    }

    /**
     * Sets the object's width and height. Its center remains the same,
     * while its corners move.
     */
    public void setDimensions(Vector2 dimensions) {
        transform.setDimensions(dimensions);
    }

    /**
     * Gets the object's tag. The tag has no inherent meaning on its own
     * and is not used by the GameObject class itself;
     * it can be assigned any meaning by the user of the class,
     * and is simply a convenience placeholder for custom info.
     */
    public String getTag() {
        return tag;
    }

    /**
     * Sets the object's tag. The tag has no inherent meaning on its own
     * and is not used by the GameObject class itself;
     * it can be assigned any meaning by the user of the class,
     * and is simply a convenience placeholder for custom info.
     */
    public void setTag(String tag) {
        this.tag = tag;
    }

    /**
     * Add a component to the GameObject. A component includes a method that should be executed
     * on every update.
     */
    public void addComponent(Component component) {
        if(components == null)
            components = new ModifiableList<>(false);
        components.add(component);
    }

    /**
     * remove a component from the GameObject. A component includes a method that should be executed
     * on every update.
     */
    public void removeComponent(Component component) {
        components.remove(component);
    }

    /**
     * access to attributes concerning the rendering of the object (the Renderable
     * that represents it, the Renderable's angle etc.)
     */
    public RendererComponent renderer() { return renderer; }

    /**
     * access to attributes concerning the object's transform (position, dimensions, etc.)
     */
    public Transform transform() { return transform; }

    /**
     * Gets the coordinate space to which the object's position is relative.
     * The default is world coordinates, but the parameter may specify that
     * the object is relative to the camera in which case it moves when the
     * camera moves.
     */
    public CoordinateSpace getCoordinateSpace() {
        return coordinateSpace;
    }

    /**
     * Sets the coordinate space to which the object's position is relative.
     * The default is world coordinates, but the parameter may specify that
     * the object is relative to the camera in which case it moves when the
     * camera moves.
     */
    public void setCoordinateSpace(CoordinateSpace coordinateSpace) {
        if(null == coordinateSpace)
            throw new IllegalArgumentException("Coordinate space cannot be null");
        this.coordinateSpace = coordinateSpace;
    }

    /**
     * Translates world coordinates to object coordinates (relative to the top-left corner and
     * indifferent to dimensions).
     * @param worldCoords a point in world coordinates
     * @return a point in object coordinates
     */
    public Vector2 worldToObjectCoords(Vector2 worldCoords) {
        freeCalculationsVector.setXY(worldCoords);
        freeCalculationsVector.selfSubtract(getTopLeftCorner());
        freeCalculationsVector.selfMultX(1f/getDimensions().x()).selfMultY(1f/getDimensions().y());
        return freeCalculationsVector;
    }

    /**
     * Translates objects coordinates to world coordinates
     * @param objectCoords a point relative to the top-left corner and
     *                     indifferent to dimensions
     * @return a point in world coordinates
     */
    public Vector2 objectToWorldCoords(Vector2 objectCoords) {
        freeCalculationsVector.setXY(objectCoords);
        freeCalculationsVector.selfMultX(getDimensions().x()).selfMultY(getDimensions().y())
                .selfAdd(getTopLeftCorner());
        return freeCalculationsVector;
    }

    /**
     * Checks whether the GameObject's collider contains a specified point.
     * @param point a point in world coordinates
     * @return true iff the collider contains the given point
     */
    public boolean containsPoint(Vector2 point) {
        return physics.collider().containsPoint(worldToObjectCoords(point));
    }

    /**
     * Invoked on the game object in case the mouse has been clicked, pressed, released, or scrolled
     * while the cursor is on top of the game object.
     * @param params info regarding the mouse's parameters in this frame and the action that occurred.
     */
    public void onMouseAction(MouseActionParams params) { }

    /**
     * access physics-related attributes of the GameObject
     */
    public GameObjectPhysics physics() {
        return physics;
    }
}
