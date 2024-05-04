package com.flytrap.rssreader.global.event;

import com.flytrap.rssreader.api.post.business.event.PostOpenEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GlobalEventPublisher {

    private final ApplicationEventPublisher eventPublisher;

    public void publish(PostOpenEvent event) {
        eventPublisher.publishEvent(event);
    }
}
