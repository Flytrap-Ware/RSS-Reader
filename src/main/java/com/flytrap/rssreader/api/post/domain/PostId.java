package com.flytrap.rssreader.api.post.domain;

import com.flytrap.rssreader.global.model.DomainId;

public record PostId(
    long value
) implements DomainId {
    public PostId {
        if (value < 0) {
            throw new IllegalArgumentException("Post id must be positive");
        }
    }
}
