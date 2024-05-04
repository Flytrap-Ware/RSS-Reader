package com.flytrap.rssreader.api.auth.infrastructure.external.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.flytrap.rssreader.api.account.domain.Account;
import com.flytrap.rssreader.api.account.domain.AuthProvider;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class OAuthUserResource {

    private Long id;
    private String email;
    private String login;
    private @JsonProperty("avatar_url") String avatarUrl;

    @Builder
    protected OAuthUserResource(Long id, String email, String login, String avatarUrl) {
        this.id = id;
        this.email = email;
        this.login = login;
        this.avatarUrl = avatarUrl;
    }

    public Account toDomainForInsert() {
        return Account.newAccount(login, email, avatarUrl, id, AuthProvider.GITHUB);
    }

    public void updateEmail(UserEmailResource userEmailResource) {
        this.email = userEmailResource.email();
    }
}
