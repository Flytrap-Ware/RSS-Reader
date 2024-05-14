package com.flytrap.rssreader.api.subscribe.infrastructure.implement;

import com.flytrap.rssreader.api.folder.domain.FolderId;
import com.flytrap.rssreader.api.subscribe.domain.Subscription;
import com.flytrap.rssreader.api.subscribe.domain.SubscriptionId;
import com.flytrap.rssreader.api.subscribe.infrastructure.output.SubscriptionOutput;
import com.flytrap.rssreader.api.subscribe.infrastructure.repository.SubscriptionDslRepository;
import com.flytrap.rssreader.global.exception.domain.NoSuchDomainException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SubscriptionQuery {

    private final SubscriptionDslRepository subscriptionDslRepository;

    public Subscription read(SubscriptionId subscriptionId) {
        return subscriptionDslRepository
            .findById(subscriptionId.value())
            .orElseThrow(() -> new NoSuchDomainException(Subscription.class))
            .toReadOnly();
    }

    public List<Subscription> readAllByFolder(FolderId folderId) {
        return subscriptionDslRepository
            .findAllByFolder(folderId.value())
            .stream()
            .map(SubscriptionOutput::toReadOnly)
            .toList();
    }
}
