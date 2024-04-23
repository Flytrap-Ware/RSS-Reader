package com.flytrap.rssreader.api.subscribe.domain;

import com.flytrap.rssreader.global.model.DomainId;

public record SubscriptionId(
    long value
) implements DomainId {

}
