package com.flytrap.rssreader.api.post.business.facade;

import com.flytrap.rssreader.api.folder.domain.Folder;
import com.flytrap.rssreader.api.folder.domain.FolderSubscribe;
import com.flytrap.rssreader.api.post.business.service.PostOpenService;
import com.flytrap.rssreader.api.post.business.service.PostReadService;
import com.flytrap.rssreader.api.post.infrastructure.implementation.PostQuery;
import com.flytrap.rssreader.api.post.infrastructure.output.EmptyOpenPostCountOutput;
import com.flytrap.rssreader.api.post.infrastructure.output.EmptySubscribePostCountOutput;
import com.flytrap.rssreader.api.post.infrastructure.output.OpenPostCountOutput;
import com.flytrap.rssreader.api.post.infrastructure.output.PostSubscribeCountOutput;
import com.flytrap.rssreader.api.subscribe.domain.Subscribe;

import com.flytrap.rssreader.api.subscribe.domain.SubscriptionId;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OpenCheckFacade {

    private final PostReadService postReadService;
    private final PostOpenService postOpenService;
    private final PostQuery postQuery;

    public FolderSubscribe addUnreadCountInFolderSubscribe(long memberId, Subscribe subscribe,
                                                           FolderSubscribe folderSubscribe) {
        SubscriptionId subscribeId = new SubscriptionId(subscribe.getId());
        Map<SubscriptionId, PostSubscribeCountOutput> countsPost = postQuery
            .countPostsInSubscription(subscribeId);
        Map<SubscriptionId, OpenPostCountOutput> countsOpen = postOpenService
            .countReadInSubscription(memberId, subscribeId);

        folderSubscribe.addUnreadCount(
            countsPost.getOrDefault(subscribeId, new EmptySubscribePostCountOutput()).getPostCount(),
            countsOpen.getOrDefault(subscribeId, new EmptyOpenPostCountOutput()).getPostCount());

        return folderSubscribe;
    }

    public List<? extends Folder> addUnreadCountInSubscribes(long id,
                                                             List<? extends Folder> foldersWithSubscribe) {
        List<SubscriptionId> subscribeIds = foldersWithSubscribe.stream()
            .map(Folder::getSubscribeIds)
            .flatMap(List::stream)
            .map(SubscriptionId::new)
            .toList();

        Map<SubscriptionId, PostSubscribeCountOutput> countsPost = postQuery.countPostsInSubscriptions(subscribeIds);
        Map<SubscriptionId, OpenPostCountOutput> countsOpen = postOpenService.countReadInSubscriptions(id, subscribeIds);

        for (Folder folder : foldersWithSubscribe) {
            folder.addUnreadCountsBySubscribes(countsPost, countsOpen);
        }

        return foldersWithSubscribe;
    }
}
