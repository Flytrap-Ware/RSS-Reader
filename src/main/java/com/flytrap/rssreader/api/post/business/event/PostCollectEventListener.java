package com.flytrap.rssreader.api.post.business.event;

import com.flytrap.rssreader.api.post.infrastructure.system.PostCollectSystem;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostCollectEventListener {

    private final PostCollectSystem postCollectSystem;

    @Async
    @EventListener(PostCollectEvent.class)
    public void handle(PostCollectEvent event) {
        postCollectSystem.enqueueHighPrioritySubscription(event.subscribeEntity());
    }
}
