package danogl.collisions;

import danogl.GameObject;

import java.util.Objects;

/**
 * package-private class to encapsulate a pair of GameObjects.
 * @author Dan Nirel
 */
class GameObjectPair {
    private GameObject go1, go2;

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof GameObjectPair))
            return false;
        GameObjectPair other = (GameObjectPair)obj;
        return (go1 == other.go1 && go2 == other.go2) ||
                (go1 == other.go2 && go2 == other.go1);
    }

    @Override
    public int hashCode() {
        return Objects.hash(go1, go2);
    }

    public GameObject getGameObject(int index) {
        if(index == 0)
            return go1;
        if(index == 1)
            return go2;
        throw new IndexOutOfBoundsException();
    }

    public void setGameObject(int index, GameObject go) {
        if(index == 0)
            go1 = go;
        else if(index == 1)
            go2 = go;
        else throw new IndexOutOfBoundsException();
    }

    public GameObject go1() { return go1; }
    public GameObject go2() { return go2; }
}
