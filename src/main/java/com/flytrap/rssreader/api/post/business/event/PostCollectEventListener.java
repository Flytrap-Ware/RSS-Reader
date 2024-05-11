package com.flytrap.rssreader.api.post.business.event;

import com.flytrap.rssreader.api.post.business.service.collect.PostCollectService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostCollectEventListener {

    private final PostCollectService postCollectService;

    @Async
    @EventListener(PostCollectEvent.class)
    public void handle(PostCollectEvent event) {
        postCollectService.enqueueHighPrioritySubscription(event.subscribeEntity());
    }
}
