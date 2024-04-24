package com.flytrap.rssreader.api.post.business.service;

import com.flytrap.rssreader.api.member.domain.AccountId;
import com.flytrap.rssreader.api.post.business.event.postOpen.PostOpenEvent;
import com.flytrap.rssreader.api.post.business.event.postOpen.PostOpenEventPublisher;
import com.flytrap.rssreader.api.post.domain.Post;
import com.flytrap.rssreader.api.post.domain.PostAggregate;
import com.flytrap.rssreader.api.post.domain.PostId;
import com.flytrap.rssreader.api.post.infrastructure.implementation.PostReader;
import com.flytrap.rssreader.api.subscribe.domain.Subscribe;
import com.flytrap.rssreader.api.subscribe.infrastructure.implement.SubscriptionReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostReadService {

    private final PostReader postReader;
    private final SubscriptionReader subscriptionReader;
    private final PostOpenEventPublisher postOpenEventPublisher;

    public Post viewPost(AccountId accountId, PostId postId) {

        PostAggregate postAggregate = postReader.readAggregate(postId, accountId);
        Subscribe subscription = subscriptionReader.read(postAggregate.getSubscriptionId());

        postOpenEventPublisher.publish(new PostOpenEvent(postAggregate, accountId));

        return postAggregate.toDomain(subscription);
    }

}
