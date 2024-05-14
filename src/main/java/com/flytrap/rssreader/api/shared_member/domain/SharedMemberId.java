package com.flytrap.rssreader.api.shared_member.domain;

import com.flytrap.rssreader.global.model.DomainId;

public record SharedMemberId(
    long value
) implements DomainId {
    public SharedMemberId {
        if (value < 0) {
            throw new IllegalArgumentException("SharedMember id must be positive");
        }
    }
}
