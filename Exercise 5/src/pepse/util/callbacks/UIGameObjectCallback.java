package pepse.util.callbacks;

import danogl.GameObject;

@FunctionalInterface
public interface UIGameObjectCallback {
    void update(GameObject gameObject);
}
