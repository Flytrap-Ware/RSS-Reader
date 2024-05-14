package com.flytrap.rssreader.api.subscribe.domain;

import com.flytrap.rssreader.global.model.DomainId;

public record SubscriptionId(
    long value
) implements DomainId {
    public SubscriptionId {
        if (value < 0) {
            throw new IllegalArgumentException("Subscription id must be positive");
        }
    }
}
