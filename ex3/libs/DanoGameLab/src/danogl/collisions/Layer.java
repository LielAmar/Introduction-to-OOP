package danogl.collisions;

/**
 * Container class for constants representing layer id's
 * @author Dan Nirel
 */
public final class Layer {
    public static final int
        BACKGROUND      = -200,
        STATIC_OBJECTS  = -100,
        DEFAULT         =  0,
        FOREGROUND      =  100,
        UI              =  200;

    private Layer() { }
}
