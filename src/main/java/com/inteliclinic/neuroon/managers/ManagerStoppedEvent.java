package com.inteliclinic.neuroon.managers;

public class ManagerStoppedEvent<T> extends ManagerEvent<T> {
    public ManagerStoppedEvent(T aClass) {
        super(aClass);
    }
}
