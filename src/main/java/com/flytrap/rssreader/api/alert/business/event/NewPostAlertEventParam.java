package com.flytrap.rssreader.api.alert.business.event;

import com.flytrap.rssreader.api.post.infrastructure.entity.PostEntity;
import com.flytrap.rssreader.api.subscribe.infrastructure.entity.SubscribeEntity;
import java.util.List;

public record NewPostAlertEventParam(
    SubscribeEntity subscribe,
    List<PostEntity> posts
) {

    public static NewPostAlertEventParam create(SubscribeEntity subscribe, List<PostEntity> posts) {
        return new NewPostAlertEventParam(subscribe, posts);
    }
}
