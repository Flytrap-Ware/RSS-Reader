package com.flytrap.rssreader.api.post.infrastructure.system;

import com.flytrap.rssreader.api.subscribe.infrastructure.entity.RssSourceEntity;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;
import org.springframework.stereotype.Component;

@Component
public class SubscribeCollectionPriorityBlockingQueue implements SubscribeCollectionPriorityQueue {

    private final Deque<RssSourceEntity> queue = new ConcurrentLinkedDeque<>();

    @Override
    public void add(RssSourceEntity subscribe, CollectPriority priority) {
        if (priority == CollectPriority.HIGH) {
            queue.addFirst(subscribe);
        } else {
            queue.addLast(subscribe);
        }
    }

    @Override
    public void addAll(List<RssSourceEntity> subscribes, CollectPriority priority) {
        if (priority == CollectPriority.HIGH) {
            for (int i = subscribes.size() - 1; i >= 0; i--) {
                queue.addFirst(subscribes.get(i));
            }
        } else {
            queue.addAll(subscribes);
        }
    }

    @Override
    public RssSourceEntity poll() {
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
