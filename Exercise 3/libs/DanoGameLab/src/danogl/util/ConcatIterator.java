package danogl.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * A package-private class required for the implementation of ModifiableList.
 * @author Dan Nirel
 */
public class ConcatIterator<E> implements Iterator<E> {
    private Iterator<Iterable<E>> iterablesIterator;
    private Iterator<E> iteratorInIterable;

    public ConcatIterator(Iterable<Iterable<E>> iterablesToConcat) {
        iterablesIterator = iterablesToConcat.iterator();
    }

    @Override
    public boolean hasNext() {
        //if this is the first time
        if(iteratorInIterable == null) {
            if(!iterablesIterator.hasNext())
                return false;
            iteratorInIterable = iterablesIterator.next().iterator();
        }
        //skip to the next iterable with elements in it, or to the end of iterables
        while (!iteratorInIterable.hasNext() && iterablesIterator.hasNext()) {
            iteratorInIterable = iterablesIterator.next().iterator();
        }
        return iteratorInIterable.hasNext();
    }

    @Override
    public E next() {
        return iteratorInIterable.next();
    }
}

