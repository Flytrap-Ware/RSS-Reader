package com.flytrap.rssreader.api.subscribe.domain;

import com.flytrap.rssreader.global.model.DefaultDomain;
import com.flytrap.rssreader.global.model.Domain;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@Domain(name = "subscribe")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Subscribe implements DefaultDomain {

    private Long id;
    private String title;
    private String url;
    private BlogPlatform platform;
    private boolean isNewSubscribe;

    @Builder
    protected Subscribe(Long id, String title, String url, BlogPlatform platform, boolean isNewSubscribe) {
        this.id = id;
        this.title = title;
        this.url = url;
        this.platform = platform;
        this.isNewSubscribe = isNewSubscribe;
    }

    public static Subscribe create(String url) {
        return Subscribe.builder()
                .url(url)
                .isNewSubscribe(true)
                .build();
    }

    public static Subscribe of(Long id, String title, String url, BlogPlatform platform, boolean isNewSubscribe) {
        return Subscribe.builder()
                .id(id)
                .title(title)
                .url(url)
                .platform(platform)
                .isNewSubscribe(isNewSubscribe)
                .build();
    }
}
