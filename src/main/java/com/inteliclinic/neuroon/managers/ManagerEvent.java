package com.inteliclinic.neuroon.managers;

public class ManagerEvent<T> {
    private final T mClass;

    public ManagerEvent(T aClass) {
        this.mClass = aClass;
    }

    public T getManager() {
        return this.mClass;
    }
}
