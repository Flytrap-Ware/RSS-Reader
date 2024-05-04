package com.flytrap.rssreader.api.post.business.service;

import com.flytrap.rssreader.api.member.domain.AccountId;
import com.flytrap.rssreader.api.post.domain.PostAggregate;
import com.flytrap.rssreader.api.post.domain.PostId;
import com.flytrap.rssreader.api.post.infrastructure.implementation.PostCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostUpdateService {

    private final PostCommand postCommand;

    public void unmarkAsOpen(AccountId accountId, PostId postId) {
        PostAggregate postAggregate = postCommand.readAggregate(postId, accountId);
        postAggregate.unmarkAsOpened();

        postCommand.updateOnlyOpen(postAggregate, accountId);
    }

    public void markAsBookmark(AccountId accountId, PostId postId) {
        PostAggregate postAggregate = postCommand.readAggregate(postId, accountId);
        postAggregate.markAsBookmarked();

        postCommand.updateOnlyOpen(postAggregate, accountId);
    }

    public void unmarkAsBookmark(AccountId accountId, PostId postId) {
        PostAggregate postAggregate = postCommand.readAggregate(postId, accountId);
        postAggregate.unmarkAsBookmarked();

        postCommand.updateOnlyBookmark(postAggregate, accountId);
    }

}
