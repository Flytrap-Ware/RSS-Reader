package com.flytrap.rssreader.fixture;

import com.flytrap.rssreader.api.account.domain.Account;
import com.flytrap.rssreader.api.account.domain.AccountId;
import com.flytrap.rssreader.api.account.domain.AccountName;
import com.flytrap.rssreader.fixture.FixtureFields.MemberFields;

public class FixtureFactory {

    public static Account generateAccount() {
        return Account.builder()
                .id(new AccountId(MemberFields.id))
                .name(new AccountName(MemberFields.name))
                .email(MemberFields.email)
                .profile(MemberFields.profile)
                .providerKey(MemberFields.oauthPk)
                .authProvider(MemberFields.authProvider)
                .build();
    }
}
