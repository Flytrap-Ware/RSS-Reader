package com.flytrap.rssreader.api.post.domain;

import com.flytrap.rssreader.global.model.DomainId;

public record PostId(
    Long value
) implements DomainId<Long> {
    public PostId {
        if (value == null || value < 0) {
            throw new IllegalArgumentException("Post id must be positive");
        }
    }
}
