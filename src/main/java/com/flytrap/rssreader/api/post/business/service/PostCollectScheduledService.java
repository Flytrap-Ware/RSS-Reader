package com.flytrap.rssreader.api.post.business.service;

import com.flytrap.rssreader.api.post.infrastructure.system.PostCollectSystem;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostCollectScheduledService {

    @Value("${collector.subscribe-queue.select-batch-size}")
    private int selectBatchSize;

    private final PostCollectSystem postCollectSystem;

//    @Scheduled(fixedDelay = 1000)
    public void collectPostsScheduled() {
        postCollectSystem.collectPosts(selectBatchSize);
    }

}
