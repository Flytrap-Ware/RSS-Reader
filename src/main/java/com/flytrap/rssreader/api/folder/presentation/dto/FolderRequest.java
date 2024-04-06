package com.flytrap.rssreader.api.folder.presentation.dto;

import com.flytrap.rssreader.api.folder.domain.Folder;
import jakarta.validation.constraints.Size;

public record FolderRequest() {

    public static record CreateRequest(@Size(min = 1, max = 255) String name) {
    }
    public static record Response(Long folderId, String folderName) {
        public static Response from(Folder newFolder) {
            return new Response(newFolder.getId(), newFolder.getName());
        }
    }

}
