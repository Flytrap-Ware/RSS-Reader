package com.flytrap.rssreader.api.post.infrastructure.system;

import com.flytrap.rssreader.api.alert.business.event.NewPostAlertEvent;
import com.flytrap.rssreader.api.parser.RssPostParser;
import com.flytrap.rssreader.api.parser.dto.RssPostsData;
import com.flytrap.rssreader.api.post.infrastructure.entity.PostEntity;
import com.flytrap.rssreader.api.post.infrastructure.repository.PostEntityJpaRepository;
import com.flytrap.rssreader.api.subscribe.infrastructure.entity.SubscribeEntity;
import com.flytrap.rssreader.api.subscribe.infrastructure.repository.SubscribeEntityJpaRepository;
import com.flytrap.rssreader.global.event.GlobalEventPublisher;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostCollectSystem {

    private final SubscribeCollectionPriorityQueue collectionQueue;
    private final SubscribeEntityJpaRepository subscribeRepository;
    private final PostEntityJpaRepository postRepository;
    private final RssPostParser postParser;
    private final GlobalEventPublisher globalEventPublisher;

    public void collectPosts(int selectBatchSize) {
        var now = Instant.now();
        var pageable = PageRequest.of(
            0, selectBatchSize,
            Sort.by(Sort.Direction.ASC, "lastCollectedAt"));
        var subscribes =
            subscribeRepository.findAll(pageable).getContent();

        collectionQueue.addAll(subscribes, CollectPriority.LOW);

        while (!collectionQueue.isQueueEmpty()) {
            SubscribeEntity subscribe = collectionQueue.poll();
            postParser.parseRssDocuments(subscribe.getUrl())
                .ifPresent(rssPostsData -> {
                    postRepository.saveAll(
                        generateCollectedPostsForUpsert(rssPostsData, subscribe));
                    subscribe.updateTitle(rssPostsData.subscribeTitle());
                    subscribe.updateLastCollectedAt(now);
                    subscribeRepository.save(subscribe);
                });
        }
    }

    public void enqueueHighPrioritySubscription(SubscribeEntity subscribeEntity) {
        collectionQueue.add(subscribeEntity, CollectPriority.HIGH);
    }

    private List<PostEntity> generateCollectedPostsForUpsert(RssPostsData postData,
        SubscribeEntity subscribe) {
        List<PostEntity> existingPosts = postRepository.findAllBySubscriptionId(subscribe.getId());

        Map<String, PostEntity> existingPostsMap = convertListToHashSet(existingPosts);
        List<PostEntity> collectedPosts = new ArrayList<>();
        List<PostEntity> newPosts = new ArrayList<>();

        for (RssPostsData.RssItemData itemData : postData.itemData()) {
            PostEntity post;

            if (existingPostsMap.containsKey(itemData.guid())) {
                post = existingPostsMap.get(itemData.guid());
                post.updateBy(itemData);
            } else {
                post = PostEntity.from(itemData, subscribe.getId());
                newPosts.add(post);
            }
            collectedPosts.add(post);
        }

        if (!newPosts.isEmpty()) {
            globalEventPublisher.publish(new NewPostAlertEvent(subscribe, newPosts));
        }

        return collectedPosts;
    }

    private static Map<String, PostEntity> convertListToHashSet(
        List<PostEntity> postEntities
    ) {
        Map<String, PostEntity> map = new HashMap<>();

        for (PostEntity postEntity : postEntities) {
            map.put(postEntity.getGuid(), postEntity);
        }

        return Collections.unmodifiableMap(map);
    }

}
