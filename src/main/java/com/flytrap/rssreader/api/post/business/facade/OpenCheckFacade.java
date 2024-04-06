package com.flytrap.rssreader.api.post.business.facade;

import com.flytrap.rssreader.api.folder.domain.Folder;
import com.flytrap.rssreader.api.folder.domain.FolderSubscribe;
import com.flytrap.rssreader.api.post.business.service.PostOpenService;
import com.flytrap.rssreader.api.post.business.service.PostReadService;
import com.flytrap.rssreader.api.post.infrastructure.output.EmptyOpenPostCountOutput;
import com.flytrap.rssreader.api.post.infrastructure.output.EmptySubscribePostCountOutput;
import com.flytrap.rssreader.api.post.infrastructure.output.OpenPostCountOutput;
import com.flytrap.rssreader.api.post.infrastructure.output.PostSubscribeCountOutput;
import com.flytrap.rssreader.api.subscribe.domain.Subscribe;

import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OpenCheckFacade {

    private final PostReadService postReadService;
    private final PostOpenService postOpenService;

    public FolderSubscribe addUnreadCountInFolderSubscribe(long memberId, Subscribe subscribe,
                                                           FolderSubscribe folderSubscribe) {
        Long subscribeId = subscribe.getId();
        Map<Long, PostSubscribeCountOutput> countsPost = postReadService.countPosts(List.of(subscribeId));
        Map<Long, OpenPostCountOutput> countsOpen = postOpenService.countOpens(memberId,
            List.of(subscribeId));

        folderSubscribe.addUnreadCount(
            countsPost.getOrDefault(subscribeId, new EmptySubscribePostCountOutput()).getPostCount(),
            countsOpen.getOrDefault(subscribeId, new EmptyOpenPostCountOutput()).getPostCount());

        return folderSubscribe;
    }

    public List<? extends Folder> addUnreadCountInSubscribes(long id,
                                                             List<? extends Folder> foldersWithSubscribe) {
        List<Long> subscribes = foldersWithSubscribe.stream().map(Folder::getSubscribeIds)
            .flatMap(List::stream).toList();

        Map<Long, PostSubscribeCountOutput> countsPost = postReadService.countPosts(subscribes);
        Map<Long, OpenPostCountOutput> countsOpen = postOpenService.countOpens(id, subscribes);

        for (Folder folder : foldersWithSubscribe) {
            folder.addUnreadCountsBySubscribes(countsPost, countsOpen);
        }

        return foldersWithSubscribe;
    }
}
