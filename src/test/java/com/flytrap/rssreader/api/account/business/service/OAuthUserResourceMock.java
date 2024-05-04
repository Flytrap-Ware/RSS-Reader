package com.flytrap.rssreader.api.account.business.service;

import com.flytrap.rssreader.api.auth.infrastructure.external.dto.UserResource;

public class OAuthUserResourceMock extends UserResource {
    public OAuthUserResourceMock(Long id, String email, String login, String avatarUrl) {
        super(id, email, login, avatarUrl);
    }
}
