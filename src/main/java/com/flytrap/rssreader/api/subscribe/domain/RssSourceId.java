package com.flytrap.rssreader.api.subscribe.domain;

import com.flytrap.rssreader.global.model.DomainId;

public record RssSourceId(
    long value
) implements DomainId {
    public RssSourceId {
        if (value < 0) {
            throw new IllegalArgumentException("RssSource id must be positive");
        }
    }
}
