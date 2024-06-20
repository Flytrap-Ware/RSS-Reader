package com.flytrap.rssreader.global.utill;

import com.flytrap.rssreader.api.post.infrastructure.system.CollectPriority;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;

public class SimpleConcurrentPriorityQueue<T> implements SimplePriorityQueue<T> {

    private final Deque<T> queue = new ConcurrentLinkedDeque<>();

    @Override
    public void add(T element, CollectPriority priority) {
        if (priority == CollectPriority.HIGH) {
            queue.addFirst(element);
        } else {
            queue.addLast(element);
        }
    }

    @Override
    public void addAll(List<T> elements, CollectPriority priority) {
        if (priority == CollectPriority.HIGH) {
            for (int i = elements.size() - 1; i >= 0; i--) {
                queue.addFirst(elements.get(i));
            }
        } else {
            queue.addAll(elements);
        }
    }

    @Override
    public T poll() {
        return queue.poll();
    }

    @Override
    public boolean isQueueEmpty() {
        return queue.isEmpty();
    }

    @Override
    public int size() {
        return queue.size();
    }
}
