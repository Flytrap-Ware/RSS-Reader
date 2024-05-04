package com.flytrap.rssreader.api.folder.domain;

import com.flytrap.rssreader.global.model.DomainId;

public record FolderId(
    long value
) implements DomainId {
    public FolderId {
        if (value < 0) {
            throw new IllegalArgumentException("Folder id must be positive");
        }
    }
}
