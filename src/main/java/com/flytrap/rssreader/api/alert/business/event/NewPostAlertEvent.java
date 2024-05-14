package com.flytrap.rssreader.api.alert.business.event;

import com.flytrap.rssreader.api.post.infrastructure.entity.PostEntity;
import com.flytrap.rssreader.api.subscribe.infrastructure.entity.RssSourceEntity;
import java.util.List;

public record NewPostAlertEvent(
    RssSourceEntity rssSource,
    List<PostEntity> posts
) {

}
