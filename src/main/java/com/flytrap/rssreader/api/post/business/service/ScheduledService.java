package com.flytrap.rssreader.api.post.business.service;

import com.flytrap.rssreader.api.alert.business.event.subscribe.SubscribeEvent;
import com.flytrap.rssreader.api.post.business.event.postInsert.PostBulkInsertQueue;
import com.flytrap.rssreader.api.post.infrastructure.entity.PostEntity;
import com.flytrap.rssreader.api.post.infrastructure.repository.PostEntityJpaRepository;
import com.flytrap.rssreader.api.subscribe.business.service.SubscribeService;
import com.flytrap.rssreader.api.subscribe.infrastructure.entity.SubscribeEntity;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScheduledService {

    private final PostCollectService postCollectService;
    private final SubscribeService subscribeService;
    private final ApplicationEventPublisher publisher;
    private final PostBulkInsertQueue bulkInsertQueue;
    private final PostEntityJpaRepository postEntityJpaRepository;

    //Post 수집 크롤링
    @Scheduled(fixedRate = 50000)
    public void collectPosts() {

        List<SubscribeEntity> subscribes = subscribeService.findSubscribeList();

        for (SubscribeEntity subscribe : subscribes) {
            CompletableFuture<Map<String, String>> future = postCollectService.processPostCollectionAsync(
                    subscribe);

            if (!future.join().isEmpty()) {
                SubscribeEvent event = new SubscribeEvent(subscribe.getId(),
                        Collections.unmodifiableMap(future.join()));
                publisher.publishEvent(event);
            }
        }
    }

    @Scheduled(fixedRate = 5000)
    public void queueBulkInsert() {
        if (bulkInsertQueue.isRemaining() && bulkInsertQueue.size() > 10) {
            List<PostEntity> posts = bulkInsertQueue.pollBatch(30);
            postEntityJpaRepository.saveAll(posts);
        }
    }
}
