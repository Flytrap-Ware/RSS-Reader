package com.flytrap.rssreader.global.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "oauth")
public record OauthProperties(Github github) {

    public record Github(String clientId,
                         String clientSecret,
                         String userResourceUri,
                         String userResourceEmailUri,
                         String accessTokenUri,
                         String redirectUri) {
    }
}
