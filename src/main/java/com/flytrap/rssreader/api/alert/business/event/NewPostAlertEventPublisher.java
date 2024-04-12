package com.flytrap.rssreader.api.alert.business.event;

import com.flytrap.rssreader.api.post.infrastructure.entity.PostEntity;
import com.flytrap.rssreader.api.subscribe.infrastructure.entity.SubscribeEntity;
import com.flytrap.rssreader.global.event.PublishEvent;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class NewPostAlertEventPublisher {

    @PublishEvent(eventType = NewPostAlertEventHolder.class,
        params = "#{T(com.flytrap.rssreader.api.alert.business.event.NewPostAlertEventParam).create(#subscribe, #newPosts)}")
    public void publishNewPostAlertEvent(SubscribeEntity subscribe, List<PostEntity> newPosts) {
    }

}
