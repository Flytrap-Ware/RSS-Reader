package com.flytrap.rssreader.api.post.business.event;

import com.flytrap.rssreader.api.subscribe.infrastructure.entity.SubscribeEntity;

public record PostCollectEvent(
    SubscribeEntity subscribeEntity // TODO: Entity가 여기에 있어도 될까?
) {

}
