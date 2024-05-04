package com.flytrap.rssreader.api.account.business.service;

import com.flytrap.rssreader.api.auth.infrastructure.external.dto.OAuthUserResource;

public class OAuthOAuthUserResourceMock extends OAuthUserResource {
    public OAuthOAuthUserResourceMock(Long id, String email, String login, String avatarUrl) {
        super(id, email, login, avatarUrl);
    }
}
