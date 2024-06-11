package com.flytrap.rssreader.api.folder.domain;

import com.flytrap.rssreader.global.model.DomainId;

public record FolderId(
    Long value
) implements DomainId<Long> {
    public FolderId {
        if (value == null || value < 0) {
            throw new IllegalArgumentException("Folder id must be positive");
        }
    }
}
