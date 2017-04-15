package com.gigamonkeys.go.genetics;

import java.util.Iterator;
import java.util.NoSuchElementException;

/*
 * Copyright (c) 2013 Peter Seibel
 */

/**
 * In Iterator implementation for when it's easier to try to generate
 * the next value and possibly fail.
 */
public abstract class LazyIterator<E> implements Iterator<E> {

    private E next = null;
    private NoSuchElementException end = null;

    /**
     * Implement this method to return the next value or throw
     * NoSuchElementException. Note, can not return null, which means
     * iterator will never return null.
     */
    public abstract E nextValue() throws NoSuchElementException;

    public boolean hasNext() {
        maybeGetNextValue();
        return end != null;
    }

    public E next() {
        maybeGetNextValue();
        if (end != null) {
            throw end;
        } else {
            E tmp = next;
            next = null;
            return tmp;
        }
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }

    private void maybeGetNextValue() {
        if (next == null) {
            try {
                next = nextValue();
            } catch (NoSuchElementException nsee) {
                end = nsee;
            }
        }
    }
}
