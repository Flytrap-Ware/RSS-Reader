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
@Domain(name = "rssSource")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RssSource implements DefaultDomain {

    private Long id;
    private String title;
    private String url;
    private BlogPlatform platform;
    private boolean isNewSubscribe;

    @Builder
    protected RssSource(Long id, String title, String url, BlogPlatform platform, boolean isNewSubscribe) {
        this.id = id;
        this.title = title;
        this.url = url;
        this.platform = platform;
        this.isNewSubscribe = isNewSubscribe;
    }

    public static RssSource create(String url) {
        return RssSource.builder()
                .url(url)
                .isNewSubscribe(true)
                .build();
    }

    public static RssSource of(Long id, String title, String url, BlogPlatform platform, boolean isNewSubscribe) {
        return RssSource.builder()
                .id(id)
                .title(title)
                .url(url)
                .platform(platform)
                .isNewSubscribe(isNewSubscribe)
                .build();
    }
}
