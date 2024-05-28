package com.flytrap.rssreader.api.folder.presentation.dto;

import jakarta.validation.constraints.Size;

public record FolderUpdateRequest(
    @Size(min = 1, max = 255, message = "폴더명은 1자 이상 255자 이하여야 합니다.") String name
) {

}
