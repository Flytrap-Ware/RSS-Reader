package com.flytrap.rssreader.api.subscribe.infrastructure.output;

import com.flytrap.rssreader.api.subscribe.domain.BlogPlatform;
import com.flytrap.rssreader.api.subscribe.domain.FolderSubscription;
import com.flytrap.rssreader.api.subscribe.domain.FolderSubscriptionId;

public record FolderSubscriptionOutput(
    long id,
    String title,
    String url,
    BlogPlatform platform
) {
    public FolderSubscription toDomain() {
        return FolderSubscription.builder()
            .id(new FolderSubscriptionId(id))
            .title(title)
            .url(url)
            .platform(platform)
            .build();
    }
}
