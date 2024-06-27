package com.flytrap.rssreader.api.post.infrastructure.system;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flytrap.rssreader.api.admin.infrastructure.system.PostCollectionThreadPoolExecutor;
import com.flytrap.rssreader.api.alert.business.event.NewPostAlertEvent;
import com.flytrap.rssreader.api.alert.business.event.NewPostAlertEvent1;
import com.flytrap.rssreader.api.alert.infrastructure.entity.OutBoxEventEntity;
import com.flytrap.rssreader.api.alert.infrastructure.repository.OutBoxEventJpaRepository;
import com.flytrap.rssreader.api.parser.RssPostParser;
import com.flytrap.rssreader.api.parser.dto.RssPostsData;
import com.flytrap.rssreader.api.post.domain.PostId;
import com.flytrap.rssreader.api.post.domain.PostIdGenerator;
import com.flytrap.rssreader.api.post.infrastructure.entity.PostEntity;
import com.flytrap.rssreader.api.post.infrastructure.entity.PostSystemEntity;
import com.flytrap.rssreader.api.post.infrastructure.repository.PostJpaRepository;
import com.flytrap.rssreader.api.post.infrastructure.repository.PostMyBatisRepository;
import com.flytrap.rssreader.api.post.infrastructure.repository.PostSystemJpaRepository;
import com.flytrap.rssreader.api.subscribe.infrastructure.entity.RssSourceEntity;
import com.flytrap.rssreader.api.subscribe.infrastructure.repository.RssSourceJpaRepository;
import com.flytrap.rssreader.global.event.GlobalEventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostCollectSystem {

    private final SimplePriorityQueue<RssSourceEntity> collectionQueue = new SimpleConcurrentPriorityQueue<>();
    private final RssSourceJpaRepository rssSourceRepository;
    private final PostJpaRepository postRepository;
    private final OutBoxEventJpaRepository outBoxEventJpaRepository;
    private final PostMyBatisRepository postMyBatisRepository;
    private final PostSystemJpaRepository postSystemJpaRepository;
    private final RssPostParser postParser;
    private final GlobalEventPublisher globalEventPublisher;
    private final PostCollectionThreadPoolExecutor postCollectionThreadPoolExecutor;
    private final ObjectMapper objectMapper;

    public void loadAndEnqueueRssResources(int selectBatchSize) {
        var pageable = PageRequest.of(
                0, selectBatchSize,
                Sort.by(Sort.Direction.ASC, "lastCollectedAt"));
        var rssResources =
                rssSourceRepository.findAll(pageable).getContent();

        collectionQueue.addAll(rssResources, CollectPriority.LOW);
    }

    public void dequeueAndSaveRssResource() {
        if (collectionQueue.isQueueEmpty()) {
            return;
        }

        Instant start = Instant.now();
        List<CompletableFuture<CollectionResult>> futures = new ArrayList<>();

        while (!collectionQueue.isQueueEmpty()) {

            RssSourceEntity rssSource = collectionQueue.poll();

            CompletableFuture<CollectionResult> future = postCollectionThreadPoolExecutor.supplyAsync(
                    () -> postParser.parseRssDocuments(rssSource.getUrl())
                            .map(rssPostsData -> {
                                int upsertCount = postMyBatisRepository.bulkUpsert(
                                        generateCollectedPostsForUpsert(rssPostsData, rssSource));

                                rssSource.updateTitle(rssPostsData.rssSourceTitle());
                                rssSource.updateLastCollectedAt(start);
                                rssSourceRepository.save(rssSource);

                                if (upsertCount >= 0) {
                                    return new CollectionResult(1, upsertCount, 0, 0);
                                } else {
                                    return new CollectionResult(1, 0, 0, upsertCount * -1);
                                }
                            })
                            .orElse(new CollectionResult(0, 0, 1, 0)));

            futures.add(future);
        }

        summarizeAndSaveCollectionResults(start, futures);
    }

    public void enqueueHighPrioritySubscription(RssSourceEntity rssSourceEntity) {
        collectionQueue.add(rssSourceEntity, CollectPriority.HIGH);
    }

    /**
     * 멀티 스레드로 동시 작업한 게시글 수집 결과를 집계하여 DB에 저장한다.
     *
     * @param start   게시글 수집 시작 시간
     * @param futures 게시글 수집 작업의 CompletableFuture 목록
     */
    private void summarizeAndSaveCollectionResults(
            Instant start, List<CompletableFuture<CollectionResult>> futures
    ) {
        CompletableFuture<Void> allOf = CompletableFuture.allOf(
                futures.toArray(new CompletableFuture[0]));
        allOf.thenApply(voidResult -> futures.stream()
                        .map(CompletableFuture::join)
                        .reduce(new CollectionResult(0, 0, 0, 0), (result1, result2) ->
                                new CollectionResult(
                                        result1.getRssCount() + result2.getRssCount(),
                                        result1.getPostCount() + result2.getPostCount(),
                                        result1.getParsingFailureCount() + result2.getParsingFailureCount(),
                                        result1.getInsertFailureCount() + result2.getInsertFailureCount()
                                )
                        ))
                .thenAccept(collectionResult -> {
                    Instant end = Instant.now();

                    postSystemJpaRepository.save(
                            PostSystemEntity.builder()
                                    .startTime(start)
                                    .endTime(end)
                                    .elapsedTimeMillis(Duration.between(start, end).toMillis())
                                    .rssCount(collectionResult.getRssCount())
                                    .postCount(collectionResult.getPostCount())
                                    .threadCount(postCollectionThreadPoolExecutor.getCorePoolSize())
                                    .parsingFailureCount(collectionResult.getParsingFailureCount())
                                    .insertFailureCount(collectionResult.getInsertFailureCount())
                                    .build()
                    );
                }).join();
    }

    private List<PostEntity> generateCollectedPostsForUpsert(RssPostsData postData,
                                                             RssSourceEntity rssResource) {

        Optional<PostEntity> latestPost = postRepository
                .findFirstByRssSourceIdOrderByPubDateDesc(rssResource.getId());
        Instant standardPubDate = latestPost.isPresent()
                ? latestPost.get().getPubDate()
                : Instant.now();

        List<PostEntity> collectedPosts = new ArrayList<>();
        List<PostEntity> newPosts = new ArrayList<>();

        // 1. 도메인 로직
        for (RssPostsData.RssItemData itemData : postData.itemData()) {
            PostId postId = null;
            try {
                postId = PostIdGenerator.generateString(itemData.pubDate(), itemData.guid());
            } catch (NoSuchAlgorithmException e) {
                return collectedPosts;
            }
            PostEntity post = PostEntity.create(postId, itemData, rssResource.getId());

            if (standardPubDate.compareTo(post.getPubDate()) < 0) {
                // standardPubDate가 post.getPubDate()보다 이른 시간일 경우 해당 post는 신규 게시글로 취급한다
                newPosts.add(post);
            }
            collectedPosts.add(post);
        }

        if (!newPosts.isEmpty()) {
            // 2. outbox table 에 이벤트 기록
            NewPostAlertEvent event = new NewPostAlertEvent(UUID.randomUUID(), rssResource, newPosts);
            // Event를 payload로 저장한다
            String payload = null;
            try {
                payload = objectMapper.writeValueAsString(event);
            } catch (JsonProcessingException e) {
                //TODO: 예외 수정
                throw new RuntimeException(e);
            }
            OutBoxEventEntity outBoxEventEntity = OutBoxEventEntity.from(event, payload);
            outBoxEventJpaRepository.save(outBoxEventEntity);
            // 3. 이벤트 발행 (Alert Event)
            globalEventPublisher.publish(new NewPostAlertEvent(UUID.randomUUID(), rssResource, newPosts));
        }

        return collectedPosts;
    }

}
