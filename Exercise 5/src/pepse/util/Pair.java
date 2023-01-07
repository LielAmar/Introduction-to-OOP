package pepse.util;

import java.util.Objects;

/**
 * A generic class that holds a Pair of type T (a,b)
 *
 * @param <T> The generic type.
 */
public class Pair<T> {

    private final T a;
    private final T b;

    public Pair(T a, T b) {
        this.a = a;
        this.b = b;
    }

    public T getA() {
        return this.a;
    }

    public T getB() {
        return this.b;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Pair)) {
            return false;
        }

        Pair<?> other = (Pair<?>) object;

        return other.getA().equals(this.getA()) && other.getB().equals(this.getB());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.a, this.b);
    }
}
