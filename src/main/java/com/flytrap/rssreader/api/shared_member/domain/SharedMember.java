package com.flytrap.rssreader.api.shared_member.domain;

import com.flytrap.rssreader.global.model.Domain;
import lombok.Builder;
import lombok.Getter;

@Getter
@Domain(name = "sharedMember")
public class SharedMember {

    private final SharedMemberId id;
    private final String name;
    private final String profileUrl;

    @Builder
    protected SharedMember(SharedMemberId id, String name, String profileUrl) {
        this.id = id;
        this.name = name;
        this.profileUrl = profileUrl;
    }
}
