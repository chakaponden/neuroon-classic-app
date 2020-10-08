package com.inteliclinic.neuroon.old_guava;

import java.util.Iterator;

public final class Iterators {
    private Iterators() {
    }

    public static <T> T get(Iterator<T> iterator, int position) {
        for (int i = 0; i < position && iterator.hasNext(); i++) {
            iterator.next();
        }
        if (iterator.hasNext()) {
            return iterator.next();
        }
        throw new IndexOutOfBoundsException("position (" + position + ") must be less than the number of elements");
    }
}
