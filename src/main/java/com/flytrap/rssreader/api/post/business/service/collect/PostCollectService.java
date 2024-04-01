package com.flytrap.rssreader.api.post.business.service.collect;

import com.flytrap.rssreader.api.parser.RssPostParser;
import com.flytrap.rssreader.api.parser.dto.RssPostsData;
import com.flytrap.rssreader.api.post.infrastructure.entity.PostEntity;
import com.flytrap.rssreader.api.post.infrastructure.repository.PostEntityJpaRepository;
import com.flytrap.rssreader.api.subscribe.domain.Subscribe;
import com.flytrap.rssreader.api.subscribe.infrastructure.entity.SubscribeEntity;
import com.flytrap.rssreader.api.subscribe.infrastructure.repository.SubscribeEntityJpaRepository;
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
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostCollectService {

    private final SubscribeCollectionPriorityQueue collectionQueue;
    private final SubscribeEntityJpaRepository subscribeRepository;
    private final PostEntityJpaRepository postRepository;
    private final RssPostParser postParser;
    // TODO: SubscribeEvent 알림 이벤트 개선하기
    // private final SubscribeEventListener subscribeEventListener;

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

    public void addNewSubscribeForCollect(Subscribe subscribe) {
        if (subscribe.isNewSubscribe()) {
            collectionQueue.add(SubscribeEntity.from(subscribe), CollectPriority.HIGH);
        }
    }

    private List<PostEntity> generateCollectedPostsForUpsert(RssPostsData postData, SubscribeEntity subscribe) {
        var pageable = PageRequest.of(
            0, postData.getItemSize(),
            Sort.by(Direction.DESC, "pubDate"));
        List<PostEntity> existingPosts = postRepository.findAllBySubscribe(
            subscribe, pageable);

        Map<String, PostEntity> existingPostsMap = convertListToHashSet(existingPosts);
        List<PostEntity> collectedPosts = new ArrayList<>();

        for (RssPostsData.RssItemData itemData : postData.itemData()) {
            PostEntity post;

            if (existingPostsMap.containsKey(itemData.guid())) {
                post = existingPostsMap.get(itemData.guid());
                post.updateBy(itemData);
            } else {
                post = PostEntity.from(itemData, subscribe);
            }
            collectedPosts.add(post);
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
