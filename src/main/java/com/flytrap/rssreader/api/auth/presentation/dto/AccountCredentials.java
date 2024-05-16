package com.flytrap.rssreader.api.auth.presentation.dto;

import com.flytrap.rssreader.api.account.domain.Account;
import com.flytrap.rssreader.api.account.domain.AccountId;

import com.flytrap.rssreader.api.account.domain.AccountRoll;
import java.io.Serializable;

/**
 * 로그인 후 Session에 저장되는 Member DTO입니다.
 * @param id
 */
public record AccountCredentials(
    AccountId id,
    AccountRoll roll
) implements Serializable {

    public boolean isAdmin() {
        return roll == AccountRoll.ADMIN;
    }

    public static AccountCredentials from(Account account) {
        return new AccountCredentials(account.getId(), account.getRoll());
    }
}
