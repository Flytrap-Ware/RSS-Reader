package com.flytrap.rssreader.api.folder.presentation.dto;

import com.flytrap.rssreader.api.folder.domain.MyOwnFolder;

public record FolderUpdateResponse(
    Long folderId,
    String folderName
) {
    public static FolderUpdateResponse from(MyOwnFolder myOwnFolder) {
        return new FolderUpdateResponse(myOwnFolder.getId().value(), myOwnFolder.getName());
    }
}
