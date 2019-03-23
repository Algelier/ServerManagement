package net.algelier.servermanagement.server.waitingqueue;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Comparator;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.locks.ReentrantLock;

public class PriorityQueue extends PriorityBlockingQueue<QGroup> {

    private final ReentrantLock lock = new ReentrantLock();
    private Method method;

    public PriorityQueue(int initialCapacity, Comparator<? super QGroup> comparator) {
        super(initialCapacity, comparator);

        try {
            this.method = PriorityBlockingQueue.class.getDeclaredMethod("removeAt", int.class);

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public int drainPlayerTo(Collection<? super QGroup> collection, int max) {
        if (collection == null)
            throw new NullPointerException();

        if (max <= 0)
            return 0;

        final ReentrantLock lock = this.lock;
        lock.lock();

        try {
            int checked = max;
            int j = 0;

            for (int i = 0; i < this.size(); i++) {
                QGroup group = (QGroup) this.toArray()[j];

                if (checked - group.getSize() >= 0) {
                    collection.add(group);

                    try {
                        this.method.setAccessible(true);
                        this.method.invoke(this, j);

                    } catch (ReflectiveOperationException e) {
                        e.printStackTrace();
                    }

                    checked -= group.getSize();
                } else {
                    j++;
                }

                if (checked <= 0)
                    break;
            }

            return max - checked;
        } finally {
            lock.unlock();
        }
    }
}
