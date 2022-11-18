package danogl.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Predicate;

/**
 * Represents a list that can be modified while iterated,
 * as opposed to Java's builtin lists
 * @param <E> The element type
 * @author Dan Nirel
 */
public class ModifiableList<E> implements Iterable<E> {
    private class MyIterator implements Iterator<E> {
        private int index;
        private int indexStep;
        private int illegalIndex;
        private boolean iteratingNow = false;

        public boolean tryReset(int startIndex, int indexStep, int illegalIndex) {
            if(iteratingNow)
                return false;
            this.index = startIndex;
            this.indexStep = indexStep;
            this.illegalIndex = illegalIndex;
            return true;
        }

        @Override
        public boolean hasNext() {
            return index != illegalIndex;
        }

        @Override
        public E next() {
            iteratingNow = true;
            var element = list.get(index);
            index += indexStep;
            if(!hasNext())
                iteratingNow = false;
            return element;
        }
    }
    private class Reverse implements Iterable<E> {
        @Override
        public Iterator<E> iterator() {
            if(iterator.tryReset(list.size()-1, -1, -1))
                return iterator;
            var newIt = new MyIterator();
            newIt.tryReset(list.size()-1, -1, -1);
            return newIt;
        }
    }

    private List<E> list = new ArrayList<>();
    private List<E> toAdd = new ArrayList<>();
    private List<E> toRemove = new ArrayList<>();
    private MyIterator iterator = new MyIterator();
    private Reverse reverseIterable;
    private boolean allowDuplicates;

    /**
     * Creates a new modfiable list
     * @param allowDuplicates whether the list should allow duplicate elements
     *                        (elements that are "equal" according to equals)
     */
    public ModifiableList(boolean allowDuplicates) {
        this.allowDuplicates = allowDuplicates;
    }

    /**
     * Add an element to the list. The element is not added immediately;
     * it's stored in a temporary buffer whose contents is only added to the list
     * on the next call to {@link #flushChanges()}.
     * @return true if the element was successfully added, or false
     * if the list does not allow duplicates and the element was already added.
     */
    public boolean add(E item) {
        if(!allowDuplicates && (toAdd.contains(item) || list.contains(item)))
            return false;
        toAdd.add(item);
        return true;
    }

    /**
     * Remove an element from the list. More specifically, it removes the first
     * element in the list that "equals" the specified item.
     * The element is not removed immediately;
     * it's stored in a temporary buffer whose contents is only removed from the list
     * on the next call to {@link #flushChanges()}. If the element in question was
     * added recently, before a call to flushChanges, the method will remove it from
     * the buffer waiting to be added.
     * <br>Note: in order to remove all elements equal to the argument, flushChanges must be called
     * in between calls to this method.
     * @return true iff some element will indeed be removed. If the element was already removed
     * recently, before a call to flushChanges, the method will return false and have no effect.
     */
    public boolean remove(E item) {
        boolean removedFromToAdd = toAdd.remove(item);
        if(!list.contains(item) || toRemove.contains(item))
            return removedFromToAdd;
        toRemove.add(item);
        return true;
    }

    /**
     * Applies to the list the modifications made to it since the last call to this method.
     */
    public void flushChanges() {
        list.removeAll(toRemove);
        toRemove.clear();
        list.addAll(toAdd);
        toAdd.clear();
    }

    /**
     * Returns an element in a given index.
     * @throws IndexOutOfBoundsException – if the index is out of range
     */
    public E get(int index) {
        return list.get(index);
    }

    /**
     * Returns the list's current size, not including changes made since the last call
     * to {@link #flushChanges()}.
     */
    public int size() {
        return list.size();
    }

    /**
     * Returns whether the list contains a given item.
     * If the element was added after the last call to {@link #flushChanges()}, the method
     * still returns true. If the element was removed after the last call to flushChanges,
     * the method will return false, even though it may contain duplicate elements that
     * would still remain after the removal is applied.
     */
    public boolean contains(E item) {
        return (list.contains(item) || toAdd.contains(item)) && !toRemove.contains(item);
    }

    @Override
    public Iterator<E> iterator() {
        if(iterator.tryReset(0, 1, list.size()))
            return iterator;
        var newIt = new MyIterator();
        newIt.tryReset(0, 1, list.size());
        return newIt;
    }

    @Override
    public Spliterator<E> spliterator() {
        return list.spliterator();
    }

    /**
     * Returns an Iterable that iterates the list in reverse.
     */
    public Iterable<E> reverseOrder() {
        if(reverseIterable == null)
            reverseIterable = new Reverse();
        return reverseIterable;
    }

    /**
     * Finds the first element in the list that matches the given predicate,
     * or null if none is found.
     */
    public E findFirst(Predicate<E> predicate) {
        for(E el : this) {
            if(!toRemove.contains(el) && predicate.test(el))
                return el;
        }
        for(E el : toAdd) {
            if(predicate.test(el))
                return el;
        }
        return null;
    }
}
