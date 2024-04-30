package com.flytrap.rssreader.api.post.business.event;

import com.flytrap.rssreader.api.post.domain.PostAggregate;
import com.flytrap.rssreader.api.post.infrastructure.implementation.PostCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostOpenEventListener {

    private final PostCommand postCommand;

    @Async
    @EventListener(PostOpenEvent.class)
    public void handle(PostOpenEvent event) {
        PostAggregate postAggregate = event.postAggregate();
        postAggregate.markAsOpened();

        postCommand.update(postAggregate, event.accountId());
    }
}
