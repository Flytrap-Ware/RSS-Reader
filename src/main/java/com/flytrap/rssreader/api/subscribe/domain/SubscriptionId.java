package com.flytrap.rssreader.api.subscribe.domain;

import com.flytrap.rssreader.global.model.DomainId;

public record SubscriptionId(
    long id
) implements DomainId {

    @Override
    public long value() {
        return id;
    }
}
