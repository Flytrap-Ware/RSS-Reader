package com.flytrap.rssreader.api.shared_member.infrastructure.output;

import com.flytrap.rssreader.api.shared_member.domain.SharedMemberId;
import com.flytrap.rssreader.api.shared_member.domain.SharedMember;

public record SharedMemberOutput(
    long id,
    String name,
    String profileUrl
) {
    public SharedMember toDomain() {
        return SharedMember.builder()
            .id(new SharedMemberId(id))
            .name(name)
            .profileUrl(profileUrl)
            .build();
    }
}
