package com.flytrap.rssreader.api.post.business.service;

import com.flytrap.rssreader.api.member.domain.AccountId;
import com.flytrap.rssreader.api.post.business.event.PostOpenEvent;
import com.flytrap.rssreader.api.post.domain.Post;
import com.flytrap.rssreader.api.post.domain.PostAggregate;
import com.flytrap.rssreader.api.post.domain.PostId;
import com.flytrap.rssreader.api.post.infrastructure.implementation.PostCommand;
import com.flytrap.rssreader.api.subscribe.domain.Subscribe;
import com.flytrap.rssreader.api.subscribe.infrastructure.implement.SubscriptionReader;
import com.flytrap.rssreader.global.event.GlobalEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostReadService {

    private final PostCommand postCommand;
    private final SubscriptionReader subscriptionReader;
    private final GlobalEventPublisher globalEventPublisher;


    public Post viewPost(AccountId accountId, PostId postId) {

        PostAggregate postAggregate = postCommand.readAggregate(postId, accountId);
        Subscribe subscription = subscriptionReader.read(postAggregate.getSubscriptionId());

        globalEventPublisher.publish(new PostOpenEvent(postAggregate, accountId));

        return postAggregate.toDomain(subscription);
    }

}
