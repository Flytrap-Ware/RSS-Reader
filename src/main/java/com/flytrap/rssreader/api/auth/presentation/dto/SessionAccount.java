package com.flytrap.rssreader.api.auth.presentation.dto;

import com.flytrap.rssreader.api.account.domain.Account;
import com.flytrap.rssreader.api.account.domain.AccountId;

import java.io.Serializable;

/**
 * 로그인 후 Session에 저장되는 Member DTO입니다.
 * @param id
 */
public record SessionAccount(AccountId id) implements Serializable {

    public static SessionAccount from(Account member) {
        return new SessionAccount(member.getId());
    }
}
