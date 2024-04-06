package com.flytrap.rssreader.api.post.business.service.collect;

import com.flytrap.rssreader.api.subscribe.infrastructure.entity.SubscribeEntity;
import java.util.List;

public interface SubscribeCollectionPriorityQueue {
    void add(SubscribeEntity subscribe, CollectPriority priority);
    void addAll(List<SubscribeEntity> subscribes, CollectPriority priority);
    SubscribeEntity poll();
    boolean isQueueEmpty();
}
