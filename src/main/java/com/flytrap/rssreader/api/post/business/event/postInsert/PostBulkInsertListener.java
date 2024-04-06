package com.flytrap.rssreader.api.post.business.event.postInsert;

import com.flytrap.rssreader.api.post.infrastructure.entity.PostEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostBulkInsertListener {

    private final PostBulkInsertQueue eventQueue;

    @EventListener
    public void onEvent(PostEntity post) {
        if (eventQueue.isFull()) {
            return;
        }
        eventQueue.offer(post);
    }
}
