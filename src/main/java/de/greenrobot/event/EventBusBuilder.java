package de.greenrobot.event;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EventBusBuilder {
    private static final ExecutorService DEFAULT_EXECUTOR_SERVICE = Executors.newCachedThreadPool();
    boolean eventInheritance = true;
    ExecutorService executorService = DEFAULT_EXECUTOR_SERVICE;
    boolean logNoSubscriberMessages = true;
    boolean logSubscriberExceptions = true;
    boolean sendNoSubscriberEvent = true;
    boolean sendSubscriberExceptionEvent = true;
    List<Class<?>> skipMethodVerificationForClasses;
    boolean throwSubscriberException;

    EventBusBuilder() {
    }

    public EventBusBuilder logSubscriberExceptions(boolean logSubscriberExceptions2) {
        this.logSubscriberExceptions = logSubscriberExceptions2;
        return this;
    }

    public EventBusBuilder logNoSubscriberMessages(boolean logNoSubscriberMessages2) {
        this.logNoSubscriberMessages = logNoSubscriberMessages2;
        return this;
    }

    public EventBusBuilder sendSubscriberExceptionEvent(boolean sendSubscriberExceptionEvent2) {
        this.sendSubscriberExceptionEvent = sendSubscriberExceptionEvent2;
        return this;
    }

    public EventBusBuilder sendNoSubscriberEvent(boolean sendNoSubscriberEvent2) {
        this.sendNoSubscriberEvent = sendNoSubscriberEvent2;
        return this;
    }

    public EventBusBuilder throwSubscriberException(boolean throwSubscriberException2) {
        this.throwSubscriberException = throwSubscriberException2;
        return this;
    }

    public EventBusBuilder eventInheritance(boolean eventInheritance2) {
        this.eventInheritance = eventInheritance2;
        return this;
    }

    public EventBusBuilder executorService(ExecutorService executorService2) {
        this.executorService = executorService2;
        return this;
    }

    public EventBusBuilder skipMethodVerificationFor(Class<?> clazz) {
        if (this.skipMethodVerificationForClasses == null) {
            this.skipMethodVerificationForClasses = new ArrayList();
        }
        this.skipMethodVerificationForClasses.add(clazz);
        return this;
    }

    public EventBus installDefaultEventBus() {
        EventBus eventBus;
        synchronized (EventBus.class) {
            if (EventBus.defaultInstance != null) {
                throw new EventBusException("Default instance already exists. It may be only set once before it's used the first time to ensure consistent behavior.");
            }
            EventBus.defaultInstance = build();
            eventBus = EventBus.defaultInstance;
        }
        return eventBus;
    }

    public EventBus build() {
        return new EventBus(this);
    }
}
