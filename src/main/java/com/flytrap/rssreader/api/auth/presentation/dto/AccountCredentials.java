package com.flytrap.rssreader.api.auth.presentation.dto;

import com.flytrap.rssreader.api.account.domain.Account;
import com.flytrap.rssreader.api.account.domain.AccountId;

import java.io.Serializable;

/**
 * 로그인 후 Session에 저장되는 Member DTO입니다.
 * @param id
 */
public record AccountCredentials(AccountId id) implements Serializable {

    public static AccountCredentials from(Account member) {
        return new AccountCredentials(member.getId());
    }
}
