package com.flytrap.rssreader.api.subscribe.domain;

import com.flytrap.rssreader.global.model.DomainId;

public record FolderSubscriptionId(
    long value
) implements DomainId {
    public FolderSubscriptionId {
        if (value < 0) {
            throw new IllegalArgumentException("FolderSubscription id must be positive");
        }
    }
}
