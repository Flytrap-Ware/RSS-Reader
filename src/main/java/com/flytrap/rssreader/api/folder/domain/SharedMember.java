package com.flytrap.rssreader.api.folder.domain;

import com.flytrap.rssreader.global.model.Domain;
import lombok.Builder;
import lombok.Getter;

@Getter
@Domain(name = "sharedMember")
public class SharedMember {

    private final FolderMemberId id;
    private final String name;
    private final String profileUrl;

    @Builder
    protected SharedMember(FolderMemberId id, String name, String profileUrl) {
        this.id = id;
        this.name = name;
        this.profileUrl = profileUrl;
    }
}
