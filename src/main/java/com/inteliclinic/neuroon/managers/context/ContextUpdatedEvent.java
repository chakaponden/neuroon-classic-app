package com.inteliclinic.neuroon.managers.context;

public class ContextUpdatedEvent {
    private IContextManager mContext;

    public ContextUpdatedEvent(IContextManager context) {
        this.mContext = context;
    }

    public IContextManager getContext() {
        return this.mContext;
    }
}
