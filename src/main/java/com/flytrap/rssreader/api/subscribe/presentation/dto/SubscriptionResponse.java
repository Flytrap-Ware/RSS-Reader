package com.flytrap.rssreader.api.subscribe.presentation.dto;

import com.flytrap.rssreader.api.subscribe.domain.FolderSubscription;

public record SubscriptionResponse(
    Long subscribeId,
    String subscribeTitle
) {
    public static SubscriptionResponse from(FolderSubscription subscription) {
        return new SubscriptionResponse(subscription.getId().value(), subscription.getTitle());
    }
}
