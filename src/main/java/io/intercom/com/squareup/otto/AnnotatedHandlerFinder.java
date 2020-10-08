package io.intercom.com.squareup.otto;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

final class AnnotatedHandlerFinder {
    private static final Map<Class<?>, Map<Class<?>, Method>> PRODUCERS_CACHE = new HashMap();
    private static final Map<Class<?>, Map<Class<?>, Set<Method>>> SUBSCRIBERS_CACHE = new HashMap();

    private static void loadAnnotatedMethods(Class<?> listenerClass) {
        Map<Class<?>, Set<Method>> subscriberMethods = new HashMap<>();
        Map<Class<?>, Method> producerMethods = new HashMap<>();
        for (Method method : listenerClass.getDeclaredMethods()) {
            if (!method.isBridge()) {
                if (method.isAnnotationPresent(Subscribe.class)) {
                    Class<?>[] parameterTypes = method.getParameterTypes();
                    if (parameterTypes.length != 1) {
                        throw new IllegalArgumentException("Method " + method + " has @Subscribe annotation but requires " + parameterTypes.length + " arguments.  Methods must require a single argument.");
                    }
                    Class<?> eventType = parameterTypes[0];
                    if (eventType.isInterface()) {
                        throw new IllegalArgumentException("Method " + method + " has @Subscribe annotation on " + eventType + " which is an interface.  Subscription must be on a concrete class type.");
                    } else if ((method.getModifiers() & 1) == 0) {
                        throw new IllegalArgumentException("Method " + method + " has @Subscribe annotation on " + eventType + " but is not 'public'.");
                    } else {
                        Set<Method> methods = subscriberMethods.get(eventType);
                        if (methods == null) {
                            methods = new HashSet<>();
                            subscriberMethods.put(eventType, methods);
                        }
                        methods.add(method);
                    }
                } else if (method.isAnnotationPresent(Produce.class)) {
                    Class<?>[] parameterTypes2 = method.getParameterTypes();
                    if (parameterTypes2.length != 0) {
                        throw new IllegalArgumentException("Method " + method + "has @Produce annotation but requires " + parameterTypes2.length + " arguments.  Methods must require zero arguments.");
                    } else if (method.getReturnType() == Void.class) {
                        throw new IllegalArgumentException("Method " + method + " has a return type of void.  Must declare a non-void type.");
                    } else {
                        Class<?> eventType2 = method.getReturnType();
                        if (eventType2.isInterface()) {
                            throw new IllegalArgumentException("Method " + method + " has @Produce annotation on " + eventType2 + " which is an interface.  Producers must return a concrete class type.");
                        } else if (eventType2.equals(Void.TYPE)) {
                            throw new IllegalArgumentException("Method " + method + " has @Produce annotation but has no return type.");
                        } else if ((method.getModifiers() & 1) == 0) {
                            throw new IllegalArgumentException("Method " + method + " has @Produce annotation on " + eventType2 + " but is not 'public'.");
                        } else if (producerMethods.containsKey(eventType2)) {
                            throw new IllegalArgumentException("Producer for type " + eventType2 + " has already been registered.");
                        } else {
                            producerMethods.put(eventType2, method);
                        }
                    }
                } else {
                    continue;
                }
            }
        }
        PRODUCERS_CACHE.put(listenerClass, producerMethods);
        SUBSCRIBERS_CACHE.put(listenerClass, subscriberMethods);
    }

    static Map<Class<?>, EventProducer> findAllProducers(Object listener) {
        Class<?> listenerClass = listener.getClass();
        Map<Class<?>, EventProducer> handlersInMethod = new HashMap<>();
        if (!PRODUCERS_CACHE.containsKey(listenerClass)) {
            loadAnnotatedMethods(listenerClass);
        }
        Map<Class<?>, Method> methods = PRODUCERS_CACHE.get(listenerClass);
        if (!methods.isEmpty()) {
            for (Map.Entry<Class<?>, Method> e : methods.entrySet()) {
                handlersInMethod.put(e.getKey(), new EventProducer(listener, e.getValue()));
            }
        }
        return handlersInMethod;
    }

    static Map<Class<?>, Set<EventHandler>> findAllSubscribers(Object listener) {
        Class<?> listenerClass = listener.getClass();
        Map<Class<?>, Set<EventHandler>> handlersInMethod = new HashMap<>();
        if (!SUBSCRIBERS_CACHE.containsKey(listenerClass)) {
            loadAnnotatedMethods(listenerClass);
        }
        Map<Class<?>, Set<Method>> methods = SUBSCRIBERS_CACHE.get(listenerClass);
        if (!methods.isEmpty()) {
            for (Map.Entry<Class<?>, Set<Method>> e : methods.entrySet()) {
                Set<EventHandler> handlers = new HashSet<>();
                for (Method m : e.getValue()) {
                    handlers.add(new EventHandler(listener, m));
                }
                handlersInMethod.put(e.getKey(), handlers);
            }
        }
        return handlersInMethod;
    }

    private AnnotatedHandlerFinder() {
    }
}
