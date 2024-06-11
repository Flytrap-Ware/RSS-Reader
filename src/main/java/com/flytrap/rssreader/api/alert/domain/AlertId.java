package com.flytrap.rssreader.api.alert.domain;

import com.flytrap.rssreader.global.model.DomainId;

public record AlertId(
    Long value
) implements DomainId<Long> {

    public AlertId {
        if (value == null || value < 0) {
            throw new IllegalArgumentException("Alert id must be positive");
        }
    }
}
