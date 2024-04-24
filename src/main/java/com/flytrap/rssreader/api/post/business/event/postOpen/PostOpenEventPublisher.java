package com.flytrap.rssreader.api.post.business.event.postOpen;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostOpenEventPublisher {

    private final ApplicationEventPublisher eventPublisher;

    public void publish(PostOpenEvent event) {
        eventPublisher.publishEvent(event);
    }

}
