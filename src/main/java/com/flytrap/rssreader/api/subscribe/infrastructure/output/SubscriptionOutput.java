package com.flytrap.rssreader.api.subscribe.infrastructure.output;

import com.flytrap.rssreader.api.subscribe.domain.BlogPlatform;
import com.flytrap.rssreader.api.subscribe.domain.Subscription;
import com.flytrap.rssreader.api.subscribe.domain.SubscriptionId;

public record SubscriptionOutput(
    long id,
    String title,
    String url,
    BlogPlatform platform
) {
    public Subscription toReadOnly() {
        return Subscription.builder()
            .id(new SubscriptionId(id))
            .title(title)
            .url(url)
            .platform(platform)
            .build();
    }
}
