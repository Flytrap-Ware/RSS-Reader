package com.flytrap.rssreader.api.post.business.service.collect;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostCollectScheduledService {

    @Value("${collector.subscribe-queue.select-batch-size}")
    private int selectBatchSize;

    private final PostCollectService postCollectService;

//    @Scheduled(fixedDelay = 1000)
    public void collectPostsScheduled() {
        postCollectService.collectPosts(selectBatchSize);
    }

}
