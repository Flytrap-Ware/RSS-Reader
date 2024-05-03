package com.flytrap.rssreader.api.folder.presentation.dto;

import jakarta.validation.constraints.Size;

public record CreateFolderRequest(
    @Size(min = 1, max = 255) String name
) {

}
