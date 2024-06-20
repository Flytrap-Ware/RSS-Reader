package com.flytrap.rssreader.global.utill;

import com.flytrap.rssreader.api.post.infrastructure.system.CollectPriority;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;

public class SimpleConcurrentPriorityQueue<T> implements SimplePriorityQueue<T> {

    private final Deque<T> lowQueue = new ConcurrentLinkedDeque<>();
    private final Deque<T> highQueue = new ConcurrentLinkedDeque<>();

    @Override
    public void add(T element, CollectPriority priority) {
        if (priority == CollectPriority.HIGH) {
            highQueue.addLast(element);
        } else {
            lowQueue.addLast(element);
        }
    }

    @Override
    public void addAll(List<T> elements, CollectPriority priority) {
        if (priority == CollectPriority.HIGH) {
            highQueue.addAll(elements);
        } else {
            lowQueue.addAll(elements);
        }
    }

    @Override
    public synchronized T poll() {
        if (highQueue.isEmpty()) {
            return lowQueue.poll();
        } else {
            return highQueue.poll();
        }
    }

    @Override
    public synchronized boolean isQueueEmpty() {
        return highQueue.isEmpty() && lowQueue.isEmpty();
    }

    @Override
    public synchronized int size() {
        return lowQueue.size() + highQueue.size();
    }
}
