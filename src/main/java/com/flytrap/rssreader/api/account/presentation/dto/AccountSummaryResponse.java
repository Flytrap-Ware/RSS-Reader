package com.flytrap.rssreader.api.account.presentation.dto;


import com.flytrap.rssreader.api.account.domain.Account;
import com.flytrap.rssreader.api.shared_member.domain.SharedMember;

public record AccountSummaryResponse(
    Long id,
    String name,
    String profile
) {
    public static AccountSummaryResponse from(Account account) {
        return new AccountSummaryResponse(
            account.getId().value(),
            account.getName().value(),
            account.getProfile()
        );
    }

    public static AccountSummaryResponse from(SharedMember sharedMember) {
        return new AccountSummaryResponse(
            sharedMember.getId().value(),
            sharedMember.getName(),
            sharedMember.getProfileUrl()
        );
    }
}
