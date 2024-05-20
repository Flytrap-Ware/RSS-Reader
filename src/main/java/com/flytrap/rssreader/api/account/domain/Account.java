package com.flytrap.rssreader.api.account.domain;

import com.flytrap.rssreader.global.model.Domain;
import com.flytrap.rssreader.global.model.DefaultDomain;
import java.time.Instant;
import lombok.Builder;
import lombok.Getter;

@Getter
@Domain(name = "account")
public class Account implements DefaultDomain {

    private final AccountId id;
    private final AccountName name;
    private final String email;
    private final String profile;
    private final ProviderInfo providerInfo;
    private final AccountRoll roll;
    private final Instant createdAt;

    @Builder
    private Account(
        AccountId id, AccountName name, String email, String profile, long providerKey,
        AuthProvider authProvider, AccountRoll roll, Instant createdAt
    ) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.profile = profile;
        this.providerInfo = new ProviderInfo(providerKey, authProvider);
        this.roll = roll;
        this.createdAt = createdAt;
    }

    public static Account of(Long id, String name, String email, String profile, Long oauthPk,
        AuthProvider authProvider, AccountRoll roll, Instant createdAt) {
        return Account.builder()
            .id(new AccountId(id))
            .name(new AccountName(name))
            .email(email)
            .profile(profile)
            .providerKey(oauthPk)
            .authProvider(authProvider)
            .roll(roll)
            .createdAt(createdAt)
            .build();
    }

    public static Account newAccount(String name, String email, String profile, Long oauthPk,
        AuthProvider authProvider, AccountRoll roll) {
        return Account.builder()
            .name(new AccountName(name))
            .email(email)
            .profile(profile)
            .providerKey(oauthPk)
            .authProvider(authProvider)
            .roll(roll)
            .build();
    }

    public static Account adminOf(long userId, String userName, String userEmail, String profile) {
        return Account.builder()
            .id(new AccountId(userId))
            .name(new AccountName(userName))
            .email(userEmail)
            .profile(profile)
            .roll(AccountRoll.ADMIN)
            .build();
    }

    public Long getProviderKey() {
        return this.providerInfo.getProviderKey();
    }

    public AuthProvider getAuthProvider() {
        return this.providerInfo.getAuthProvider();
    }
}
