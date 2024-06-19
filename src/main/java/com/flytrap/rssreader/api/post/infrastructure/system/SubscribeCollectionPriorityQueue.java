package com.flytrap.rssreader.api.post.infrastructure.system;

import com.flytrap.rssreader.api.subscribe.infrastructure.entity.RssSourceEntity;
import java.util.List;

public interface SubscribeCollectionPriorityQueue {
    void add(RssSourceEntity subscribe, CollectPriority priority);
    void addAll(List<RssSourceEntity> subscribes, CollectPriority priority);
    RssSourceEntity poll();
    boolean isQueueEmpty();
    int size();
}
