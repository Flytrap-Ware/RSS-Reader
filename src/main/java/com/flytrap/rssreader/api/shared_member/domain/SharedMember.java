package com.flytrap.rssreader.api.shared_member.domain;

import com.flytrap.rssreader.api.account.domain.AccountId;
import com.flytrap.rssreader.global.model.DefaultDomain;
import com.flytrap.rssreader.global.model.Domain;
import lombok.Builder;
import lombok.Getter;

@Getter
@Domain(name = "sharedMember")
public class SharedMember implements DefaultDomain {

    private final SharedMemberId id;
    private final AccountId accountId;
    private final String name;
    private final String profileUrl;

    @Builder
    protected SharedMember(SharedMemberId id, AccountId accountId, String name, String profileUrl) {
        this.id = id;
        this.accountId = accountId;
        this.name = name;
        this.profileUrl = profileUrl;
    }
}
