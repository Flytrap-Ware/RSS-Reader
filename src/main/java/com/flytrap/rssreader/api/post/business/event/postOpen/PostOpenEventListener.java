package com.flytrap.rssreader.api.post.business.event.postOpen;

import com.flytrap.rssreader.api.post.domain.PostAggregate;
import com.flytrap.rssreader.api.post.infrastructure.implementation.PostUpdater;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostOpenEventListener {

    private final PostUpdater postUpdater;

    @Async
    @EventListener(PostOpenEvent.class)
    public void handle(PostOpenEvent event) {
        PostAggregate postAggregate = event.postAggregate();
        postAggregate.markAsOpened();

        postUpdater.update(postAggregate, event.accountId());
    }
}
