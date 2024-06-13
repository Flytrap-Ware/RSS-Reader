package com.flytrap.rssreader.api.post.domain;

import com.flytrap.rssreader.global.model.DomainId;

public record PostId(
    String value
) implements DomainId<String> {
    public PostId {
        if (value == null) {
            throw new IllegalArgumentException("Post ID cannot be null.");
        }
    }
}
