package com.flytrap.rssreader.api.post.business.service;

import com.flytrap.rssreader.api.folder.domain.AccessibleFolder;
import com.flytrap.rssreader.api.folder.domain.FolderId;
import com.flytrap.rssreader.api.folder.infrastructure.implementatioin.FolderQuery;
import com.flytrap.rssreader.api.member.domain.AccountId;
import com.flytrap.rssreader.api.post.domain.Post;
import com.flytrap.rssreader.api.post.domain.PostFilter;
import com.flytrap.rssreader.api.post.infrastructure.implementation.PostQuery;
import com.flytrap.rssreader.api.subscribe.domain.SubscriptionId;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@AllArgsConstructor
@Service
public class PostListReadService {

    private final PostQuery postQuery;
    private final FolderQuery folderQuery;

    public List<Post> getPostsByAccount(AccountId accountId, PostFilter postFilter,
        Pageable pageable) {

        return postQuery.readAllByAccount(accountId, postFilter, pageable);
    }

    public List<Post> getPostsByFolder(AccountId accountId, FolderId folderId,
        PostFilter postFilter, Pageable pageable) {

        AccessibleFolder accessibleFolder = folderQuery.readAccessible(folderId, accountId);

        return postQuery.readAllByFolder(accountId, new FolderId(accessibleFolder.getId()),
            postFilter, pageable);
    }

    public List<Post> getPostsBySubscription(AccountId accountId, SubscriptionId subscriptionId,
        PostFilter postFilter, Pageable pageable) {

        return postQuery.readAllBySubscription(accountId, subscriptionId, postFilter, pageable);
    }

    public List<Post> getBookmarkedPosts(AccountId accountId, PostFilter postFilter,
        Pageable pageable) {

        return postQuery.readAllBookmarked(accountId, postFilter, pageable);
    }
}
