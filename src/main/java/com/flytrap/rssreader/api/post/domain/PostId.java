package com.flytrap.rssreader.api.post.domain;


import com.flytrap.rssreader.global.model.DomainId;

public record PostId(
    long id
) implements DomainId {

    @Override
    public long value() {
        return id;
    }
}
