package com.flytrap.rssreader.api.post.business.service;

import com.flytrap.rssreader.api.post.business.event.postInsert.PostBulkInsertPublisher;
import com.flytrap.rssreader.api.post.business.event.postInsert.PostBulkInsertQueue;
import com.flytrap.rssreader.api.post.infrastructure.entity.PostEntity;
import com.flytrap.rssreader.api.post.infrastructure.repository.PostEntityJpaRepository;
import com.flytrap.rssreader.api.subscribe.domain.Subscribe;
import com.flytrap.rssreader.api.parser.RssPostParser;
import com.flytrap.rssreader.api.parser.dto.RssPostsData;
import com.flytrap.rssreader.api.subscribe.infrastructure.entity.SubscribeEntity;
import com.flytrap.rssreader.api.subscribe.infrastructure.repository.SubscribeEntityJpaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@AllArgsConstructor
@Service
public class PostCollectService {

    //  private static final int TEN_MINUTE = 600_000;
    private final RssPostParser postParser;
    private final SubscribeEntityJpaRepository subscribeEntityJpaRepository;
    private final PostEntityJpaRepository postEntityJpaRepository;
    private final PostBulkInsertQueue bulkInsertQueue;
    private final PostBulkInsertPublisher publisher;

    /**
     * 구독한 블로그의 RSS에서 게시글들을 읽어서 DB에 저장한다.<br> 비동기 처리되어 있으며 블로그 하나당 하나의 스레드에서 동작한다.
     *
     * @param subscribe 구독한 블로그
     * @return
     */
    public CompletableFuture<Map<String, String>> processPostCollectionAsync(
            SubscribeEntity subscribe) {
        return CompletableFuture.supplyAsync(
                () -> postParser.parseRssDocuments(subscribe.getUrl())
                        .map(resource -> {
                            updateSubscribeTitle(resource, subscribe);
                            return savePostsForBulkInsert(resource, subscribe);
                        }).orElse(new HashMap<>()));
    }

    /**
     * 최초로 구독한 블로그의 RSS에서 게시글들을 파싱한 후 수집(DB에 저장)합니다.
     * 최초로 구독한 블로그이기에 기존에 저장된 블로그와 비교하여 게시글 변경 시 업데이트 하는 로직이 존재하지 않습니다.
     * 따라서 기존에 존재하는 블로그의 게시글 수집에는 사용하지 마세요.
     *
     * @param subscribe 구독한 블로그
     */
    public void collectPostsFromNewSubscribe(Subscribe subscribe) {

        postParser.parseRssDocuments(subscribe.getUrl()).ifPresent(
                resource -> {
                    saveAllPostsForNewSubscribe(resource, SubscribeEntity.from(subscribe));
                }
        );
    }

    private void updateSubscribeTitle(RssPostsData postData,
                                      SubscribeEntity subscribe) {
        subscribe.updateTitle(postData.subscribeTitle());
        subscribeEntityJpaRepository.save(subscribe);
    }

    private Map<String, String> savePostsForBulkInsert(RssPostsData postData,
                                                       SubscribeEntity subscribe) {
        List<PostEntity> posts = postEntityJpaRepository.findAllBySubscribeOrderByPubDateDesc(
                subscribe);

        Map<String, PostEntity> postMap = convertListToHashSet(posts);
        Map<String, String> postUrlMap = new HashMap<>();

        for (RssPostsData.RssItemData itemData : postData.itemData()) {
            PostEntity post;

            if (postMap.containsKey(itemData.guid())) {
                post = postMap.get(itemData.guid());
                post.updateBy(itemData);
            } else {
                post = PostEntity.from(itemData, subscribe);
                postUrlMap.put(post.getGuid(), post.getTitle());
            }
            publisher.publish(post);
        }
        return postUrlMap;
    }

    /**
     * 최초로 구독한 블로그 RSS에서 파싱된 게시글들을 DB에 INSERT합니다.
     * 최초로 구독한 블로그이기에 기존에 저장된 블로그와 비교하여 게시글 변경 시 업데이트 하는 로직이 존재하지 않습니다.
     * 따라서 기존에 존재하는 블로그의 게시글 저장에는 사용하지 마세요.
     *
     * @param postData 최초로 구독한 블로그 RSS에서 파싱된 게시글 데이터 리스트.
     * @param subscribe 최초로 구독한 블로그. PostEntity를 생성할 때 subscribeId를 주입하기 위해 사용됩니다.
     */
    private void saveAllPostsForNewSubscribe(RssPostsData postData,
                                             SubscribeEntity subscribe) {

        List<PostEntity> postsToSave = new ArrayList<>();

        for (RssPostsData.RssItemData itemData : postData.itemData()) {
            postsToSave.add(PostEntity.from(itemData, subscribe));
        }

        postEntityJpaRepository.saveAll(postsToSave);
    }

    private static Map<String, PostEntity> convertListToHashSet
            (List<PostEntity> postEntities) {
        Map<String, PostEntity> map = new HashMap<>();

        for (PostEntity postEntity : postEntities) {
            map.put(postEntity.getGuid(), postEntity);
        }

        return map;
    }
}
