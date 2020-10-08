package com.inteliclinic.neuroon.managers;

public class ManagerStartedEvent<T> extends ManagerEvent<T> {
    public ManagerStartedEvent(T aClass) {
        super(aClass);
    }
}
