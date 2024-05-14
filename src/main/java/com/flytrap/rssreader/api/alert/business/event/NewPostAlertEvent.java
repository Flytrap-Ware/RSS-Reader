package com.flytrap.rssreader.api.alert.business.event;

import com.flytrap.rssreader.api.post.infrastructure.entity.PostEntity;
import com.flytrap.rssreader.api.subscribe.infrastructure.entity.SubscribeEntity;
import java.util.List;

public record NewPostAlertEvent(
    SubscribeEntity subscribe,
    List<PostEntity> posts
) {

}
