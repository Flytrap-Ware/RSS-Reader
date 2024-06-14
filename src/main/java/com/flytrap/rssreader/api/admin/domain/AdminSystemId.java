package com.flytrap.rssreader.api.admin.domain;

import com.flytrap.rssreader.global.model.DomainId;

public record AdminSystemId(
    Long value
) implements DomainId<Long> {
    public AdminSystemId {
        if (value == null || value < 0) {
            throw new IllegalArgumentException("AdminSystem id must be positive");
        }
    }
}
