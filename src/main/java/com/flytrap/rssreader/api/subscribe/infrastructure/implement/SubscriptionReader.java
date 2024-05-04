package com.flytrap.rssreader.api.subscribe.infrastructure.implement;

import com.flytrap.rssreader.api.subscribe.domain.Subscribe;
import com.flytrap.rssreader.api.subscribe.domain.SubscriptionId;
import com.flytrap.rssreader.api.subscribe.infrastructure.repository.SubscribeEntityJpaRepository;
import com.flytrap.rssreader.global.exception.domain.NoSuchDomainException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SubscriptionReader {

    private final SubscribeEntityJpaRepository subscribeEntityJpaRepository; // TODO: Subscription으로 변경하기

    public Subscribe read(SubscriptionId subscriptionId) {
        return subscribeEntityJpaRepository.findById(subscriptionId.value())
            .orElseThrow(() -> new NoSuchDomainException(Subscribe.class))
            .toExistingSubscribeDomain();
    }

}
