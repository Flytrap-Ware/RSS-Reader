package com.flytrap.rssreader.api.post.business.event;

import com.flytrap.rssreader.api.subscribe.infrastructure.entity.RssSourceEntity;

public record PostCollectEvent(
    RssSourceEntity rssSourceEntity // TODO: Entity가 여기에 있어도 될까?
) {

}
