package com.flytrap.rssreader.api.shared_member.domain;

import com.flytrap.rssreader.global.model.DomainId;

public record SharedMemberId(
    Long value
) implements DomainId<Long> {
    public SharedMemberId {
        if (value == null || value < 0) {
            throw new IllegalArgumentException("SharedMember id must be positive");
        }
    }
}
