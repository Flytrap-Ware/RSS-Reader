package com.flytrap.rssreader.global.utill;

import com.flytrap.rssreader.api.post.infrastructure.system.CollectPriority;
import java.util.List;

public interface SimplePriorityQueue<T> {
    void add(T element, CollectPriority priority);
    void addAll(List<T> elements, CollectPriority priority);
    T poll();
    boolean isQueueEmpty();
    int size();
}
