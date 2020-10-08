package com.inteliclinic.neuroon.old_guava;

import java.util.ArrayList;
import java.util.Iterator;

public final class Lists {
    private Lists() {
    }

    public static <E> ArrayList<E> newArrayList(Iterator<? extends E> elements) {
        ArrayList<E> list = new ArrayList<>();
        while (elements.hasNext()) {
            list.add(elements.next());
        }
        return list;
    }
}
