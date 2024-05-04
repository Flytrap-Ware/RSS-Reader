package com.flytrap.rssreader.api.folder.infrastructure.output;

import com.flytrap.rssreader.api.folder.domain.FolderMemberId;
import com.flytrap.rssreader.api.folder.domain.SharedMember;

public record FolderMemberOutput(
    long id,
    String name,
    String profileUrl
) {
    public SharedMember toDomain() {
        return SharedMember.builder()
            .id(new FolderMemberId(id))
            .name(name)
            .profileUrl(profileUrl)
            .build();
    }
}
