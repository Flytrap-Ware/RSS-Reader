package com.flytrap.rssreader.api.folder.domain;

import com.flytrap.rssreader.global.model.DomainId;

public record FolderMemberId(
    long value
) implements DomainId {
    public FolderMemberId {
        if (value < 0) {
            throw new IllegalArgumentException("FolderMember id must be positive");
        }
    }
}
