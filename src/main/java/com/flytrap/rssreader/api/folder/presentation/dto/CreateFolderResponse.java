package com.flytrap.rssreader.api.folder.presentation.dto;

import com.flytrap.rssreader.api.folder.domain.MyOwnFolder;

public record CreateFolderResponse(
    Long folderId, String folderName
) {
    public static CreateFolderResponse from(MyOwnFolder myOwnFolder) {
        return new CreateFolderResponse(myOwnFolder.getId().value(), myOwnFolder.getName());
    }
}
