package io.intercom.com.squareup.otto;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;

public class Bus {
    public static final String DEFAULT_IDENTIFIER = "default";
    private final ThreadEnforcer enforcer;
    private final ThreadLocal<ConcurrentLinkedQueue<EventWithHandler>> eventsToDispatch;
    private final ConcurrentMap<Class<?>, Set<Class<?>>> flattenHierarchyCache;
    private final HandlerFinder handlerFinder;
    private final ConcurrentMap<Class<?>, Set<EventHandler>> handlersByType;
    private final String identifier;
    private final ThreadLocal<Boolean> isDispatching;
    private final ConcurrentMap<Class<?>, EventProducer> producersByType;

    public Bus() {
        this(DEFAULT_IDENTIFIER);
    }

    public Bus(String identifier2) {
        this(ThreadEnforcer.MAIN, identifier2);
    }

    public Bus(ThreadEnforcer enforcer2) {
        this(enforcer2, DEFAULT_IDENTIFIER);
    }

    public Bus(ThreadEnforcer enforcer2, String identifier2) {
        this(enforcer2, identifier2, HandlerFinder.ANNOTATED);
    }

    Bus(ThreadEnforcer enforcer2, String identifier2, HandlerFinder handlerFinder2) {
        this.handlersByType = new ConcurrentHashMap();
        this.producersByType = new ConcurrentHashMap();
        this.eventsToDispatch = new ThreadLocal<ConcurrentLinkedQueue<EventWithHandler>>() {
            /* access modifiers changed from: protected */
            public ConcurrentLinkedQueue<EventWithHandler> initialValue() {
                return new ConcurrentLinkedQueue<>();
            }
        };
        this.isDispatching = new ThreadLocal<Boolean>() {
            /* access modifiers changed from: protected */
            public Boolean initialValue() {
                return false;
            }
        };
        this.flattenHierarchyCache = new ConcurrentHashMap();
        this.enforcer = enforcer2;
        this.identifier = identifier2;
        this.handlerFinder = handlerFinder2;
    }

    public String toString() {
        return "[Bus \"" + this.identifier + "\"]";
    }

    /* JADX WARNING: Code restructure failed: missing block: B:23:0x00c0, code lost:
        r7 = new java.util.concurrent.CopyOnWriteArraySet<>();
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void register(java.lang.Object r15) {
        /*
            r14 = this;
            if (r15 != 0) goto L_0x000a
            java.lang.NullPointerException r11 = new java.lang.NullPointerException
            java.lang.String r12 = "Object to register must not be null."
            r11.<init>(r12)
            throw r11
        L_0x000a:
            io.intercom.com.squareup.otto.ThreadEnforcer r11 = r14.enforcer
            r11.enforce(r14)
            io.intercom.com.squareup.otto.HandlerFinder r11 = r14.handlerFinder
            java.util.Map r4 = r11.findAllProducers(r15)
            java.util.Set r11 = r4.keySet()
            java.util.Iterator r11 = r11.iterator()
        L_0x001d:
            boolean r12 = r11.hasNext()
            if (r12 == 0) goto L_0x009c
            java.lang.Object r10 = r11.next()
            java.lang.Class r10 = (java.lang.Class) r10
            java.lang.Object r9 = r4.get(r10)
            io.intercom.com.squareup.otto.EventProducer r9 = (io.intercom.com.squareup.otto.EventProducer) r9
            java.util.concurrent.ConcurrentMap<java.lang.Class<?>, io.intercom.com.squareup.otto.EventProducer> r12 = r14.producersByType
            java.lang.Object r8 = r12.putIfAbsent(r10, r9)
            io.intercom.com.squareup.otto.EventProducer r8 = (io.intercom.com.squareup.otto.EventProducer) r8
            if (r8 == 0) goto L_0x0078
            java.lang.IllegalArgumentException r11 = new java.lang.IllegalArgumentException
            java.lang.StringBuilder r12 = new java.lang.StringBuilder
            r12.<init>()
            java.lang.String r13 = "Producer method for type "
            java.lang.StringBuilder r12 = r12.append(r13)
            java.lang.StringBuilder r12 = r12.append(r10)
            java.lang.String r13 = " found on type "
            java.lang.StringBuilder r12 = r12.append(r13)
            java.lang.Object r13 = r9.target
            java.lang.Class r13 = r13.getClass()
            java.lang.StringBuilder r12 = r12.append(r13)
            java.lang.String r13 = ", but already registered by type "
            java.lang.StringBuilder r12 = r12.append(r13)
            java.lang.Object r13 = r8.target
            java.lang.Class r13 = r13.getClass()
            java.lang.StringBuilder r12 = r12.append(r13)
            java.lang.String r13 = "."
            java.lang.StringBuilder r12 = r12.append(r13)
            java.lang.String r12 = r12.toString()
            r11.<init>(r12)
            throw r11
        L_0x0078:
            java.util.concurrent.ConcurrentMap<java.lang.Class<?>, java.util.Set<io.intercom.com.squareup.otto.EventHandler>> r12 = r14.handlersByType
            java.lang.Object r6 = r12.get(r10)
            java.util.Set r6 = (java.util.Set) r6
            if (r6 == 0) goto L_0x001d
            boolean r12 = r6.isEmpty()
            if (r12 != 0) goto L_0x001d
            java.util.Iterator r12 = r6.iterator()
        L_0x008c:
            boolean r13 = r12.hasNext()
            if (r13 == 0) goto L_0x001d
            java.lang.Object r5 = r12.next()
            io.intercom.com.squareup.otto.EventHandler r5 = (io.intercom.com.squareup.otto.EventHandler) r5
            r14.dispatchProducerResultToHandler(r5, r9)
            goto L_0x008c
        L_0x009c:
            io.intercom.com.squareup.otto.HandlerFinder r11 = r14.handlerFinder
            java.util.Map r3 = r11.findAllSubscribers(r15)
            java.util.Set r11 = r3.keySet()
            java.util.Iterator r11 = r11.iterator()
        L_0x00aa:
            boolean r12 = r11.hasNext()
            if (r12 == 0) goto L_0x00e4
            java.lang.Object r10 = r11.next()
            java.lang.Class r10 = (java.lang.Class) r10
            java.util.concurrent.ConcurrentMap<java.lang.Class<?>, java.util.Set<io.intercom.com.squareup.otto.EventHandler>> r12 = r14.handlersByType
            java.lang.Object r6 = r12.get(r10)
            java.util.Set r6 = (java.util.Set) r6
            if (r6 != 0) goto L_0x00d0
            java.util.concurrent.CopyOnWriteArraySet r7 = new java.util.concurrent.CopyOnWriteArraySet
            r7.<init>()
            java.util.concurrent.ConcurrentMap<java.lang.Class<?>, java.util.Set<io.intercom.com.squareup.otto.EventHandler>> r12 = r14.handlersByType
            java.lang.Object r6 = r12.putIfAbsent(r10, r7)
            java.util.Set r6 = (java.util.Set) r6
            if (r6 != 0) goto L_0x00d0
            r6 = r7
        L_0x00d0:
            java.lang.Object r2 = r3.get(r10)
            java.util.Set r2 = (java.util.Set) r2
            boolean r12 = r6.addAll(r2)
            if (r12 != 0) goto L_0x00aa
            java.lang.IllegalArgumentException r11 = new java.lang.IllegalArgumentException
            java.lang.String r12 = "Object already registered."
            r11.<init>(r12)
            throw r11
        L_0x00e4:
            java.util.Set r11 = r3.entrySet()
            java.util.Iterator r11 = r11.iterator()
        L_0x00ec:
            boolean r12 = r11.hasNext()
            if (r12 == 0) goto L_0x0134
            java.lang.Object r0 = r11.next()
            java.util.Map$Entry r0 = (java.util.Map.Entry) r0
            java.lang.Object r10 = r0.getKey()
            java.lang.Class r10 = (java.lang.Class) r10
            java.util.concurrent.ConcurrentMap<java.lang.Class<?>, io.intercom.com.squareup.otto.EventProducer> r12 = r14.producersByType
            java.lang.Object r9 = r12.get(r10)
            io.intercom.com.squareup.otto.EventProducer r9 = (io.intercom.com.squareup.otto.EventProducer) r9
            if (r9 == 0) goto L_0x00ec
            boolean r12 = r9.isValid()
            if (r12 == 0) goto L_0x00ec
            java.lang.Object r2 = r0.getValue()
            java.util.Set r2 = (java.util.Set) r2
            java.util.Iterator r12 = r2.iterator()
        L_0x0118:
            boolean r13 = r12.hasNext()
            if (r13 == 0) goto L_0x00ec
            java.lang.Object r1 = r12.next()
            io.intercom.com.squareup.otto.EventHandler r1 = (io.intercom.com.squareup.otto.EventHandler) r1
            boolean r13 = r9.isValid()
            if (r13 == 0) goto L_0x00ec
            boolean r13 = r1.isValid()
            if (r13 == 0) goto L_0x0118
            r14.dispatchProducerResultToHandler(r1, r9)
            goto L_0x0118
        L_0x0134:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: io.intercom.com.squareup.otto.Bus.register(java.lang.Object):void");
    }

    private void dispatchProducerResultToHandler(EventHandler handler, EventProducer producer) {
        Object event = null;
        try {
            event = producer.produceEvent();
        } catch (InvocationTargetException e) {
            throwRuntimeException("Producer " + producer + " threw an exception.", e);
        }
        if (event != null) {
            dispatch(event, handler);
        }
    }

    public void unregister(Object object) {
        if (object == null) {
            throw new NullPointerException("Object to unregister must not be null.");
        }
        this.enforcer.enforce(this);
        for (Map.Entry<Class<?>, EventProducer> entry : this.handlerFinder.findAllProducers(object).entrySet()) {
            Class<?> key = entry.getKey();
            EventProducer producer = getProducerForEventType(key);
            EventProducer value = entry.getValue();
            if (value == null || !value.equals(producer)) {
                throw new IllegalArgumentException("Missing event producer for an annotated method. Is " + object.getClass() + " registered?");
            }
            ((EventProducer) this.producersByType.remove(key)).invalidate();
        }
        for (Map.Entry<Class<?>, Set<EventHandler>> entry2 : this.handlerFinder.findAllSubscribers(object).entrySet()) {
            Set<EventHandler> currentHandlers = getHandlersForEventType(entry2.getKey());
            Collection<EventHandler> eventMethodsInListener = entry2.getValue();
            if (currentHandlers == null || !currentHandlers.containsAll(eventMethodsInListener)) {
                throw new IllegalArgumentException("Missing event handler for an annotated method. Is " + object.getClass() + " registered?");
            }
            for (EventHandler handler : currentHandlers) {
                if (eventMethodsInListener.contains(handler)) {
                    handler.invalidate();
                }
            }
            currentHandlers.removeAll(eventMethodsInListener);
        }
    }

    public void post(Object event) {
        if (event == null) {
            throw new NullPointerException("Event to post must not be null.");
        }
        this.enforcer.enforce(this);
        boolean dispatched = false;
        for (Class<?> eventType : flattenHierarchy(event.getClass())) {
            Set<EventHandler> wrappers = getHandlersForEventType(eventType);
            if (wrappers != null && !wrappers.isEmpty()) {
                dispatched = true;
                for (EventHandler wrapper : wrappers) {
                    enqueueEvent(event, wrapper);
                }
            }
        }
        if (!dispatched && !(event instanceof DeadEvent)) {
            post(new DeadEvent(this, event));
        }
        dispatchQueuedEvents();
    }

    /* access modifiers changed from: protected */
    public void enqueueEvent(Object event, EventHandler handler) {
        this.eventsToDispatch.get().offer(new EventWithHandler(event, handler));
    }

    /* access modifiers changed from: protected */
    public void dispatchQueuedEvents() {
        if (!this.isDispatching.get().booleanValue()) {
            this.isDispatching.set(true);
            while (true) {
                try {
                    EventWithHandler eventWithHandler = (EventWithHandler) this.eventsToDispatch.get().poll();
                    if (eventWithHandler != null) {
                        if (eventWithHandler.handler.isValid()) {
                            dispatch(eventWithHandler.event, eventWithHandler.handler);
                        }
                    } else {
                        return;
                    }
                } finally {
                    this.isDispatching.set(false);
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public void dispatch(Object event, EventHandler wrapper) {
        try {
            wrapper.handleEvent(event);
        } catch (InvocationTargetException e) {
            throwRuntimeException("Could not dispatch event: " + event.getClass() + " to handler " + wrapper, e);
        }
    }

    /* access modifiers changed from: package-private */
    public EventProducer getProducerForEventType(Class<?> type) {
        return (EventProducer) this.producersByType.get(type);
    }

    /* access modifiers changed from: package-private */
    public Set<EventHandler> getHandlersForEventType(Class<?> type) {
        return (Set) this.handlersByType.get(type);
    }

    /* access modifiers changed from: package-private */
    public Set<Class<?>> flattenHierarchy(Class<?> concreteClass) {
        Set<Class<?>> classes = (Set) this.flattenHierarchyCache.get(concreteClass);
        if (classes != null) {
            return classes;
        }
        Set<Class<?>> classesCreation = getClassesFor(concreteClass);
        Set<Class<?>> classes2 = this.flattenHierarchyCache.putIfAbsent(concreteClass, classesCreation);
        if (classes2 == null) {
            return classesCreation;
        }
        return classes2;
    }

    private Set<Class<?>> getClassesFor(Class<?> concreteClass) {
        List<Class<?>> parents = new LinkedList<>();
        Set<Class<?>> classes = new HashSet<>();
        parents.add(concreteClass);
        while (!parents.isEmpty()) {
            Class<?> clazz = parents.remove(0);
            classes.add(clazz);
            Class<? super Object> superclass = clazz.getSuperclass();
            if (superclass != null) {
                parents.add(superclass);
            }
        }
        return classes;
    }

    private static void throwRuntimeException(String msg, InvocationTargetException e) {
        Throwable cause = e.getCause();
        if (cause != null) {
            throw new RuntimeException(msg + ": " + cause.getMessage(), cause);
        }
        throw new RuntimeException(msg + ": " + e.getMessage(), e);
    }

    static class EventWithHandler {
        final Object event;
        final EventHandler handler;

        public EventWithHandler(Object event2, EventHandler handler2) {
            this.event = event2;
            this.handler = handler2;
        }
    }
}
