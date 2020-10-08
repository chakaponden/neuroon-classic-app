package io.fabric.sdk.android.services.concurrency;

import io.fabric.sdk.android.services.concurrency.Dependency;
import io.fabric.sdk.android.services.concurrency.PriorityProvider;
import io.fabric.sdk.android.services.concurrency.Task;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class DependencyPriorityBlockingQueue<E extends Dependency & Task & PriorityProvider> extends PriorityBlockingQueue<E> {
    static final int PEEK = 1;
    static final int POLL = 2;
    static final int POLL_WITH_TIMEOUT = 3;
    static final int TAKE = 0;
    final Queue<E> blockedQueue = new LinkedList();
    private final ReentrantLock lock = new ReentrantLock();

    public E take() throws InterruptedException {
        return get(0, (Long) null, (TimeUnit) null);
    }

    public E peek() {
        try {
            return get(1, (Long) null, (TimeUnit) null);
        } catch (InterruptedException e) {
            return null;
        }
    }

    public E poll(long timeout, TimeUnit unit) throws InterruptedException {
        return get(3, Long.valueOf(timeout), unit);
    }

    public E poll() {
        try {
            return get(2, (Long) null, (TimeUnit) null);
        } catch (InterruptedException e) {
            return null;
        }
    }

    public int size() {
        try {
            this.lock.lock();
            return this.blockedQueue.size() + super.size();
        } finally {
            this.lock.unlock();
        }
    }

    public <T> T[] toArray(T[] a) {
        try {
            this.lock.lock();
            return concatenate(super.toArray(a), this.blockedQueue.toArray(a));
        } finally {
            this.lock.unlock();
        }
    }

    public Object[] toArray() {
        try {
            this.lock.lock();
            return concatenate(super.toArray(), this.blockedQueue.toArray());
        } finally {
            this.lock.unlock();
        }
    }

    public int drainTo(Collection<? super E> c) {
        try {
            this.lock.lock();
            int numberOfItems = super.drainTo(c) + this.blockedQueue.size();
            while (!this.blockedQueue.isEmpty()) {
                c.add(this.blockedQueue.poll());
            }
            return numberOfItems;
        } finally {
            this.lock.unlock();
        }
    }

    public int drainTo(Collection<? super E> c, int maxElements) {
        try {
            this.lock.lock();
            int numberOfItems = super.drainTo(c, maxElements);
            while (!this.blockedQueue.isEmpty() && numberOfItems <= maxElements) {
                c.add(this.blockedQueue.poll());
                numberOfItems++;
            }
            return numberOfItems;
        } finally {
            this.lock.unlock();
        }
    }

    public boolean contains(Object o) {
        try {
            this.lock.lock();
            return super.contains(o) || this.blockedQueue.contains(o);
        } finally {
            this.lock.unlock();
        }
    }

    public void clear() {
        try {
            this.lock.lock();
            this.blockedQueue.clear();
            super.clear();
        } finally {
            this.lock.unlock();
        }
    }

    public boolean remove(Object o) {
        try {
            this.lock.lock();
            return super.remove(o) || this.blockedQueue.remove(o);
        } finally {
            this.lock.unlock();
        }
    }

    public boolean removeAll(Collection<?> collection) {
        try {
            this.lock.lock();
            return super.removeAll(collection) | this.blockedQueue.removeAll(collection);
        } finally {
            this.lock.unlock();
        }
    }

    /* access modifiers changed from: package-private */
    public E performOperation(int operation, Long time, TimeUnit unit) throws InterruptedException {
        switch (operation) {
            case 0:
                return (Dependency) super.take();
            case 1:
                return (Dependency) super.peek();
            case 2:
                return (Dependency) super.poll();
            case 3:
                return (Dependency) super.poll(time.longValue(), unit);
            default:
                return null;
        }
    }

    /* access modifiers changed from: package-private */
    public boolean offerBlockedResult(int operation, E result) {
        try {
            this.lock.lock();
            if (operation == 1) {
                super.remove(result);
            }
            return this.blockedQueue.offer(result);
        } finally {
            this.lock.unlock();
        }
    }

    /* access modifiers changed from: package-private */
    public E get(int operation, Long time, TimeUnit unit) throws InterruptedException {
        E result;
        while (true) {
            result = performOperation(operation, time, unit);
            if (result == null || canProcess(result)) {
                return result;
            }
            offerBlockedResult(operation, result);
        }
        return result;
    }

    /* access modifiers changed from: package-private */
    public boolean canProcess(E result) {
        return result.areDependenciesMet();
    }

    public void recycleBlockedQueue() {
        try {
            this.lock.lock();
            Iterator<E> iterator = this.blockedQueue.iterator();
            while (iterator.hasNext()) {
                E blockedItem = (Dependency) iterator.next();
                if (canProcess(blockedItem)) {
                    super.offer(blockedItem);
                    iterator.remove();
                }
            }
        } finally {
            this.lock.unlock();
        }
    }

    /* access modifiers changed from: package-private */
    public <T> T[] concatenate(T[] arr1, T[] arr2) {
        int arr1Len = arr1.length;
        int arr2Len = arr2.length;
        T[] C = (Object[]) ((Object[]) Array.newInstance(arr1.getClass().getComponentType(), arr1Len + arr2Len));
        System.arraycopy(arr1, 0, C, 0, arr1Len);
        System.arraycopy(arr2, 0, C, arr1Len, arr2Len);
        return C;
    }
}
