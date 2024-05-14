package com.flytrap.rssreader.api.post.infrastructure.system;

import com.flytrap.rssreader.api.alert.business.event.NewPostAlertEvent;
import com.flytrap.rssreader.api.parser.RssPostParser;
import com.flytrap.rssreader.api.parser.dto.RssPostsData;
import com.flytrap.rssreader.api.post.infrastructure.entity.PostEntity;
import com.flytrap.rssreader.api.post.infrastructure.repository.PostJpaRepository;
import com.flytrap.rssreader.api.subscribe.infrastructure.entity.RssSourceEntity;
import com.flytrap.rssreader.api.subscribe.infrastructure.repository.RssResourceJpaRepository;
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
    private final RssResourceJpaRepository rssResourceRepository;
    private final PostJpaRepository postRepository;
    private final RssPostParser postParser;
    private final GlobalEventPublisher globalEventPublisher;

    public void collectPosts(int selectBatchSize) {
        var now = Instant.now();
        var pageable = PageRequest.of(
            0, selectBatchSize,
            Sort.by(Sort.Direction.ASC, "lastCollectedAt"));
        var rssResources =
            rssResourceRepository.findAll(pageable).getContent();

        collectionQueue.addAll(rssResources, CollectPriority.LOW);

        while (!collectionQueue.isQueueEmpty()) {
            RssSourceEntity rssResource = collectionQueue.poll();
            postParser.parseRssDocuments(rssResource.getUrl())
                .ifPresent(rssPostsData -> {
                    postRepository.saveAll(
                        generateCollectedPostsForUpsert(rssPostsData, rssResource));
                    rssResource.updateTitle(rssPostsData.rssSourceTitle());
                    rssResource.updateLastCollectedAt(now);
                    rssResourceRepository.save(rssResource);
                });
        }
    }

    public void enqueueHighPrioritySubscription(RssSourceEntity rssSourceEntity) {
        collectionQueue.add(rssSourceEntity, CollectPriority.HIGH);
    }

    private List<PostEntity> generateCollectedPostsForUpsert(RssPostsData postData,
        RssSourceEntity rssResource) {
        List<PostEntity> existingPosts = postRepository
            .findAllByRssSourceId(rssResource.getId());

        Map<String, PostEntity> existingPostsMap = convertListToHashSet(existingPosts);
        List<PostEntity> collectedPosts = new ArrayList<>();
        List<PostEntity> newPosts = new ArrayList<>();

        for (RssPostsData.RssItemData itemData : postData.itemData()) {
            PostEntity post;

            if (existingPostsMap.containsKey(itemData.guid())) {
                post = existingPostsMap.get(itemData.guid());
                post.updateBy(itemData);
            } else {
                post = PostEntity.from(itemData, rssResource.getId());
                newPosts.add(post);
            }
            collectedPosts.add(post);
        }

        if (!newPosts.isEmpty()) {
            globalEventPublisher.publish(new NewPostAlertEvent(rssResource, newPosts));
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
