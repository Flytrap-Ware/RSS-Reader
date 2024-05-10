package com.flytrap.rssreader.api.post.business.service;

import com.flytrap.rssreader.api.account.domain.AccountId;
import com.flytrap.rssreader.api.folder.domain.FolderDomain;
import com.flytrap.rssreader.api.folder.domain.FolderId;
import com.flytrap.rssreader.api.folder.infrastructure.implementatioin.FolderQuery;
import com.flytrap.rssreader.api.folder.infrastructure.implementatioin.FolderValidation;
import com.flytrap.rssreader.api.post.domain.Post;
import com.flytrap.rssreader.api.post.domain.PostFilter;
import com.flytrap.rssreader.api.post.infrastructure.implementation.PostQuery;
import com.flytrap.rssreader.api.subscribe.domain.SubscriptionId;
import com.flytrap.rssreader.global.exception.domain.ForbiddenAccessFolderException;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@AllArgsConstructor
@Service
public class PostListReadService {

    private final FolderValidation folderValidation;
    private final PostQuery postQuery;
    private final FolderQuery folderQuery;

    public List<Post> getPostsByAccount(AccountId accountId, PostFilter postFilter,
        Pageable pageable) {

        return postQuery.readAllByAccount(accountId, postFilter, pageable);
    }

    public List<Post> getPostsByFolder(AccountId accountId, FolderId folderId,
        PostFilter postFilter, Pageable pageable) {

        if (!folderValidation.isAccessibleFolder(folderId, accountId))
            throw new ForbiddenAccessFolderException(FolderDomain.class);

        return postQuery.readAllByFolder(accountId, folderId, postFilter, pageable);
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
