package com.flytrap.rssreader.api.admin.domain;

import com.flytrap.rssreader.global.model.DomainId;

public record AdminSystemId(
    long value
) implements DomainId {
    public AdminSystemId {
        if (value < 0) {
            throw new IllegalArgumentException("AdminSystem id must be positive");
        }
    }
}
