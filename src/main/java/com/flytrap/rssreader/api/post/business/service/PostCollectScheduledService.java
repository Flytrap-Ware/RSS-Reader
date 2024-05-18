package com.flytrap.rssreader.api.post.business.service;

import com.flytrap.rssreader.api.post.infrastructure.system.PostCollectSystem;
import com.flytrap.rssreader.api.post.infrastructure.system.PostCollectionEnableLoader;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostCollectScheduledService {

    @Value("${collector.subscribe-queue.select-batch-size}")
    private int selectBatchSize;

    private final PostCollectSystem postCollectSystem;
    private final PostCollectionEnableLoader postCollectionEnableLoader;

    @Scheduled(initialDelay = 0, fixedDelay = 50_000)
    public void schedulePostCollection() {

        if (postCollectionEnableLoader.isDisabled()) {
            return;
        }

        postCollectSystem.loadAndEnqueueRssResources(selectBatchSize);
    }

    @Scheduled(fixedDelay = 1000)
    public void schedulePostPersistence() {

        if (postCollectionEnableLoader.isDisabled()) {
            return;
        }

        postCollectSystem.dequeueAndSaveRssResource();
    }

}
