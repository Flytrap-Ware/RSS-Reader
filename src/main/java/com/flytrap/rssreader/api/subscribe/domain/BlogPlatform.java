package com.flytrap.rssreader.api.subscribe.domain;

import java.util.Arrays;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BlogPlatform {
    VELOG("v2.velog.io/rss/"),
    TISTORY(".tistory.com/rss"),
    MEDIUM("medium.com/feed/"),
    ETC(" 기타");

    private final String signatureUrl;

    public static BlogPlatform parseLink(String url) {

        return Arrays.stream(BlogPlatform.values())
                .filter(blogPlatform -> url.contains(blogPlatform.getSignatureUrl()))
                .findFirst()
                .orElse(BlogPlatform.ETC);
    }
}
