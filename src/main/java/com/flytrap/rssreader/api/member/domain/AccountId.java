package com.flytrap.rssreader.api.member.domain;

import com.flytrap.rssreader.global.model.DomainId;

public record AccountId(
    long id
) implements DomainId {

    @Override
    public long value() {
        return id;
    }
}
