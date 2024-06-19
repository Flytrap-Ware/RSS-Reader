package com.flytrap.rssreader.api.subscribe.domain;

import com.flytrap.rssreader.global.model.DomainId;

public record RssSourceId(
    Long value
) implements DomainId<Long> {
    public RssSourceId {
        if (value == null || value < 0) {
            throw new IllegalArgumentException("RssSource id must be positive");
        }
    }
}
