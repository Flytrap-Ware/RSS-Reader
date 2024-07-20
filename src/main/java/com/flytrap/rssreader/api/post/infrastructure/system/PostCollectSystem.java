package com.flytrap.rssreader.api.post.infrastructure.system;

import com.flytrap.rssreader.api.admin.infrastructure.system.PostCollectionThreadPoolExecutor;
import com.flytrap.rssreader.api.alert.business.event.NewPostAlertEvent;
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
import com.flytrap.rssreader.global.utill.SimpleConcurrentPriorityQueue;
import com.flytrap.rssreader.global.utill.SimplePriorityQueue;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostCollectSystem {

    private final SimplePriorityQueue<RssSourceEntity> collectionQueue = new SimpleConcurrentPriorityQueue<>();
    private final RssSourceJpaRepository rssSourceRepository;
    private final PostJpaRepository postRepository;
    private final PostMyBatisRepository postMyBatisRepository;
    private final PostSystemJpaRepository postSystemJpaRepository;
    private final RssPostParser postParser;
    private final GlobalEventPublisher globalEventPublisher;
    private final PostCollectionThreadPoolExecutor postCollectionThreadPoolExecutor;

    public void enqueueHighPrioritySubscription(RssSourceEntity rssSourceEntity) {
        collectionQueue.add(rssSourceEntity, CollectPriority.HIGH);
    }

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

            CompletableFuture<CollectionResult> future = postCollectionThreadPoolExecutor
                .supplyAsync(() -> {
                        Optional<RssPostsData> rssPostsData = postParser
                            .parseRssDocuments(rssSource.getUrl());
                        return saveParsedPosts(rssPostsData, rssSource, start);
                    }
                );

            futures.add(future);
        }

        summarizeAndSaveCollectionResults(start, futures);
    }

    /**
     * 파싱된 RSS 데이터를 DB에 반영한다
     *
     * @param rssPostsData 파싱된 RSS 데이터
     * @param rssSource    파싱에 사용된 원본 RSS 문서 Entity
     * @param start        파싱 시작 시간
     * @return 파싱 및 저장 결과에 대한 통계 정보
     */
    private CollectionResult saveParsedPosts(
        Optional<RssPostsData> rssPostsData, RssSourceEntity rssSource, Instant start
    ) {
        return rssPostsData
            .map(data -> {
                int upsertCount = postMyBatisRepository.bulkUpsert(
                    generatePostEntitiesForUpsert(data, rssSource));

                rssSource.updateTitle(data.rssSourceTitle());
                rssSource.updateLastCollectedAt(start);
                rssSourceRepository.save(rssSource);

                if (upsertCount >= 0) {
                    return new CollectionResult(1, upsertCount, 0, 0);
                } else {
                    return new CollectionResult(1, 0, 0, upsertCount * -1);
                }
            })
            .orElse(new CollectionResult(0, 0, 1, 0));
    }

    /**
     * 파싱한 게시글 리스트 데이터를 PostEntity 리스트로 변환해서 반환한다
     *
     * @param postData    파싱한 게시글 리스트 데이터
     * @param rssResource 게시글을 파싱했던 RssResource Entity
     * @return 파싱한 게시글 리스트 데이터에서 변환된 PostEntity 리스트
     */
    private List<PostEntity> generatePostEntitiesForUpsert(RssPostsData postData,
        RssSourceEntity rssResource) {

        Optional<PostEntity> latestPost = postRepository
            .findFirstByRssSourceIdOrderByPubDateDesc(rssResource.getId());
        Instant standardPubDate = latestPost.isPresent() // 신규 게시글을 분류하기 위한 기준으로 사용됨
            ? latestPost.get().getPubDate()
            : Instant.now();

        List<PostEntity> collectedPosts = new ArrayList<>();
        List<PostEntity> newPosts = new ArrayList<>();

        for (RssPostsData.RssItemData itemData : postData.itemData()) {
            PostId postId = PostIdGenerator.generate(itemData.pubDate(), itemData.guid());
            PostEntity post = PostEntity.create(postId, itemData, rssResource.getId());

            if (standardPubDate.compareTo(post.getPubDate()) < 0) {
                // standardPubDate가 post.getPubDate()보다 이른 시간일 경우 해당 post는 신규 게시글로 취급한다
                // 신규 게시글을 따로 분류하는 이유는 신규 게시글 알림을 보내기 위해서
                newPosts.add(post);
            }
            collectedPosts.add(post);
        }

        if (!newPosts.isEmpty()) {
            globalEventPublisher.publish(new NewPostAlertEvent(rssResource, newPosts));
        }

        return collectedPosts;
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

}
