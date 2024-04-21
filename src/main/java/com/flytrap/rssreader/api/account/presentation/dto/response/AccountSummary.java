package com.flytrap.rssreader.api.account.presentation.dto.response;


import com.flytrap.rssreader.api.account.domain.Account;

import java.util.List;

public record AccountSummary(Long id,
                             String name,
                             String profile) {

    public static AccountSummary from(Account account) {
        return new AccountSummary(
                account.getId().value(),
                account.getName().value(),
                account.getProfile()
        );
    }
}
