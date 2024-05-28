package com.flytrap.rssreader.api.post.business.service;

import com.flytrap.rssreader.api.account.domain.AccountId;
import com.flytrap.rssreader.api.post.business.event.PostOpenEvent;
import com.flytrap.rssreader.api.post.domain.Post;
import com.flytrap.rssreader.api.post.domain.PostAggregate;
import com.flytrap.rssreader.api.post.domain.PostId;
import com.flytrap.rssreader.api.post.infrastructure.implementation.PostCommand;
import com.flytrap.rssreader.api.subscribe.domain.RssSource;
import com.flytrap.rssreader.api.subscribe.infrastructure.implement.RssSourceQuery;
import com.flytrap.rssreader.global.event.GlobalEventPublisher;
import com.flytrap.rssreader.global.exception.domain.NoSuchDomainException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostQueryService {

    private final PostCommand postCommand;
    private final RssSourceQuery rssSourceQuery;
    private final GlobalEventPublisher globalEventPublisher;

    public Post viewPost(AccountId accountId, PostId postId) {

        PostAggregate postAggregate = postCommand.readAggregate(postId, accountId)
            .orElseThrow(() -> new NoSuchDomainException(PostAggregate.class));
        RssSource rssSource = rssSourceQuery.read(postAggregate.getRssSourceId());

        globalEventPublisher.publish(new PostOpenEvent(postAggregate, accountId));

        return postAggregate.toReadOnly(rssSource);
    }

}
