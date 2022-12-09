package image;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.BiFunction;

/**
 * A package-private class of the package image.
 * @author Dan Nirel
 */
class TwoDArrayIterator<T> implements Iterable<T> {

    private final T[][] array;

    public TwoDArrayIterator(T[][] array) {
        this.array = array;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            int x = 0, y = 0;

            @Override
            public boolean hasNext() {
                return array != null && y < array.length;
            }

            @Override
            public T next() {
                if (!hasNext())
                    throw new NoSuchElementException();
                var next = array[y][x];

                x += 1;

                if (x >= array[y].length) {
                    x = 0;
                    y += 1;
                }
                return next;
            }
        };
    }
}
