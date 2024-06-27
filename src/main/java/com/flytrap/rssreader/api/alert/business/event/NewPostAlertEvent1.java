package com.flytrap.rssreader.api.alert.business.event;

import com.flytrap.rssreader.api.post.infrastructure.entity.PostEntity;
import com.flytrap.rssreader.api.subscribe.infrastructure.entity.RssSourceEntity;

import java.util.List;
import java.util.UUID;

public record NewPostAlertEvent1(

        UUID eventId, // post가 정상 수집될경우 Event가 발생하게 되고 이때 UUID값을 하나 가집니다.
        RssSourceEntity rssSource,
        List<PostEntity> posts
) {

}
