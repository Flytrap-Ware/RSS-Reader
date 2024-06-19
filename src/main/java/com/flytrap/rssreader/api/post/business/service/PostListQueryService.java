package com.flytrap.rssreader.api.post.business.service;

import com.flytrap.rssreader.api.account.domain.AccountId;
import com.flytrap.rssreader.api.folder.domain.Folder;
import com.flytrap.rssreader.api.folder.domain.FolderId;
import com.flytrap.rssreader.api.folder.infrastructure.implementatioin.FolderValidator;
import com.flytrap.rssreader.api.post.domain.Post;
import com.flytrap.rssreader.api.post.domain.PostFilter;
import com.flytrap.rssreader.api.post.infrastructure.implementation.PostQuery;
import com.flytrap.rssreader.api.subscribe.domain.Subscription;
import com.flytrap.rssreader.api.subscribe.domain.SubscriptionId;
import com.flytrap.rssreader.api.subscribe.infrastructure.implement.SubscriptionQuery;
import com.flytrap.rssreader.global.exception.domain.ForbiddenAccessFolderException;
import com.flytrap.rssreader.global.exception.domain.NoSuchDomainException;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@AllArgsConstructor
@Service
public class PostListQueryService {

    private final FolderValidator folderValidator;
    private final SubscriptionQuery subscriptionQuery;
    private final PostQuery postQuery;

    public List<Post> getPostsByAccount(AccountId accountId, PostFilter postFilter,
        Pageable pageable) {

        return postQuery.readAllByAccount(accountId, postFilter, pageable);
    }

    public List<Post> getPostsByFolder(AccountId accountId, FolderId folderId,
        PostFilter postFilter, Pageable pageable) {

        if (!folderValidator.isAccessibleFolder(folderId, accountId))
            throw new ForbiddenAccessFolderException(Folder.class);

        return postQuery.readAllByFolder(accountId, folderId, postFilter, pageable);
    }

    public List<Post> getPostsBySubscription(AccountId accountId, SubscriptionId subscriptionId,
        PostFilter postFilter, Pageable pageable) {
        Subscription subscription = subscriptionQuery.read(subscriptionId)
            .orElseThrow(() -> new NoSuchDomainException(Subscription.class));

        return postQuery.readAllBySubscription(accountId, subscription.getRssSourceId(), postFilter, pageable);
    }

    public List<Post> getBookmarkedPosts(AccountId accountId, PostFilter postFilter,
        Pageable pageable) {

        return postQuery.readAllBookmarked(accountId, postFilter, pageable);
    }
}
