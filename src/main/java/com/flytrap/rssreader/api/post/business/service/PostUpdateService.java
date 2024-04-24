package com.flytrap.rssreader.api.post.business.service;

import com.flytrap.rssreader.api.member.domain.AccountId;
import com.flytrap.rssreader.api.post.domain.PostAggregate;
import com.flytrap.rssreader.api.post.domain.PostId;
import com.flytrap.rssreader.api.post.infrastructure.implementation.PostReader;
import com.flytrap.rssreader.api.post.infrastructure.implementation.PostUpdater;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostUpdateService {

    private final PostReader postReader;
    private final PostUpdater postUpdater;

    public void unmarkAsOpen(AccountId accountId, PostId postId) {
        PostAggregate postAggregate = postReader.readAggregate(postId, accountId);
        postAggregate.unmarkAsOpened();

        postUpdater.update(postAggregate, accountId);
    }

    public void markAsBookmark(AccountId accountId, PostId postId) {
        PostAggregate postAggregate = postReader.readAggregate(postId, accountId);
        postAggregate.markAsBookmarked();

        postUpdater.update(postAggregate, accountId);
    }

    public void unmarkAsBookmark(AccountId accountId, PostId postId) {
        PostAggregate postAggregate = postReader.readAggregate(postId, accountId);
        postAggregate.unmarkAsBookmarked();

        postUpdater.update(postAggregate, accountId);
    }

}
