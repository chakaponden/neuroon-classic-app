package de.greenrobot.event;

import android.util.Log;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

class SubscriberMethodFinder {
    private static final int BRIDGE = 64;
    private static final int MODIFIERS_IGNORE = 5192;
    private static final String ON_EVENT_METHOD_NAME = "onEvent";
    private static final int SYNTHETIC = 4096;
    private static final Map<String, List<SubscriberMethod>> methodCache = new HashMap();
    private final Map<Class<?>, Class<?>> skipMethodVerificationForClasses = new ConcurrentHashMap();

    SubscriberMethodFinder(List<Class<?>> skipMethodVerificationForClassesList) {
        if (skipMethodVerificationForClassesList != null) {
            for (Class<?> clazz : skipMethodVerificationForClassesList) {
                this.skipMethodVerificationForClasses.put(clazz, clazz);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public List<SubscriberMethod> findSubscriberMethods(Class<?> subscriberClass) {
        List<SubscriberMethod> subscriberMethods;
        String key = subscriberClass.getName();
        synchronized (methodCache) {
            subscriberMethods = methodCache.get(key);
        }
        if (subscriberMethods != null) {
            return subscriberMethods;
        }
        List<SubscriberMethod> subscriberMethods2 = new ArrayList<>();
        Class<?> clazz = subscriberClass;
        HashMap<String, Class> eventTypesFound = new HashMap<>();
        StringBuilder methodKeyBuilder = new StringBuilder();
        while (clazz != null) {
            String name = clazz.getName();
            if (name.startsWith("java.") || name.startsWith("javax.") || name.startsWith("android.")) {
                break;
            }
            try {
                filterSubscriberMethods(subscriberMethods2, eventTypesFound, methodKeyBuilder, clazz.getDeclaredMethods());
                clazz = clazz.getSuperclass();
            } catch (Throwable th) {
                th.printStackTrace();
                Method[] methods = subscriberClass.getMethods();
                subscriberMethods2.clear();
                eventTypesFound.clear();
                filterSubscriberMethods(subscriberMethods2, eventTypesFound, methodKeyBuilder, methods);
            }
        }
        if (subscriberMethods2.isEmpty()) {
            throw new EventBusException("Subscriber " + subscriberClass + " has no public methods called " + ON_EVENT_METHOD_NAME);
        }
        synchronized (methodCache) {
            methodCache.put(key, subscriberMethods2);
        }
        return subscriberMethods2;
    }

    private void filterSubscriberMethods(List<SubscriberMethod> subscriberMethods, HashMap<String, Class> eventTypesFound, StringBuilder methodKeyBuilder, Method[] methods) {
        ThreadMode threadMode;
        for (Method method : methods) {
            String methodName = method.getName();
            if (methodName.startsWith(ON_EVENT_METHOD_NAME)) {
                int modifiers = method.getModifiers();
                Class<?> methodClass = method.getDeclaringClass();
                if ((modifiers & 1) != 0 && (modifiers & MODIFIERS_IGNORE) == 0) {
                    Class<?>[] parameterTypes = method.getParameterTypes();
                    if (parameterTypes.length == 1 && (threadMode = getThreadMode(methodClass, method, methodName)) != null) {
                        Class<?> eventType = parameterTypes[0];
                        methodKeyBuilder.setLength(0);
                        methodKeyBuilder.append(methodName);
                        methodKeyBuilder.append('>').append(eventType.getName());
                        String methodKey = methodKeyBuilder.toString();
                        Class methodClassOld = eventTypesFound.put(methodKey, methodClass);
                        if (methodClassOld == null || methodClassOld.isAssignableFrom(methodClass)) {
                            subscriberMethods.add(new SubscriberMethod(method, threadMode, eventType));
                        } else {
                            eventTypesFound.put(methodKey, methodClassOld);
                        }
                    }
                } else if (!this.skipMethodVerificationForClasses.containsKey(methodClass)) {
                    Log.d(EventBus.TAG, "Skipping method (not public, static or abstract): " + methodClass + "." + methodName);
                }
            }
        }
    }

    private ThreadMode getThreadMode(Class<?> clazz, Method method, String methodName) {
        String modifierString = methodName.substring(ON_EVENT_METHOD_NAME.length());
        if (modifierString.length() == 0) {
            return ThreadMode.PostThread;
        }
        if (modifierString.equals("MainThread")) {
            return ThreadMode.MainThread;
        }
        if (modifierString.equals("BackgroundThread")) {
            return ThreadMode.BackgroundThread;
        }
        if (modifierString.equals("Async")) {
            return ThreadMode.Async;
        }
        if (this.skipMethodVerificationForClasses.containsKey(clazz)) {
            return null;
        }
        throw new EventBusException("Illegal onEvent method, check for typos: " + method);
    }

    static void clearCaches() {
        synchronized (methodCache) {
            methodCache.clear();
        }
    }
}
