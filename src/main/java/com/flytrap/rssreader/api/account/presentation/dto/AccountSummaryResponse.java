package com.flytrap.rssreader.api.account.presentation.dto;


import com.flytrap.rssreader.api.account.domain.Account;

public record AccountSummaryResponse(Long id,
                                     String name,
                                     String profile) {

    public static AccountSummaryResponse from(Account account) {
        return new AccountSummaryResponse(
                account.getId().value(),
                account.getName().value(),
                account.getProfile()
        );
    }
}
