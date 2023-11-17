package com.flytrap.rssreader.infrastructure.api;

import com.flytrap.rssreader.infrastructure.api.dto.AccessToken;
import com.flytrap.rssreader.infrastructure.api.dto.UserResource;
import reactor.core.publisher.Mono;

public interface AuthProvider {

    /**
     * Oauth 인증 서버에 인증 코드를 보내고 Access Token을 발급받습니다.
     * @param code
     * @return AccessToken
     */
    Mono<AccessToken> requestAccessToken(String code);

    /**
     * Oauth 사용자 Resource 서버에 Access token을 보내고 사용자 Resource를 반환받습니다.
     * @param accessToken
     * @return UserResource
     */
    Mono<UserResource> requestUserResource(AccessToken accessToken);

}
