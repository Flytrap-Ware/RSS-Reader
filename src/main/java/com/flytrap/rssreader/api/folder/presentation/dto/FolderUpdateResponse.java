package com.flytrap.rssreader.api.folder.presentation.dto;

import com.flytrap.rssreader.api.folder.domain.FolderAggregate;

public record FolderUpdateResponse(
    Long folderId,
    String folderName
) {

    public static FolderUpdateResponse from(FolderAggregate folderAggregate) {
        return new FolderUpdateResponse(folderAggregate.getId().value(), folderAggregate.getName());
    }
}
