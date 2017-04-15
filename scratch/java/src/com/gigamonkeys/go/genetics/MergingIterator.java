package com.gigamonkeys.go.genetics;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/*
 * Copyright (c) 2013 Peter Seibel
 */

/**
 * Merge multiple iterators that return sorted results into a single
 * sorted iterator.
 */
public class MergingIterator<E> implements Iterator<E> {

    private final Iterator<E> i1;
    private final Iterator<E> i2;
    
    private E next1 = null;
    private E next2 = null;
    private Comparator<E> comparator;

    public MergingIterator(Iterator<E> i1, Iterator<E> i2, Comparator<E> comparator) {
        this.i1         = i1;
        this.i2         = i2;
        this.comparator = comparator;
    }
    
    public boolean hasNext() {
        maybeGetNextValues();
        return next1 != null || next2 != null;
    }

    public E next() {
        maybeGetNextValues();
        if (next1 != null && next2 == null) {
            return next1;
        } else if (next1 == null && next2 != null) {
            return next2;
        } else if (next1 != null && next2 != null) {
            int c = comparator.compare(next1, next2);
            if (c <= 0) {
                E tmp = next1;
                next1 = null;
                return tmp;
            } else {
                E tmp = next2;
                next2 = null;
                return tmp;
            }
        } else {
            throw new NoSuchElementException();
        }
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }

    private void maybeGetNextValues() {
        if (next1 == null && i1.hasNext()) {
            next1 = i1.next();
        }

        if (next2 == null && i2.hasNext()) {
            next2 = i2.next();
        }
    }

}
