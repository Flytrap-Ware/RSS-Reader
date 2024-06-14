package com.flytrap.rssreader.api.subscribe.domain;

import com.flytrap.rssreader.global.model.Domain;
import com.flytrap.rssreader.global.model.DefaultDomain;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@Domain(name = "rssSource")
public class RssSource implements DefaultDomain<Long> {

    private final RssSourceId id;
    private final String title;
    private final String url;
    private final BlogPlatform platform;
    private final boolean isNewSubscribe;

    @Builder
    protected RssSource(RssSourceId id, String title, String url, BlogPlatform platform, boolean isNewSubscribe) {
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

    public static RssSource of(RssSourceId id, String title, String url, BlogPlatform platform, boolean isNewSubscribe) {
        return RssSource.builder()
                .id(id)
                .title(title)
                .url(url)
                .platform(platform)
                .isNewSubscribe(isNewSubscribe)
                .build();
    }
}
