package com.flytrap.rssreader.api.subscribe.domain;

import com.flytrap.rssreader.global.model.Domain;
import com.flytrap.rssreader.global.model.DefaultDomain;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@Domain(name = "subscription")
public class Subscription implements DefaultDomain<Long> {

    private final SubscriptionId id;
    private final String title;
    private final String url;
    private final BlogPlatform platform;
    private final RssSourceId rssSourceId;

    @Builder
    protected Subscription(SubscriptionId id, String title, String url, BlogPlatform platform,
        RssSourceId rssSourceId) {
        this.id = id;
        this.title = title;
        this.url = url;
        this.platform = platform;
        this.rssSourceId = rssSourceId;
    }
}
