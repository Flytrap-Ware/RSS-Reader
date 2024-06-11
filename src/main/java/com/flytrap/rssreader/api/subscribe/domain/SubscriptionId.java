package com.flytrap.rssreader.api.subscribe.domain;

import com.flytrap.rssreader.global.model.DomainId;

public record SubscriptionId(
    Long value
) implements DomainId<Long> {
    public SubscriptionId {
        if (value == null || value < 0) {
            throw new IllegalArgumentException("Subscription id must be positive");
        }
    }
}
