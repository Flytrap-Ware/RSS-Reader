package com.flytrap.rssreader.api.post.business.event.postInsert;

import com.flytrap.rssreader.api.post.infrastructure.entity.PostEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostBulkInsertPublisher {

    private final ApplicationEventPublisher publisher;

    public void publish(PostEntity post) {
        publisher.publishEvent(post);
    }
}
