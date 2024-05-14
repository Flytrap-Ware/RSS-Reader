package com.flytrap.rssreader.api.shared_member.infrastructure.output;

import com.flytrap.rssreader.api.account.domain.AccountId;
import com.flytrap.rssreader.api.shared_member.domain.SharedMemberId;
import com.flytrap.rssreader.api.shared_member.domain.SharedMember;

public record SharedMemberOutput(
    long id,
    long accountId,
    String name,
    String profileUrl
) {
    public SharedMember toReadOnly() {
        return SharedMember.builder()
            .id(new SharedMemberId(id))
            .accountId(new AccountId(accountId))
            .name(name)
            .profileUrl(profileUrl)
            .build();
    }
}
